package net.neferett.linaris.api;

import java.util.HashMap;
import java.util.Map;

import net.neferett.linaris.BukkitAPI;

public class SettingsManager {

    public static Map<String, String> getSettings(String player) {
    	player = player.toLowerCase();
        Map<String, String> data = BukkitAPI.get().getPlayerDataManager().getPlayerData(player).getValues();
        HashMap<String, String> settings = new HashMap<>();
        data.entrySet().stream().filter(line -> line.getKey().startsWith("settings.")).forEach(line -> {
            String setting = line.getKey().split(".")[0];
            settings.put(setting, line.getValue());
        });

        return settings;
    }

    public static boolean isSetting(String player, Games game,String setting) {
		return BukkitAPI.get().getPlayerDataManager().getPlayerData(player).contains("settings." + setting);
    }
    
    public static void removeSetting(String player, Games game,String setting) {
		BukkitAPI.get().getPlayerDataManager().getPlayerData(player).remove("settings." + game.getID() + "_"+ setting);
    }
    
    public static String getSetting(String player, Games game,String setting) {
		return BukkitAPI.get().getPlayerDataManager().getPlayerData(player).get("settings." + game.getID() + "_"+ setting);
    }

    public static String getSetting(String player, Games game,String setting, String def) {
        String val = getSetting(player, game, setting);
        return (val == null) ? def : val;
    }

    public static boolean isEnabled(String player, Games game,String setting) {
        return BukkitAPI.get().getPlayerDataManager().getPlayerData(player).getBoolean("settings." + game.getID() + "_" + setting);
    }

    public static boolean isEnabled(String player, Games game,String setting, boolean val) {
        return BukkitAPI.get().getPlayerDataManager().getPlayerData(player).getBoolean("settings." + game.getID() + "_"+  setting, val);
    }

    public static void setSetting(String player, Games game,String setting, String value) {
        BukkitAPI.get().getPlayerDataManager().getPlayerData(player).set("settings." + game.getID() + "_"+  setting, value);
    }
}
