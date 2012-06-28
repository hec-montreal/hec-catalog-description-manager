package ca.hec.cdm.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ca.hec.cdm.api.CatalogDescriptionDao;
import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.model.CatalogDescription;


public class CatalogDescriptionDaoImpl extends HibernateDaoSupport implements CatalogDescriptionDao 
{
    private static Log log = LogFactory.getLog(CatalogDescriptionDaoImpl.class);

	public void init() {
		log.info("init");
	}

	public CatalogDescription getCatalogDescription(Long id) {
		return (CatalogDescription)getHibernateTemplate().get(CatalogDescription.class, id);
	}
	
	public List<CatalogDescription> getCatalogDescriptionsByCareer(String career) {
		DetachedCriteria dc = DetachedCriteria.forClass(CatalogDescription.class)
				.add(Restrictions.eq("career", career.toUpperCase()));
		
		return getHibernateTemplate().findByCriteria(dc);
	}

	public List<CatalogDescription> getCatalogDescriptionsByDepartment(String department) {
		DetachedCriteria dc = DetachedCriteria.forClass(CatalogDescription.class)
				.add(Restrictions.eq("department", department.toUpperCase()));
		
		List<CatalogDescription> catalogDescriptions = new ArrayList<CatalogDescription>();
		for (Object o : getHibernateTemplate().findByCriteria(dc))
		{
			catalogDescriptions.add((CatalogDescription)o);
		}
		return catalogDescriptions;
	}

	public void saveCatalogDescription(CatalogDescription cd) 
			throws StaleDataException, DatabaseException {
		try{
			getHibernateTemplate().saveOrUpdate(cd);
		}
		//TODO why arent these caught?
		catch(HibernateOptimisticLockingFailureException e) {
			e.printStackTrace();
			throw new StaleDataException();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new DatabaseException();
		}
	}
}
