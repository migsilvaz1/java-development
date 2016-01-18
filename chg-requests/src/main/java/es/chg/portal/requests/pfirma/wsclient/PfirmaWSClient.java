package es.chg.portal.requests.pfirma.wsclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.rpc.holders.StringHolder;

import org.apache.axis.AxisProperties;
import org.apache.log4j.Logger;

import es.chg.portal.requests.RequestsUtils;
import es.chg.portal.requests.pfirma.wsclient.modify.ModifyServiceProxy;
import es.chg.portal.requests.pfirma.wsclient.query.QueryServiceProxy;
import es.chg.portal.requests.pfirma.wsclient.type.Authentication;
import es.chg.portal.requests.pfirma.wsclient.type.DocumentType;
import es.chg.portal.requests.pfirma.wsclient.type.Job;
import es.chg.portal.requests.pfirma.wsclient.type.Request;
import es.chg.portal.requests.pfirma.wsclient.type.User;

public class PfirmaWSClient {
	
	private static Logger log = Logger.getLogger(PfirmaWSClient.class);
	
	private static ResourceBundle config = ResourceBundle.getBundle("es.chg.portal.requests.config.pfirma");
	
	public static String getUrlPf() {
		return config.getString("pfirma.url");
	}
	
	private static void applyConfiguration() {

		String truststore_path = config.getString("pfirma.ssl.trustStore.path");

		if (!"".equals(truststore_path.trim())) {
			System.setProperty("javax.net.ssl.trustStore", truststore_path);
			System.setProperty("javax.net.ssl.trustStorePassword", config.getString("pfirma.ssl.trustStore.password"));

			System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol.https");
			java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		}
		
		if ("true".equals(config.getString("pfirma.ssl.disableCheckCerticate"))) {
			AxisProperties.setProperty("axis.socketSecureFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");
		}
	}
	
