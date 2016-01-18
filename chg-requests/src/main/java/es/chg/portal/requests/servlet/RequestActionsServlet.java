package es.chg.portal.requests.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.gfi.afirma.wsclient.certificate.ValidarCertificado;
import com.gfi.afirma.wsclient.dss.DSSAfirmaVerify;

import es.chg.portal.requests.RequestsUtils;
import es.chg.portal.requests.db.RequestVO;
import es.chg.portal.requests.db.RequestsDAO;
import es.chg.portal.requests.db.UserVO;
import es.chg.portal.requests.pfirma.wsclient.PfirmaWSClient;
import es.chg.portal.requests.pfirma.wsclient.type.Document;
import es.chg.portal.requests.pfirma.wsclient.type.Job;
import es.chg.portal.requests.pfirma.wsclient.type.Request;
import es.chg.portal.requests.pfirma.wsclient.type.SignLine;
import es.chg.portal.requests.pfirma.wsclient.type.SignLineType;
import es.chg.portal.requests.pfirma.wsclient.type.SignType;
import es.chg.portal.requests.pfirma.wsclient.type.Signer;
import es.chg.portal.requests.pfirma.wsclient.type.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class RequestActionsServlet
 */
public class RequestActionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(RequestActionsServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequestActionsServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getSession().getAttribute("chg-requests-id-user") == null) {
			throw new ServletException("Sesion expirada");
		}
		
	
		if (request.getParameter("init-sign") != null) {
			
			response.sendRedirect("request-detail.jsp?id-request=" + request.getParameter("id-request") + "&init-sign");
			
		} else if (request.getParameter("return-list") != null) {
			
			response.sendRedirect("requests-list.jsp");
			
		} else if (request.getParameter("return-detail") != null) {
			
			response.sendRedirect("request-detail.jsp?id-request=" + request.getParameter("id-request"));			
			
		} else if (request.getParameter("sync-pf") != null) {
			
			try {
			
				List requests = RequestsDAO.getRequestsPendingApprovalByUser((String)request.getSession().getAttribute("chg-requests-id-user"));
				
				if (requests != null) {
					for (Iterator iteratorRequests = requests.iterator(); iteratorRequests
							.hasNext();) {
						RequestVO requestvo = (RequestVO) iteratorRequests.next();
						
						Map resultgetRequestStatusPf = PfirmaWSClient.getRequestStatus(requestvo.getPfReference());
						
						if (resultgetRequestStatusPf == null || resultgetRequestStatusPf.isEmpty()) {
							throw new Exception("Se ha producido un error al invocar a Port@firmas para obtener el estado de la petición " + requestvo.getPfReference());
						}
						
						String pfDetailedStatus = "";
						String status = "";
						
						String statusPf = (String)resultgetRequestStatusPf.get("STATUS");
						
				   	 	if ("ACEPTADO".equals(statusPf)) {
				   	 		status =  "APROBADO";
				   	 	} else if ("RECHAZADO".equals(statusPf) || "CADUCADO".equals(statusPf) || "RETIRADO".equals(statusPf)) {
				   	 		status =  "RECHAZADO";
				   	 	} else { // EN PROCESO u otros
				   	 		status = "PENDIENTE_APROBACION";
				   	 	}
						
			   	 		JSONObject pfDetailedStatusJson = new JSONObject();
			   	 		
			   	 		JSONArray statusUsersJson = new JSONArray();
			   	 		
			   	 		List statusUsers = (ArrayList) resultgetRequestStatusPf.get("STATUS_USERS");
			   	 		for (Iterator iterator = statusUsers.iterator(); iterator
								.hasNext();) {
							String statusUser = (String) iterator.next();
							String[] statusUserArray = statusUser.split("#__#");
							
							JSONObject statusUserJson = new JSONObject();
							statusUserJson.put("user", statusUserArray[0]);
							statusUserJson.put("status", statusUserArray[1]);
							statusUserJson.put("date", statusUserArray[2]);
							
							statusUsersJson.add(statusUserJson);
						}
			   	 		
			   	 		pfDetailedStatusJson.put("STATUS_USERS", statusUsersJson);	
			   	 		
			   	 		JSONArray commentsReturnedJson = new JSONArray();
			   	 		
				   	 	List commentsReturned = (ArrayList) resultgetRequestStatusPf.get("COMMENTS_RETURNED");
			   	 		for (Iterator iterator = commentsReturned.iterator(); iterator
								.hasNext();) {
							String commentReturned = (String) iterator.next();
							String[] commentReturnedArray = commentReturned.split("#__#");
							
							JSONObject commentReturnedJson = new JSONObject();
							commentReturnedJson.put("user", commentReturnedArray[0]);
							commentReturnedJson.put("message", commentReturnedArray[1]);
							
							commentsReturnedJson.add(commentReturnedJson);
						}
			   	 		
			   	 		pfDetailedStatusJson.put("COMMENTS_RETURNED", commentsReturnedJson);
			   	 		
			   	 		pfDetailedStatus = pfDetailedStatusJson.toString();
						
						RequestsDAO.updateRequestPfStatus(String.valueOf(requestvo.getId()), pfDetailedStatus, status);
					}
				}
				
				response.sendRedirect("requests-list.jsp");	
			
			} catch (Exception e) {
				if (e.getMessage().startsWith("ERROR-")) {
					response.sendRedirect("error.jsp?id-error="+ e.getMessage().split("-")[1] + "&source=requests-list.jsp");
				} else {
					log.error(e, e);
					response.sendRedirect("error.jsp?id-error=0&source=requests-list.jsp");
				}
			}
			
		} else if (request.getParameter("save-sign") != null) {
			
			try {
			
				String requestStatus = RequestsDAO.getRequestStatus((String)request.getSession().getAttribute("chg-requests-id-user"), request.getParameter("id-request"));
				
				if ("PENDIENTE_FIRMA".equals(requestStatus)) {
					
					ValidarCertificado validarCertificado = new ValidarCertificado();
					Map<String, String> resultValidarCertificado = validarCertificado.esCertificadoValidoConNIF(request.getParameter("certificate-b64"));
					
					if (resultValidarCertificado == null || resultValidarCertificado.isEmpty()) {
						throw new Exception("ERROR-1"); // Error en la validación del certificado
					} else if (ValidarCertificado.RES_VALIDACION_OK.equals(resultValidarCertificado.get(ValidarCertificado.RES_RESULTADO))) {
						UserVO uservo = RequestsDAO.getUserByUserId((String)request.getSession().getAttribute("chg-requests-id-user"));
						if (uservo == null || !uservo.getIdentifier().toUpperCase().equals(resultValidarCertificado.get(ValidarCertificado.RES_NIF).toUpperCase())) {
							throw new Exception("ERROR-2"); // El NIF del certificado no coincide con el NIF del usuario autenticado
						}
					} else {
						throw new Exception("ERROR-3"); // Certificado no válido
					}
					
					DSSAfirmaVerify dssAfirmaVerify = new DSSAfirmaVerify();
					String firmaConSello = dssAfirmaVerify.incluirSelloDeTiempoAFirmaPDF(request.getParameter("signature-b64"));
					
					if (firmaConSello == null) {
						throw new Exception("No se ha podido aplicar el sello de tiempo a la firma generada");
					}
					
					RequestsDAO.updateRequestSignature(request.getParameter("id-request"), new ByteArrayInputStream(RequestsUtils.decodeBase64(firmaConSello)));
				}
				
				response.sendRedirect("request-detail.jsp?id-request=" + request.getParameter("id-request"));
			
			} catch (Exception e) {
				if (e.getMessage().startsWith("ERROR-")) {
					response.sendRedirect("error.jsp?id-error="+ e.getMessage().split("-")[1] + "&id-request=" + request.getParameter("id-request"));
				} else {
					log.error(e, e);
					response.sendRedirect("error.jsp?id-error=0&id-request=" + request.getParameter("id-request"));
				}
			}
			
		} else if (request.getParameter("send-pf") != null) {
			
			try {
				
				RequestVO requestDetail = RequestsDAO.getRequestDetail((String)request.getSession().getAttribute("chg-requests-id-user"), request.getParameter("id-request"));
				
				if ("FIRMADO".equals(requestDetail.getStatus())) {
					String[] datosCircuito = requestDetail.getTemplate().getApprovalCircuit().split(":");
	
					String tipoCircuito = datosCircuito[0]; // PARALELO|CASCADA
					
					UserVO uservo = RequestsDAO.getUserByUserId((String)request.getSession().getAttribute("chg-requests-id-user"));
					byte[] requestSignedPdfContent = RequestsDAO.getRequestSignedPDF((String)request.getSession().getAttribute("chg-requests-id-user"), request.getParameter("id-request"));
					
					Request requestPf = new Request();
					
					requestPf.setSubject(requestDetail.getTemplate().getName() + " - " + uservo.getName());
					requestPf.setReference(request.getParameter("id-request"));
					requestPf.setText("Se requiere la aprobación del documento adjunto enviado y firmado por " + uservo.getName() + ".");
					requestPf.setSignType(SignType.fromString("PARALELO".equals(tipoCircuito)? "PARALELA" : "CASCADA"));
					
					Document document = new Document();
					document.setName("doc-"+ request.getParameter("id-request") +".pdf");
					document.setMime("application/pdf");
					document.setContent(requestSignedPdfContent);
					document.setSign(Boolean.TRUE);
					Document[] documentList = new Document[1];
					documentList[0] = document;
					requestPf.setDocumentList(documentList);
					
					SignLine[] signLineList = new SignLine[datosCircuito.length - 1];
					
					for (int i = 1; i < datosCircuito.length; i++) {
						String[] datosFase = datosCircuito[i].split("-");
						
						String firmante = datosFase[0]; // Cargo o NIF del firmante
						String tipoFase = datosFase[1]; // VISTO_BUENO|FIRMA
						
						Signer[] signerList = new Signer[1];
						Signer signer = new Signer();
						
						if (firmante.matches("[0-9]{8}[a-zA-Z]")) { // Es NIF
							User user = PfirmaWSClient.getUser(firmante);
							if (user == null) {
								throw new Exception("Firmante " + firmante + " no existente o se ha producido un error al invocar a Port@firmas");
							}
							signer.setUserJob(user);
						} else { // Es Cargo
							Job job = PfirmaWSClient.getJob(firmante);
							if (job == null) {
								throw new Exception("Cargo " + firmante + " no existente o se ha producido un error al invocar a Port@firmas");
							}
							signer.setUserJob(job);
						}
						
						signerList[0] = signer;
						
						SignLine signLine = new SignLine();
						signLine.setSignerList(signerList);
						signLine.setType(SignLineType.fromString("VISTO_BUENO".equals(tipoFase) ? "VISTOBUENO" : "FIRMA"));
						signLineList[i - 1] = signLine;
					}
					
					requestPf.setSignLineList(signLineList);
					
					String idRequestPf = PfirmaWSClient.sendRequest(requestPf);
					
					if (idRequestPf == null) {
						throw new Exception("Se ha producido un error al invocar a Port@firmas para el envío de la petición");
					}
					
					RequestsDAO.updateRequestPfReference(request.getParameter("id-request"), idRequestPf);
					
					Map resultgetRequestStatusPf = PfirmaWSClient.getRequestStatus(idRequestPf);
					
					if (resultgetRequestStatusPf == null || resultgetRequestStatusPf.isEmpty()) {
						throw new Exception("Se ha producido un error al invocar a Port@firmas para obtener el estado de la petición " + idRequestPf);
					}
					
					String pfDetailedStatus = "";
					String status = "";
					
					String statusPf = (String)resultgetRequestStatusPf.get("STATUS");
					
			   	 	if ("ACEPTADO".equals(statusPf)) {
			   	 		status =  "APROBADO";
			   	 	} else if ("RECHAZADO".equals(statusPf) || "CADUCADO".equals(statusPf) || "RETIRADO".equals(statusPf)) {
			   	 		status =  "RECHAZADO";
			   	 	} else { // EN PROCESO u otros
			   	 		status = "PENDIENTE_APROBACION";
			   	 	}
					
		   	 		JSONObject pfDetailedStatusJson = new JSONObject();
		   	 		
		   	 		JSONArray statusUsersJson = new JSONArray();
		   	 		
		   	 		List statusUsers = (ArrayList) resultgetRequestStatusPf.get("STATUS_USERS");
		   	 		for (Iterator iterator = statusUsers.iterator(); iterator
							.hasNext();) {
						String statusUser = (String) iterator.next();
						String[] statusUserArray = statusUser.split("#__#");
						
						JSONObject statusUserJson = new JSONObject();
						statusUserJson.put("user", statusUserArray[0]);
						statusUserJson.put("status", statusUserArray[1]);
						statusUserJson.put("date", statusUserArray[2]);
						
						statusUsersJson.add(statusUserJson);
					}
		   	 		
		   	 		pfDetailedStatusJson.put("STATUS_USERS", statusUsersJson);	
		   	 		
		   	 		JSONArray commentsReturnedJson = new JSONArray();
		   	 		
			   	 	List commentsReturned = (ArrayList) resultgetRequestStatusPf.get("COMMENTS_RETURNED");
		   	 		for (Iterator iterator = commentsReturned.iterator(); iterator
							.hasNext();) {
						String commentReturned = (String) iterator.next();
						String[] commentReturnedArray = commentReturned.split("#__#");
						
						JSONObject commentReturnedJson = new JSONObject();
						commentReturnedJson.put("user", commentReturnedArray[0]);
						commentReturnedJson.put("message", commentReturnedArray[1]);
						
						commentsReturnedJson.add(commentReturnedJson);
					}
		   	 		
		   	 		pfDetailedStatusJson.put("COMMENTS_RETURNED", commentsReturnedJson);
		   	 		
		   	 		pfDetailedStatus = pfDetailedStatusJson.toString();
					
					RequestsDAO.updateRequestPfStatus(request.getParameter("id-request"), pfDetailedStatus, status);
				}
				
				response.sendRedirect("request-detail.jsp?id-request=" + request.getParameter("id-request"));
			
			} catch (Exception e) {
				if (e.getMessage().startsWith("ERROR-")) {
					response.sendRedirect("error.jsp?id-error="+ e.getMessage().split("-")[1] + "&id-request=" + request.getParameter("id-request"));
				} else {
					log.error(e, e);
					response.sendRedirect("error.jsp?id-error=0&id-request=" + request.getParameter("id-request"));
				}
			}
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getSession().getAttribute("chg-requests-id-user") == null) {
			throw new ServletException("Sesion expirada");
		}
		
		if (request.getParameter("download-sign-report") != null) {
			
			try {
				
				RequestVO requestvo = RequestsDAO.getRequestDetail((String)request.getSession().getAttribute("chg-requests-id-user"), request.getParameter("id-request"));
				
				if (requestvo == null) {
					
					throw new Exception("No se han podido recuperar los datos de la solicitud " + request.getParameter("id-request"));
					
				} else {
					
					String pfDocumentId = PfirmaWSClient.getRequestDocumentId(requestvo.getPfReference());
					
					if (pfDocumentId == null) {
						throw new Exception("Se ha producido un error al invocar a Port@firmas para obtener el identificador del documento de la petición " + request.getParameter("id-request"));
					}
					
					response.sendRedirect(PfirmaWSClient.getUrlPf() + "/informeFirma?idRequest=" + requestvo.getPfReference() + "&idDocument=" + pfDocumentId);
				}
				
			} catch (Exception e) {
				if (e.getMessage().startsWith("ERROR-")) {
					response.sendRedirect("error.jsp?id-error="+ e.getMessage().split("-")[1]);
				} else {
					log.error(e, e);
					response.sendRedirect("error.jsp?id-error=0");
				}
			}
		}
			
	}
	
}
