package ca.hec.cdm.logic;

import java.util.List;

import ca.hec.cdm.model.CatalogDescription;

/**
 * An interface to abstract all Sakai related API calls in a central method that can be injected into our app.
 * 
 * @author Mike Jennings (mike_jennings@unc.edu)
 *
 */
public interface SakaiProxy {
	
	/**
	 * save catalogDescription
	 * @return - true for success, false if failure
	 */
	public Boolean updateCatalogDescription(Long id, String description);
	
	/**
	 * get the list of catalogDescription for current user
	 * @return - the list of catalog descriptions for the current user
	 */
	public List<CatalogDescription> getCatalogDescriptionsForUser();
	
	/**
	 * get catalogDescription
	 * @return - the catalog description corresponding to the Id
	 */
	public CatalogDescription getCatalogDescriptionsById(Long id);
	
	/**
	 * Get current siteid
	 * @return
	 */
	public String getCurrentSiteId();
	
	/**
	 * Get current user id
	 * @return
	 */
	public String getCurrentUserId();
	
	/**
	 * Get current user display name
	 * @return
	 */
	public String getCurrentUserDisplayName();
	
	/**
	 * Is the current user a superUser? (anyone in admin realm)
	 * @return
	 */
	public boolean isSuperUser();
	
	/**
	 * Post an event to Sakai
	 * 
	 * @param event			name of event
	 * @param reference		reference
	 * @param modify		true if something changed, false if just access
	 * 
	 */
	public void postEvent(String event,String reference,boolean modify);
	
	/**
	 * Wrapper for ServerConfigurationService.getString("skin.repo")
	 * @return
	 */
	public String getSkinRepoProperty();
	
	/**
	 * Gets the tool skin CSS first by checking the tool, otherwise by using the default property.
	 * @param	the location of the skin repo
	 * @return
	 */
	public String getToolSkinCSS(String skinRepo);
}
