package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;

import net.neferett.linaris.events.MultiConnectionEvent;

public class ActionOnMultiConnection extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final MultiConnectionEvent e = (MultiConnectionEvent)event;
        e.allow();
        return true;
    }
}
