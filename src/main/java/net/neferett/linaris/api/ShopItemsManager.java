package net.neferett.linaris.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.neferett.linaris.BukkitAPI;

public class ShopItemsManager {

    public static List<ItemInfo> getItems(String player) {
        Map<String, String> data = BukkitAPI.get().getPlayerDataManager().getPlayerData(player).getValues();
        List<ItemInfo> items = new ArrayList<ItemInfo>();
        data.entrySet().stream().filter(line -> line.getKey().startsWith("items.")).forEach(line -> {
            String setting = line.getKey().split(".")[0];
            try {
				int level = Integer.parseInt(line.getValue());
	            items.add(new ItemInfo(setting, level));
			} catch (Exception e) {
			}
        });

        return items;
    }

    public static ItemInfo getItem(String player, String shopID, String item) {
    	try {
    		int level =Integer.parseInt(BukkitAPI.get().getPlayerDataManager().getPlayerData(player).get("items." + shopID + "_" + item));
    		return new ItemInfo(item, level);
		} catch (Exception e) {
			return new ItemInfo(item, 0);
		}
    }
    
    public static boolean haveItem(String player, String shopID, String item) {
    	return BukkitAPI.get().getPlayerDataManager().getPlayerData(player).contains("items." + shopID + "_" + item);
    }
    
    public static void setItem(String player, String shopID, ItemInfo item) {
        BukkitAPI.get().getPlayerDataManager().getPlayerData(player).set("items." + shopID + "_" + item.getUUID(), Integer.toString(item.getLevel()));
    }

    /*
     * 
     *  Game Items
     * 
     */

    public static ItemInfo getItem(String player, String item) {
    	return getItem(player, String.valueOf(Games.getByDisplayName(BukkitAPI.get().getServerInfos().getGameName()).getID()), item);
    }
    
    public static boolean haveItem(String player, String item) {
    	return haveItem(player, String.valueOf(Games.getByDisplayName(BukkitAPI.get().getServerInfos().getGameName()).getID()), item);
    }
    
    public static void setItem(String player, ItemInfo item) {
    	setItem(player, String.valueOf(Games.getByDisplayName(BukkitAPI.get().getServerInfos().getGameName()).getID()), item);
    }
    
}
