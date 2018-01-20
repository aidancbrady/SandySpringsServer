package com.aidancbrady.sandysprings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

public class FileHandler
{
    public static File dataDir = new File(getHomeDirectory() + File.separator + "Documents" + File.separator + "SandySpringsServer" + File.separator + "Data");
    public static File dataFile = new File(dataDir, "Devices.txt");
    
    public static void save()
    {
        if(SandySpringsServer.logs)
        {
            System.out.println("Saving devices list...");
        }
        
        try {
            if(dataFile.exists())
            {
                dataFile.delete();
            }
            
            dataFile.createNewFile();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
            
            for(String s : SandySpringsServer.instance().deviceIDs)
            {
                writer.append(s);
                writer.newLine();
            }
            
            writer.flush();
            writer.close();
        } catch(Exception e) {
            System.err.println("An error occured while saving to data file:");
            e.printStackTrace();
        }
    }
    
    public static void load()
    {
        System.out.println("Loading devices list...");
        
        try {
            dataDir.mkdirs();
            
            if(!dataFile.exists())
            {
                return;
            }
            
            Set<String> deviceIDs = new HashSet<>();
            
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            
            String readingLine;
            
            while((readingLine = reader.readLine()) != null)
            {
                deviceIDs.add(readingLine);
            }
            
            SandySpringsServer.instance().deviceIDs = deviceIDs;
            
            reader.close();
        } catch(Exception e) {
            System.err.println("An error occured while loading from data file:");
            e.printStackTrace();
        }
    }
    
    public static String getHomeDirectory()
    {
        return System.getProperty("user.home");
    }
}
