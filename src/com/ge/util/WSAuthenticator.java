
package com.ge.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class WSAuthenticator {

	private final String USERNAME;

	private final String PASSWORD;

	private static final char[] val_array =
	{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
			'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	protected PrintStream stream;

	int count = 20;

	/**
	 * 
	 * @param user sourceSystemId
	 * @param password sourceSystemPwd
	 */
	public WSAuthenticator( Properties props ) {

		if ( props == null )
		{
			USERNAME = null;
			PASSWORD = null;
		}
		else
		{
			USERNAME = props.getProperty( "system.source.id" );
			PASSWORD = encrypt( props.getProperty( "system.source.pwd" ) );
		}
	}

	/**
	 * 
	 * @param str - supplied program argument: sourceSystemPwd
	 * 
	 * @return DES encrypted sourceSystemPwd
	 */
	public String encrypt( String str ) {

		byte[] salt =
		{ - 87, - 101, - 56, 50, 86, 52, - 29, 3 };

		int iterationCount = 19;
		try
		{
			KeySpec keySpec = new PBEKeySpec( str.toCharArray(), salt,
					iterationCount );

			SecretKey key = SecretKeyFactory.getInstance( "PBEWithMD5AndDES" )
					.generateSecret( keySpec );

			Cipher cipher = Cipher.getInstance( key.getAlgorithm() );

			AlgorithmParameterSpec paramSpec = new PBEParameterSpec( salt,
					iterationCount );

			cipher.init( 1, key, paramSpec );

			byte[] utf8 = str.getBytes( "UTF8" );

			byte[] enc = cipher.doFinal( utf8 );

			return encode( enc );


		}
		catch ( InvalidAlgorithmParameterException e )
		{
			System.out
					.println( "EXCEPTION: InvalidAlgorithmParameterException" );
		}
		catch ( InvalidKeySpecException e )
		{
			System.out.println( "EXCEPTION: InvalidKeySpecException" );
		}
		catch ( NoSuchPaddingException e )
		{
			System.out.println( "EXCEPTION: NoSuchPaddingException" );
		}
		catch ( NoSuchAlgorithmException e )
		{
			System.out.println( "EXCEPTION: NoSuchAlgorithmException" );
		}
		catch ( InvalidKeyException e )
		{
			System.out.println( "EXCEPTION: InvalidKeyException" );
		}
		catch ( BadPaddingException e )
		{}
		catch ( IllegalBlockSizeException e )
		{}
		catch ( UnsupportedEncodingException e )
		{}

		return null;

	}

	public boolean validate( final String sourceSystemId,
			final String sourceSystemPwd ) {

		if ( ( ( sourceSystemId == null ) || ( sourceSystemId.trim().length() == 0 ) )
				|| ( ( sourceSystemPwd == null ) || ( sourceSystemPwd.trim()
						.length() == 0 ) ) ) { return false; }

		String encPwd = encrypt( sourceSystemPwd );

		return sourceSystemId.equals( USERNAME ) && encPwd.equals( PASSWORD );
	}

	public String encode( byte[] aBuffer ) {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ByteArrayInputStream inStream = new ByteArrayInputStream( aBuffer );
		String retVal = null;
		try
		{
			encode( inStream, outStream );

			retVal = outStream.toString( "8859_1" );
		}
		catch ( Exception IOException )
		{
			throw new Error( "CharacterEncoder.encode internal error" );
		}

		return retVal;
	}

	public void encode( InputStream inStream, OutputStream outStream )
			throws IOException {

		byte[] tmpbuffer = new byte[ bytesPerLine() ];

		encodeBufferPrefix( outStream );
		while ( true )
		{
			int numBytes = readFully( inStream, tmpbuffer );

			if ( numBytes == 0 )
			{
				break;
			}

			encodeLinePrefix( outStream, numBytes );
			for ( int j = 0; j < numBytes; j += bytesPerAtom() )
			{
				if ( ( j + bytesPerAtom() ) <= numBytes )
				{
					encodeAtom( outStream, tmpbuffer, j, bytesPerAtom() );
				}
				else
				{
					encodeAtom( outStream, tmpbuffer, j, numBytes - j );
				}

			}

			if ( numBytes < bytesPerLine() )
			{
				break;
			}

			encodeLineSuffix( outStream );
		}

		encodeBufferSuffix( outStream );
	}

	protected int bytesPerLine() {

		return 57;
	}

	protected void encodeAtom( OutputStream outStream, byte[] data, int offset,
			int len ) throws IOException {

		if ( len == 1 )
		{
			byte a = data[ offset ];

			byte b = 0;
			outStream.write( val_array[ ( ( a >>> 2 ) & 0x3F ) ] );

			outStream
					.write( val_array[ ( ( ( a << 4 ) & 0x30 ) + ( ( b >>> 4 ) & 0xF ) ) ] );

			outStream.write( 61 );
			outStream.write( 61 );
		}
		else if ( len == 2 )
		{
			byte a = data[ offset ];
			byte b = data[ ( offset + 1 ) ];
			byte c = 0;
			outStream.write( val_array[ ( ( a >>> 2 ) & 0x3F ) ] );
			outStream
					.write( val_array[ ( ( ( a << 4 ) & 0x30 ) + ( ( b >>> 4 ) & 0xF ) ) ] );
			outStream
					.write( val_array[ ( ( ( b << 2 ) & 0x3C ) + ( ( c >>> 6 ) & 0x3 ) ) ] );

			outStream.write( 61 );
		}
		else
		{
			byte a = data[ offset ];

			byte b = data[ ( offset + 1 ) ];
			byte c = data[ ( offset + 2 ) ];

			outStream.write( val_array[ ( ( a >>> 2 ) & 0x3F ) ] );
			outStream
					.write( val_array[ ( ( ( a << 4 ) & 0x30 ) + ( ( b >>> 4 ) & 0xF ) ) ] );

			outStream
					.write( val_array[ ( ( ( b << 2 ) & 0x3C ) + ( ( c >>> 6 ) & 0x3 ) ) ] );
			outStream.write( val_array[ ( c & 0x3F ) ] );
		}
	}

	protected int bytesPerAtom() {

		return 3;
	}

	protected void encodeLineSuffix( OutputStream aStream ) throws IOException {

		this.stream.println();
	}

	protected void encodeLinePrefix( OutputStream aStream, int aLength )
			throws IOException {

	}

	protected void encodeBufferPrefix( OutputStream aStream )
			throws IOException {

		this.stream = new PrintStream( aStream );
	}

	protected void encodeBufferSuffix( OutputStream aStream )
			throws IOException {

	}

	protected int readFully( InputStream in, byte[] buffer ) throws IOException {

		for ( int i = 0; i < buffer.length; i++ )
		{
			int q = in.read();
			if ( q == - 1 ) { return i; }
			buffer[ i ] = ( ( byte ) q );
		}

		return buffer.length;
	}

	// generate encrypted key
	public static void main( String[] args ) throws Exception {

		System.out.println( new WSAuthenticator( null ).encrypt( args[ 0 ] ) );

	}
}
