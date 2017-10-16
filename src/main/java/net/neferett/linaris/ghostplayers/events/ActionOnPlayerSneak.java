package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerSneak extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerToggleSneakEvent e = (PlayerToggleSneakEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
