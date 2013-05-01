package ca.hec.cdm.logic;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.PermissionException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.model.CatalogDescription;

/**
 * An interface to abstract all Sakai related API calls in a central method that can be injected into our app.
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
	 * get the list of catalogDescriptions for a given Hibernate query
	 * @return - the list of catalog descriptions
	 */
	public List<CatalogDescription> getCatalogDescriptions(Map<String, String> eqCriteria, Map<String, String> searchCriteria);
	

	/**
	 * get the list of catalogDescriptions for a given Hibernate query
	 * @return - the list of catalog descriptions
	 */
	public List<CatalogDescription> getCatalogDescriptions(Map<String, String> eqCriteria);

	/**
	 * get the list of catalogDescription for current user
	 * @return - the list of catalog descriptions for the current user
	 */
	public List<CatalogDescription> getCatalogDescriptionsForUser(boolean showInactives);
	
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
	 * Gets the career group of the specified career
	 * A career group can include several careers
	 *
	 * @param career	the career to find a career group for
	 * @return the description
	 */
	public String getCareerGroup(String career);

	/**
	 * Gets the department group of the specified department
	 * A department group can include several departments
	 *
	 * @param department	the department to find a description for
	 * @return the description
	 */
	public String getDepartmentGroup(String department);
}
