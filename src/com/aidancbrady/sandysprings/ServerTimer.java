package com.aidancbrady.sandysprings;


public final class ServerTimer extends Thread
{
	public ServerTimer()
	{
		setDaemon(true);
	}
	
	@Override
	public void run()
	{
		while(SandySpringsServer.instance().serverRunning)
		{
			try {
				Thread.sleep(1000*60*15);
				
				FileHandler.save();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
