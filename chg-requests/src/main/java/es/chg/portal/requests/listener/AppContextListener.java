package es.chg.portal.requests.listener;

import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import es.chg.portal.requests.db.DBConnectionFactory;

public class AppContextListener implements ServletContextListener {
	
	private static Logger logger = Logger.getLogger(AppContextListener.class);
	
	private static ResourceBundle configDB = ResourceBundle.getBundle("es.chg.portal.requests.config.db");

    public AppContextListener() {
    	PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("es/chg/portal/requests/config/log4j.properties"));
    }

    public void contextInitialized(ServletContextEvent arg0) {
    	try {
			DBConnectionFactory.configure(configDB.getString("db.url"), configDB.getString("db.user"), configDB.getString("db.password"), configDB.getString("db.driver"));
		} catch (Exception e) {
			logger.error(e, e);
		}
    }

    public void contextDestroyed(ServletContextEvent arg0) {
    	try {
			DBConnectionFactory.shutdownDriver();
		} catch (Exception e) {
			logger.error(e, e);
		}
    }
	
}
