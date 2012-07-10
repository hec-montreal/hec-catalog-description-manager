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

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ca.hec.cdm.api.CatalogDescriptionDao;
import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.jobs.model.CourseOffering;
import ca.hec.cdm.model.CatalogDescription;

/**
 * @author <a href="mailto:philippe.rancourt@hec.ca">Philippe Rancourt</a>
 * @version $Id: $
 */
public class CreateCatalogDescriptionJob implements Job {

    @Getter
    @Setter
    private CatalogDescriptionJobDao courseOfferingDao;

    @Getter
    @Setter
    private CatalogDescriptionDao catalogDescriptionDao;

    CatalogDescription cd = null;

    private static Log log = LogFactory
	    .getLog(CreateCatalogDescriptionJob.class);

    public void init() {

    }

    
    
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
	List<CourseOffering> listCo = courseOfferingDao.getListCourseOffering();

	for (CourseOffering co : listCo) {
	    cd = new CatalogDescription();
	    cd.setCourseId(formatCourseId(co.getCatalog_nbr()));
	    cd.setTitle(co.getCourse_title_long());
	    cd.setDepartment(co.getAcad_org());
	    cd.setCareer(co.getAcad_career());
	    cd.setCredits(co.getCredits());
	    cd.setLanguage(co.getLanguage());
	    cd.setRequirements(co.getRequirement());
	    cd.setCreatedDate(new Date(java.util.Calendar.getInstance()
		    .getTimeInMillis()));
	    cd.setEffectiveDate(new Date(java.util.Calendar.getInstance()
		    .getTimeInMillis()));
	    cd.setStatus('I');
	    cd.setCreatedBy("quartz job");

	    try {
		catalogDescriptionDao.saveCatalogDescription(cd);
	    } catch (StaleDataException e) {
		log.error("Exception durant la creation de description annuaire :"
			+ e);
	    } catch (DatabaseException e) {
		log.error("Exception durant la creation de description annuaire :"
			+ e);
	    }

	}
    }

    private String formatCourseId(String courseId) {
	String cheminement;
	String numero;
	String annee;
	String formattedCourseId;

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
