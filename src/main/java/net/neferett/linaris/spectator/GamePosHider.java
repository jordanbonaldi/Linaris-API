package net.neferett.linaris.spectator;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.packetwrapper.WrapperPlayServerEntityStatus;

public class GamePosHider implements Listener
{
    private static List<String> noPosPlayers;
    
    public static boolean seePos(final Player player) {
        return !GamePosHider.noPosPlayers.contains(player.getName());
    }
    
    public static void hidePos(final Player player) {
        GamePosHider.noPosPlayers.add(player.getName());
        reducedDebugInfo(player, true);
    }
    
    public static void showPos(final Player player) {
        GamePosHider.noPosPlayers.remove(player.getName());
        reducedDebugInfo(player, false);
    }
    
    private static void reducedDebugInfo(final Player player, final boolean value) {
    	WrapperPlayServerEntityStatus packet = new WrapperPlayServerEntityStatus();
    	packet.setEntityID(player.getEntityId());
    	packet.setEntityStatus((byte) (value ? 22 : 23));
    	packet.sendPacket(player);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeave(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        GamePosHider.noPosPlayers.remove(player.getName());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (seePos(player)) {
            reducedDebugInfo(player, false);
        }
        else {
            reducedDebugInfo(player, true);
        }
    }
    
    static {
        GamePosHider.noPosPlayers = new ArrayList<String>();
    }
}
