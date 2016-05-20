package com.kingbase.wsdl.parser.util;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.kingbase.wsdl.parser.info.ServiceInfo;

/**
 * 将wsdl解析结果缓存
 * @author ganliang
 */
public class CacheUtil {

	private static final ConcurrentHashMap<String, ServiceInfo> serviceInfos=new ConcurrentHashMap<String, ServiceInfo>();
	
	/**
	 * 查看 该uri是否已经缓存
	 * @param uri
	 * @return
	 */
	public static boolean contains(String uri){
		return serviceInfos.containsKey(uri);
	}
	
	/**
	 * 缓存 解析结果
	 * @param uri
	 * @param serverInfo
	 */
	public static void put(String uri,ServiceInfo serviceInfo){
		serviceInfos.put(uri, serviceInfo);
	}
	
	/**
	 * 从缓存中 获取serverInfo
	 * @param uri
	 * @return
	 */
	public static ServiceInfo get(String uri){
		return serviceInfos.get(uri);
	}
	
	public static ConcurrentHashMap<String, ServiceInfo> getAll(){
		return serviceInfos;
	}
	
	public static ServiceInfo getServiceInfo(String serverName){
		for (Entry<String, ServiceInfo> entry : serviceInfos.entrySet()) {
			ServiceInfo serviceInfo = entry.getValue();
			if(serviceInfo.getName().equals(serverName)){
				return serviceInfo;
			}
		}
		return null;
	}
}
