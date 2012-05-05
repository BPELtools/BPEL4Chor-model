/**
 * 
 */
package org.bpel4chor.model.grounding.impl;

import javax.xml.namespace.QName;

/**
 * The representation of property in BPEL4Chor grounding
 * 
 * @since Oct 22, 2011
 * @author Daojun.Cui
 */
public class Property {
	
	/** property name */
	protected String name = null;
	
	/** wsdlProperty */
	protected QName WSDLproperty = null;

	/**
	 * Constructor
	 * @param name
	 * @param wSDLProperty
	 */
	public Property(String name, QName wSDLProperty)
	{
		this.name = name;
		this.WSDLproperty = wSDLProperty;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public QName getWSDLproperty() {
		return WSDLproperty;
	}

	public void setWSDLproperty(QName wSDLproperty) {
		WSDLproperty = wSDLproperty;
	}

	public String toString() {
		return "PropertyImpl [name=" + name + ", WSDLproperty=" + WSDLproperty
				+ "]";
	}

	
}
