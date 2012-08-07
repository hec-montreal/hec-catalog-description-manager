package ca.hec.cdm.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import lombok.Setter;

import ca.hec.cdm.api.CatalogDescriptionDao;
import ca.hec.cdm.api.CatalogDescriptionService;
import ca.hec.cdm.exception.DatabaseException;
import ca.hec.cdm.exception.StaleDataException;
import ca.hec.cdm.model.CatalogDescription;

public class CatalogDescriptionServiceImpl implements CatalogDescriptionService {
    @Setter
    private CatalogDescriptionDao catalogDescriptionDao;

    public void init() {
	// TODO Auto-generated method stub
    }

    public CatalogDescription getCatalogDescription(Long id) {
	return catalogDescriptionDao.getCatalogDescription(id);
    }

    public CatalogDescription getCatalogDescription(String course_id) {
	return catalogDescriptionDao.getCatalogDescription(course_id);
    }

    public List<CatalogDescription> getCatalogDescriptions() {
	return catalogDescriptionDao.getCatalogDescriptions();
    }

    public List<CatalogDescription> getCatalogDescriptionsByCareer(String career) {
	return catalogDescriptionDao.getCatalogDescriptionsByCareer(career);
    }

    public List<CatalogDescription> getCatalogDescriptionsByDepartment(
	    String department) {
	return catalogDescriptionDao
		.getCatalogDescriptionsByDepartment(department);
    }

    public void updateDescription(CatalogDescription cd)
	    throws StaleDataException, DatabaseException {
	try {
	    catalogDescriptionDao.saveCatalogDescription(cd);

	    // TODO why don't these get caught in the DAO? ideally this would
	    // happen there.
	} catch (HibernateOptimisticLockingFailureException e) {
	    throw new StaleDataException();
	} catch (DataAccessException e) {
	    throw new DatabaseException();
	}
    }

    public boolean descriptionExists(String course_id) {
	return catalogDescriptionDao.descriptionExists(course_id);
    }

    public List<String> getDepartmentNameWithAtLeastOneCaWithNoDescription() {
	return catalogDescriptionDao
		.getDepartmentNameWithAtLeastOneCaWithNoDescription();
    }

    public List<String> getCareerNameWithAtLeastOneCaWithNoDescription() {
	return catalogDescriptionDao
		.getCareerNameWithAtLeastOneCaWithNoDescription();
    }    
   
}
