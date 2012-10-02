package ca.hec.cdm.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.event.api.EventTrackingService;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;

import ca.hec.cdm.api.CatalogDescriptionService;
import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.PermissionException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.model.CatalogDescription;
import ca.hec.portal.api.PortalManagerService;

/**
 * Implementation of {@link SakaiProxy}
 */
public class SakaiProxyImpl implements SakaiProxy {
    
    private static final Logger log = Logger.getLogger(SakaiProxyImpl.class);
    
    private static HashMap<String, String> departmentIdMap = null;

    private static final String CERTIFICATE_DEPARTMENT_ID = "115";
    
    private static final String CERTIFICATE_ACADEMIC_CAREER_CODE = "CERT";
   
    private static final String LDAP_POSTE_ACTIF = "posteActif";
    
    @Getter
    @Setter
    private CatalogDescriptionService catalogDescriptionService;

    @Getter
    @Setter
    private ToolManager toolManager;

    @Getter
    @Setter
    private SessionManager sessionManager;

    @Getter
    @Setter
    private UserDirectoryService userDirectoryService;

    @Getter
    @Setter
    private SecurityService securityService;

    @Getter
    @Setter
    private EventTrackingService eventTrackingService;

    @Getter
    @Setter
    private ServerConfigurationService serverConfigurationService;

    @Getter
    @Setter
    private SiteService siteService;

