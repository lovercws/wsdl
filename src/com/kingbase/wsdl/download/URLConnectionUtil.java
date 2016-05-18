package com.kingbase.wsdl.download;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * HttpURLConnection 工具类
 * @author ganliang
 */
public class URLConnectionUtil {

	private static final Logger log=Logger.getLogger(URLConnectionUtil.class);
	//初始化连接请求
	protected HttpURLConnection initializeConnection(String connectionURL) {
		HttpURLConnection connection;
		try {
			URL url = new URL(connectionURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "text/xml; charset=GB2312");
			connection.setRequestMethod("GET");
			//connection.setConnectTimeout(10000);//ms unit
			connection.setDoOutput(true);
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//发送请求
	protected boolean sendRecord(String connectionURL, String parameters) {
		if (parameters == null) {
			parameters = "";
		}
		HttpURLConnection connection = initializeConnection(connectionURL);
		if (connection == null) {
			return false;
		}
		try {
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();

			int responseCode = connection.getResponseCode();
			return responseCode == 200;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//发送请求 并接受返回值
	protected String receive(String connectionURL) {
		// 初始化连接
		HttpURLConnection connection = initializeConnection(connectionURL);
		if (connection == null) {
			return null;
		}
		try {
			// 发送请求
			int responseCode = connection.getResponseCode();
			// 接受返回数据
			if (responseCode == 200) {
				BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = buffer.readLine()) != null) {
					response.append(inputLine);
				}
				buffer.close();
				return response.toString();
			}
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		URLConnectionUtil connectionUtil=new URLConnectionUtil();
		String receive = connectionUtil.receive("http://www.webxml.com.cn/webservices/ChinaStockSmallImageWS.asmx?wsdl");
		System.out.println(receive);
	}
}
