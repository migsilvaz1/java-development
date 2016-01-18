package es.chg.portal.requests.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.chg.portal.requests.db.RequestsDAO;

/**
 * Servlet implementation class DownloadTemplateServlet
 */
public class DownloadRequestDocServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadRequestDocServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getSession().getAttribute("chg-requests-id-user") == null) {
			throw new ServletException("Sesion expirada");
		}
		
		String mode = "inline";
		if (request.getParameter("mode") != null) {
			mode = request.getParameter("mode");
		}
		
		String type = "default";
		if (request.getParameter("type") != null) {
			type = request.getParameter("type");
		}
		
        InputStream in = null;
        
		try {
			if ("signed".equals(type)) {
				in = new ByteArrayInputStream(RequestsDAO.getRequestSignedPDF((String)request.getSession().getAttribute("chg-requests-id-user"), request.getParameter("id")));
			} else {
				in = new ByteArrayInputStream(RequestsDAO.getRequestPDF((String)request.getSession().getAttribute("chg-requests-id-user"), request.getParameter("id")));
			}
		} catch (SQLException e) {
			throw new ServletException(e);
		}
		
        byte [] buffer = new byte[4096];
        int nBytes = in.read(buffer);

        OutputStream outS = response.getOutputStream();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", mode + "; filename=\"request-" + request.getParameter("id") + ".pdf\"");

        while (nBytes != -1) {
            outS.write(buffer, 0, nBytes);
            nBytes = in.read(buffer);
        }
        
        in.close();
        outS.flush();
        outS.close();
        
	}

}
