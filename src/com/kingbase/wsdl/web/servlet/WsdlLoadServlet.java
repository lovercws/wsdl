package com.kingbase.wsdl.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdom.output.XMLOutputter;

import com.kingbase.wsdl.parser.caller.AxisCaller;
import com.kingbase.wsdl.parser.info.OperationInfo;
import com.kingbase.wsdl.parser.info.ParameterInfo;
import com.kingbase.wsdl.parser.info.ServiceInfo;
import com.kingbase.wsdl.parser.parse.WSDLParser;
import com.kingbase.wsdl.parser.util.CacheUtil;
import com.kingbase.wsdl.parser.util.XMLSupport;

@WebServlet(urlPatterns={"/WsdlLoadServlet"})
public class WsdlLoadServlet extends HttpServlet{

	private static final Logger log=Logger.getLogger(WsdlLoadServlet.class);
	private static final long serialVersionUID = -3198990186345076733L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("application/json;charset=UTF-8");
		//response.setContentType("application/x-json");
		response.setCharacterEncoding("utf-8");
		String type=request.getParameter("type");
		String json="";
	    if(type==null){
	    	throw new IllegalArgumentException();
	    }
	    switch (type) {
	    //加载解析过的 wsdl
		case "load":
			json=load();
			break;
		//提交一个 soap
		case "soapSubmit":
			String wsdlUri=request.getParameter("wsdlUri");
			WSDLParser parser=new WSDLParser();
			parser.parse(wsdlUri);
			json=load();
			break;
		//获取参数
		case "getParameterDATA":
			json=getParameterDATA(request);
			json=transformParameterXML(json);
			break;
		//调用方法 返回结果
		case "getResultDATA":
			json=getResultDATA(request);
			json=transformResultXML(json);
			break;
		default:
			break;
		}
	    log.info(json);
	    response.getWriter().print(json);
	}

	/**
	 * 加载数据
	 * @return
	 */
	private String load() {
		ConcurrentHashMap<String,ServiceInfo> map = CacheUtil.getAll();
		if(map.size()==0){
			return "[{\"text\":\"Projects\"}]";
		}
		StringBuilder serviceBuilder=new StringBuilder("[");
		Iterator<Entry<String, ServiceInfo>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, ServiceInfo> entry = iterator.next();
			ServiceInfo serviceInfo = entry.getValue();
			
			StringBuilder operationBuilder=new StringBuilder("[");
			Iterator<OperationInfo> operations = serviceInfo.getOperations();
			while(operations.hasNext()){
				OperationInfo operationInfo = operations.next();
				operationBuilder.append("{\"text\":\""+operationInfo.getTargetMethodName()+"\",\"parentName\":\""+serviceInfo.getName()+"\"}");
				if(operations.hasNext()){
					operationBuilder.append(",");
				}
			}
			operationBuilder.append("]");
			
			serviceBuilder.append("{\"text\":\""+serviceInfo.getName()+"\",\"children\":"+operationBuilder.toString()+"}");
			if(iterator.hasNext()){
				serviceBuilder.append(",");
			}
		}
		serviceBuilder.append("]");
		
		String json="[{\"text\":\"Projects\",\"children\":"+serviceBuilder.toString()+"}]";
		return json;
	}
	
	/**
	 * 获取参数
	 * @param request 
	 */
	private String getParameterDATA(HttpServletRequest request) {
		String serverName=request.getParameter("serverName");
		String methodName=request.getParameter("methodName");
		
		ServiceInfo serviceInfo = CacheUtil.getServiceInfo(serverName);
		if(serviceInfo==null){
		    return "";
		}
		Map<String, Object> parameterMap=getParameterMap(serviceInfo, methodName);
		
		OMElement omElement = XMLSupport.createParameterElement(serviceInfo.getTargetnamespace(), methodName, parameterMap);
	    return omElement.toString();
	}
	
	/**
	 * 获取到参数map
	 * @param serviceInfo
	 * @param methodName
	 * @return
	 */
	private Map<String, Object> getParameterMap(ServiceInfo serviceInfo,String methodName){
		Map<String, Object> parameterMap=new HashMap<String, Object>();
		List<OperationInfo> operations = serviceInfo.getOperation();
		
		loop:
		for (OperationInfo operationInfo : operations) {
			if(operationInfo.getTargetMethodName().equals(methodName)){
				List<ParameterInfo> inparameters = operationInfo.getInparameters();
				for (ParameterInfo parameterInfo : inparameters) {
					parameterMap.put(parameterInfo.getName(), "?");
				}
				break loop;
			}
		}
		return parameterMap;
	}
	
	/**
	 * 获取方法调用的结果
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getResultDATA(HttpServletRequest request) {
		String serverName=request.getParameter("serverName");
		String methodName=request.getParameter("methodName");
		String value=request.getParameter("parameterXML");
		String json="";
		try {
			ServiceInfo serviceInfo = CacheUtil.getServiceInfo(serverName);
			if(serviceInfo==null){
			    return "";
			}
			Document document = DocumentHelper.parseText(value);
			Element rootElement = document.getRootElement();
			List<Element> elements = rootElement.elements();
			
			Map<String,Object> parameterMap=new HashMap<String,Object>();
			for (Element element : elements) {
				parameterMap.put(element.getName(), element.getTextTrim());
			}
			//调用
			AxisCaller caller=new AxisCaller();
			json = caller.caller(serviceInfo.getWsdllocation(), serviceInfo.getTargetnamespace(), methodName, parameterMap);
		} catch (Exception e) {
			json="";
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 将xml字符串添加换行 空格等
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String transformParameterXML(String xml) {
		StringBuilder builder=new StringBuilder();
		try {
			Document document = DocumentHelper.parseText(xml);
			//添加根几点
			Element rootElement = document.getRootElement();
			String namespaceURI = rootElement.getNamespaceURI();
			builder.append("<"+rootElement.getName()+"  xmlns=\""+namespaceURI+"\">\r\n");
			//遍历子节点
			List<Element> elements = rootElement.elements();
			for (Element element : elements) {
				String name = element.getName();
				String text = element.getText();
				builder.append("&nbsp&nbsp<"+name+">"+text+"</"+name+">\r\n");
			}
			
			builder.append("</"+rootElement.getName()+">");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		//XMLSupport.outputString(doc);
		return builder.toString();
	}
	
	/**
	 * 将xml字符串添加换行 空格等
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String transformResultXML(String xml) {
		StringBuilder builder=new StringBuilder();
		try {
			Document document = DocumentHelper.parseText(xml);
			//添加根几点
			Element rootElement = document.getRootElement();
			String namespaceURI = rootElement.getNamespaceURI();
			builder.append("<"+rootElement.getName()+"  xmlns=\""+namespaceURI+"\">\r\n");
			//遍历子节点
			List<Element> elements = rootElement.elements();
			for (Element element : elements) {
				String name = element.getName();
				builder.append("&nbsp&nbsp<"+name+">\r\n");
				
				List<Element> elements2 = element.elements();
				for (Element ele : elements2) {
					builder.append("&nbsp&nbsp&nbsp&nbsp<"+ele.getName()+">"+ele.getText()+"</"+ele.getName()+">\r\n");
				}
				
				builder.append("&nbsp&nbsp</"+name+">\r\n");
			}
			
			builder.append("</"+rootElement.getName()+">");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
