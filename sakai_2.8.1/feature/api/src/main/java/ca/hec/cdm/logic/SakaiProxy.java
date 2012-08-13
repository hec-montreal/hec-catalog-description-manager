package ca.hec.cdm.logic;

import java.util.List;

import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.PermissionException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.model.CatalogDescription;

/**
 * An interface to abstract all Sakai related API calls in a central method that can be injected into our app.
 * 
 * @author Mike Jennings (mike_jennings@unc.edu)
 *
 */
public interface SakaiProxy {
	

	/**
	 * check if a catalogDescription exists (by course id)
	 *
	 * @param String course_id - the id of the catalog description
	 * @return true if it exists, false otherwise
	 */
	public boolean catalogDescriptionExists(String course_id);

	/**
	 * save catalogDescription
	 *
	 * @param cd - the catalog descriptiong to update
	 * @throws StaleDataException 
	 * @throws DatabaseException
	 */
	public void updateCatalogDescription(CatalogDescription cd) 
		throws StaleDataException, DatabaseException, PermissionException;
	
	/**
	 * get the list of catalogDescription for current user
	 * @return - the list of catalog descriptions for the current user
	 */
	public List<CatalogDescription> getCatalogDescriptionsForUser();
	
	/**
	 * get catalogDescription
	 *
	 * @param id - the database id of the catalog description to retrieve
	 * @return - the catalog description corresponding to the Id
	 */
	public CatalogDescription getCatalogDescriptionById(Long id);
	
	/**
	 * get catalogDescription by courseId
	 *
	 * @param courseId - the course id of the catalog description to retrieve
	 * @return - the catalog description corresponding to the course Id
	 */
	public CatalogDescription getCatalogDescription(String id);

	/**
	 * get the list of catalogDescription for a department
	 *
	 * @param department - the department to search on
	 * @return - the list of catalog description corresponding to the department
	 */
	public List<CatalogDescription> getCatalogDescriptionsByDepartment(
		String department);

	/**
	 * get the list of catalogDescription for a career
	 *
	 * @param career - the career to search on
	 * @return - the list of catalog description corresponding to the career
	 */
	public List<CatalogDescription> getCatalogDescriptionsByCareer(
		String career);
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

	/**
	 * Gets the description of the specified department
	 *
	 * @param department	the department to find a description for
	 * @return the description
	 */
	public String getDepartmentDescription(String department);

	/**
	 * Gets the career of the specified department
	 *
	 * @param career	the career to find a description for
	 * @return the description
	 */
	public String getCareerDescription(String career);
}
