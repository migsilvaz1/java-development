package es.chg.portal.requests.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.gfi.afirma.wsclient.certificate.ValidarCertificado;

import es.chg.portal.requests.db.RequestsDAO;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(LoginServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {
						
			ValidarCertificado validarCertificado = new ValidarCertificado();
			Map<String, String> resultValidarCertificado = validarCertificado.esCertificadoValidoConNIF(request.getParameter("certificate-b64"));

			if (resultValidarCertificado == null || resultValidarCertificado.isEmpty()) {
				
				throw new Exception("ERROR-1"); // Error en la validación del certificado
				
			} else if (ValidarCertificado.RES_VALIDACION_OK.equals(resultValidarCertificado.get(ValidarCertificado.RES_RESULTADO))) {
				
				String userId = RequestsDAO.getUserIdByIdentifier(resultValidarCertificado.get(ValidarCertificado.RES_NIF));
				
				if (userId != null) {
					request.getSession().setAttribute("chg-requests-id-user", userId);
					response.sendRedirect("requests-list.jsp");
				} else {
					request.getSession().removeAttribute("chg-requests-id-user");
					throw new Exception("ERROR-4"); // No existe ningún usuario registrado con esas credenciales
				}

			} else {
				throw new Exception("ERROR-3"); // Certificado no válido
			}
		
		} catch (Exception e) {
			if (e.getMessage().startsWith("ERROR-")) {
				response.sendRedirect("error.jsp?id-error="+ e.getMessage().split("-")[1] + "&source=login.jsp");
			} else {
				log.error(e, e);
				response.sendRedirect("error.jsp?id-error=0&source=login.jsp");
			}
		}

	}
	
}
