package ca.hec.cdm.logic;

import java.util.ArrayList;
import java.util.List;

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

import ca.hec.cdm.model.CatalogDescription;

/**
 * Implementation of {@link SakaiProxy}
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au)
 * 
 */
public class SakaiProxyImpl implements SakaiProxy {

    private static final Logger log = Logger.getLogger(SakaiProxyImpl.class);

    public void saveCatalogDescription(Integer id, String description) {
    }

    public List<CatalogDescription> getListCatalogDescription() {
	List<CatalogDescription> catalogDescriptions = new ArrayList<CatalogDescription>();

	CatalogDescription cd1 = new CatalogDescription();
	CatalogDescription cd2 = new CatalogDescription();
	CatalogDescription cd3 = new CatalogDescription();

	cd1.setId(new Long(1));
	cd1.setTitle("titre1");
	cd1.setDescription("description1");
	cd1.setDepartment("department1");
	cd1.setCareer("career1");
	cd1.setRequirements("requirements1");
	cd1.setCredits(new Float(1));
	cd1.setLanguage("language1");

	cd2.setId(new Long(2));
	cd2.setTitle("titre2");
	cd2.setDescription("description2");
	cd1.setDepartment("department2");
	cd1.setCareer("career2");
	cd1.setRequirements("requirements2");
	cd1.setCredits(new Float(2));
	cd1.setLanguage("language2");

	cd3.setId(new Long(3));
	cd3.setTitle("titre3");
	cd3.setDescription("description3");
	cd1.setDepartment("department3");
	cd1.setCareer("career3");
	cd1.setRequirements("requirements3");
	cd1.setCredits(new Float(3));
	cd1.setLanguage("language3");

	catalogDescriptions.add(cd1);
	catalogDescriptions.add(cd2);
	catalogDescriptions.add(cd3);

	return catalogDescriptions;
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
	eventTrackingService.post(eventTrackingService.newEvent(event,
		reference, modify));
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
     * init - perform any actions required here for when this bean starts up
     */
    public void init() {
	log.info("init");
    }

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

}
