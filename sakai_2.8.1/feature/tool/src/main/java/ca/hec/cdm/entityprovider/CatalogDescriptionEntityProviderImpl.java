package ca.hec.cdm.entityprovider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.CollectionResolvable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.Outputable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.Resolvable;
import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.entityprovider.search.Restriction;
import org.sakaiproject.entitybroker.entityprovider.search.Search;
import org.sakaiproject.entitybroker.util.AbstractEntityProvider;
import org.springframework.beans.factory.annotation.Autowired;

import ca.hec.cdm.logic.SakaiProxy;
import ca.hec.cdm.model.CatalogDescription;
import ca.hec.cdm.model.SimpleCatalogDescription;

public class CatalogDescriptionEntityProviderImpl extends
	AbstractEntityProvider implements CoreEntityProvider,
	AutoRegisterEntityProvider, Resolvable, CollectionResolvable,
	Outputable {

    public final static String ENTITY_PREFIX = "catalogDescription";

    @Setter
    @Autowired
    private SakaiProxy sakaiProxy;

    public String getEntityPrefix() {
	return ENTITY_PREFIX;
    }

    public String[] getHandledOutputFormats() {
	return new String[] { Formats.JSON };
    }

    public Object getEntity(EntityReference ref) {
	return simplifyCatalogDescription(sakaiProxy.getCatalogDescription(ref
		.getId()));
    }

    /** get catalog descriptions according to criterias passed in parameter
     * We separate criterias into 2 categories:
     * @eqCriteria:  criterias such as (department = XX) or (career = YY) used to filter results 
     * @searchCriteria: criterias from the search toolbar: (description like %KEYWORD%) or (title like %KEYWORD%)
     * @return
     */
    public List<?> getEntities(EntityReference ref, Search search) {
	Map<String, String> eqCriteria = new HashMap<String, String>();
	Map<String, String> seachCriteria = new HashMap<String, String>();
	List<String> seachScopesList = null;
	    
	    if  (search.getRestrictionByProperty("searchScope") != null){
		String stringSearchScope = search.getRestrictionByProperty("searchScope").getStringValue();
		seachScopesList = Arrays.asList(stringSearchScope.split(",")); 
		}
	
	for (Restriction r : search.getRestrictions()) {
	    if (r.getProperty().equals("department") ||
		    r.getProperty().equals("career")||
		    r.getProperty().equals("language")){
		eqCriteria.put(r.getProperty(), r.getStringValue());
	    }
	    
	    if  (r.getProperty().equals("searchWords")){
		String stringSearch = r.getStringValue();
		    for (String scope : seachScopesList){
			  seachCriteria.put(scope, stringSearch); 
		    }		
	    }
		
	}
	return simplifyCatalogDescriptions(sakaiProxy.getCatalogDescriptions(eqCriteria, seachCriteria));
    }

    public boolean entityExists(String course_id) {
	return sakaiProxy.catalogDescriptionExists(course_id);
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

    public SimpleCatalogDescription simplifyCatalogDescription(
	    CatalogDescription cd) {
	SimpleCatalogDescription scd = new SimpleCatalogDescription();

	scd.setTitle(cd.getTitle());
	scd.setDescription(cd.getDescription());
	scd.setDepartment(sakaiProxy.getDepartmentDescription(cd
		.getDepartment()));
	scd.setCareer(sakaiProxy.getCareerDescription(cd.getCareer()));
	scd.setRequirements(cd.getRequirements());
	scd.setCourseId(cd.getCourseId());
	scd.setCredits("" + cd.getCredits());

	String lang = cd.getLanguage();
	if (lang != null)
	    scd.setLang(lang.substring(0, 2));

	return scd;
    }

    public Object getSampleEntity() {
	return new SimpleCatalogDescription();
    }
}
