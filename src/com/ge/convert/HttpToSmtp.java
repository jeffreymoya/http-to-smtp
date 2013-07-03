
package com.ge.convert;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.ge.fault.HttpToSmtpException;
import com.ge.mailer.SMTPMailer;
import com.ge.util.CommonUtils;
import com.ge.util.WSAuthenticator;

@WebService( name = "HttpToSmtpService", wsdlLocation = "HttpToSmtpService.wsdl" )
public class HttpToSmtp {

	private SMTPMailer mailer;

	private WSAuthenticator authenticator;

	public HttpToSmtp() {

	}

	public HttpToSmtp( SMTPMailer mailer, WSAuthenticator authenticator )
			throws Exception {

		this.authenticator = authenticator;
		this.mailer = mailer;
	}

	/**
	 * Web service endpoint
	 * 
	 * @param sender
	 * @param recipient
	 * @param bcc
	 * @param cc
	 * @param subject
	 * @param body
	 * @param sourceSystemId
	 * @param sourceSystemPwd
	 * @return
	 * @throws HttpToSmtpException
	 */
	@WebMethod( operationName = "translate" )
	@WebResult( name = "result" )
	public String translate( @WebParam( name = "sender" ) String sender,
			@WebParam( name = "recipient" ) String recipient,
			@WebParam( name = "cc" ) String cc,
			@WebParam( name = "bcc" ) String bcc,
			@WebParam( name = "subject" ) String subject,
			@WebParam( name = "body" ) String body,
			@WebParam( name = "sourceSystemId" ) String sourceSystemId,
			@WebParam( name = "sourceSystemPwd" ) String sourceSystemPwd )
			throws HttpToSmtpException {

		try
		{
			if ( ! authenticator.validate( sourceSystemId, sourceSystemPwd ) )
			{
				String err = "Authentication error: Invalid credentials";
				CommonUtils.log(
						new String[]
						{ err, "FROM: " + sender, "TO: " + recipient,
								"CC: " + cc, "BCC: " + bcc,
								"SUBJECT: " + subject, "BODY: ",
								"SSID: " + sourceSystemId,
								"SSPWD: " + sourceSystemPwd }, null,
						CommonUtils.LOG_FILE );
				return err;

			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			return "An error has occurred. Please try again later.";
		}


		return mailer.send( sender, recipient, cc, bcc, subject, body );

	}
}
