package ca.hec.cdm.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ca.hec.cdm.api.CatalogDescriptionDao;
import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.model.CatalogDescription;

public class CatalogDescriptionDaoImpl extends HibernateDaoSupport implements
	CatalogDescriptionDao {
    private static Log log = LogFactory.getLog(CatalogDescriptionDaoImpl.class);

    public void init() {
	log.info("init");
    }

    public CatalogDescription getCatalogDescription(Long id) {
	return (CatalogDescription) getHibernateTemplate().get(
		CatalogDescription.class, id);
    }

    public List<CatalogDescription> getCatalogDescriptions() {
	DetachedCriteria dc =
		DetachedCriteria.forClass(CatalogDescription.class)
			.add(Restrictions.eq("active", true));

	List<CatalogDescription> catalogDescriptions =
		new ArrayList<CatalogDescription>();
	for (Object o : getHibernateTemplate().findByCriteria(dc)) {
	    catalogDescriptions.add((CatalogDescription) o);
	}
	return catalogDescriptions;
    }

    public List<CatalogDescription> getCatalogDescriptionsByCareer(String career) {
	DetachedCriteria dc =
		DetachedCriteria.forClass(CatalogDescription.class)
			.add(Restrictions.eq("career", career.toUpperCase()))
			.add(Restrictions.eq("active", true));

	List<CatalogDescription> catalogDescriptions =
		new ArrayList<CatalogDescription>();
	for (Object o : getHibernateTemplate().findByCriteria(dc)) {
	    catalogDescriptions.add((CatalogDescription) o);
	}
	return catalogDescriptions;
    }

    public List<CatalogDescription> getCatalogDescriptionsByDepartment(
	    String department) {
	DetachedCriteria dc =
		DetachedCriteria
			.forClass(CatalogDescription.class)
			.add(Restrictions.eq("department",
				department.toUpperCase()))
			.add(Restrictions.eq("active", true));

	List<CatalogDescription> catalogDescriptions =
		new ArrayList<CatalogDescription>();

	for (Object o : getHibernateTemplate().findByCriteria(dc)) {
	    catalogDescriptions.add((CatalogDescription) o);
	}

	return catalogDescriptions;
    }

    public CatalogDescription getCatalogDescription(String course_id) {
	// there should only ever be one active description, but it can't hurt
	// to order by db id.
	DetachedCriteria dc =
		DetachedCriteria
			.forClass(CatalogDescription.class)
			.add(Restrictions.eq("courseId",
				course_id.toUpperCase()))
			.add(Restrictions.eq("active", true))
			.addOrder(Order.desc("id"));

	return (CatalogDescription) getHibernateTemplate().findByCriteria(dc)
		.get(0);
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

    public List<CatalogDescription> getCatalogDescriptionsByDepartmentWithNoDescription(
	    String department) {

	DetachedCriteria dc =
		DetachedCriteria
			.forClass(CatalogDescription.class)
			.add(Restrictions.eq("department",
				department.toUpperCase()))
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

    public List<String> getCareerNameWithAtLeastOneCaWithNoDescription() {
	return (List<String>) getHibernateTemplate()
		.find("select distinct cd.career from CatalogDescription cd where cd.description is null");
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
