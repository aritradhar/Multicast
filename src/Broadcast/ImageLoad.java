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
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ImageLoad 
{
	
	public static byte[] FILE_MARKER = {0x00};
	public static byte[] HASH_MARKER = {0x01};
	
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
		byte[][] out = new byte[files.length * 2][];
		
		int i = 0;
		byte[] filebytes, destFile, hashbytes, destHash = null;
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		
		for(File file : files)
		{		
			filebytes = Files.readAllBytes(file.toPath());
			destFile = new byte[filebytes.length + 1];
			
			System.arraycopy(FILE_MARKER, 0, destFile, 0, 1);
			System.arraycopy(filebytes, 0, destFile, 1, filebytes.length);
			
			hashbytes = md.digest(filebytes);
			destHash = new byte[hashbytes.length + 1];
			
			System.arraycopy(HASH_MARKER, 0, destHash, 0, 1);
			System.arraycopy(hashbytes, 0, destHash, 1, hashbytes.length);
			
			out[i++] = destFile;
			out[i++] = destHash;
			
			md.reset();
		}
		
		return out;
	}
	
}
