package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.world.StructureGrowEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnStructureGrow extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final StructureGrowEvent e = (StructureGrowEvent)event;
        return !GhostManager.isGhost(e.getPlayer());
    }
}