    @Getter
    @Setter
    private PortalManagerService portalManagerService;
    
   
    /**
     * {@inheritDoc}
     */
    public boolean catalogDescriptionExists(String course_id) {
	return catalogDescriptionService.descriptionExists(course_id);
    }

    
    /**
     * {@inheritDoc}
     */
    public List<CatalogDescription> getCatalogDescriptions(Map<String, String> eqCriteria, Map<String, String> searchCriteria) {
	return catalogDescriptionService.getCatalogDescriptions(eqCriteria, searchCriteria);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<CatalogDescription> getCatalogDescriptions(Map<String, String> eqCriteria) {
	return catalogDescriptionService.getCatalogDescriptions(eqCriteria, null);
    }

    public void updateCatalogDescription(CatalogDescription cd)
	    throws StaleDataException, DatabaseException, PermissionException {

	if (isUserUpdateAllowed(cd)) {
	    cd.setLastModifiedBy(getCurrentUserDisplayName());

	    catalogDescriptionService.updateDescription(cd);
	} else {
	    throw new PermissionException();
	}
    }
    
    public CatalogDescription getCatalogDescriptionById(Long id) {
    	return catalogDescriptionService.getCatalogDescription(id);
    }

    public CatalogDescription getCatalogDescription(String courseId) {
    	return catalogDescriptionService.getCatalogDescription(courseId);
    }

    
    public List<CatalogDescription> getCatalogDescriptionsForUser() {
	
	List<CatalogDescription> catalogDescriptions = null;
    	
	
	if(isAdmin()){
	    catalogDescriptions = catalogDescriptionService.getAllCatalogDescriptions();
	}
	else{
	    catalogDescriptions = new ArrayList<CatalogDescription>();
	    
	    List<String> posteActifs = getLDAPPosteActif();

	    // posteActifs is null if the property is not present
	    if (null != posteActifs) {

		for (String posteActif : posteActifs) {

		    String deptId = getLDAPDepartmentId(posteActif);

		    // if this is a certificate secretary : special case
		    if (deptId.equals(CERTIFICATE_DEPARTMENT_ID)) {
			catalogDescriptions.addAll(catalogDescriptionService
				.getAllCatalogDescriptionsForCertificate());
		    } else {
			catalogDescriptions
				.addAll(getCatalogDescriptionsForDepartment(deptId));
		    }

		}// end for
		
	    }//end if postActifs!=null
	    
	}
	 		
	return catalogDescriptions;
    }
    
    
    private boolean isUserUpdateAllowed(CatalogDescription cd){
	
	boolean isAllowed = false;
	
	List<String> posteActifs = getLDAPPosteActif();

	// posteActifs is null if the property is not present
	if (null != posteActifs) {
	    
	    for (String posteActif : posteActifs) {
		
		String deptId = getLDAPDepartmentId(posteActif);
		
		//if this is a certificate secretary : special case
		if(deptId.equals(CERTIFICATE_DEPARTMENT_ID)){
		    
		    if(cd.getCareer().equals(CERTIFICATE_ACADEMIC_CAREER_CODE)){
			isAllowed = true;
			break;
		    }
		}
		else{
		    String depCode = getDepartmentIdMap().get(deptId);
		    
		    if(depCode.equals(cd.getDepartment())){
			isAllowed = true;
			break;
		    }
		}
		
	    }//end for
	}
	
	if(isAdmin()){
	    isAllowed = true; 
	}
		
	return isAllowed;
    }
   
    
    private List<CatalogDescription> getCatalogDescriptionsForDepartment(String deptId){
	
	//convert the department id into an academic department code
	String depCode = getDepartmentIdMap().get(deptId);
	
	return catalogDescriptionService.getCatalogDescriptionsByDepartment(depCode);
    }
    
    
 
    private List<String> getLDAPPosteActif(){
	
	// for now sakai only retrieves one value even if LDAP has many for the
	// same property, but that could change (or be changed).
	return userDirectoryService.getCurrentUser().getProperties()
			.getPropertyList(LDAP_POSTE_ACTIF);
    }
    
    
    private String getLDAPDepartmentId(String posteActif){

	// split the posteActif parameter at |
	String[] posteSplit = posteActif.split("\\|");

	//department id is the parameter at index 4
	return posteSplit[4];
    }
    
    
    private HashMap<String, String> getDepartmentIdMap(){
	
	if(departmentIdMap==null){
	    departmentIdMap = new HashMap<String, String>();
	    
	    departmentIdMap.put("112", "BAA");
	    departmentIdMap.put("120", "BUR.REGIST");
	    departmentIdMap.put(CERTIFICATE_DEPARTMENT_ID, "CERTIFICAT");
	    departmentIdMap.put("118", "DIPLOMES");
	    departmentIdMap.put("116", "DOCTORAT");
	    departmentIdMap.put("117", "MSC");
	    departmentIdMap.put("103", "FINANCE");
	    departmentIdMap.put("102", "GRH");
	    departmentIdMap.put("107", "IEA");
	    departmentIdMap.put("109", "MARKETING");
	    departmentIdMap.put("113", "MBA");
	    departmentIdMap.put("105", "MNGT");
	    departmentIdMap.put("101", "MQG");
	    departmentIdMap.put("119", "QUAL.COMM.");
	    departmentIdMap.put("106", "SC.COMPT.");
	    departmentIdMap.put("108", "TI");
	    departmentIdMap.put("110", "DIR.PROG.");
	    departmentIdMap.put("186", "INTERNAT");
	    departmentIdMap.put("104", "GOL");
	    departmentIdMap.put("152", "P.ETUD.SUP");
	}
	
	return departmentIdMap;
    }
    
    private String getCurrentUserDisplayName() {
    	return userDirectoryService.getCurrentUser().getDisplayName();
    }
   
    private String getCurrentUserId() {
    	return userDirectoryService.getCurrentUser().getEid();
    }

    private boolean isAdmin(){
	return (getCurrentUserId().equalsIgnoreCase("admin"));
    }
    
    /**
     * init - perform any actions required here for when this bean starts up
     */
    public void init() {
    	log.info("init");
    }

    /**
     * {@inheritDoc}
     */
    public String getDepartmentDescription(String department) {
	return portalManagerService.getDepartmentDescription(department);
    }

    /**
     * {@inheritDoc}
     */
    public String getCareerDescription(String career) {
	return portalManagerService.getCareerDescription(career);
    }

    
    /**
     * {@inheritDoc}
     */
    public String getCareerGroup(String career) {
	return portalManagerService.getCareerGroup(career);
    }

    /**
     * {@inheritDoc}
     */
    public String getDepartmentGroup(String department) {
	return portalManagerService.getDepartmentGroup(department);
    }
}
