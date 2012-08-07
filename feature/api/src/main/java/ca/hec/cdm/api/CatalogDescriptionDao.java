package ca.hec.cdm.api;

import java.util.List;

import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.model.CatalogDescription;

/**
 * This is the interface for the Dao for our Catalog Description tool, it
 * handles the database access functionality of the tool
 * 
 * @author <a href="mailto:curtis.van-osch@hec.ca">Curtis van Osch</a>
 * @version $Id: $
 */
public interface CatalogDescriptionDao {

    public void init();

    /**
     * Return a catalog description based on the database id
     * 
     * @param id - the database id of the catalog description
     * @return - the CatalogDescription
     */
    public CatalogDescription getCatalogDescription(Long id);
    
    /**
     * Return the last version of a catalog description based on the course id
     * 
     * @param id - the course id of the catalog description
     * @return - the last version of the CatalogDescription
     */
    public CatalogDescription getLastVersionCatalogDescription(String courseId);

    /**
     * Return the active catalog description based on the course id
     * 
     * @param id - the course id of the catalog description
     * @return - the last version of the CatalogDescription
     */
    public CatalogDescription getCatalogDescription(String course_id);

    /**
     * Return all active catalog descriptions
     * 
     * @return - the list of CatalogDescription
     */
    public List<CatalogDescription> getCatalogDescriptions();

    /**
     * Return a list of CatalogDescriptions by career
     * 
     * @param career - the career for which the catalog descriptions should be
     *            returned
     * @return - the list of CatalogDescriptions
     */
    public List<CatalogDescription> getCatalogDescriptionsByCareer(String career);

    /**
     * Return a list of CatalogDescriptions by department
     * 
     * @param department - the department for which the catalog descriptions
     *            should be returned
     * @return - the list of CatalogDescriptions
     */
    public List<CatalogDescription> getCatalogDescriptionsByDepartment(
	    String department);

    /**
     * save the catalog description to the database
     * 
     * @param cd - the catalog description to save
     * @throws StaleDataException
     * @throws DatabaseException
     */
    public void saveCatalogDescription(CatalogDescription cd)
	    throws StaleDataException, DatabaseException;

    /**
     * Return a list of Course id (used by quartz job to determine if the catalog description is already created)
     * 
     * @return - the list of Course id
     */
    public List<String> getListCourseId();
    
    /**
     * Set all catalog description to "inactive" status
     * 
     */
    public void setCatalogDescriptionToInactive();
    
    /**
     * Get catalog description from department that have no description
     * 
     */
    public List<CatalogDescription> getCatalogDescriptionsByDepartmentWithNoDescription(String department);
    
    /**
     * Get all departments that have at least one catalog description with an empty description
     * 
     */
    public List<String> getDepartmentNameWithAtLeastOneCaWithNoDescription();
    
    /**
     * Get all departments that have at least one catalog description with an empty description
     * 
     */
    public List<String> getCareerNameWithAtLeastOneCaWithNoDescription();

    /**
     * return whether or not the specified Catalog Description exists.
     * 
     * @param course_id - the course_id of the catalog description
     * @return true if the catalog description exists, false otherwise
     */
    public boolean descriptionExists(String course_id);
}
