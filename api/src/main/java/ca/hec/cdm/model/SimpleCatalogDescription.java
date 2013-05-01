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

	
	// If there is not 3 parts in the catalog description (numSession - numCourse - yearCourse) then we put it at the end of the list
	if (comparableCatalogDescriptionTitleSections.size() != 3){
	    return -1;
	}
	if (catalogDescriptionTitleSections.size() != 3){
	    return 1;
	}
	
	
	String comparableNumCourse = null;
	String comparableYearCourse = null;
	String stringComparableNumSession = comparableCatalogDescriptionTitleSections.get(0);
	boolean isComparableNumSessionNumeric = stringComparableNumSession.matches("^[\\d]+$");	

	String numCourse = null;
	String yearCourse = null;
	String stringNumSession =  catalogDescriptionTitleSections.get(0);
	boolean isNumSessionNumeric = stringNumSession.matches("^[\\d]+$");
	
	int resultComparaisonNumSession;

	try {
	    //We compare number if the course num sessions are numbers
	    if (isComparableNumSessionNumeric && isNumSessionNumeric) {
		resultComparaisonNumSession =
			Integer.valueOf(stringNumSession).compareTo(Integer.valueOf(stringComparableNumSession));
	    }
	    //We compare strings if the course num sessions are strings
	    else if (!isComparableNumSessionNumeric && !isNumSessionNumeric) {
		resultComparaisonNumSession =
			stringNumSession.compareTo(stringComparableNumSession);
	    }
	    //if the first course session is a number and the second a string, the first goes upper
	    else if (isComparableNumSessionNumeric && !isNumSessionNumeric) {
		resultComparaisonNumSession = 1;
	    }
	    //if the first course session is a string and the second a number, the second goes upper
	    else if (!isComparableNumSessionNumeric && isNumSessionNumeric) {
		resultComparaisonNumSession = -1;
	    }
	    else{
		resultComparaisonNumSession = 0;
	    }

	    comparableNumCourse =
		    comparableCatalogDescriptionTitleSections.get(1);
	    comparableYearCourse =
		    comparableCatalogDescriptionTitleSections.get(2);
	    numCourse = catalogDescriptionTitleSections.get(1);
	    yearCourse = catalogDescriptionTitleSections.get(2);
	} catch (Exception e) {
	    e.printStackTrace();
	    return 0;
	}

	if (resultComparaisonNumSession != 0) {
	    return resultComparaisonNumSession;
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
