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
public class CatalogDescription implements Entity, Comparable {
	
	private String courseId;
	private String title;
	private String description;
	private String department;
	private String career;
	private String requirements;
	private String language;
	
	private String createdBy;
	private Date createdDate; 
	
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUrl(String rootProperty) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getReference(String rootProperty) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public ResourceProperties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public Element toXml(Document doc, Stack stack) {
		// TODO Auto-generated method stub
		return null;
	}


}
