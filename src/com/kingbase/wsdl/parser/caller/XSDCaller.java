package com.kingbase.wsdl.parser.caller;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

public class XSDCaller {

	public static void main(String[] args) throws AxisFault {
		String soapBindingAddress = "http://192.168.8.144:9999/services/helloWord?wsdl";  
        
        EndpointReference endpointReference = new EndpointReference(soapBindingAddress);  
        
        OMFactory factory = OMAbstractFactory.getOMFactory();  
        OMNamespace namespace = factory.createOMNamespace("http://service.cytoscape.com/", "xsd");  
        
        OMElement nameElement =factory.createOMElement("lgan", null);  
        nameElement.addChild(factory.createOMText(nameElement, "ganliang")); 
        
        OMElement method = factory.createOMElement("sayHi", namespace);  
        method.addChild(nameElement);  
          
        Options options = new Options();  
        options.setAction("http://service.cytoscape.com/sayHi");  //此处对应于
        
        options.setTo(endpointReference); 
        
        ServiceClient sender = new ServiceClient();  
        sender.setOptions(options);  

        //下面的输出结果为<xsd:test xmlns:xsd="http://www.mycompany.com"><name>java</name></xsd:test>  
        System.out.println(method.toString());  

        //发送并得到结果，至此，调用成功，并得到了结果  
        OMElement result = sender.sendReceive(method);  
        System.out.println(result);
        
	}
}
