package org.bpel4chor.utils;

/**
 * Constants for BPEL4Chor Model
 * 
 * @since Oct 21, 2011
 * @author Daojun Cui
 */
public class BPEL4ChorConstants {
	
	/** Namespace for wsu:id */
	public final static String XMLNS_WSU = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
	
	/** The 2.0 BPEL Namespace for abstract processes */
	public final static String ABSTRACT_PROCESS_NAMESPACE = "http://docs.oasis-open.org/wsbpel/2.0/process/abstract";
	
	/** Namespace of xml schema instance */
	public final static String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	
	/** Schema location */
	public final static String XSI_SCHEMA_LOCATION = "http://docs.oasis-open.org/wsbpel/2.0/process/abstract http://docs.oasis-open.org/wsbpel/2.0/OS/process/abstract/ws-bpel_abstract_common_base.xsd";
	
	/** abstractProcessProfile for Participant Behavior Description */
	public final static String PBD_ABSTRACT_PROCESS_PROFILE = "urn:HPI_IAAS:choreography:profile:2006/12";
	
	/** The default namespace for topology */
	public final static String TOPOLOGY_XMLNS = "urn:HPI_IAAS:choreography:schemas:choreography:topology:2006/12";
	
	/**
	 * The default targetNamespace for topology,
	 * urn:HPI_IAAS:choreography:schemas:choreography:topology:2006/12
	 */
	public final static String TOPOLOGY_TARGET_NAMESPACE = "urn:HPI_IAAS:choreography:schemas:choreography:topology:2006/12";
	
	/**
	 * The default namespace for grounding,
	 * urn:HPI_IAAS:choreography:schemas:choreography:grounding:2006/12
	 */
	public final static String GROUNDING_XMLNS = "urn:HPI_IAAS:choreography:schemas:choreography:grounding:2006/12";
	
	/** The default targetNamespace for grounding */
	public final static String GOURNDING_TARGET_NAMESPACE = "urn:HPI_IAAS:choreography:schemas:choreography:grounding:2006/12";
	
	/** containment value option : add-if-not-exists */
	public final static String CONTAINMENT_ADD_IF_NOT_EXISTS = "add-if-not-exists";
	
	/** containment value option : must-add */
	public final static String CONTAINMENT_MUST_ADD = "must-add";
	
	/** containment value option : required */
	public final static String CONTAINMENT_REQUIRED = "required";
	
	/** Prefix base for new namespace, the whole prefix will be the base+number */
	public final static String TOPOLOGY_PREFIX_BASE = "tns";
	
	/** Prefix base for new namespace, the whole prefix will be the base+number */
	public final static String GOURNDING_PREFIX_BASE = "gns";
	
	/** Default Attribute Name for Declaring xml namespace */
	public final static String DEFAULT_ATTRIBUTE_NAME = "xmlns";
	
	/**
	 * Prefix base for new choreography namespace, the whole prefix will be the
	 * base+number
	 */
	public final static String CHOREOGRAPHY_PREFIX_BASE = "cns";
	
	/** Default export directory C:\tmp\bpel4chor */
	public static final String BPEL4CHOR_DEFAULT_WRITE_DIR = "C:\\temp\\bpel4chor";
}
