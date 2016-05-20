package com.kingbase.wsdl.parser.caller;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.soap.Constants;
import org.apache.soap.Fault;
import org.apache.soap.SOAPException;
import org.apache.soap.encoding.SOAPMappingRegistry;
import org.apache.soap.encoding.soapenc.StringDeserializer;
import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Parameter;
import org.apache.soap.rpc.Response;
import org.apache.soap.transport.http.SOAPHTTPConnection;
import org.apache.soap.util.xml.QName;

/**
 * wsdl方法调用(报错)
 * @author ganliang
 */
public class SOAPCaller {

	public static void main(String[] args) {
		URL url = null;
		try {
			url = new URL("http://www.webxml.com.cn/WebServices/WeatherWS.asmx?wsdl");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		SOAPMappingRegistry smr = new SOAPMappingRegistry();
		StringDeserializer deser = new StringDeserializer();
		smr.mapTypes(Constants.NS_URI_SOAP_ENC, new QName("http://WebXml.com.cn/", "getRegionDatasetResult"), null, null,
				deser);

		SOAPHTTPConnection st = new SOAPHTTPConnection();

		Call call = new Call();
		call.setSOAPTransport(st);
		call.setSOAPMappingRegistry(smr);

		// 创建传输路径和参数
		call.setTargetObjectURI("WeatherWS");
		call.setMethodName("getRegionDataset");
		call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);

		// 发出调用
		Response resp = null;
		try {
			resp = call.invoke(url, "http://WebXml.com.cn/getRegionDataset");
		} catch (SOAPException e) {
			System.err.println("Caught SOAPException (" + e.getFaultCode() + "): " + e.getMessage());
			System.exit(-1);
		}
		//Parameter returnValue = resp.getReturnValue();
		//System.out.println(returnValue.getValue());
		if (!resp.generatedFault()) {
			Parameter ret = resp.getReturnValue();
			System.out.println(ret);
		} else {
			Fault fault = resp.getFault();
			System.err.println("Generated fault: ");
			System.out.println(" Fault Code = " + fault.getFaultCode());
			System.out.println(" Fault String = " + fault.getFaultString());
		}
	}
}
