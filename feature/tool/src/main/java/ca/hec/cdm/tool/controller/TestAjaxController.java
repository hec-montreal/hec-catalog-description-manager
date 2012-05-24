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

import java.util.Date;

import ca.hec.cdm.logic.SakaiProxy;

@Controller
public class TestAjaxController {

	/**
	 * Hello World Controller
	 * 
	 * @author Mike Jennings (mike_jennings@unc.edu)
	 * 
	 */
	@Setter
	@Getter
	private SakaiProxy sakaiProxy = null;

	@RequestMapping(value = "/testAjax.htm")
	public ModelAndView getAjaxMessage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map model = new HashMap();
		String ajaxMessage = "ajax message updated at " + new Date().toString();
		model.put("message", ajaxMessage);

		return new ModelAndView("jsonView", model);
	}

}
