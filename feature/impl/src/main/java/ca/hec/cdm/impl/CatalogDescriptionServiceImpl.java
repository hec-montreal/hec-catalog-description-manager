package ca.hec.cdm.impl;

import java.util.List;

import lombok.Setter;

import ca.hec.cdm.api.CatalogDescriptionDao;
import ca.hec.cdm.api.CatalogDescriptionService;
import ca.hec.cdm.model.CatalogDescription;

public class CatalogDescriptionServiceImpl implements CatalogDescriptionService {
	@Setter
	private CatalogDescriptionDao catalogDescriptionDao;
	
	public void init() {
		// TODO Auto-generated method stub
	}

	public CatalogDescription getCatalogDescription(Long id) {
		return catalogDescriptionDao.getCatalogDescription(id);
	}
	
	public List getCatalogDescriptionsByCareer(String career) {
		return catalogDescriptionDao.getCatalogDescriptionsByCareer(career);
	}
	
	public List getCatalogDescriptionsByDepartment(String department) {
		return catalogDescriptionDao.getCatalogDescriptionsByDepartment(department);
	}
	
	public boolean updateDescription(Long id, String description) {
		CatalogDescription cd = getCatalogDescription(id);
		cd.setDescription(description);
		
		return catalogDescriptionDao.saveCatalogDescription(cd);
	}
}
