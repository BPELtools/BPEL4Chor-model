package org.bpel4chor.utils.exceptions;

/**
 * MalFormedGroundingSyntaxException shows up when the given property of a link
 * matches to multiple links in process.
 * 
 * @since Oct 11, 2012
 * @author Peter Debicki
 */
public class MalformedTLGLSyntaxException extends Exception {
	
	private static final long serialVersionUID = -3356471663647258305L;
	
	
	public MalformedTLGLSyntaxException() {
		super();
	}
	
	public MalformedTLGLSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public MalformedTLGLSyntaxException(String message) {
		super(message);
	}
	
	public MalformedTLGLSyntaxException(Throwable cause) {
		super(cause);
	}
	
}
