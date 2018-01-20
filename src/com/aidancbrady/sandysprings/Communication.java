package com.aidancbrady.sandysprings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Communication extends Thread
{
	public Socket socket;
	
	public BufferedReader reader;
	
	public PrintWriter writer;
	
	public boolean disconnected;
	
	public Communication(Socket s)
	{
		socket = s;
	}
	
	@Override
	public void run()
	{
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			
			String reading = "";
			
			while((reading = reader.readLine()) != null && !disconnected)
			{
				String[] msg = reading.trim().split(SandySpringsServer.SPLITTER);
				
				if(msg[0].equals("PUSHID"))
				{
					if(SandySpringsServer.logs)
					{
						System.out.println(socket.getInetAddress() + " sent device ID of " + msg[2] + " as " + msg[1]);
					}
					
					if(msg.length == 2)
					{
    					String id = msg[1];
    					SandySpringsServer.instance().deviceIDs.add(id);
    					writer.println("Success");
					}
					else {
					    writer.println("Invalid arguments");
					}
				}
				else {
					writer.println("Unknown command");
				}
			}
			
			writer.flush();
			
			if(SandySpringsServer.logs)
			{
				System.out.println("Closing connection with " + socket.getInetAddress());
			}
			
			close();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void close()
	{
		disconnected = true;
		
		try {
			socket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
