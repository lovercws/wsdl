package com.kingbase.wsdl.parser.info;

/**
 * 一个方法对应的参数
 */
public class ParameterInfo {

	private String name;// 参数名
	private String kind;// 参数类型
	private int id;// 参数标识
	private String value;// 参数值
	private String serviceid;// 服务id
	private String operationname;// 操作名
	private String inputtype = null;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInputtype() {
		return inputtype;
	}

	public void setInputtype(String inputtype) {
		this.inputtype = inputtype;
	}

	public String getOperationname() {
		return operationname;
	}

	public void setOperationname(String operationname) {
		this.operationname = operationname;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String name2) {
		this.kind = name2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ParameterInfo [name=" + name + ", kind=" + kind + ", id=" + id + ", value=" + value + ", serviceid="
				+ serviceid + ", operationname=" + operationname + ", inputtype=" + inputtype + ", type=" + type + "]";
	}
}
