package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;

import net.neferett.linaris.events.SoloConnectionEvent;
import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnSoloConnection extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final SoloConnectionEvent e = (SoloConnectionEvent)event;
        if (e.getArgs().length > 0) {
        	String command = e.getArgs()[0];
        	if (command.equals("ghostmode")) {
        		GhostManager.addWaitedGhostPlayer(e.getPlayer());
        	}
        }
        e.allow();
        return !GhostManager.isGhost(e.getPlayer());
    }
}
