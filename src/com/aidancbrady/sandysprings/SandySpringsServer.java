package com.aidancbrady.sandysprings;

import java.io.File;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SandySpringsServer 
{
	private static SandySpringsServer instance = new SandySpringsServer();
	
	public Set<String> deviceIDs = new HashSet<>();
	
	public static final String ROOT_DIR = File.separator + "root" + File.separator + "core_files" + File.separator + "SandySpringsServer" + File.separator;
	public static final File DEV_CERTIFICATE = new File(ROOT_DIR + "DevCertificate.p12");
	public static final File PUB_CERTIFICATE = new File(ROOT_DIR + "PubCertificate.p12");
	
	public static final String CERT_PASS = "push_password";
	public static final String SPLITTER = ":";
	
	public boolean serverRunning;
	
	public static boolean dev = false;
	public static boolean logs = false;
	
	public static final int SERVER_PORT = 26840;
	
	public ServerSocket serverSocket;
	
	public static void main(String[] args)
	{
		instance().init();
	}
	
	public void init()
	{
	    FileHandler.load();
	    
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			serverRunning = true;
			
			new ServerTimer().start();
			new ConnectionHandler().start();
			
			System.out.println("Initiated server");
			
			Scanner scan = new Scanner(System.in);
			
			while(scan.hasNext())
			{
				String s = scan.nextLine();
				
				if(s.equals("stop") || s.equals("quit"))
				{
					System.out.println("Shutting down");
					quit();
				}
				else if(s.equals("dev"))
				{
					System.out.println("Development mode toggled to " + (dev = !dev));
				}
				else if(s.equals("log"))
				{
					System.out.println("Detailed logs toggled to " + (logs = !logs));
				}
				else if(s.equals("save"))
				{
					FileHandler.save();
					System.out.println("Successfully saved device data");
				}
				else if(s.equals("load"))
				{
					FileHandler.load();
					System.out.println("Successfully loaded device data");
				}
				else if(s.startsWith("notify_all"))
				{
					String[] split = s.split(" ");
					
					if(split.length >= 2)
					{
						NotificationManager.test(s.substring(split[0].length() + 1));
						System.out.println("Sent notification to all users");
					}
					else {
						System.out.println("Invalid parameters");
					}
				}
				else if(s.startsWith("clear_IDs"))
				{
				    deviceIDs.clear();
				    FileHandler.save();
				}
				else {
					System.out.println("Unknown command");
				}
			}
			
			scan.close();
		} catch(Exception e) {
			System.out.println("Unable to start server");
			e.printStackTrace();
		}
		
		serverRunning = false;
	}
	
	public void quit()
	{
		serverRunning = false;
		FileHandler.save();
		
		try {
			serverSocket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	public static SandySpringsServer instance()
	{
		return instance;
	}
}
