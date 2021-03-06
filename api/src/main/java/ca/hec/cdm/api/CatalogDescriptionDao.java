package ca.hec.cdm.api;

import java.util.List;
import java.util.Map;

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
     * Return the active catalog description based on the course id
     * 
     * @param id - the course id of the catalog description
     * @return - the last version of the CatalogDescription
     */
    public CatalogDescription getCatalogDescription(String course_id);

    /**
     * Return the last version of a catalog description based on the course id
     * 
     * @param id - the course id of the catalog description
     * @return - the last version of the CatalogDescription (null if nothing is found)
     */
    public CatalogDescription getLastVersionCatalogDescription(String courseId);

    
    /**
     * Return active catalog descriptions that correspond to the given department
     * (can be null for all)
     * 
     * @return - the list of CatalogDescription
     */
    public List<CatalogDescription> getCatalogDescriptionsByDepartment(String department);
    
    /**
     * Return active catalog descriptions that correspond to the given department with flag to indicate if we return or not inactive catalog descriptions     
     */
    public List<CatalogDescription> getCatalogDescriptionsByDepartment(String department, boolean showInactives);
    
    
    /**
     * Return active catalog descriptions that correspond to the given academic career
     * (can be null for all)
     * 
     * @return - the list of CatalogDescription
     */
    public List<CatalogDescription> getCatalogDescriptionsByCareer(String career);
    
    /**
     * Return active catalog descriptions that correspond to the given academic career with flag to indicate if we return or not inactive catalog descriptions
     */
    public List<CatalogDescription> getCatalogDescriptionsByCareer(String career, boolean showInactives);
    
    
    /**
     * Return all active catalog descriptions for certificates programs
     * (can be null for all)
     * 
     * @return - the list of CatalogDescription
     */
    
    public List<CatalogDescription> getAllCatalogDescriptionsForCertificate();
    
    /**
     * Return all active catalog descriptions for certificates programs  with flag to indicate if we return or not inactive catalog descriptions
     */
    
    public List<CatalogDescription> getAllCatalogDescriptionsForCertificate(boolean showInactives);
    
    
    /**
     * Return active catalog descriptions that correspond to the given criteria map
     * (can be null for all)
     * 
     * @return - the list of CatalogDescription
     */
    public List<CatalogDescription> getCatalogDescriptions(Map<String, String> eqCriteria, Map<String, String> searchCriteria);

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
     * @return all active catlog descriptions
     */
    public List<CatalogDescription> getAllCatalogDescriptions();
    
    
    /**
     * Set all catalog description to "inactive" status
     * 
     */
    public void setCatalogDescriptionToInactive();
    
    /**
     * Get catalog description from department that have no description and which are not certificate courses
     * 
     */
    public List<CatalogDescription> getCatalogDescriptionsByDepartmentWithNoDescription(String department);
    
    
    /**
     * Get catalog description for certificates courses that have no description
     * 
     */
    public List<CatalogDescription> getAllCatalogDescriptionsForCertificatesWithNoDescription();
    
    /**
     * Get all departments that have at least one catalog description with an empty description
     * 
     */
    public List<String> getDepartmentNameWithAtLeastOneCaWithNoDescription();
    
 
    /**
     * return whether or not the specified Catalog Description exists.
     * 
     * @param course_id - the course_id of the catalog description
     * @return true if the catalog description exists, false otherwise
     */
    public boolean descriptionExists(String course_id);
}
