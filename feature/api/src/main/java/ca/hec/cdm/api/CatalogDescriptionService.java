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
	 * Access a list of catalog descriptions
	 * 
	 * @param 
	 *        
	 * @return A list of catalog description objects that meet the criteria; may be empty
	 */
	public List getCatalogDescriptionsByDepartment(String department);
	
	/**
	 * 
	 * @param 
	 *        
	 * @return 
	 */
	public boolean updateDescription(Long id, String description);
}
