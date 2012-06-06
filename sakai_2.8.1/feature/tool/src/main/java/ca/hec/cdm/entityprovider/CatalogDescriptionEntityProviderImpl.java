package ca.hec.cdm.entityprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.entitybroker.EntityView;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.annotations.EntityCustomAction;
import org.sakaiproject.entitybroker.entityprovider.capabilities.ActionsExecutable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.Outputable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.Resolvable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.Sampleable;
import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.util.AbstractEntityProvider;

import ca.hec.cdm.api.CatalogDescriptionService;
import ca.hec.cdm.model.CatalogDescription;

public class CatalogDescriptionEntityProviderImpl extends AbstractEntityProvider 
	implements CoreEntityProvider, AutoRegisterEntityProvider, Resolvable, Sampleable, Outputable, ActionsExecutable {

	public final static String ENTITY_PREFIX = "catalog-description";
	
	// should this be the proxy?
	@Setter 
	private CatalogDescriptionService catalogDescriptionService;
	
	public String getEntityPrefix() {
		return ENTITY_PREFIX;
	}

	public String[] getHandledOutputFormats() {
		return new String[] { Formats.JSON };
	}

	@EntityCustomAction(action="department", viewKey=EntityView.VIEW_LIST)
	public List<?> getCatalogDescriptionsByDepartment(EntityView view, Map<String, Object> params) {

		// get department
		String department = view.getPathSegment(2);

		//check that department is supplied
		if (StringUtils.isBlank(department)) {
			throw new IllegalArgumentException("Department must be set in order to get the catalog descriptions via the URL /catalog-description/department/departmentId");
		}

		return simplifyCatalogDescriptions(
				catalogDescriptionService.getCatalogDescriptionsByDepartment(department));
	}

//	@EntityCustomAction(action="program", viewKey=EntityView.VIEW_LIST)
//	public List<?> getCatalogDescriptionsByProgram(EntityView view, Map<String, Object> params) {
//
//		// get program
//		String career = view.getPathSegment(2);
//
//		//check that department is supplied
//		if (StringUtils.isBlank(career)) {
//			throw new IllegalArgumentException("Program must be set in order to get the catalog descriptions via the URL /catalog-description/program/programId");
//		}
//
//		List<SimpleCatalogDescription> decoratedCatalogDescriptions = new ArrayList<SimpleCatalogDescription>();
//		
//		//convert raw CatalogDescriptions into decorated catalog descriptions
//		for (int i = 0; i < 5; i++)
//		{
//			SimpleCatalogDescription dcd = 
//					new SimpleCatalogDescription();
//			
//			dcd.setTitle("Catalog Description " + i);
//			dcd.setDescription("La description du plan de cours annuaire");
//			dcd.setDepartment("department");
//			dcd.setCareer(career);
//			decoratedCatalogDescriptions.add(dcd);
//		}
//
//		return decoratedCatalogDescriptions;
//	}

	public Object getEntity(EntityReference ref) {
		return new String("a single entity");
	}

	public boolean entityExists(String id) {
		return false;
	}
	
	public List<SimpleCatalogDescription> simplifyCatalogDescriptions(List<CatalogDescription> catalogDescriptions) {
		List<SimpleCatalogDescription> simpleCatalogDescriptions = new ArrayList<SimpleCatalogDescription>();

		//convert raw CatalogDescriptions into decorated catalog descriptions
		for (CatalogDescription cd : catalogDescriptions)
		{
			SimpleCatalogDescription scd = 
					new SimpleCatalogDescription();
						
			scd.setTitle(cd.getTitle());
			scd.setDescription(cd.getDescription());
			scd.setDepartment(cd.getDepartment());
			scd.setCareer(cd.getCareer());
			scd.setRequirements(cd.getRequirements());
			
			simpleCatalogDescriptions.add(scd);
		}

		return simpleCatalogDescriptions;
	}
	
	/**
	 * Class to hold the subset of the fields that we want to display 
	 */
	@Data
	public class SimpleCatalogDescription implements Comparable<Object>{
		private String title;
		private String description;
		
		private String department;
		private String career;
		private String requirements;
		
		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	public Object getSampleEntity() {
		return new SimpleCatalogDescription();
	}
}
