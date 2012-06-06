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
     * 
     * 
     * @param 
     * @return
     */
	public CatalogDescription getCatalogDescription(Long id);

	/**
     * 
     * 
     * @param 
     * @return
     */
	public List<CatalogDescription> getCatalogDescriptionsByDepartment(String department);
	
	/**
	 * save the catalog description to the database
	 * @param cd - the catalog description to save
	 * @return - true for success, false if failure
	 */
	public boolean saveCatalogDescription(CatalogDescription cd);
	
}
