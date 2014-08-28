/******************************************************************************
 * $Id: $
 ******************************************************************************
 *
 * Copyright (c) 2012 The Sakai Foundation, The Sakai Quebec Team.
 *
 * Licensed under the Educational Community License, Version 1.0
 * (the "License"); you may not use this file except in compliance with the
 * License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package ca.hec.cdm.jobs;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.email.api.ContentType;
import org.sakaiproject.email.api.EmailAddress;
import org.sakaiproject.email.api.EmailAddress.RecipientType;
import org.sakaiproject.email.api.EmailMessage;
import org.sakaiproject.email.api.EmailService;
import org.sakaiproject.util.ResourceLoader;

import ca.hec.cdm.api.CatalogDescriptionService;
import ca.hec.cdm.model.CatalogDescription;

public class SendEmailNotificationJob implements Job {
    
    @Getter
    @Setter
    private EmailService emailService;
    
    @Getter
    @Setter
    private CatalogDescriptionService catalogDescriptionService;
    
    private static Log log = LogFactory
	    .getLog(SendEmailNotificationJob.class);
    
    private ResourceLoader msgs = null; 
    
    public void init() {
    	msgs = new ResourceLoader("cdm");
    }

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
	
	sendEmailToCertificate();
	
	sendEmailToDepartments();
    }

    
    private void sendEmailToCertificate() {
	
	List<CatalogDescription> listCatalogDescription =
		catalogDescriptionService
			.getAllCatalogDescriptionsForCertificatesWithNoDescription();

	if (listCatalogDescription != null
		&& listCatalogDescription.size() != 0) {
	    
	    sendEmail("CERTIFICAT", listCatalogDescription);
	}

    }
    
    private void sendEmailToDepartments() {

	List<String> listDepartments =
		catalogDescriptionService
			.getDepartmentNameWithAtLeastOneCaWithNoDescription();

	for (String department : listDepartments) {

	    List<CatalogDescription> listCatalogDescription =
		    catalogDescriptionService
			    .getCatalogDescriptionsByDepartmentWithNoDescription(department);

	    if (listCatalogDescription != null
		    && listCatalogDescription.size() != 0) {
		sendEmail(department, listCatalogDescription);
	    }
	}
    }
    
    
    private void sendEmail(String departName,
	    List<CatalogDescription> listCatalogDescription) {

	// Get the recipient address from the department
	String recipientAdress =
		ServerConfigurationService.getString("recipientAdress_"
			+ departName);

	if (recipientAdress != null && !recipientAdress.equals("")) {

	    String messageIntroduction = msgs.getString("email_template");

	    String messageSubject = msgs.getString("email_subject");

	    List<EmailAddress> toRecipients = new ArrayList<EmailAddress>();
	    toRecipients.add(new EmailAddress(recipientAdress));

	    // Create message Body
	    StringBuilder messageBody = new StringBuilder();
	    messageBody.append(messageIntroduction);

	    messageBody
		    .append("<table border=\"1\" style=\"border-collapse:collapse;\"><thead><tr><th>"
			    + msgs.getString("header_course_id")
			    + "</th><th>"
			    + msgs.getString("header_course_title")
			    + "</th><th>"
			    + msgs.getString("header_date_modification")
			    + "</th></tr></thead><tbody>");

	    for (CatalogDescription catalogDescription : listCatalogDescription) {
		
		String courseUrl = ServerConfigurationService.getServerUrl()+"/portail/#cours="+catalogDescription.getCourseId();
		
		messageBody.append("<tr><td>");
		messageBody.append("<a href='");
		messageBody.append(courseUrl);
		messageBody.append("'>");
		messageBody.append(catalogDescription.getCourseId());
		messageBody.append("</a></td><td>");
		messageBody.append(catalogDescription.getTitle());
		messageBody.append("</td><td>");
		messageBody.append(catalogDescription.getCreatedDate());
		messageBody.append("</td></tr>");
	    }
	    messageBody.append("</tbody></table>");

	    // Initialize message
	    EmailMessage message = new EmailMessage();
	    message.setSubject(messageSubject);
	    message.setContentType(ContentType.TEXT_HTML);
	    message.setBody(messageBody.toString());
	    message.setFrom(getZoneCours2EMail());
	    message.setRecipients(RecipientType.TO, toRecipients);

	    List<EmailAddress> ccList = new ArrayList<EmailAddress>();
	    ccList.add(new EmailAddress(getZoneCours2EMail()));
	    ccList.addAll(getCCAddresses());
	    message.setRecipients(RecipientType.CC, ccList);

	    // print the email in the log
	    log.debug("----------------------------------------");
	    log.debug("MAILTO: " + recipientAdress);
	    log.debug("Subject: " + messageSubject);
	    log.debug("Message: " + messageBody);

	    // send mail to department
	    try {
		emailService.send(message);
	    } catch (Exception e) {
			log.error("Could not send email to notify empty catalog description to "
				+ departName + " :" + e);
			//SEND MESSAGE TO ZC2 ANYWAY
			try{
				toRecipients = new ArrayList<EmailAddress>();
			    toRecipients.add(new EmailAddress(getZoneCours2EMail()));
				message.setRecipients(RecipientType.TO, toRecipients);
				emailService.send(message);
			} catch (Exception ex){}
		e.printStackTrace();
	    }
	}//end if

    }
    
    private String getZoneCours2EMail(){
	return ServerConfigurationService.getString("mail.zc2");
    }

    private List<EmailAddress> getCCAddresses(){
    	List<EmailAddress> ccList = new ArrayList<EmailAddress>();
    	String addresses = ServerConfigurationService.getString("recipientCCAdress");
    	
    	if (addresses == null)
    		return new ArrayList<EmailAddress>();
    	String [] add = addresses.split(",");
    	for (int i=0; i<add.length; i++){
    		ccList.add(new EmailAddress(add[i]));
    	}
    	
    	return ccList;
    }
}
