package com.kingbase.wsdl.parser.caller;

import java.util.HashMap;
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.log4j.Logger;

import com.kingbase.wsdl.parser.util.XMLSupport;

/**
 * 使用axis2调用wsdl
 * @author ganliang
 */
public class AxisCaller {

	private static final Logger log=Logger.getLogger(AxisCaller.class);
	
	/**
	 * 调用wsdl方法
	 * @param urlwsdl url地址
	 * @param nameSpace 命名空间
	 * @param methodName 调用的方法名称
	 * @param parameterMap 参数map
	 * @return
	 */
	public String caller(String url,String nameSpace,String methodName, Map<String, Object> parameterMap) {
		OMElement result = null;
		try {
			Options options = new Options();
			// 指定调用WebService的URL
			options.setTo(new EndpointReference(url));
			options.setAction(nameSpace+methodName);

			ServiceClient serviceClient = new ServiceClient();
			serviceClient.setOptions(options);

			OMElement parameterElement = XMLSupport.createParameterElement(nameSpace,methodName,parameterMap);
            System.out.println(parameterElement.toString());
			result = serviceClient.sendReceive(parameterElement);
		} catch (AxisFault axisFault) {
			log.error("axis2调用wsdl出现异常", axisFault);
		}
		log.debug("调用结果 "+result.toString());
		return result.toString();
	}
	
	
	
	public static void main(String[] args) {
		AxisCaller caller=new AxisCaller();
		Map<String, Object> parameterMap=new HashMap<String, Object>();
		parameterMap.put("lgan", "ganliang");
		parameterMap.put("lover", "cws");
		
		//String caller2=caller.caller("http://www.webxml.com.cn/WebServices/WeatherWS.asmx?wsdl", "http://WebXml.com.cn/", "getRegionDataset", parameterMap);
		String caller2 = caller.caller("http://192.168.8.144:9999/services/helloWord?wsdl", "http://service.cytoscape.com/", "sayHi", parameterMap);
		System.out.println(caller2);
	}
}
