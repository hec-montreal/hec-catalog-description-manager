package ca.hec.cdm.dao;

import java.util.List;

import ca.hec.cdm.model.CatalogDescription;

/**
 * 
 * @author <a href="mailto:curtis.van-osch@hec.ca">Curtis van Osch</a>
 * @version $Id: $
 */
public interface CatalogDescriptionDao {
	
    /**
     * 
     * 
     * @param 
     * @return
     */
	public List<CatalogDescription> getCatalogDescriptions();
	
	public void init();
}
