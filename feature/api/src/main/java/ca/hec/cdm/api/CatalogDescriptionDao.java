package ca.hec.cdm.api;

import java.util.List;

import ca.hec.cdm.model.CatalogDescription;

/**
 * This is the interface for the Dao for our Catalog Description tool, 
 * it handles the database access functionality of the tool
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
     * Return a list of CatalogDescriptions by department
     * 
     * @param department - the department for which the catalog descriptions should be returned
     * @return - the list of CatalogDescriptions
     */
	public List<CatalogDescription> getCatalogDescriptionsByDepartment(String department);
	
	/**
	 * save the catalog description to the database
	 * @param cd - the catalog description to save
	 * @return - true for success, false if failure
	 */
	public boolean saveCatalogDescription(CatalogDescription cd);
	
}