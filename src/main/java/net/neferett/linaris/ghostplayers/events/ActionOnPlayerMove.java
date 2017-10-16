package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerMove extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerMoveEvent e = (PlayerMoveEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
