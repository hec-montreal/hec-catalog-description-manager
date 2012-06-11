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

import ca.hec.cdm.api.CatalogDescriptionService;
import ca.hec.cdm.model.CatalogDescription;

/**
 * Implementation of {@link SakaiProxy}
 */
public class SakaiProxyImpl implements SakaiProxy {

    private static final Logger log = Logger.getLogger(SakaiProxyImpl.class);

    public Boolean updateCatalogDescription(Long id, String description) {
    	return catalogDescriptionService.updateDescription(id, description);
    }

    public List<CatalogDescription> getCatalogDescriptionsForUser() {
    	// je mets finance pour tester, mais un jour il faudra que ce soit specifique au service de l'usager
    	return catalogDescriptionService.getCatalogDescriptionsByDepartment("FINANCE");
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

}
