package com.aidancbrady.sandysprings;

import java.io.File;
import java.util.Iterator;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

public class NotificationManager 
{
	public static void test(String msg)
	{
		sendNotification("TEST", msg);
	}
	
	private static void sendNotification(String type, String msg)
	{
		File certFile = SandySpringsServer.dev ? SandySpringsServer.DEV_CERTIFICATE : SandySpringsServer.PUB_CERTIFICATE;
		ApnsService service = null;
		
		if(SandySpringsServer.dev)
		{
			service = APNS.newService()
					.withCert(certFile.getAbsolutePath(), SandySpringsServer.CERT_PASS)
					.withSandboxDestination()
					.build();
		}
		else {
			service = APNS.newService()
					.withCert(certFile.getAbsolutePath(), SandySpringsServer.CERT_PASS)
					.withProductionDestination()
					.build();
		}
		
		String payload = APNS.newPayload().alertBody(msg).build();
		
		for(Iterator<String> iter = SandySpringsServer.instance().deviceIDs.iterator(); iter.hasNext();)
		{
		    String id = iter.next();
		    
		    try {
    			service.push(id, payload);
		    } catch(Exception e) {
		        if(e.getMessage().contains("Invalid hex character"))
		        {
		            System.out.println("Removed invalid device ID: " + id);
		            iter.remove();
		        }
		    }
		}
	}
}
