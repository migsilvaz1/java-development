package es.babel.schedule;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.babel.db.DataCRUD;
import es.babel.model.DataModel;

@Controller
public class AppController {

	public static final Log log = LogFactory.getLog(AppController.class);

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLogin(ModelMap model) {
		model.addAttribute("message", "");
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginAction(HttpServletRequest request, Model model) {
		String pass = request.getParameter("pass");
		ResourceBundle configDB = ResourceBundle.getBundle("configuration");
		String mainPass = configDB.getString("access.pass");
		if (pass.equals(mainPass)) {
			return "list";
		} else {
			model.addAttribute("message", "La contraseña es incorrecta");
			return "login";
		}
	}

	@RequestMapping(value = "/listado", method = RequestMethod.GET)
	public String dataModelList(Model model) {
		return "list";
	}
	
	@RequestMapping(value = "/listado", method = RequestMethod.POST)
	public String dataModelAdd(HttpServletRequest request, Model model) {
		Integer userId = Integer.valueOf(request.getParameter("userId"));
		Integer groupId = Integer.valueOf(request.getParameter("groupId"));
		Boolean privateLayout = Boolean.valueOf(request.getParameter("privateLayout"));
		Integer parentLayoutId = Integer.valueOf(request.getParameter("parentLayoutId"));
		String name = request.getParameter("name");
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String keywords = request.getParameter("keywords");
		String robots = request.getParameter("robots");
		String type = request.getParameter("type");
		String typeSettings = request.getParameter("typeSettings");
		Boolean hidden = Boolean.valueOf(request.getParameter("hidden"));
		String friendlyUrl = request.getParameter("friendlyUrl");
		
		DataCRUD.createData(new DataModel(0, userId, groupId, privateLayout, parentLayoutId, name, title, description, keywords, robots, type, typeSettings, hidden, friendlyUrl));
		
		return "list";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String dataModelDelete(@PathVariable("id") Integer id) {
		if(id != null){
			DataCRUD.deleteData(DataCRUD.readData(id));
		}
		return "list";
	}
}
