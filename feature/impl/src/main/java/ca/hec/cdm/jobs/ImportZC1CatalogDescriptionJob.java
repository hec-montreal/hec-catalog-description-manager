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

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.sakaiproject.component.cover.ServerConfigurationService;

import ca.hec.cdm.api.CatalogDescriptionDao;
import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.model.CatalogDescription;


/**
 * A one-time job to transfer the catalog descriptions from zc1 database to zc2 database
 *
 * @author <a href="mailto:philippe.rancourt@hec.ca">Philippe Rancourt</a>
 * @version $Id: $
 */
public class ImportZC1CatalogDescriptionJob implements Job {

  
    @Getter
    @Setter
    private CatalogDescriptionDao catalogDescriptionDao;

 
    private static Log log = LogFactory
	    .getLog(ImportZC1CatalogDescriptionJob.class);

    
    private static final String ZC1_REQUEST = "select PLANCOURS.KOID, PLANZONE.HTML from PLANZONE, PLANCOURS where PLANCOURS.koid=PLANZONE.koid and PLANCOURS.TYPE = 'annuaire'"; 
    
    
    private int savedDesc = 0;
    
    private int unknownDesc = 0;
    
    private int noDesc = 0;
    
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
	
	Connection connex = getZC1Connection();
	PreparedStatement ps = null;
	
	try{	
	     ps = connex.prepareStatement(ZC1_REQUEST);
	    	    
	     ResultSet rs = ps.executeQuery();
	     	     
	     while(rs.next()){
		 
		 String koid = rs.getString(1);
		 Clob htmlClob = rs.getClob(2);
		 
		 String courseId = formatCourseId(koid);
		 
		 String html = htmlClob.getSubString((long)1, (int)htmlClob.length());
		 String desc = formatHtml(html);
		 
		 if(desc!=null){
		     
		    /** log.error("----------------------------------------------------------------------------------");
		     log.error("course id: "+courseId);
		     log.error("desc: "+desc);**/
		     
		     saveInZC2(courseId, desc);
		 }
		 else{
		     noDesc++;
		 }
		 
	     }//end while
	
	     log.error("----------------------------------------------------------------------------------");
	     log.error("FIN DE LA JOB");
	     log.error("saved desc:"+savedDesc);
	     log.error("unknow desc:"+unknownDesc);
	     log.error("no desc found:"+noDesc);
	}
	catch(SQLException sqex){
	    log.error("Error database: "+sqex.toString());
	}
	finally{
	    try{
		ps.close();
	    }catch(Exception ex){}
	    try{
		connex.close();
	    }catch(Exception ex){}	    
	}
	
    }
    
    
    private void saveInZC2(String courseId, String desc){
	
	CatalogDescription cd = catalogDescriptionDao.getCatalogDescription(courseId);
	
	if(cd!=null){
	    
	    cd.setDescription(desc);
		
	    try{
		catalogDescriptionDao.saveCatalogDescription(cd);
		savedDesc++;
	    }
	    catch(StaleDataException staleEx){
		log.error("Error Stale Data:"+staleEx.toString());
	    }
	    catch(DatabaseException dex){
		log.error("Error database dao:"+dex.toString());
	    }    	    
	}
	else{
	    log.error("Uknown desc:"+courseId);
	    unknownDesc++;
	}
		
    }

    
    
    private Connection getZC1Connection(){
			
	String driverName = ServerConfigurationService
		.getString("hec.zonecours.conn.portail.driver.name");
        String url = ServerConfigurationService
        		.getString("hec.zonecours.conn.portail.url");
        String user = ServerConfigurationService
        		.getString("hec.zonecours.conn.portail.user");
        String password = ServerConfigurationService
        		.getString("hec.zonecours.conn.portail.password");
	
	Connection zc1con = null;
	
	try{
	    Class.forName(driverName);
	    
	    zc1con = DriverManager.getConnection(url, user, password);
	}
	catch(ClassNotFoundException cnf){
	    log.error("Driver not found !");
	}
	catch(SQLException sqlex){
	    log.error("Database connection error:"+sqlex.toString());	    
	}


	return zc1con;
    }

    
    private String formatHtml(String html) {
	
	String desc = null;
	
	int annuaireMarker = html.indexOf("encadreTableAnnuaireBlanc");
	
	if(annuaireMarker!=-1){
	    
	    html = html.substring(annuaireMarker+"encadreTableAnnuaireBlanc".length());
	    
	    
	    int beginDiv = html.indexOf("<div class='texte'>");
	    
	    if(beginDiv!=-1){
		
		html = html.substring(beginDiv+"<div class='texte'>".length());
		
		int endDiv = html.indexOf("</div>");
		
		if(endDiv!=-1){
		    
		    html = html.substring(0, endDiv);
		    
		    desc = html.trim();
		}
		
	    }
	    
	}
		
	return desc;
    }
    
    
    private String formatCourseId(String courseId) {
	
	String cheminement;
	String numero;
	String annee;
	String formattedCourseId;

	//ajouter pour la table plancours
	if(courseId.substring(0,2).equalsIgnoreCase("a-")){
	    courseId = courseId.substring(2);
	}
	
	
	if (courseId.length() == 6) {
	    cheminement = courseId.substring(0, 1);
	    numero = courseId.substring(1, 4);
	    annee = courseId.substring(4);
	}

	else if (courseId.length() == 7) {
	    if (courseId.endsWith("A") || courseId.endsWith("E")
		    || courseId.endsWith("R")) {
		cheminement = courseId.substring(0, 1);
		numero = courseId.substring(1, 4);
		annee = courseId.substring(4);
	    } else {
		cheminement = courseId.substring(0, 2);
		numero = courseId.substring(2, 5);
		annee = courseId.substring(5);
	    }
	}

	else if (courseId.length() == 8) {
	    cheminement = courseId.substring(0, 2);
	    numero = courseId.substring(2, 5);
	    annee = courseId.substring(5);
	}

	else {
	    return courseId;
	}

	formattedCourseId = cheminement + "-" + numero + "-" + annee;
	
	return formattedCourseId;
    }
    
    
}