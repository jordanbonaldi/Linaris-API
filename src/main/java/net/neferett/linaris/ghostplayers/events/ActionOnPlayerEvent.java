package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerEvent extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerEvent e = (PlayerEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
