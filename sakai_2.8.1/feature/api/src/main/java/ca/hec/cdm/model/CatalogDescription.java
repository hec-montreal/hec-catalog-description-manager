/**
 * 
 */
package ca.hec.cdm.model;

import java.util.Date;
import java.util.Stack;

import lombok.Data;

import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.ResourceProperties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p>
 * CatalogDescription is the base interface for catalog descriptions for use with the Catalog Description Manager tool.
 * </p>
 */
@Data
public class CatalogDescription implements Comparable {
	
	private Long id;
	private String courseId;
	private String title;
	private String description;
	private String department;
	private String career;
	private String requirements;
	
	private Float credits;
	private Date effectiveDate;
	private Character status;
	private Long ancestorId;
	private String language;

	private String lastModifiedBy;
	private Date lastModifiedDate;
	
	private String createdBy;
	private Date createdDate; 
	
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
