package ca.hec.cdm.tool.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

import ca.hec.cdm.logic.SakaiProxy;

@Controller
public class HelloWorldController {

	/**
	 * Hello World Controller
	 * 
	 * @author Mike Jennings (mike_jennings@unc.edu)
	 * 
	 */
	@Setter
	@Getter
	@Autowired
	private SakaiProxy sakaiProxy = null;

	@RequestMapping(value = "/index.htm")
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentSiteId", sakaiProxy.getCurrentSiteId());
		map.put("userDisplayName", sakaiProxy.getCurrentUserDisplayName());

		return new ModelAndView("index", map);
	}

}
