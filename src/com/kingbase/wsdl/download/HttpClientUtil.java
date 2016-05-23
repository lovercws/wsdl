package com.kingbase.wsdl.download;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.print.Doc;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * wsdl为网络上发布的服务 可以缓存wsdl
 * @author ganliang
 *
 */
public class HttpClientUtil {

	private static final Logger log = Logger.getLogger(URLConnectionUtil.class);
    private static final ConcurrentHashMap<String,String> wsdlMap=new ConcurrentHashMap<String,String>();//缓存下载结果
    
    //发送httpClient请求
	public String sendRecord(String uri) {
   		String responseBody=null;
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();

			HttpGet http = new HttpGet(uri);
			CloseableHttpResponse response = httpClient.execute(http);

			responseBody = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			log.error("", e);
		} catch (ParseException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}
		return responseBody;
	}

	//首先查询wsdlMap 存在则直接返回 否则网上下载
	public String receive(String uri){
		if(uri==null||"".equals(uri)){
			throw new IllegalArgumentException();
		}
		String resource = wsdlMap.get(uri);
		if(resource==null){
			resource=sendRecord(uri);
			log.debug("发送请求【"+uri+"】");
			wsdlMap.put(uri, resource);
		}
		log.debug("返回结果  "+resource);
		return resource;
	}
	
	public static void main(String[] args) throws DocumentException {
		HttpClientUtil clientUtil = new HttpClientUtil();
		String sendRecord = clientUtil.sendRecord("http://www.webxml.com.cn/webservices/ChinaStockSmallImageWS.asmx?wsdl");
		System.out.println(sendRecord);
		
		Document document = DocumentHelper.parseText(sendRecord);
		System.out.println(document);
	}
}
