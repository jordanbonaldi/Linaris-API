package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerChangeWorld extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerChangedWorldEvent e = (PlayerChangedWorldEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
