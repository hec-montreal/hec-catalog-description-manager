package ca.hec.cdm.entityprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.beans.factory.annotation.Autowired;

import ca.hec.cdm.api.CatalogDescriptionService;
import ca.hec.cdm.logic.SakaiProxy;
import ca.hec.cdm.model.CatalogDescription;
import ca.hec.cdm.model.SimpleCatalogDescription;
import ca.hec.portal.api.PortalManagerService;

public class CatalogDescriptionEntityProviderImpl extends AbstractEntityProvider 
	implements CoreEntityProvider, AutoRegisterEntityProvider, Resolvable, Sampleable, Outputable, ActionsExecutable {

    public final static String ENTITY_PREFIX = "catalogDescription";

    // should this be the proxy?
    @Setter
    private CatalogDescriptionService catalogDescriptionService;
    
    @Setter
    private PortalManagerService portalManagerService;
    
    @Setter
    @Autowired
    private SakaiProxy sakaiProxy;

    public String getEntityPrefix() {
	return ENTITY_PREFIX;
    }

    public String[] getHandledOutputFormats() {
	return new String[] { Formats.JSON };
    }

    @EntityCustomAction(action = "getCatalogDescriptionsByDepartment", viewKey = EntityView.VIEW_LIST)
    public List<?> getCatalogDescriptionsByDepartment(EntityView view,
	    Map<String, Object> params) {

	String department = view.getPathSegment(2);

	// check that department is supplied
	if (StringUtils.isBlank(department)) {
	    throw new IllegalArgumentException(
		    "Department must be set in order to get the catalog descriptions via the URL /catalog-description/department/departmentId");
	}

	return simplifyCatalogDescriptions(catalogDescriptionService
		.getCatalogDescriptionsByDepartment(department));
    }

    @EntityCustomAction(action = "getCatalogDescriptionsByCareer", viewKey = EntityView.VIEW_LIST)
    public List<?> getCatalogDescriptionsByCareer(EntityView view,
	    Map<String, Object> params) {

	String career = view.getPathSegment(2);

	// check that department is supplied
	if (StringUtils.isBlank(career)) {
	    throw new IllegalArgumentException(
		    "Career must be set in order to get the catalog descriptions via the URL /catalog-description/career/careerId");
	}

	return simplifyCatalogDescriptions(catalogDescriptionService
		.getCatalogDescriptionsByCareer(career));
    }

    @EntityCustomAction(action = "getDepartments", viewKey = EntityView.VIEW_LIST)
    public List<?> getDepartments(EntityView view, Map<String, Object> params) {

	return catalogDescriptionService
		.getDepartmentNameWithAtLeastOneCaWithNoDescription();
    }

    @EntityCustomAction(action = "getCareers", viewKey = EntityView.VIEW_LIST)
    public List<?> getCareers(EntityView view, Map<String, Object> params) {

	return catalogDescriptionService
		.getCareerNameWithAtLeastOneCaWithNoDescription();
    }

    public Object getEntity(EntityReference ref) {
	return simplifyCatalogDescription(catalogDescriptionService.getCatalogDescription(ref.getId()));
    }

    public boolean entityExists(String course_id) {
	return catalogDescriptionService.descriptionExists(course_id);
    }

    public List<SimpleCatalogDescription> simplifyCatalogDescriptions(
	    List<CatalogDescription> catalogDescriptions) {
	List<SimpleCatalogDescription> simpleCatalogDescriptions =
		new ArrayList<SimpleCatalogDescription>();

	// convert raw CatalogDescriptions into decorated catalog descriptions
	for (CatalogDescription cd : catalogDescriptions) {
	simpleCatalogDescriptions.add(simplifyCatalogDescription(cd));	
	}

	return simpleCatalogDescriptions;
    }
    
    public SimpleCatalogDescription simplifyCatalogDescription(CatalogDescription cd) {
	SimpleCatalogDescription scd = new SimpleCatalogDescription();
	
	scd.setTitle(cd.getTitle());
	scd.setDescription(cd.getDescription());
	scd.setDepartment(portalManagerService.getDescriptionDepartment(cd.getDepartment()));
	scd.setCareer(portalManagerService.getDescriptionCareer(cd.getCareer()));
	scd.setRequirements(cd.getRequirements());
	scd.setCourseId(cd.getCourseId());
	scd.setCredits("" + cd.getCredits());
	scd.setLang(cd.getLanguage().substring(0, 2));
	    
	// TODO: make this conditional, only if catalog description didn't have one in the db
	scd.setSpecificCourse(sakaiProxy.getSpecificCourse(cd.getCourseId()));
	
	return scd;
    }

    public Object getSampleEntity() {
	return new SimpleCatalogDescription();
    }
}
