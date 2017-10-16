package net.neferett.linaris.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;

import org.bukkit.Bukkit;

public class file {

	private static String fileName()
	{
		return (Paths.get("").toAbsolutePath().toString() + "/logs/latest.log");
	}
	
	public static void getFile()
	{
		String file = fileName();
		try{
			BufferedReader rd = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = rd.readLine()) != null)
			{
				if (line.contains("com/comphenix/protocol/events/PacketContainer") || line.contains("An objective of name 'health' already exists"))
					Bukkit.shutdown();
			}
			rd.close();		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	} 
}
