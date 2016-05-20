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

import com.kingbase.wsdl.parser.info.OperationInfo;
import com.kingbase.wsdl.parser.info.ParameterInfo;
import com.kingbase.wsdl.parser.info.ServiceInfo;
import com.kingbase.wsdl.parser.parse.WSDLParser;
import com.kingbase.wsdl.parser.util.CacheUtil;
import com.kingbase.wsdl.parser.util.XMLSupport;

@WebServlet(urlPatterns={"/WsdlLoadServlet"})
public class WsdlLoadServlet extends HttpServlet{

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
			json="{\"data\":\""+URLEncoder.encode(json, "UTF-8")+"\"}";
			break;
		default:
			break;
		}
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
		
		OMElement omElement = XMLSupport.createParameterElement(serviceInfo.getTargetnamespace(), methodName, parameterMap);
	    return omElement.toString();
	}
}
