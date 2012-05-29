 /**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/announcement/trunk/announcement-tool/tool/src/java/org/sakaiproject/announcement/entityprovider/AnnouncementEntityProviderImpl.java $
 * $Id: AnnouncementEntityProviderImpl.java 87813 2011-01-28 13:42:17Z savithap@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008, 2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

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
import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.util.AbstractEntityProvider;

import ca.hec.cdm.api.CatalogDescriptionService;

public class CatalogDescriptionEntityProviderImpl extends AbstractEntityProvider 
	implements CoreEntityProvider, AutoRegisterEntityProvider, Resolvable, Outputable, ActionsExecutable {

	public final static String ENTITY_PREFIX = "catalog-description";
	
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

		List<SimpleCatalogDescription> decoratedCatalogDescriptions = new ArrayList<SimpleCatalogDescription>();
		
		String test = catalogDescriptionService.getLabel();
		//convert raw CatalogDescriptions into decorated catalog descriptions
		for (int i = 0; i < 5; i++)
		{
			SimpleCatalogDescription dcd = 
					new SimpleCatalogDescription("Catalog Description " + i, "La description du plan de cours annuaire");
			dcd.setRequirements("label: " + test );
			dcd.setDepartment(department);
			dcd.setCareer("career");
			decoratedCatalogDescriptions.add(dcd);
		}

		return decoratedCatalogDescriptions;
	}

	@EntityCustomAction(action="program", viewKey=EntityView.VIEW_LIST)
	public List<?> getCatalogDescriptionsByProgram(EntityView view, Map<String, Object> params) {

		// get program
		String career = view.getPathSegment(2);

		//check that department is supplied
		if (StringUtils.isBlank(career)) {
			throw new IllegalArgumentException("Program must be set in order to get the catalog descriptions via the URL /catalog-description/program/programId");
		}

		List<SimpleCatalogDescription> decoratedCatalogDescriptions = new ArrayList<SimpleCatalogDescription>();
		
		String test = catalogDescriptionService.getLabel();
		//convert raw CatalogDescriptions into decorated catalog descriptions
		for (int i = 0; i < 5; i++)
		{
			SimpleCatalogDescription dcd = 
					new SimpleCatalogDescription("Catalog Description " + i, "La description du plan de cours annuaire");
			dcd.setRequirements("label: " + test );
			dcd.setDepartment("department");
			dcd.setCareer(career);
			decoratedCatalogDescriptions.add(dcd);
		}

		return decoratedCatalogDescriptions;
	}

	public Object getEntity(EntityReference ref) {
		return new String("a single entity");
	}

	public boolean entityExists(String id) {
		return false;
	}
	
	/**
	 * Class to hold the subset of the fields that we want to display 
	 */
	@Data
	public class SimpleCatalogDescription implements Comparable<Object>{
		private final String title;
		private final String description;
		
		private String department;
		private String career;
		private String requirements;
		
		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}
