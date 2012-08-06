
package ca.hec.cdm.model;

import lombok.Data;

/**
 * Class to hold the subset of the fields that we want to display
 */
@Data
public class SimpleCatalogDescription implements Comparable<Object> {

	private String courseId;
	private String title;
	private String description;
	private String department;
	private String career;
	private String requirements;
	private String credits;
	private String lang;

	public int compareTo(Object o) {
	    // TODO Auto-generated method stub
	    return 0;
	}
}



