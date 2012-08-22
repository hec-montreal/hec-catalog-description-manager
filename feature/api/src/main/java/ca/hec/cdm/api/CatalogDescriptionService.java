package ca.hec.cdm.api;

import java.util.List;
import java.util.Map;

import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.model.CatalogDescription;

//import org.sakaiproject.entity.api.EntityProducer;

public interface CatalogDescriptionService // extends EntityProducer
{
    public void init();

    /**
     * Get a Catalog Description by database id
     * 
     * @param id - the db id of the catalog description
     * @return the catalog description with details
     */
    public CatalogDescription getCatalogDescription(Long id);

    /**
     * Get a Catalog Description by course id
     * 
     * @param id - the id of the catalog description
     * @return the catalog description with details
     */
    public CatalogDescription getCatalogDescription(String course_id);

    /**
     * Access a list of catalog descriptions where each key in the criteria map is equal to the value.
     * 
     * @return A list of catalog description objects; may
     *         be empty
     */
    public List<CatalogDescription> getCatalogDescriptions(Map<String, String> criteria);

    /**
     * Get all departments that have at least one catalog description with an
     * empty description
     */
    public List<String> getDepartmentNameWithAtLeastOneCaWithNoDescription();

    /**
     * Get all departments that have at least one catalog description with an
     * empty description
     */
    public List<String> getCareerNameWithAtLeastOneCaWithNoDescription();

    /**
     * Update a CatalogDescription's description in the database
     * 
     * @param cd - the catalog description to update
     * @param description - the description to use for the update
     * @throws StaleDataException
     * @throws DatabaseException
     */
    public void updateDescription(CatalogDescription cd)
	    throws StaleDataException, DatabaseException;

    /**
     * Return whether or not the specified catalog description (by course_id)
     * exists
     * 
     * @param course_id - the course id of the catalog description to update
     * @return true if it exists, false otherwise
     */
    public boolean descriptionExists(String course_id);
}
