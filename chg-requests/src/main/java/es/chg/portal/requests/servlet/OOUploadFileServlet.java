package es.chg.portal.requests.servlet;

import http.utils.multipartrequest.MultipartRequest;
import http.utils.multipartrequest.ServletMultipartRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.chg.portal.requests.db.RequestsDAO;

/**
 * Servlet implementation class OOUploadFileServlet
 */
public class OOUploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OOUploadFileServlet() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if (request.getSession().getAttribute("chg-requests-id-user") == null) {
			throw new ServletException("Sesion expirada");
		}
		
		InputStream in = null;
		
		try {
			
	        MultipartRequest parser = new ServletMultipartRequest(request, ".", MultipartRequest.MAX_READ_BYTES);

	        for (Enumeration e = parser.getFileParameterNames(); e.hasMoreElements();) {
		        String name = (String) e.nextElement();
		        
		        in = new FileInputStream(parser.getFile(name));
		        
		        String idTemplate = parser.getURLParameter("doc");
		        
		        String rowid = (String) request.getSession().getAttribute("chg-requests-rowid");
		        
		        if (rowid == null) {
		        	throw new ServletException("Sesion expirada");
		        } else if (rowid.length() == 0) {
		        	rowid = RequestsDAO.insertRequest((String)request.getSession().getAttribute("chg-requests-id-user"), idTemplate, in);
		        } else {
		        	RequestsDAO.updateRequest(rowid, (String)request.getSession().getAttribute("chg-requests-id-user"), idTemplate, in);
		        }
		        
		        request.getSession().setAttribute("chg-requests-rowid", rowid);
		        
	        }

			resp.setStatus(200);
			resp.setContentType("text/plain");
			
			PrintWriter out = resp.getWriter(); 
			out.println("OK");
			out.flush();
			
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {}
		}

	}

}
