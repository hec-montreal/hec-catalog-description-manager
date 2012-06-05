//package ca.hec.cdm.impl;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.Stack;
//
//import org.sakaiproject.entity.api.Entity;
//import org.sakaiproject.entity.api.HttpAccess;
//import org.sakaiproject.entity.api.Reference;
//import org.sakaiproject.entity.api.ResourceProperties;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//
//import ca.hec.cdm.api.CatalogDescriptionService;
//
//public abstract class BaseCatalogDescriptionService 
//	implements CatalogDescriptionService {
//	//implement EntityTransferrer, EntityTransferrerRefMigrator also? 
//	
//	public String getLabel() {
//		return "catalog-description";
//	}
//
//	public boolean willArchiveMerge() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	public String archive(String siteId, Document doc, Stack stack,
//			String archivePath, List attachments) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String merge(String siteId, Element root, String archivePath,
//			String fromSiteId, Map attachmentNames, Map userIdTrans,
//			Set userListAllowImport) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public boolean parseEntityReference(String reference, Reference ref) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	public String getEntityDescription(Reference ref) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public ResourceProperties getEntityResourceProperties(Reference ref) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Entity getEntity(Reference ref) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public String getEntityUrl(Reference ref) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Collection getEntityAuthzGroups(Reference ref, String userId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public HttpAccess getHttpAccess() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
