package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerRespawn extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerRespawnEvent e = (PlayerRespawnEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
