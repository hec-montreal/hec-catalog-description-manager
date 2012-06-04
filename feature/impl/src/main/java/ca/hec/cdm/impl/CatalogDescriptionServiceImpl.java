package ca.hec.cdm.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

import org.sakaiproject.tool.api.SessionManager;

import ca.hec.cdm.api.CatalogDescriptionService;
import ca.hec.cdm.dao.CatalogDescriptionDao;
import ca.hec.cdm.model.CatalogDescription;

public class CatalogDescriptionServiceImpl implements CatalogDescriptionService {

	// beans to be injected by spring need a setter
	@Setter	private SessionManager sessionManager;
	@Setter	private CatalogDescriptionDao catalogDescriptionDao;
	
	public List getCatalogDescriptions() {
		
		List catalogDescriptions = new ArrayList<CatalogDescription>();
		
		CatalogDescription cd = new CatalogDescription();
		
		cd.setTitle("Le Titre du cours");
		cd.setDescription("La description du cours");
		
		catalogDescriptions.add(cd);
		
		return catalogDescriptions;
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}
}
