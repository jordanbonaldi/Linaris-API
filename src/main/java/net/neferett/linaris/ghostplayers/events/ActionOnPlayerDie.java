package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerDie extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerDeathEvent e = (PlayerDeathEvent)event;
        return !GhostManager.isGhost(e.getEntity());
    }
}
