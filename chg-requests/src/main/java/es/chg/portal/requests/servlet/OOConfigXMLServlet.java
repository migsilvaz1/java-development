package es.chg.portal.requests.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class OOConfigXMLServlet
 */
public class OOConfigXMLServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OOConfigXMLServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getSession().getAttribute("chg-requests-id-user") == null) {
			throw new ServletException("Sesion expirada");
		}
		
		request.getSession().setAttribute("chg-requests-rowid", "");
		
		String url = request.getRequestURL().toString();
		String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
		
		response.setContentType("application/xml"); 
		
		PrintWriter out = response.getWriter(); 
		
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version='1.0' encoding='ISO-8859-15' ?>\n");
		sb.append("<ooconfig>");
		sb.append("<document type=\"edit\" copies=\"1\" autosave=\"false\" localcopy=\"false\">\n");
		sb.append("<source type=\"loaded\">\n");
		sb.append("<path>" + baseURL + "/DownloadTemplateServlet?idTemplate=" + request.getParameter("idTemplate") + "</path>\n");
		sb.append("</source>\n");
		sb.append("<storage>\n");
		sb.append("<path>" + baseURL + "/OOUploadFileServlet/?docExp=" + request.getParameter("idTemplate") + ";nombDoc=solicitud.pdf</path>\n");
		//sb.append("<path>/OOUploadFileServlet</path>\n");
		sb.append("<defaulttype>writer_pdf_Export</defaulttype>\n");
		sb.append("</storage>\n");
		sb.append("</document>\n");
		sb.append("</ooconfig>\n");

		out.println(sb.toString());
		out.flush();
		      
	}

}
