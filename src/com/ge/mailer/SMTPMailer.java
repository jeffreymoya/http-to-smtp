
package com.ge.mailer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang3.StringEscapeUtils;

import com.ge.fault.HttpToSmtpException;
import com.ge.util.CommonUtils;

public class SMTPMailer {

	private Properties properties;

	private boolean log = true;

	public SMTPMailer( Properties properties ) {

		this.properties = properties;
	}

	public SMTPMailer( Properties properties, boolean log ) {

		this.properties = properties;
		this.log = log;
	}

	/**
	 * Creates and forwards email to SMTP server using email information sent by
	 * web service client and configuration from mail.properties. Accepts email
	 * addresses that conform to RFC 822 standards
	 * 
	 * @param sender - email sender
	 * @param recipient - email recipient/s
	 * @param bcc - email BCC/s
	 * @param cc - email CC/s
	 * @param subject - email subject
	 * @param body - email body
	 * 
	 * @return success/failed message
	 * 
	 * @throws HttpToSmtpException
	 */
	public String send( String sender, String recipient, String cc, String bcc,
			String subject, String body ) throws HttpToSmtpException {

		PrintWriter writer = null;
		String success = "";
		String failed = "";
		InputStream in = null;
		Transport transport = null;
		Message message = null;

		try
		{

			InternetAddress[] param1, param2, param3, param4;
			param1 = CommonUtils.parseEmail( sender, "sender", true );
			param2 = CommonUtils.parseEmail( recipient, "recipient", true );
			param3 = CommonUtils.parseEmail( cc, "cc", false );
			param4 = CommonUtils.parseEmail( bcc, "bcc", false );

			if ( CommonUtils.isBlank( subject ) ) { throw new HttpToSmtpException(
					"Invalid email subject" ); }
			if ( CommonUtils.isBlank( body ) ) { throw new HttpToSmtpException(
					"Invalid email body" ); }

			String protocol = properties.getProperty( "mail.protocol" );
			String host = properties.getProperty( "mail." + protocol + ".host" );

			success = properties.getProperty( "mail.message.success" );
			failed = properties.getProperty( "mail.message.failed" );
			boolean debugSession = Boolean.valueOf( properties
					.getProperty( "mail.session.debug" ) );
			boolean auth = Boolean.valueOf( properties.getProperty( "mail."
					+ protocol + ".auth" ) );

			Session session = Session.getInstance( properties );
			session.setDebug( debugSession );
			message = new MimeMessage( session );

			message.setFrom( param1[ 0 ] );
			message.setHeader( "X-mailer", "MERALCO" );
			message.addRecipients( Message.RecipientType.TO, param2 );

			message.addRecipients( Message.RecipientType.CC, param3 );
			message.addRecipients( Message.RecipientType.BCC, param4 );
			message.setSubject( subject );
			message.setSentDate( new Date() );
			message.setDataHandler( new DataHandler( new ByteArrayDataSource(
					StringEscapeUtils.unescapeHtml4( body ), "text/html" ) ) );


			if ( auth )
			{
				String username = properties.getProperty( "mail.username" );
				String pwd = properties.getProperty( "mail.userpassword" );
				transport = session.getTransport( protocol );
				transport.connect( host, username, pwd );
				transport.sendMessage( message, message.getAllRecipients() );
				System.out
						.println( "Email sent to " + param2[ 0 ].getAddress() );
			}
			else
			{
				Transport.send( message );
			}

		}
		catch ( HttpToSmtpException e )
		{
			if ( log )
			{
				CommonUtils.log( e.getMessage(), writer, CommonUtils.LOG_FILE );
			}
			throw new HttpToSmtpException( e.getMessage() );
		}
		catch ( Exception e )
		{
			if ( log )
			{
				CommonUtils.log( e.getMessage(), writer, CommonUtils.LOG_FILE );
			}
			System.err.println( e.getMessage() );
			throw new HttpToSmtpException( failed );
		}
		finally
		{
			if ( transport != null )
			{
				try
				{
					transport.close();
				}
				catch ( MessagingException e )
				{
					System.err
							.println( "Failed to close mail transport instance. "
									+ e.getMessage() );
				}
			}

			if ( in != null )
			{
				try
				{
					in.close();
				}
				catch ( IOException e )
				{
					System.err
							.println( "Failed to close properties input stream. "
									+ e.getMessage() );
				}
			}
			if ( writer != null )
			{
				writer.close();
			}
		}

		return success;

	}
}
