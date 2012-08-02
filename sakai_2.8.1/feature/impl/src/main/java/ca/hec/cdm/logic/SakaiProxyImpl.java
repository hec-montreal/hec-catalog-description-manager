package ca.hec.cdm.logic;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.event.api.EventTrackingService;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;

import ca.hec.cdm.api.CatalogDescriptionService;
import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.model.CatalogDescription;

import org.sakaiquebec.opensyllabus.common.api.OsylSiteService;

/**
 * Implementation of {@link SakaiProxy}
 */
public class SakaiProxyImpl implements SakaiProxy {
    private static final Logger log = Logger.getLogger(SakaiProxyImpl.class);

    public void updateCatalogDescription(CatalogDescription cd) 
    		throws StaleDataException, DatabaseException {
    	
   		catalogDescriptionService.updateDescription(cd);
    }

    public CatalogDescription getCatalogDescriptionById(Long id) {
    	return catalogDescriptionService.getCatalogDescription(id);
    }

    public List<CatalogDescription> getCatalogDescriptionsForUser() {
		List<CatalogDescription> catalogDescriptions = new ArrayList<CatalogDescription>();
    	
    	// add the catalog descriptions for all departments
  		for(String dept : getUserDepartments())
   		{
			catalogDescriptions.addAll(catalogDescriptionService.getCatalogDescriptionsByDepartment(dept));
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
   				departments.add(posteSplit[1]);
    		}
    	}
    	
    	return departments;
    }

    /**
     * {@inheritDoc}
     */
    public String getCurrentSiteId() {
    	return toolManager.getCurrentPlacement().getContext();
    }

    /**
     * {@inheritDoc}
     */
    public String getCurrentUserId() {
    	return sessionManager.getCurrentSessionUserId();
    }

    /**
     * {@inheritDoc}
     */
    public String getCurrentUserDisplayName() {
    	return userDirectoryService.getCurrentUser().getDisplayName();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSuperUser() {
    	return securityService.isSuperUser();
    }

    /**
     * {@inheritDoc}
     */
    public void postEvent(String event, String reference, boolean modify) {
    	eventTrackingService.post(eventTrackingService.newEvent(event, reference, modify));
    }

    /**
     * {@inheritDoc}
     */
    public String getSkinRepoProperty() {
    	return serverConfigurationService.getString("skin.repo");
    }

    /**
     * {@inheritDoc}
     */
    public String getToolSkinCSS(String skinRepo) {

    	String skin = siteService.findTool(
    			sessionManager.getCurrentToolSession().getPlacementId())
    			.getSkin();

    	if (skin == null) {
    		skin = serverConfigurationService.getString("skin.default");
    	}

    	return skinRepo + "/" + skin + "/tool.css";
    }

    /**
     * {@inheritDoc}
     */
    public String getSpecificCourse(String courseId) {
	List<Site> sites = siteService.getSites(SiteService.SelectionType.ANY, "course",
		courseId, null, SiteService.SortType.CREATED_ON_DESC, null);
	
	for (Site s : sites) {
	    if (osylSiteService.hasBeenPublished(s.getId()))
		return s.getTitle();
	}
	return "";
    }

    /**
     * init - perform any actions required here for when this bean starts up
     */
    public void init() {
    	log.info("init");
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
    private OsylSiteService osylSiteService;
}
