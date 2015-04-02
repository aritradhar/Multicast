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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import Constants.ENV;

public class ImageBroadCastServer 
{
	String imageDBPath;
	private byte[][] imagedata;
	
	/**
	 * 
	 * @param imageDBPath path to the image database where all the advertisements are stored
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public ImageBroadCastServer(String imageDBPath) throws NoSuchAlgorithmException, IOException, InterruptedException
	{
		this.imageDBPath = imageDBPath;
		this.imagedata = ImageLoad.loadImages(imageDBPath);
		this.doBroadcast();
	}
	
	/**
	 * 
	 * @throws UnknownHostException
	 * @throws InterruptedException
	 */
	private void doBroadcast() throws UnknownHostException, InterruptedException
	{
		   InetAddress addr = InetAddress.getByName(ENV.INET_ADDR);
		     
	        try (DatagramSocket serverSocket = new DatagramSocket()) 
	        {
	        	int i = 0;
	        	
	            for (;;) 
	            {
	            	if(i == this.imagedata.length)
	            		i = 0;
	            	
	                byte[] b = this.imagedata[i];
	                
	                DatagramPacket msgPacket = new DatagramPacket(b,
	                        b.length, addr, ENV.PORT);
	                serverSocket.send(msgPacket);     

	                System.out.println("Sent image " + i);
                
	                i++;
	                Thread.sleep(ENV.TRANSMISSION_INTERVAL);
	            }
	        } 
	        
	        catch (IOException ex) 
	        {
	            ex.printStackTrace();
	        }
	}
		
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InterruptedException 
	{
		new ImageBroadCastServer("C:\\ImageDB");
	}
	
}
