package com.kingbase.wsdl.parser;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.soap.Fault;
import org.apache.soap.Header;
import org.apache.soap.SOAPException;
import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Parameter;
import org.apache.soap.rpc.Response;
import org.apache.soap.rpc.SOAPContext;
import org.apache.soap.util.xml.QName;

/**
 * wsdl方法调用
 * 
 * @author ganliang
 */
public class WSDLCaller {

	public static void main(String[] args) {
		URL url=null;
		try {
			url = new URL("http://www.webxml.com.cn/WebServices/WeatherWS.asmx?wsdl");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		// 构造call
		Call call = new Call();
		call.setFullTargetObjectURI("http://WebXml.com.cn/getRegionCountry");
		
		//call.setTargetObjectURI("urn:WeatherWS");
		call.setMethodName("getRegionCountry");
		call.setEncodingStyleURI(org.apache.soap.Constants.NS_URI_SOAP_ENC);

		Header header=new Header();
		header.setAttribute(new QName("", ""), "");
		call.setHeader(header);
		/*
		 * Vector params = new Vector(); params.addElement(new Parameter("name",
		 * String.class, name, null)); call.setParams(params);
		 */

		// 发出调用
		Response resp = null;
		try {
			resp = call.invoke(url, "");
		} catch (SOAPException e) {
			System.err.println("Caught SOAPException (" + e.getFaultCode() + "): " + e.getMessage());
			System.exit(-1);
		}

		if (!resp.generatedFault()) {
			Parameter ret = resp.getReturnValue();
			Object value = ret.getValue();
			System.out.println(value);
		} else {
			Fault fault = resp.getFault();
			System.err.println("Generated fault: ");
			System.out.println(" Fault Code = " + fault.getFaultCode());
			System.out.println(" Fault String = " + fault.getFaultString());
		}

	}
}
