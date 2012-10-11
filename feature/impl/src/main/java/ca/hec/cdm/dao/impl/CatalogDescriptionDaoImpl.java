package ca.hec.cdm.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ca.hec.cdm.api.CatalogDescriptionDao;
import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.model.CatalogDescription;
import ca.hec.cdm.util.Stopwords;

public class CatalogDescriptionDaoImpl extends HibernateDaoSupport implements
	CatalogDescriptionDao {

    private static final String CERTIFICATE = "CERT";

    private static Log log = LogFactory.getLog(CatalogDescriptionDaoImpl.class);

    @Setter
    @Getter
    private Stopwords stopWordList = null;

    public void init() {
	log.info("init");
    }

    public CatalogDescription getCatalogDescription(Long id) {
	return (CatalogDescription) getHibernateTemplate().get(
		CatalogDescription.class, id);
    }

    public CatalogDescription getCatalogDescription(String course_id) {

	CatalogDescription catDesc = null;

	// there should only ever be one active description, but it can't hurt
	// to order by db id.
	DetachedCriteria dc =
		DetachedCriteria
			.forClass(CatalogDescription.class)
			.add(Restrictions.eq("courseId",
				course_id.toUpperCase()))
			.add(Restrictions.eq("active", true))
			.addOrder(Order.desc("id"));

	List descList = getHibernateTemplate().findByCriteria(dc);

	if (descList != null && descList.size() != 0) {
	    catDesc = (CatalogDescription) descList.get(0);
	}

	return catDesc;
    }

    public List<CatalogDescription> getCatalogDescriptionsByDepartment(
	    String department) {
	HashMap<String, String> criteria = new HashMap<String, String>();
	criteria.put("department", department);

	return getCatalogDescriptions(criteria);
    }

    public List<CatalogDescription> getCatalogDescriptionsByCareer(String career) {
	HashMap<String, String> criteria = new HashMap<String, String>();
	criteria.put("career", career);

	return getCatalogDescriptions(criteria);
    }

    public List<CatalogDescription> getAllCatalogDescriptions() {
	return getCatalogDescriptions(null);
    }

    public List<CatalogDescription> getAllCatalogDescriptionsForCertificate() {
	return getCatalogDescriptionsByCareer(CERTIFICATE);
    }

    public List<CatalogDescription> getCatalogDescriptions(
	    Map<String, String> eqCriteria) {
	return getCatalogDescriptions(eqCriteria, null);
    }

    /**
     * get catalog descriptions according to criterias passed in parameter
     * 
     * @param eqCriteria: criterias such as (department = XX) or (career = YY)
     *            used to filter results
     * @param searchCriteria: criterias from the search toolbar: (description
     *            like %KEYWORD%) or (title like %KEYWORD%)
     * @return
     */
    public List<CatalogDescription> getCatalogDescriptions(
	    Map<String, String> eqCriteria, Map<String, String> searchCriteria) {

	// add each of the search criteria in the map to the Hibernate
	// DetachedCriteria object
	if (eqCriteria != null && !eqCriteria.isEmpty()) {
	    return getCatalogDescriptionsByItem(eqCriteria);
	}

	if (searchCriteria != null) {
	    return getCatalogDescriptionsSearch(searchCriteria);
	}

	return new ArrayList<CatalogDescription>();
    }

    public List<CatalogDescription> getCatalogDescriptionsSearch(
	    Map<String, String> criteria) {
	List<CatalogDescription> catalogDescriptions =
		new ArrayList<CatalogDescription>();

	DetachedCriteria dc =
		DetachedCriteria.forClass(CatalogDescription.class).add(
			Restrictions.eq("active", true));

	// We create a disjunction because the search return catalog
	// descriptions that have at least one keyword in their
	// description/title (OR operator between criterias)
	Disjunction searchCriteriasDisjunction = Restrictions.disjunction();

	// We create a projection that will store an accuracy integer for the
	// result set (the higher the accuracy the higher the number)
	StringBuilder accuracyProjection = new StringBuilder();

	for (Map.Entry<String, String> entry : criteria.entrySet()) {
	    List<String> listPossibleValues =
		    Arrays.asList(entry.getValue().split(","));
	    for (String searchValue : listPossibleValues) {
		if (!stopWordList.isStopword(searchValue)) { // we don't add
							     // stopWords to the
							     // search
		    // we don't have the same matching rule if the search word is the beginning of an course id
		    if (isCourseIdSearch(searchValue)){
			searchCriteriasDisjunction.add(Restrictions.ilike(
				    "courseId", searchValue + "%"));
		    }
		    else{
			searchCriteriasDisjunction.add(Restrictions.ilike(
				    entry.getKey(), "%" + searchValue + "%"));
			    if ("title".equals(entry.getKey())) { // we calculate the
								  // accuracy with the
								  // number of search
								  // words that are in
								  // the title
				accuracyProjection.append("+ (CONTAINS(title,'%"
					+ searchValue + "%')) ");
			    }		    
		    }
		}
	    }
	}

	

	dc.add(searchCriteriasDisjunction);

	ProjectionList projectList = Projections.projectionList();

	projectList.add(Projections.property("courseId"));
	projectList.add(Projections.property("title"));
	projectList.add(Projections.property("department"));
	projectList.add(Projections.property("career"));
	projectList.add(Projections.property("language"));
	projectList.add(Projections.property("description"));

	if (accuracyProjection.length() != 0){
	    accuracyProjection.append("  as accuracy ");
		accuracyProjection.deleteCharAt(0);
		projectList.add(Projections.alias(Projections.sqlProjection(
			accuracyProjection.toString(), new String[] { "accuracy" },
			new Type[] { Hibernate.INTEGER }), "accuracy"));
		// We sort the result set by accuracy
		dc.addOrder(Order.desc("accuracy"));
	}
	else{
	    dc.addOrder(Order.asc("courseId"));
	}
	

	dc.setProjection(projectList);

	

	for (Object cdproperties : getHibernateTemplate().findByCriteria(dc)) {
	    CatalogDescription cd = new CatalogDescription();
	    cd.setCourseId("" + ((Object[]) cdproperties)[0]);
	    cd.setTitle("" + ((Object[]) cdproperties)[1]);
	    cd.setDepartment("" + ((Object[]) cdproperties)[2]);
	    cd.setCareer("" + ((Object[]) cdproperties)[3]);
	    cd.setLanguage("" + ((Object[]) cdproperties)[4]);
	    catalogDescriptions.add(cd);
	}

	return catalogDescriptions;
    }
    
    //we consider that if the first charcter of the searc word is a number, then it is a course id search
    private boolean isCourseIdSearch(String searchWord){
	return searchWord.matches("^[\\d].*");
    }

    public List<CatalogDescription> getCatalogDescriptionsByItem(
	    Map<String, String> criteria) {
	List<CatalogDescription> catalogDescriptions =
		new ArrayList<CatalogDescription>();

	DetachedCriteria dc =
		DetachedCriteria.forClass(CatalogDescription.class).add(
			Restrictions.eq("active", true));

	for (Map.Entry<String, String> entry : criteria.entrySet()) {
	    List<String> listPossibleValues =
		    Arrays.asList(entry.getValue().split(","));
	    dc.add(Restrictions.in(entry.getKey(), listPossibleValues));
	}

	for (Object o : getHibernateTemplate().findByCriteria(dc)) {
	    catalogDescriptions.add((CatalogDescription) o);
	}

	return catalogDescriptions;
    }

    public List<String> getListCourseId() {
	return (List<String>) getHibernateTemplate().find(
		"select distinct cd.courseId from CatalogDescription cd");
    }

    public void saveCatalogDescription(CatalogDescription cd)
	    throws StaleDataException, DatabaseException {
	try {
	    getHibernateTemplate().saveOrUpdate(cd);
	}
	// TODO why aren't these caught?
	catch (HibernateOptimisticLockingFailureException e) {
	    e.printStackTrace();
	    throw new StaleDataException();
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new DatabaseException();
	}
    }

    public void setCatalogDescriptionToInactive() {
	getHibernateTemplate().bulkUpdate(
		"update CatalogDescription cd set cd.active=false");
    }

    public CatalogDescription getLastVersionCatalogDescription(String courseId) {
	DetachedCriteria dc =
		DetachedCriteria.forClass(CatalogDescription.class)
			.add(Restrictions.eq("courseId", courseId))
			.addOrder(Order.desc("id"));

	return (CatalogDescription) getHibernateTemplate().findByCriteria(dc,
		0, 1).get(0);
    }

    public List<CatalogDescription> getAllCatalogDescriptionsForCertificatesWithNoDescription() {

	DetachedCriteria dc =
		DetachedCriteria.forClass(CatalogDescription.class)
			.add(Restrictions.eq("career", CERTIFICATE))
			.add(Restrictions.eq("active", true))
			.add(Restrictions.isNull("description"));

	List<CatalogDescription> catalogDescriptions =
		new ArrayList<CatalogDescription>();

	for (Object o : getHibernateTemplate().findByCriteria(dc)) {
	    catalogDescriptions.add((CatalogDescription) o);
	}

	return catalogDescriptions;
    }

    public List<CatalogDescription> getCatalogDescriptionsByDepartmentWithNoDescription(
	    String department) {

	DetachedCriteria dc =
		DetachedCriteria
			.forClass(CatalogDescription.class)
			.add(Restrictions.eq("department",
				department.toUpperCase()))
			.add(Restrictions.ne("career", CERTIFICATE))
			.add(Restrictions.eq("active", true))
			.add(Restrictions.isNull("description"));

	List<CatalogDescription> catalogDescriptions =
		new ArrayList<CatalogDescription>();

	for (Object o : getHibernateTemplate().findByCriteria(dc)) {
	    catalogDescriptions.add((CatalogDescription) o);
	}

	return catalogDescriptions;
    }

    public List<String> getDepartmentNameWithAtLeastOneCaWithNoDescription() {
	return (List<String>) getHibernateTemplate()
		.find("select distinct cd.department from CatalogDescription cd where cd.description is null");
    }

    public boolean descriptionExists(String course_id) {
	DetachedCriteria dc =
		DetachedCriteria.forClass(CatalogDescription.class)
			.add(Restrictions.eq("courseId", course_id))
			.add(Restrictions.eq("active", true));

	if (getHibernateTemplate().findByCriteria(dc).isEmpty()) {
	    return false;
	} else {
	    return true;
	}
    }

}
