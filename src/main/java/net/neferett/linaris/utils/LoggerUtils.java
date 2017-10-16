package net.neferett.linaris.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class LoggerUtils
{
    public static boolean debug;
    
    private static void OpMessage(final String message) {
        for (final OfflinePlayer p : Bukkit.getOperators()) {
            if (p.isOnline()) {
                p.getPlayer().sendMessage(message);
            }
        }
    }
    
    public static void logWithColor(final String string) {
        Bukkit.getServer().getConsoleSender().sendMessage(string);
    }
    
    public static void debug(final String string) {
        if (!LoggerUtils.debug) {
            return;
        }
        debug(string, true);
    }
    
    public static void debug(final String string, final boolean spamOp) {
        if (!LoggerUtils.debug) {
            return;
        }
        final String prefix = "§5[Debug]§r";
        Bukkit.getServer().getConsoleSender().sendMessage(prefix + " " + string);
        if (spamOp) {
            OpMessage(prefix + " " + string);
        }
    }
    
    public static void warning(final String string) {
        final String prefix = "§4[WARNING]§r";
        Bukkit.getServer().getConsoleSender().sendMessage(prefix + " " + string);
        OpMessage(prefix + " " + string);
    }
    
    public static void info(final String string) {
        final String prefix = "§4[INFO]§r";
        Bukkit.getServer().getConsoleSender().sendMessage(prefix + " " + string);
    }
    
    static {
        LoggerUtils.debug = false;
    }
}
