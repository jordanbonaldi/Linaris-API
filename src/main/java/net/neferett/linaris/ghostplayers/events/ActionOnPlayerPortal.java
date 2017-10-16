package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPortalEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerPortal extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerPortalEvent e = (PlayerPortalEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
