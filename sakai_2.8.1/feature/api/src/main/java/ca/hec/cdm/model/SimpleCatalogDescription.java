package ca.hec.cdm.model;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

/**
 * Class to hold the subset of the fields that we want to display
 */
@Data
public class SimpleCatalogDescription implements
	Comparable<SimpleCatalogDescription> {

    private String courseId;
    private String title;
    private String description;
    private String department;
    private String career;
    private String requirements;
    private String credits;
    private String lang;
    private String departmentGroup;
    private String careerGroup;

    // When we sort catalog descriptions we do it according to the session #,
    // course # and year (we get it from courseId parameter)
    public int compareTo(SimpleCatalogDescription cd) {
	List<String> comparableCatalogDescriptionTitleSections =
		Arrays.asList(cd.getCourseId().split("-"));
	List<String> catalogDescriptionTitleSections =
		Arrays.asList(courseId.split("-"));

	Integer comparableNumSession = null;
	String comparableNumCourse = null;
	String comparableYearCourse = null;

	Integer numSession = null;
	String numCourse = null;
	String yearCourse = null;

	try {
	    comparableNumSession =
		    Integer.parseInt(comparableCatalogDescriptionTitleSections
			    .get(0));
	    comparableNumCourse =
		    comparableCatalogDescriptionTitleSections.get(1);
	    comparableYearCourse =
		    comparableCatalogDescriptionTitleSections.get(2);
	    numSession =
		    Integer.parseInt(catalogDescriptionTitleSections.get(0));
	    numCourse = catalogDescriptionTitleSections.get(1);
	    yearCourse = catalogDescriptionTitleSections.get(2);
	} catch (Exception e) {
	    e.printStackTrace();
	    return 0;
	}

	if (numSession.compareTo(comparableNumSession) != 0) {
	    return numSession.compareTo(comparableNumSession);
	}

	else if (numCourse.compareTo(comparableNumCourse) != 0) {
	    return numCourse.compareTo(comparableNumCourse);
	}

	else if (yearCourse.compareTo(comparableYearCourse) != 0) {
	    return yearCourse.compareTo(comparableYearCourse);
	}

	else {
	    return 0;
	}
    }
}
