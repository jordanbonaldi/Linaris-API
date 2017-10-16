package net.neferett.linaris.ghostplayers.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnInventoryClose extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final InventoryCloseEvent e = (InventoryCloseEvent)event;
        return e.getPlayer() == null || !(e.getPlayer() instanceof Player) || !GhostManager.isGhost((Player)e.getPlayer());
    }
}
