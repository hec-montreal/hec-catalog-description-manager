/**
 * 
 */
package ca.hec.cdm.api;

import org.sakaiproject.entity.api.Entity;

/**
 * <p>
 * CatalogDescription is the base interface for catalog descriptions for use with the Catalog Description Manager tool.
 * </p>
 */
public interface CatalogDescription extends Entity, Comparable {
	/**
	 * Access the title.
	 * 
	 * @return The title.
	 */
	public String getTitle();


}
