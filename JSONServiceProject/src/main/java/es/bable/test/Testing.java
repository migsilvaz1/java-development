package es.bable.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.http.HttpEntity;
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
import org.apache.http.util.EntityUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class Testing {

	public static void main(String[] args) {
		try {
			ResourceBundle configDB = ResourceBundle.getBundle("configuration");
			String ip = configDB.getString("liferay.ip");
			Integer port = Integer.valueOf(configDB.getString("liferay.port"));

			// configure petition
			HttpHost targetHost = new HttpHost(ip, port, "http");
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.getCredentialsProvider().setCredentials(
					new AuthScope(targetHost.getHostName(), targetHost.getPort()),
					new UsernamePasswordCredentials("test@liferay.com", "test"));

			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);

			BasicHttpContext ctx = new BasicHttpContext();
			ctx.setAttribute(ClientContext.AUTH_CACHE, authCache);
			HttpPost post = new HttpPost("/api/jsonws/layout/add-layout");

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("groupId", ""));
			params.add(new BasicNameValuePair("privateLayout", ""));
			params.add(new BasicNameValuePair("parentLayoutId", ""));
			params.add(new BasicNameValuePair("name", ""));
			params.add(new BasicNameValuePair("title", ""));
			params.add(new BasicNameValuePair("description", ""));
			params.add(new BasicNameValuePair("type", ""));
			params.add(new BasicNameValuePair("hidden", ""));
			params.add(new BasicNameValuePair("friendlyURL", ""));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			post.setEntity(entity);
			
			HttpResponse resp = httpclient.execute(targetHost, post, ctx);
			String response = EntityUtils.toString(resp.getEntity());
			System.out.println(response);
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
	}

}
