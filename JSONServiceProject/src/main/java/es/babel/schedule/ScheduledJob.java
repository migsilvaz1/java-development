package es.babel.schedule;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.springframework.scheduling.annotation.Scheduled;

import es.babel.db.DataCRUD;
import es.babel.model.DataModel;

public class ScheduledJob {
	// Time mesured in miliseconds from first start to next start
	// @Scheduled(fixedRate = 5000)

	public static final Log log = LogFactory.getLog(ScheduledJob.class);
	public static int lastIndex = 0;

	@Scheduled(cron = "${cron.expression}")
	public void doJob() {
		ResourceBundle configDB = ResourceBundle.getBundle("configuration");
		Boolean active = Boolean.valueOf(configDB.getString("scheduler.active"));
		if (active) {
			log.info("Job initialized at " + new Date());
			try {
				// Load data from db
				List<DataModel> dataList = DataCRUD.readAllData();
				if (dataList != null) {
					// Load configuration

					String ip = configDB.getString("liferay.ip");
					Integer port = Integer.valueOf(configDB.getString("liferay.port"));
					Integer size = Integer.valueOf(configDB.getString("cron.items.sincro"));
					String user = configDB.getString("liferay.user");
					String pass = configDB.getString("liferay.pass");

					// configure petition
					HttpHost targetHost = new HttpHost(ip, port, "http");
					DefaultHttpClient httpclient = new DefaultHttpClient();
					httpclient.getCredentialsProvider().setCredentials(
							new AuthScope(targetHost.getHostName(), targetHost.getPort()),
							new UsernamePasswordCredentials(user, pass));

					AuthCache authCache = new BasicAuthCache();
					BasicScheme basicAuth = new BasicScheme();
					authCache.put(targetHost, basicAuth);

					// List to update
					List<DataModel> toUpdate = dataList.subList(lastIndex, lastIndex + size);
					for (DataModel data : toUpdate) {
						log.info("creating page");
						BasicHttpContext ctx = new BasicHttpContext();
						ctx.setAttribute(ClientContext.AUTH_CACHE, authCache);
						HttpPost post = new HttpPost("/api/jsonws/layout/add-layout");

						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("groupId", data.getGroupId().toString()));
						params.add(new BasicNameValuePair("privateLayout", data.getPrivateLayout().toString()));
						params.add(new BasicNameValuePair("parentLayoutId", data.getParentLayoutId().toString()));
						params.add(new BasicNameValuePair("name", data.getName()));
						params.add(new BasicNameValuePair("title", data.getTitle()));
						params.add(new BasicNameValuePair("description", data.getDescription()));
						params.add(new BasicNameValuePair("type", data.getType()));
						params.add(new BasicNameValuePair("hidden", data.getHidden().toString()));
						params.add(new BasicNameValuePair("friendlyURL", data.getFriendlyURL()));
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
						post.setEntity(entity);
						HttpResponse resp = httpclient.execute(targetHost, post, ctx);

						log.info(resp.getStatusLine());
						resp.getEntity().writeTo(System.out);
					}
					httpclient.getConnectionManager().shutdown();

					log.info("Job finished at " + new Date());
					lastIndex = lastIndex + size;
				}
			} catch (HttpHostConnectException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			log.info("Job inactive");
		}
	}
}
