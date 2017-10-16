package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerSprint extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerToggleSprintEvent e = (PlayerToggleSprintEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
