package com.vht.sms.content.dmvt;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HTTPGateway {
	
	public static List<String> HttpPost(URL url, List<HttpHeader> requestList)
			throws IOException {
		List<String> responseList = new ArrayList<String>();
		URLConnection con;
		BufferedReader in;
		OutputStreamWriter out;
		StringBuffer req;
		String line;
		con = url.openConnection();
		con.setConnectTimeout(20000);
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		out = new OutputStreamWriter(con.getOutputStream());
		req = new StringBuffer();
		for (int i = 0, n = requestList.size(); i < n; i++) {
			if (i != 0)
				req.append("&");
			req.append(requestList.get(i).key);
			req.append("=");
			if (requestList.get(i).unicode) {
				StringBuffer tmp = new StringBuffer(200);
				byte[] uniBytes = requestList.get(i).value
						.getBytes("UnicodeBigUnmarked");
				for (int j = 0; j < uniBytes.length; j++)
					tmp
							.append(Integer.toHexString(uniBytes[j]).length() == 1 ? "0"
									+ Integer.toHexString(uniBytes[j])
									: Integer.toHexString(uniBytes[j]));
				req.append(tmp.toString().replaceAll("ff", ""));
			} else
				req.append(URLEncoder.encode(requestList.get(i).value,"utf-8"));
		}
		System.out.println(req.toString().replaceAll("%40", "@"));
		out.write(req.toString().replaceAll("%40", "@"));
		out.flush();
		out.close();
		in = new BufferedReader(new InputStreamReader((con.getInputStream())));
		while ((line = in.readLine()) != null)
			responseList.add(line);

		in.close();
		return responseList;
	}
	public static String postToURL(String url, String message,
			DefaultHttpClient httpClient) throws Exception {
		HttpPost postRequest = new HttpPost(url);

		StringEntity input = new StringEntity(message);
		input.setContentType("application/x-www-form-urlencoded");
		postRequest.setEntity(input);
		
		HttpResponse response = httpClient.execute(postRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(response.getEntity().getContent())));

		String output;
		StringBuffer totalOutput = new StringBuffer();
		while ((output = br.readLine()) != null) {
			totalOutput.append(output);
			System.out.println("responese:   "+output);
		}
		return totalOutput.toString();
	}
	public static List<String> HttpGet(URL url) throws IOException {
		List<String> responseList = new ArrayList<String>();
		URLConnection con = url.openConnection();
		con.setConnectTimeout(80000);
		con.setAllowUserInteraction(false);
		BufferedReader in = new BufferedReader(new InputStreamReader(con
				.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			responseList.add(inputLine);
		in.close();
		return responseList;
	}

	public String ExpandHttpHeaders(List<HttpHeader> httpHeaderList) {
		StringBuffer buffer = new StringBuffer();
		for (HttpHeader h : httpHeaderList) {
			buffer.append(h.key);
			buffer.append("=");
			buffer.append(h.value);
			buffer.append("&");
		}
		return buffer.toString();
	}

}
