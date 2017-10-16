package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerChatEvent extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final AsyncPlayerChatEvent e = (AsyncPlayerChatEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
