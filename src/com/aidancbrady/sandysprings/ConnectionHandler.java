package com.aidancbrady.sandysprings;

import java.net.Socket;

public class ConnectionHandler extends Thread
{
	@Override
	public void run()
	{
		try {
			while(SandySpringsServer.instance().serverRunning)
			{
				Socket connection = SandySpringsServer.instance().serverSocket.accept();
				
				if(SandySpringsServer.instance().serverRunning)
				{
					if(SandySpringsServer.logs) 
					{
						System.out.println("Initiating connection with " + connection.getInetAddress());
					}
					
					new Communication(connection).start();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
