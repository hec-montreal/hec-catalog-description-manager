package ca.hec.cdm.api;

import java.util.List;

//import org.sakaiproject.entity.api.EntityProducer;

public interface CatalogDescriptionService //extends EntityProducer 
{
	public void init();
	/**
	 * Access a list of catalog descriptions
	 * 
	 * @param 
	 *        
	 * @return A list of catalog description objects that meet the criteria; may be empty
	 */
	public List getCatalogDescriptions();

}
