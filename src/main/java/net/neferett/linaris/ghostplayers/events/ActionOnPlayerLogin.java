package net.neferett.linaris.ghostplayers.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerLoginEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerLogin extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerLoginEvent e = (PlayerLoginEvent)event;
        final Player player = e.getPlayer();
        if (GhostManager.isGhost(player)) {
            GhostManager.addGhost(player);
            return false;
        }
        return true;
    }
}
