package net.neferett.linaris.spectator;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import net.neferett.linaris.inventory.VirtualInventory;
import net.neferett.linaris.specialitems.SpecialItem;
import net.neferett.linaris.utils.LoggerUtils;
import net.neferett.linaris.utils.PlayerUtils;
import net.neferett.linaris.utils.tasksmanager.TaskManager;

public class SpectatorManager
{
    public static boolean displayTeam;
    private static String prefix;
    private static EpicubeVirtualMenuPlayerTracker menu;
    private static EpicubeMenuItemSpectatorCompass compass;
    
    public static void init() {
        EpicubeVirtualMenuPlayerTracker.id = VirtualInventory.registerVirtualInventory(new EpicubeVirtualMenuPlayerTracker());
        EpicubeMenuItemSpectatorCompass.id = SpecialItem.registerItem(new EpicubeMenuItemSpectatorCompass());
        SpectatorManager.menu = (EpicubeVirtualMenuPlayerTracker)VirtualInventory.get(EpicubeVirtualMenuPlayerTracker.id);
        SpectatorManager.compass = (EpicubeMenuItemSpectatorCompass)SpecialItem.get(EpicubeMenuItemSpectatorCompass.id);
        TaskManager.scheduleSyncRepeatingTask("SpectatorAntiAbuse", new SpectatorAntiAbuse(), 0, 60);
    }
    
    public static void warning(final String msg) {
        LoggerUtils.warning(SpectatorManager.prefix + msg);
    }
    
    public static void setSpectator(final Player player) {
        PlayerUtils.razPlayer(player);
        player.setGameMode(GameMode.SPECTATOR);
        PlayerUtils.givePlayerBackToHubItem(player);
        giveSpectatorItem(player);
        player.setAllowFlight(true);
        player.setFlying(true);
        PlayerUtils.sendActionMessage(player, "§b§lInventaire §7(§e§lE§7) §b§lpour acc\u00e9der au §e§lmenu");
        GamePosHider.hidePos(player);
        remove(player);
    }
    
    public static void giveSpectatorItem(final Player player) {
        player.getInventory().setItem(0, SpectatorManager.compass.getClonedItem());
    }
    
    public static void addPlayer(final Player player) {
        SpectatorManager.menu.addTrackedPlayer(player);
    }
    
    public static void remove(final Player player) {
        SpectatorManager.menu.removeTrackedPlayer(player);
    }
    
    public static boolean isTrackable(final Player teleport) {
        return SpectatorManager.menu.isTrackable(teleport.getName());
    }
    
    static {
        SpectatorManager.displayTeam = true;
        SpectatorManager.prefix = "[SpecManager] ";
    }
}
