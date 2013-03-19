package ca.hec.cdm.impl;

import java.util.List;
import java.util.Map;

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

    public List<CatalogDescription> getCatalogDescriptionsByDepartment(
	    String department, boolean showInactives) {
	return catalogDescriptionDao.getCatalogDescriptionsByDepartment(
		department, showInactives);
    }

    public List<CatalogDescription> getCatalogDescriptionsByDepartment(
	    String department) {
	return catalogDescriptionDao
		.getCatalogDescriptionsByDepartment(department);
    }

    public List<CatalogDescription> getCatalogDescriptionsByCareer(String career) {
	return catalogDescriptionDao.getCatalogDescriptionsByCareer(career);
    }

    public List<CatalogDescription> getCatalogDescriptionsByCareer(
	    String career, boolean showInactives) {
	return catalogDescriptionDao.getCatalogDescriptionsByCareer(career, showInactives);
    }

    public List<CatalogDescription> getCatalogDescriptions(
	    Map<String, String> criteria) {
	return catalogDescriptionDao.getCatalogDescriptions(criteria, null);
    }

    public List<CatalogDescription> getCatalogDescriptions(
	    Map<String, String> criteria, Map<String, String> searchCriteria) {
	return catalogDescriptionDao.getCatalogDescriptions(criteria,
		searchCriteria);
    }

    public List<CatalogDescription> getAllCatalogDescriptionsForCertificate(
	    boolean showInactives) {
	return catalogDescriptionDao
		.getAllCatalogDescriptionsForCertificate(showInactives);
    }

    public List<CatalogDescription> getAllCatalogDescriptionsForCertificate() {
	return catalogDescriptionDao.getAllCatalogDescriptionsForCertificate();
    }

    public List<CatalogDescription> getAllCatalogDescriptions() {
	return catalogDescriptionDao.getAllCatalogDescriptions();
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

    public List<CatalogDescription> getCatalogDescriptionsByDepartmentWithNoDescription(
	    String department) {
	return catalogDescriptionDao
		.getCatalogDescriptionsByDepartmentWithNoDescription(department);
    }

    public List<CatalogDescription> getAllCatalogDescriptionsForCertificatesWithNoDescription() {
	return catalogDescriptionDao
		.getAllCatalogDescriptionsForCertificatesWithNoDescription();
    }

}
