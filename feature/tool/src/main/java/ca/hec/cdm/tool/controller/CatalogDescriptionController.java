package ca.hec.cdm.tool.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.util.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.StaleDataException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ca.hec.cdm.logic.SakaiProxy;
import ca.hec.cdm.model.CatalogDescription;

/* Controller that handle ajax calls made by the Catalog Descriptions Manager front end
 */
@Controller
public class CatalogDescriptionController {
	// DateFormat for serializing the last_modified_date to the level of precision required
	DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

    @Setter
    @Getter
    @Autowired
    private SakaiProxy sakaiProxy = null;

    private ResourceLoader msgs = null; 

    private static Log log = LogFactory.getLog(CatalogDescriptionController.class);

    @PostConstruct
    public void init() {
    	msgs = new ResourceLoader("cdm");
    }

    /*
     * Called whenever a user press the "Save" button of the editor dialog box.
     * It calls the updateCatalogDescription function of the sakai proxy with
     * the parameters entered by the user with the parameters entered by the
     * user It returns a JSON object containing the status of the save opeation.
     */
    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    public ModelAndView saveCatalogDescription(HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
    	
    	String description = request.getParameter("description");
    	// last_modified_date is important for hibernate optimistic locking
    	String last_modified_date = request.getParameter("last_modified_date");
    	String id = request.getParameter("id");
    	Long id_long = Long.decode(id);

    	String returnMessage = null;
    	String returnStatus = null;

    	try {
    		CatalogDescription cd = sakaiProxy.getCatalogDescriptionById(id_long);
    		cd.setDescription(description);
    		cd.setLastModifiedDate(df.parse(last_modified_date));
    		
 			sakaiProxy.updateCatalogDescription(cd);
    		
   			returnMessage = msgs.getString("message_sav_ok");
   			returnStatus = "success";
    	}
    	catch (StaleDataException e) {	
	    	log.error("Exception while saving catalog description: " + e);
    		returnMessage = msgs.getString("message_sav_stale");
    		returnStatus = "failure";
    	}
    	catch (Exception e) {
	    	log.error("Exception while saving catalog description: " + e);
    		returnMessage = msgs.getString("message_sav_nok");
    		returnStatus = "failure";
    	}

    	Map<String, String> model = new HashMap<String, String>();
    	model.put("message", returnMessage);
    	model.put("status", returnStatus);

    	return new ModelAndView("jsonView", model);
    }

    /*
     * Called at the initialisation of the Catalog Descriptions table. It calls
     * the getCatalogDescriptionsForUser function of the sakai proxy and
     * retrieves the list of the Catalog Descriptions (JSON response) that will
     * populate the table
     */
    @RequestMapping(value = "/list.json")
    public ModelAndView listCatalogDescription(HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

    	List<CatalogDescription> listCd =    			
    			sakaiProxy.getCatalogDescriptionsForUser();

    	List<Object> tableValue = new ArrayList<Object>();

    	for (CatalogDescription cd : listCd) {
    		List<String> array = new ArrayList<String>();

    		boolean isDescription =
    				(cd.getDescription() != null && !cd.getDescription()
    				.isEmpty());

    		array.add("" + cd.getId());
    		array.add("" + cd.getCourseId());
    		array.add("" + cd.getTitle());
    		array.add("" + cd.getDepartment());
    		array.add("" + cd.getCareer());
    		array.add("" + isDescription);
    		tableValue.add(array);
    	}

    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put("aaData", tableValue);
    	model.put("sEcho", listCd.size());
    	model.put("iTotalRecords", listCd.size());
    	model.put("iTotalDisplayRecords", listCd.size());

    	return new ModelAndView("jsonView", model);
    }

    /*
     * Called whenever a user click on a row of the Catalog Descriptions table.
     * It calls the getCatalogDescriptionsById function of the sakai proxy and
     * retrieves the properties of the Catalog Description (JSON response)
     * associated with the clicked row
     */
    @RequestMapping(value = "/get.json")
    public ModelAndView getCatalogDescription(HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
    	String id = request.getParameter("id");
    	Long id_long = Long.decode(id);

    	CatalogDescription cd = sakaiProxy.getCatalogDescriptionById(id_long);
    	
    	Map<String, String> model = new HashMap<String, String>();

    	if (cd != null && cd.getId() != null) {
    		model.put("courseid", cd.getCourseId());
    		model.put("last_modified_date", 
    				df.format(cd.getLastModifiedDate()));
    		model.put("title", cd.getTitle());
    		model.put("description", cd.getDescription());
    		model.put("acad_department", cd.getDepartment());
    		model.put("acad_career", cd.getCareer());
    		model.put("credits", "" + cd.getCredits());
    		model.put("requirements", cd.getRequirements());
    		model.put("language", cd.getLanguage());
    		model.put("status", "success");
    	} else {
    		model.put("status", "failure");
    		model.put("message", msgs.getString("message_get_nok"));
    	}

    	return new ModelAndView("jsonView", model);
    }

    /*
     * Called to get tool bundles (for use in javascript function)
     */
    @RequestMapping(value = "/bundle.json")
    public ModelAndView getBundle(HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Map<String, String> msgsBundle = new HashMap<String, String>();
	for (Object key : msgs.keySet()) {
	    msgsBundle.put((String) key, (String) msgs.get(key));
	}

	Map<String, Object> model = new HashMap<String, Object>();

	model.put("data", msgsBundle);

	return new ModelAndView("jsonView", model);
    }

}
