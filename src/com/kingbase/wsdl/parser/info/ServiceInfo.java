package com.kingbase.wsdl.parser.info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.exolab.castor.xml.schema.Schema;

/**
 * 服务
 */
public class ServiceInfo {

	private String name;
	private String wsdllocation;// wsdl url地址
	private String endpoint;
	private String targetnamespace;//目标命名空间
	private Schema wsdlType;

	/** The list of operations that this service defines. */
	List<OperationInfo> operations = new ArrayList<OperationInfo>();

	public Schema getWsdlType() {
		return wsdlType;
	}

	public void setWsdlTypes(Schema wsdlType) {
		this.wsdlType = wsdlType;
	}

	public List<OperationInfo> getOperation() {
		return operations;
	}

	public Iterator<OperationInfo> getOperations() {
		return operations.iterator();
	}

	public void addOperation(OperationInfo operation) {
		operations.add(operation);
	}

	public String getTargetnamespace() {
		return targetnamespace;
	}

	public void setTargetnamespace(String targetnamespace) {
		this.targetnamespace = targetnamespace;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getWsdllocation() {
		return wsdllocation;
	}

	public void setWsdllocation(String wsdllocation) {
		this.wsdllocation = wsdllocation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
