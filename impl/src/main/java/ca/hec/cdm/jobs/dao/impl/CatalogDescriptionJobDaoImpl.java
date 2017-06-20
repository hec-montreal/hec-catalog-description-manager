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
package ca.hec.cdm.jobs.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ca.hec.cdm.jobs.CatalogDescriptionJobDao;
import ca.hec.cdm.jobs.model.CourseOffering;

/**
 *
 * @author <a href="mailto:mathieu.cantin@hec.ca">Mathieu Cantin</a>
 * @version $Id: $
 */
public class CatalogDescriptionJobDaoImpl  extends HibernateDaoSupport implements CatalogDescriptionJobDao {

    public List<CourseOffering> getListCourseOffering() {
	return (List<CourseOffering>) getHibernateTemplate().find("from CourseOffering");
		
    }
    
    public CourseOffering getCourseOffering(String catalogNbr) {
        CourseOffering offering = null;

        // there should only ever be one description, but it can't hurt
        // to order by db id.
        DetachedCriteria dc =
                DetachedCriteria
                        .forClass(CourseOffering.class)
                        .add(Restrictions.eq("catalog_nbr",
                                catalogNbr.toUpperCase()));

        List offList = getHibernateTemplate().findByCriteria(dc);

        if (offList != null && offList.size() != 0) {
            offering = (CourseOffering) offList.get(0);
        }

        return offering;
    }

}

