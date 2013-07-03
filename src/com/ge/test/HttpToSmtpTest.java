/**
 * 
 */

package com.ge.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ge.mailer.SMTPMailer;
import com.ge.util.WSAuthenticator;


/**
 * @author AMIIS025
 * 
 */
public class HttpToSmtpTest {

	SMTPMailer mailer;

	Properties properties;

	FileInputStream file;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		properties = new Properties();
		file = new FileInputStream( "mail.properties" );
		properties.load( file );
		mailer = new SMTPMailer( properties, false );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		mailer = null;
		properties.clear();
		file.close();
	}

	@Test
	public void testConfig() throws Exception {

		assertNotNull( properties );
		String protocol = properties.getProperty( "mail.protocol" );
		assertNotNull( protocol );
		assertNotNull( properties.getProperty( "mail." + protocol + ".host" ) );
		assertNotNull( properties.getProperty( "mail." + protocol + ".port" ) );
		assertNotNull( properties.getProperty( "mail.message.success" ) );
		assertNotNull( properties.getProperty( "mail.message.failed" ) );

	}

	@Test
	public void testAuthenticator() throws Exception {

		String user = properties.getProperty( "system.source.id" ), pass = properties
				.getProperty( "system.source.pwd" );
		WSAuthenticator auth = new WSAuthenticator( properties );
		assertTrue( auth.validate( user, pass ) );
		assertFalse( auth.validate( user + pass, pass + user ) );
	}

	@Test
	public void testSendMail() throws Exception {

		properties.setProperty( "mail.session.debug", "false" );

		assertNotNull( mailer );

		String success = properties.getProperty( "mail.message.success" ), email = "junit.httptosmtp@mailinator.com", emails = "junit.httptosmtp@mailinator.com,JUnit 4 <junit.personal.httptosmtp@mailinator.com>,junit.personal2.httptosmtp@mailinator.com (JUnit 4)", str = "test", html = "&lt;br/&gt;&lt;a href=&quot;http://meralco.com.ph&quot;&gt;URL&lt;/a&gt;.";

		assertEquals( success,
				mailer.send( email, email, email, email, str, str ) );
		assertEquals( success,
				mailer.send( email, email, email, email, str, html ) );
		assertEquals( success,
				mailer.send( emails, emails, emails, emails, str, html ) );
		assertEquals( success,
				mailer.send( emails, emails, null, emails, str, html ) );
		assertEquals( success,
				mailer.send( emails, emails, emails, null, str, html ) );

		assertFalse( sendMail( emails, emails, emails, emails, str, null ) );
		assertFalse( sendMail( emails, emails, emails, emails, null, str ) );
		assertFalse( sendMail( emails, emails, emails, str, str, str ) );
		assertFalse( sendMail( emails, emails, str, emails, str, str ) );
		assertFalse( sendMail( emails, str, emails, emails, str, str ) );
		assertFalse( sendMail( str, emails, emails, emails, str, str ) );
		assertFalse( sendMail( null, emails, emails, emails, str, str ) );
		assertFalse( sendMail( emails, null, emails, emails, str, str ) );
	}

	private boolean sendMail( String sender, String recipient, String bcc,
			String cc, String subject, String body ) {

		try
		{
			mailer.send( sender, recipient, bcc, cc, subject, body );
		}
		catch ( Exception e )
		{
			return false;
		}

		return true;
	}

}
