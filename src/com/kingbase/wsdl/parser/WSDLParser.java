package com.kingbase.wsdl.parser;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.kingbase.wsdl.parser.info.OperationInfo;
import com.kingbase.wsdl.parser.info.ParameterInfo;
import com.kingbase.wsdl.parser.info.ServiceInfo;
import com.kingbase.wsdl.parser.util.ComponentBuilder;

/**
 * 解析wsdl
 * @author ganliang
 */
public class WSDLParser {

	private static final Logger log=Logger.getLogger(WSDLParser.class);
	
	/**
	 * 解析wsdl生成ServiceInfo
	 * @param url
	 * @return
	 */
	public ServiceInfo parse(String url){
		ServiceInfo serviceInfo = new ServiceInfo();
		ComponentBuilder builder = new ComponentBuilder();
		try {
			serviceInfo.setWsdllocation(url);
			
			serviceInfo = builder.buildserviceinformation(serviceInfo);
		} catch (Exception e) {
			log.error("解析wsdl异常",e);
		}
		return serviceInfo;
	}
	
	/**
	 * 返回方法的集合
	 * @param serviceInfo
	 * @return
	 */
	public List<OperationInfo> getOperations(ServiceInfo serviceInfo){
		return serviceInfo.getOperation();
	}
	
	/**
	 * 打印方法 参数
	 * @param serviceInfo
	 */
	public void print(ServiceInfo serviceInfo){
		int i=0;
		Iterator<OperationInfo> iter = serviceInfo.getOperations();
		System.out.println(serviceInfo.getName() + "提供的操作有:");
		
		while (iter.hasNext()) {
			i++;
			OperationInfo oper = (OperationInfo) iter.next();
			System.out.println("");
			System.out.println("操作:" + i + " " + oper.getTargetMethodName());
			List<ParameterInfo> inps = oper.getInparameters();
			List<ParameterInfo> outps = oper.getOutparameters();
			if (inps.size() == 0) {
				System.out.println("此操作所需的输入参数为:");
				System.out.println("执行此操作不需要输入任何参数!");
			} else {
				System.out.println("此操作所需的输入参数为:");
				for (Iterator<ParameterInfo> iterator1 = inps.iterator(); iterator1.hasNext();) {
					ParameterInfo element = (ParameterInfo) iterator1.next();
					System.out.println("参数名为:" + element.getName()+ "参数类型为:" + element.getKind());
				}
			}
			if (outps.size() == 0) {
				System.out.println("执行此操作不返回任何参数!");
			} else {
				System.out.println("此操作的输出参数为:");
				for (Iterator<ParameterInfo> iterator2 = outps.iterator(); iterator2.hasNext();) {
					ParameterInfo element = (ParameterInfo) iterator2.next();
					System.out.println("参数名:" + element.getName()+"  类型为:" + element.getKind());
				}
			}
			System.out.println("");
		}
	}
	
	public static void main(String[] args){
		//String wsdllocation = "http://www.webxml.com.cn/webservices/ChinaStockSmallImageWS.asmx?wsdl";
		//String wsdllocation = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";
		String wsdllocation = "http://www.webxml.com.cn/WebServices/WeatherWS.asmx?wsdl";
		
		WSDLParser parser=new WSDLParser();
		ServiceInfo serviceInfo = parser.parse(wsdllocation);
		parser.print(serviceInfo);
	}
}
