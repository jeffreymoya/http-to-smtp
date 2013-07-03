
package com.ge.util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringEscapeUtils;

import com.ge.fault.HttpToSmtpException;

public class CommonUtils {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String LOG_FILE = "errors.log";

	public static InternetAddress[] parseEmail( String email, String label )
			throws HttpToSmtpException {

		try
		{
			InternetAddress[] ia = InternetAddress.parse( StringEscapeUtils
					.unescapeHtml4( email ) );
			for ( InternetAddress e : ia )
			{
				e.validate();
				if ( ! e.getAddress().matches( EMAIL_PATTERN ) ) { throw new HttpToSmtpException(
						"Invalid email " + label + ". " + email ); }
			}
			return ia;
		}
		catch ( AddressException e )
		{

			throw new HttpToSmtpException( "Invalid email " + label + ". "
					+ e.getMessage() + " - " + email );
		}
	}

	public static InternetAddress[] parseEmail( String email, String label,
			boolean required ) throws HttpToSmtpException {


		if ( CommonUtils.isBlank( email ) )
		{
			if ( required )
			{
				throw new HttpToSmtpException( label + " must not be empty" );
			}
			else
			{
				return new InternetAddress[ 0 ];
			}
		}
		InternetAddress[] addresses = parseEmail( email, label );

		return addresses;

	}

	public static boolean isBlank( String str ) {

		return ( str == null ) || ( str.trim().length() == 0 );
	}

	public static void log( String error, PrintWriter writer, String file ) {


		try
		{
			if ( writer == null )
			{
				writer = new PrintWriter( new FileWriter( file, true ) );
			}

			writer.println( new SimpleDateFormat( "[MMM dd,yyyy HH:mm:ss] " )
					.format( new Date() ) + error );
			writer.flush();

		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
		}
	}

	public static void log( String[] error, PrintWriter writer, String file ) {


		try
		{
			if ( writer == null )
			{
				writer = new PrintWriter( new FileWriter( file, true ) );
			}

			for ( String s : error )
			{
				writer.println( new SimpleDateFormat( "[MMM dd,yyyy HH:mm:ss] " )
						.format( new Date() ) + s );
			}
			writer.flush();

		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
		}
	}
}
