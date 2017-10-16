package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerTeleport extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerTeleportEvent e = (PlayerTeleportEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
