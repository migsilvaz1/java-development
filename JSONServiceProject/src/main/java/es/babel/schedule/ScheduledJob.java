package es.babel.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.scheduling.annotation.Scheduled;

import es.babel.db.DataCRUD;
import es.babel.model.DataModel;

public class ScheduledJob {
	// Time mesured in miliseconds from first start to next start
	// @Scheduled(fixedRate = 5000)

	public static final Log log = LogFactory.getLog(ScheduledJob.class);
	public static final ResourceBundle configDB = ResourceBundle.getBundle("configuration");
	public static int lastIndex = 0;

	@Scheduled(cron = "${cron.expression}")
	public void doJob() {
		Boolean active = Boolean.valueOf(configDB.getString("scheduler.active"));
		if (active) {
			log.info("Job initialized at " + new Date());
			log.info("Deleting older pages");
			// get page list
			List<DataModel> dataList = DataCRUD.readAllData();
			List<String> LiferayFriendlyUrls = deletePages(dataList);
			log.info("creating new pages");
			createPages(dataList, LiferayFriendlyUrls);
			log.info("Job finished at " + new Date());
		} else {
			log.info("Job inactive");
		}
	}

	public static void createPages(List<DataModel> dataList, List<String> LiferayFriendlyUrls) {
		try {
			if (dataList != null) {
				// Load configuration

				String ip = configDB.getString("liferay.ip");
				Integer port = Integer.valueOf(configDB.getString("liferay.port"));
				String user = configDB.getString("liferay.user");
				String pass = configDB.getString("liferay.pass");
				Integer size;

				try {
					size = Integer.valueOf(configDB.getString("cron.items.sincro"));
				} catch (NumberFormatException e) {
					size = null;
				}

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
				List<DataModel> toUpdate = (size != null) ? dataList.subList(lastIndex, lastIndex + size) : dataList;
				for (DataModel data : toUpdate) {
					if (!LiferayFriendlyUrls.contains(data.getFriendlyURL())) {
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
				}
				httpclient.getConnectionManager().shutdown();

				log.info("Job finished at " + new Date());
				if (size != null)
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
	}

	public static List<String> deletePages(List<DataModel> datas) {
		List<String> liferayFirendlyUrls = new ArrayList<String>();
		try {
			String ip = configDB.getString("liferay.ip");
			Integer port = Integer.valueOf(configDB.getString("liferay.port"));
			String user = configDB.getString("liferay.user");
			String pass = configDB.getString("liferay.pass");

			List<String> friendly = new ArrayList<String>();
			for (DataModel data : datas) {
				friendly.add(data.getFriendlyURL());
			}

			HttpHost targetHost = new HttpHost(ip, port, "http");
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.getCredentialsProvider().setCredentials(
					new AuthScope(targetHost.getHostName(), targetHost.getPort()),
					new UsernamePasswordCredentials(user, pass));

			// Create AuthCache instance
			AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local
			// auth cache
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);

			// Add AuthCache to the execution context
			BasicHttpContext ctx = new BasicHttpContext();
			ctx.setAttribute(ClientContext.AUTH_CACHE, authCache);

			HttpPost post = new HttpPost("/api/jsonws/layout/get-layouts");
			HttpPost postDelete = new HttpPost("/api/jsonws/layout/delete-layout");

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("groupId", "20182"));
			params.add(new BasicNameValuePair("privateLayout", "false"));

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			post.setEntity(entity);
			HttpResponse resp = httpclient.execute(targetHost, post, ctx);
			BufferedReader reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(), "UTF-8"));
			String json = reader.readLine();
			JSONTokener tokener = new JSONTokener(json);
			JSONArray finalResult = new JSONArray(tokener);
			for (int i = 0; i < finalResult.length(); ++i) {
				JSONObject rec = finalResult.getJSONObject(i);
				String friendlyURL = rec.getString("friendlyURL");
				String type = rec.getString("type");

				liferayFirendlyUrls.add(friendlyURL);

				if ((!friendly.contains(friendlyURL)) && type.equalsIgnoreCase("url")) {
					Long plid = rec.getLong("plid");
					List<NameValuePair> paramsDelete = new ArrayList<NameValuePair>();
					paramsDelete.add(new BasicNameValuePair("plid", String.valueOf(plid)));
					UrlEncodedFormEntity entityDelete = new UrlEncodedFormEntity(paramsDelete, "UTF-8");
					postDelete.setEntity(entityDelete);
					HttpResponse respDelete = httpclient.execute(targetHost, postDelete, ctx);
					log.info(respDelete.getStatusLine());
					respDelete.getEntity().writeTo(System.out);
				}
			}
			httpclient.getConnectionManager().shutdown();
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return liferayFirendlyUrls;
	}
}
