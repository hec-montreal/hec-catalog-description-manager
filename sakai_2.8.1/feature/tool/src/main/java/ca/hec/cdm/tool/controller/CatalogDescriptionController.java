package ca.hec.cdm.tool.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ca.hec.cdm.logic.SakaiProxy;
import ca.hec.cdm.model.CatalogDescription;

@Controller
public class CatalogDescriptionController {

    @Setter
    @Getter
    @Autowired
    private SakaiProxy sakaiProxy = null;

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    public ModelAndView saveCatalogDescription(HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String description = request.getParameter("description");
	String id = request.getParameter("id");
	Long id_long = Long.decode(id);
	String returnMessage = null;
	String returnStatus = null;
	
	Boolean result = sakaiProxy.updateCatalogDescription(id_long, description);
	if (result){
	    returnMessage = "Sauvegarde OK  " ;
	    returnStatus = "success" ;
	}
	else{
	    returnMessage = "Erreur pendant la sauvegarde  " ;
	    returnStatus = "failure" ;
	}
	Map<String,String> model = new HashMap<String,String>();
	model.put("message", returnMessage);	
	model.put("status", returnStatus);
	return new ModelAndView("jsonView", model);
    }

    @RequestMapping(value = "/list.json")
    public ModelAndView listCatalogDescription(HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	List<CatalogDescription> listCd =
		sakaiProxy.getCatalogDescriptionsForUser();

	List tableValue = new ArrayList();

	for (CatalogDescription cd : listCd) {

	    List array = new ArrayList<String>();

	    boolean isDescription =
		    (cd.getDescription() != null && !cd.getDescription()
			    .isEmpty());

	    array.add("" + cd.getId());
	    array.add("" + cd.getCourseId());
	    array.add("" + cd.getTitle());
	    array.add("" + cd.getDepartment());
	    array.add("" + cd.getCareer());
	    array.add("" + cd.getLanguage());
	    array.add("" + isDescription);
	    tableValue.add(array);
	}

	Map model = new HashMap();
	model.put("aaData", tableValue);
	model.put("sEcho", listCd.size());
	model.put("iTotalRecords", listCd.size());
	model.put("iTotalDisplayRecords", listCd.size());

	return new ModelAndView("jsonView", model);
    }
    
    @RequestMapping(value = "/get.json")
    public ModelAndView getCatalogDescription(HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String id = request.getParameter("id");
	Long id_long = Long.decode(id);
	
	CatalogDescription cd = sakaiProxy.getCatalogDescriptionsById(id_long);
	Map<String,String> model = new HashMap<String,String>();
	
	
	if (cd != null){
	    model.put("courseid", cd.getCourseId());	
		model.put("title", cd.getTitle());
		model.put("description", cd.getDescription());
		 model.put("status", "success");
	}
	else{
	    model.put("status", "failure");
	}
	
	
	return new ModelAndView("jsonView", model);
    }
}
