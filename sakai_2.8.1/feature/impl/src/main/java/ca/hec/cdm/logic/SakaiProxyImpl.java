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

    /**
     * {@inheritDoc}
     */
    public boolean catalogDescriptionExists(String course_id) {
	return catalogDescriptionService.descriptionExists(course_id);
    }

    /**
     * {@inheritDoc}
     */
    public List<CatalogDescription> getCatalogDescriptions(Map<String, String> criteria) {
	return catalogDescriptionService.getCatalogDescriptions(criteria);
    }

    public void updateCatalogDescription(CatalogDescription cd) 
    		throws StaleDataException, DatabaseException, PermissionException {
    	
	cd.setLastModifiedBy(getCurrentUserDisplayName());
	
	if (getUserDepartments().contains(cd.getDepartment())) {
	    catalogDescriptionService.updateDescription(cd);
	}
	else {
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
	List<CatalogDescription> catalogDescriptions = new ArrayList<CatalogDescription>();
    	
	// add the catalog descriptions for all departments
	for(String dept : getUserDepartments())
	{
	    Map<String, String> searchCriteria = new HashMap<String, String>();
	    searchCriteria.put("department", dept);
	    catalogDescriptions.addAll(catalogDescriptionService.getCatalogDescriptions(searchCriteria));
	}
  		
	return catalogDescriptions;
    }
    
    private List<String> getUserDepartments() {
    	List<String> departments = new ArrayList<String>();
    	// for now sakai only retrieves one value even if LDAP has many for the same property, but that could change (or be changed).
    	List<String> posteActifs = userDirectoryService.getCurrentUser().getProperties().getPropertyList("posteActif");
    	
    	// posteActifs is null if the property is not present
    	if (null != posteActifs)
    	{
    		for(String posteActif : posteActifs)
    		{
    			// split the posteActif parameter at | 
    			String[] posteSplit = posteActif.split("\\|");
    			
  				// department is the parameter at index 1
   				departments.add(posteSplit[1].toUpperCase());
    		}
    	}
    	
    	return departments;
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
    public String getCurrentUserDisplayName() {
    	return userDirectoryService.getCurrentUser().getDisplayName();
    }
    
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
}
