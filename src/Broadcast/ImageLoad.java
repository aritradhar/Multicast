//*************************************************************************************
//*********************************************************************************** *
//author Aritra Dhar 																* *
//Research Engineer																  	* *
//Xerox Research Center India													    * *
//Bangalore, India																    * *
//--------------------------------------------------------------------------------- * * 
///////////////////////////////////////////////// 									* *
//The program will do the following:::: // 											* *
///////////////////////////////////////////////// 									* *
//version 1.0 																		* *
//*********************************************************************************** *
//*************************************************************************************


package Broadcast;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ImageLoad 
{
	
	/**
	 * Send the byte[] of the image and the hash,
	 * 0 at the front is file, 1 at the front is hash 
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[][] loadImages(String path) throws IOException, NoSuchAlgorithmException
	{
		File f = new File(path);
		if(!(f.isDirectory() && f.exists()))
		{
			throw new RuntimeException("Invalid path");
		}
		
		File[] files = f.listFiles();
		byte[][] out = new byte[files.length][];
		
		int i = 0;
		byte[] filebytes, destFile, hashbytes = null;
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		
		for(File file : files)
		{		
			
			filebytes = Files.readAllBytes(file.toPath());
			
			ByteBuffer br = ByteBuffer.allocate(4);
			br.putInt(filebytes.length);
			byte[] lenbytes = br.array();
			
			hashbytes = md.digest(filebytes);
			
			//legth || payload || hash
			destFile = new byte [lenbytes.length + filebytes.length + hashbytes.length];
			
			System.arraycopy(lenbytes, 0, destFile, 0, lenbytes.length);
			System.arraycopy(filebytes, 0, destFile, lenbytes.length, filebytes.length);
			System.arraycopy(hashbytes, 0, destFile, lenbytes.length + filebytes.length, hashbytes.length);
			
			out[i++] = destFile;

			
			md.reset();
		}
		
		return out;
	}
	
}
