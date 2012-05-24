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

import lombok.Data;

import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.CollectionResolvable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.Outputable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.Resolvable;
import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.entityprovider.search.Search;
import org.sakaiproject.entitybroker.util.AbstractEntityProvider;

public class CatalogDescriptionEntityProviderImpl extends AbstractEntityProvider 
	implements CoreEntityProvider, AutoRegisterEntityProvider, Resolvable, CollectionResolvable, Outputable {

	public final static String ENTITY_PREFIX = "catalog-description";
	
	public String getEntityPrefix() {
		return ENTITY_PREFIX;
	}

	public String[] getHandledOutputFormats() {
		return new String[] { Formats.JSON };
	}

	public List<?> getEntities(EntityReference ref, Search search) {
		List<DecoratedCatalogDescription> decoratedCatalogDescriptions = new ArrayList<DecoratedCatalogDescription>();
		
		//convert raw CatalogDescriptions into decorated catalog descriptions
		for (int i = 0; i < 5; i++)
		{
			DecoratedCatalogDescription dcd = 
					new DecoratedCatalogDescription("Catalog Description " + i, "Teh descriptions!!");
			dcd.setRequirements("these are teh requirements");
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
	
	@Data
	public class DecoratedCatalogDescription implements Comparable<Object>{
		private final String title;
		private final String description;
		
		private String requirements;
		
		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			// sort order by 
			return 0;
		}
		
	
	}

}
