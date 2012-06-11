package ca.hec.cdm.api;

import java.util.List;

import ca.hec.cdm.model.CatalogDescription;

//import org.sakaiproject.entity.api.EntityProducer;

public interface CatalogDescriptionService //extends EntityProducer 
{
	public void init();
	
	/**
	 * 
	 * @param 
	 *        
	 * @return 
	 */
	public CatalogDescription getCatalogDescription(Long id);
	
	/**
	 * Access a list of catalog descriptions by career
	 * 
	 * @param career - the career to search on
	 *        
	 * @return A list of catalog description objects that meet the criteria; may be empty
	 */
	public List getCatalogDescriptionsByCareer(String career);
	
	/**
	 * Access a list of catalog descriptions by department
	 * 
	 * @param department - the department to search on
	 *        
	 * @return A list of catalog description objects that meet the criteria; may be empty
	 */
	public List getCatalogDescriptionsByDepartment(String department);
	
	/**
	 * Update a CatalogDescription's description in the database
	 * 
	 * @param id - the database id of the catalog description to update
	 * @param description - the description to use for the update       
	 * 
	 * @return true for success; false otherwise
	 */
	public boolean updateDescription(Long id, String description);
}
