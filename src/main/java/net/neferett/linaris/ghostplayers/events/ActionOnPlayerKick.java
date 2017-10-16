package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerKickEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerKick extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerKickEvent e = (PlayerKickEvent)event;
        if (!GhostManager.isGhost(e.getPlayer())) {
            e.setCancelled(true);
            return false;
        }
        return true;
    }
}
