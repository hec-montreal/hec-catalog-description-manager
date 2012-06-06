package ca.hec.cdm.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ca.hec.cdm.api.CatalogDescriptionDao;
import ca.hec.cdm.model.CatalogDescription;


public class CatalogDescriptionDaoImpl extends HibernateDaoSupport implements CatalogDescriptionDao 
{
    private static Log log = LogFactory.getLog(CatalogDescriptionDaoImpl.class);

	public void init() {
		// TODO Auto-generated method stub
	}

	public CatalogDescription getCatalogDescription(Long id) {
		return (CatalogDescription)getHibernateTemplate().get(CatalogDescription.class, id);
	}
	
	public List<CatalogDescription> getCatalogDescriptionsByDepartment(String department) {
		DetachedCriteria dc = DetachedCriteria.forClass(CatalogDescription.class)
				.add(Restrictions.eq("department", department));
		
		return getHibernateTemplate().findByCriteria(dc);
	}

	public boolean saveCatalogDescription(CatalogDescription cd) {
		try{
			getHibernateTemplate().saveOrUpdate(cd);
			return true;
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			log.error("Hibernate could not save: " + e.toString());
			return false;
		}
	}
}
