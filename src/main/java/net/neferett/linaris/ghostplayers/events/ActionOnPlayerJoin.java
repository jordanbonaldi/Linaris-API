package net.neferett.linaris.ghostplayers.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.PlayerLocal;
import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerJoin extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerJoinEvent e = (PlayerJoinEvent)event;
        final Player player = e.getPlayer();
        PlayerLocal pl = BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(e.getPlayer().getName());
		pl.inits();
		BukkitAPI.get().heartbeat();
        if (GhostManager.isGhost(player)) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("§b§lConnect\u00e9 en mode Ghost... :o");
            e.setJoinMessage((String)null);
            return false;
        }
        return true;
    }
}
