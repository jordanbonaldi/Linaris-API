package net.neferett.linaris.utils;

public class Log {
	
	private static void log(Level level, String message) {
		System.out.println("[GameServer] [" + level.name() + "] " + message);
	}
	
	public static void info(String message) {
		log(Level.INFO, message);
	}
	
	public static void warn(String message) {
		log(Level.WARNING, message);
	}
	
	public static void severe(String message) {
		log(Level.SEVERE, message);
	}
	
	public enum Level {
		INFO,
		WARNING,
		SEVERE;
	}

}
