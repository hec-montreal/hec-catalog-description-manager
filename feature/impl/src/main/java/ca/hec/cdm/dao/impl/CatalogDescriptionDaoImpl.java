package ca.hec.cdm.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ca.hec.cdm.dao.CatalogDescriptionDao;
import ca.hec.cdm.model.CatalogDescription;


public class CatalogDescriptionDaoImpl extends HibernateDaoSupport implements
		CatalogDescriptionDao {

    private static Log log = LogFactory.getLog(CatalogDescriptionDaoImpl.class);

	public List<CatalogDescription> getCatalogDescriptions() {
		
		List<CatalogDescription> catalogDescriptions = new ArrayList<CatalogDescription>();
		CatalogDescription cd = new CatalogDescription();
		cd.setTitle("le titre du cd");
		
		catalogDescriptions.add(cd);
		return catalogDescriptions;
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

}
