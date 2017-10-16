package net.neferett.linaris.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL; 

public class GetIpUtils {
	
	public static String getIp() throws IOException{
	    URL whatismyip = new URL("http://icanhazip.com");
	    BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
	    return in.readLine();
	}

}
