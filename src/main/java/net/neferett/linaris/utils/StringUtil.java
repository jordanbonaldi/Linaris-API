package net.neferett.linaris.utils;

public class StringUtil {

	public static String join(String[] arr, String separator) {
		StringBuilder sb = new StringBuilder();
		if(arr.length == 0) return "";
		else if(arr.length == 1) sb.append(arr[0]);
		else {
			for(int i = 0; i < arr.length - 1; i ++) {
				if(i != 0) sb.append(separator);
				sb.append(arr[i]);
			}
		}
		return sb.toString();
	}
	
}