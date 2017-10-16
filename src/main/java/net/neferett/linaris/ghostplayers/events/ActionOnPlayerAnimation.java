package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerAnimationEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerAnimation extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerAnimationEvent e = (PlayerAnimationEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
