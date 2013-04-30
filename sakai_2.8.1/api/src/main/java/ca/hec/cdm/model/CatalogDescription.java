/**
 * 
 */
package ca.hec.cdm.model;

import java.util.Date;

import lombok.Data;

/**
 * <p>
 * CatalogDescription is the object for catalog descriptions for use with the Catalog Description Manager tool.
 * 
 * Does not implement org.sakaiproject.entity.api.Entity, though it could (but be careful with getId() as it exists there too but returns int)
 * </p>
 */
@Data
public class CatalogDescription {
	
	private Long id;
	private String courseId;
	private String title;
	private String description;
	private String department;
	private String career;
	private String requirements;
	
	private Float credits;
	private boolean active;
	private String language;

	private String lastModifiedBy;
	private Date lastModifiedDate;
	
	private String createdBy;
	private Date createdDate; 
}
