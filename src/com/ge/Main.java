
package com.ge;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.ws.Endpoint;

import com.ge.convert.HttpToSmtp;
import com.ge.mailer.SMTPMailer;
import com.ge.util.WSAuthenticator;


public class Main {

	/**
	 * Application entry point. parses mail.properties and publishes web service
	 * on server.
	 * 
	 * @param args[0] domain name of the host computer where the service will be
	 *            deployed
	 * 
	 * @param args[1] available port on the host computer
	 * 
	 * @param args[2] web service sourceSystemId
	 * 
	 * @param args[3] web service sourceSystemPwd
	 * 
	 */
	public static void main( String[] args ) {

		String endpointURL = "";
		WSAuthenticator auth = null;
		if ( ( args != null ) && ( args.length == 2 )
				&& ( args[ 0 ].length() > 0 ) && ( args[ 1 ].length() > 0 ) )
		// && ( args[ 2 ].length() > 0 ) && ( args[ 3 ].length() > 0 ) )
		{

			endpointURL = "http://" + args[ 0 ] + ":" + args[ 1 ]
					+ "/http_to_smtp";

			// if ( ! args[ 2 ].matches( "^[a-zA-Z0-9_\\$@!]*$" ) )
			// {
			// System.err
			// .println(
			// "Invalid service username. Must be alphanumeric and can only contain _ $ @ !"
			// );
			// System.exit( 0 );
			// }
			//
			// if ( ! args[ 3 ].matches( "^[a-zA-Z0-9_\\$@!]*$" ) )
			// {
			// System.err
			// .println(
			// "Invalid service password. Must be alphanumeric and can only contain _ $ @ !"
			// );
			// System.exit( 0 );
			// }

			// capture sourceSystemId and sourceSystemPwd from supplied
			// program arguments

		}
		else
		{
			System.out
					.println( "Usage: http-to-smtp.jar <machine name> <port>" );
			System.exit( 0 );
		}
		try
		{
			Properties properties = new Properties();
			properties.load( new FileInputStream( "mail.properties" ) );
			auth = new WSAuthenticator( properties );

			SMTPMailer mailer = new SMTPMailer( properties );

			HttpToSmtp endpoint = new HttpToSmtp( mailer, auth );

			// publish webservice
			Endpoint.publish( endpointURL, endpoint );
		}
		catch ( IOException e )
		{
			System.err
					.println( "Unable to load properties file. Please place 'mail.properties' in the same directory as this jar." );
			System.exit( 0 );
		}
		catch ( Exception e )
		{
			System.err.println( e.getMessage() );
			System.exit( 0 );
		}

		System.out.println( "Service published at " + endpointURL );


	}
}