	public static String sendRequest(Request requestPf) {
		
		String idRequestPf = null;
		
		try {
			
			applyConfiguration();
			
			ModifyServiceProxy modifyServiceProxy = new ModifyServiceProxy(config.getString("pfirma.url") + "/servicesv2/ModifyService");
	
			Authentication authentication = new Authentication(config.getString("pfirma.authentication.username"), config.getString("pfirma.authentication.password"));
			
			requestPf.setApplication(config.getString("pfirma.applicationId"));
			
			DocumentType documentType = new DocumentType();
			documentType.setIdentifier(config.getString("pfirma.documentTypeId"));
			requestPf.getDocumentList()[0].setDocumentType(documentType);
			
			User user = PfirmaWSClient.getUser(config.getString("pfirma.remitterUserId"));
			if (user == null) {
				throw new Exception("Usuario remitente " + config.getString("pfirma.remitterUserId") + " no existe o se ha producido un error al invocar a Port@firmas");
			}
			User[] remitterList = new User[1];
			remitterList[0] = user;
			requestPf.setRemitterList(remitterList);
			
			idRequestPf = modifyServiceProxy.createRequest(authentication, requestPf);
			
			modifyServiceProxy.sendRequest(authentication, new StringHolder(idRequestPf));
			
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return idRequestPf;
	}
	
	public static User getUser(String identifier) {
		
		User user = null;
		
		try {
			
			applyConfiguration();
			
			QueryServiceProxy queryServiceProxy = new QueryServiceProxy(config.getString("pfirma.url") + "/servicesv2/QueryService");
	
			Authentication authentication = new Authentication(config.getString("pfirma.authentication.username"), config.getString("pfirma.authentication.password"));
			
			User[] resultQueryUsers = queryServiceProxy.queryUsers(authentication, identifier);
			if (resultQueryUsers != null && resultQueryUsers.length != 0) {
				return resultQueryUsers[0];
			}
			
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return user;
	}
	
	public static Job getJob(String identifier) {
		
		Job job = null;
		
		try {
			
			applyConfiguration();
			
			QueryServiceProxy queryServiceProxy = new QueryServiceProxy(config.getString("pfirma.url") + "/servicesv2/QueryService");
	
			Authentication authentication = new Authentication(config.getString("pfirma.authentication.username"), config.getString("pfirma.authentication.password"));
			
			Job[] resultQueryJobs = queryServiceProxy.queryJobs(authentication, identifier);
			if (resultQueryJobs != null && resultQueryJobs.length != 0) {
				return resultQueryJobs[0];
			}
			
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return job;
	}
	
	public static Map getRequestStatus(String requestPfId) {
		
		Map result = new HashMap();
		
		try {
			
			applyConfiguration();
			
			QueryServiceProxy queryServiceProxy = new QueryServiceProxy(config.getString("pfirma.url") + "/servicesv2/QueryService");
	
			Authentication authentication = new Authentication(config.getString("pfirma.authentication.username"), config.getString("pfirma.authentication.password"));
			
			Request resultQueryRequest = queryServiceProxy.queryRequest(authentication, requestPfId);
			if (resultQueryRequest != null && !resultQueryRequest.getIdentifier().isEmpty()) {

				result.put("STATUS", resultQueryRequest.getRequestStatus().getValue()); // ACEPTADO, RECHAZADO, CADUCADO, EN PROCESO, RETIRADO 
				
				List statusByUser = new ArrayList(); // FIRMADO, VISTOBUENO, DEVUELTO, CADUCADO, RETIRADO, EN ESPERA, LEIDO, NUEVO
				
				int nSignLineList = resultQueryRequest.getSignLineList().length;
				for (int i = 0; i < nSignLineList; i++) {
					if (resultQueryRequest.getSignLineList()[i].getSignerList()[0].getUserJob() instanceof User) {
						statusByUser.add(((User)resultQueryRequest.getSignLineList()[i].getSignerList()[0].getUserJob()).getName() + " " + ((User)resultQueryRequest.getSignLineList()[i].getSignerList()[0].getUserJob()).getSurname1() + " " + ((User)resultQueryRequest.getSignLineList()[i].getSignerList()[0].getUserJob()).getSurname2() + "#__#" + resultQueryRequest.getSignLineList()[i].getSignerList()[0].getState().getIdentifier() + "#__#" + RequestsUtils.getFormatDate(resultQueryRequest.getSignLineList()[i].getSignerList()[0].getFstate().getTime()));	
					} else {
						statusByUser.add(((Job)resultQueryRequest.getSignLineList()[i].getSignerList()[0].getUserJob()).getDescription() + "#__#" + resultQueryRequest.getSignLineList()[i].getSignerList()[0].getState().getIdentifier() + "#__#" + RequestsUtils.getFormatDate(resultQueryRequest.getSignLineList()[i].getSignerList()[0].getFstate().getTime()));
					}
				}
				
				result.put("STATUS_USERS", statusByUser);
				
				List commentsReturned = new ArrayList();
				
				int nCommentList = resultQueryRequest.getCommentList().length;
				for (int i = 0; i < nCommentList; i++) {
					if ("DEVUELTO".equals(resultQueryRequest.getCommentList()[i].getSubject())) {
						commentsReturned.add(((User)resultQueryRequest.getCommentList()[i].getUser()).getName() + " " + ((User)resultQueryRequest.getCommentList()[i].getUser()).getSurname1() + " " + ((User)resultQueryRequest.getCommentList()[i].getUser()).getSurname2() + "#__#" + resultQueryRequest.getCommentList()[i].getTextComment());
					}
				}
				
				result.put("COMMENTS_RETURNED", commentsReturned);

			}
			
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return result;
	}
	
	public static String getRequestDocumentId(String requestPfId) {
		
		String result = null;
		
		try {
			
			applyConfiguration();
			
			QueryServiceProxy queryServiceProxy = new QueryServiceProxy(config.getString("pfirma.url") + "/servicesv2/QueryService");
	
			Authentication authentication = new Authentication(config.getString("pfirma.authentication.username"), config.getString("pfirma.authentication.password"));
			
			Request resultQueryRequest = queryServiceProxy.queryRequest(authentication, requestPfId);
			if (resultQueryRequest != null && !resultQueryRequest.getIdentifier().isEmpty()) {
				result = resultQueryRequest.getDocumentList()[0].getIdentifier(); 
			}
			
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return result;
	}

}
