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
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import Constants.ENV;

public class Client 
{
	public static void main(String[] args) throws UnknownHostException, NoSuchAlgorithmException 
    {
        InetAddress address = InetAddress.getByName(ENV.INET_ADDR);
        
        byte[] buf = new byte[65556];

        try (MulticastSocket clientSocket = new MulticastSocket(ENV.PORT))
        {
            clientSocket.joinGroup(address);
     
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            while (true) 
            {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);
                
                byte[] imgBytes = new byte[buf.length - 32];
                byte[] hashBytes = new byte[32];
                
                System.arraycopy(buf, 0, imgBytes, 0, buf.length - 32);
                System.arraycopy(buf, buf.length - 32, hashBytes, 0, 32);
                
                byte[] d = md.digest(imgBytes);
                
                if(Arrays.equals(d, hashBytes))
                	System.out.println("Matched");
                else
                	System.err.println("hash error");
                
                md.reset();

            }
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
}