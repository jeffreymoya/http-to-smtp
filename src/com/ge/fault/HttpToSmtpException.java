
package com.ge.fault;

import javax.xml.ws.WebFault;

@WebFault
public class HttpToSmtpException extends Exception {

	private static final long serialVersionUID = 1576489681276588818L;

	private Fault faultInfo;

	public HttpToSmtpException( final String message ) {

		super( message );
	}

	public HttpToSmtpException( final String message, final Fault faultInfo ) {

		super( message );
		this.faultInfo = faultInfo;
	}

	public HttpToSmtpException( final String message, final Fault faultInfo,
			final Throwable cause ) {

		super( message, cause );
		this.faultInfo = faultInfo;
	}

	public Fault getFaultInfo() {

		return faultInfo;
	}
}
