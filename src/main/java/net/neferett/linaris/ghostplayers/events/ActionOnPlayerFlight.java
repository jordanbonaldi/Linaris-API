package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerFlight extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final PlayerToggleFlightEvent e = (PlayerToggleFlightEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
