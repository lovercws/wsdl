package com.kingbase.wsdl.download;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Demo {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		String soapRequestData = ""
				+ "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">"
				+ "  <soap:Body>"
				+ "   <qqCheckOnline xmlns=\"http://WebXml.com.cn/\">"
				+ "    <qqCode>396738007</qqCode>"
				+ "   </qqCheckOnline>"
				+ "  </soap:Body>"
				+ "</soap:Envelope>";
		
		URL u = new URL("http://www.webxml.com.cn/WebServices/WeatherWS.asmx?wsdl");

		HttpURLConnection connection = (HttpURLConnection) u.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");

		PrintWriter pw = new PrintWriter(connection.getOutputStream());
		pw.println(soapRequestData);
		pw.close();

		// 发送请求
		int responseCode = connection.getResponseCode();
		// 接受返回数据
		BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while ((inputLine = buffer.readLine()) != null) {
			response.append(inputLine);
		}
		buffer.close();
		System.out.println(response.toString());

	}
}
