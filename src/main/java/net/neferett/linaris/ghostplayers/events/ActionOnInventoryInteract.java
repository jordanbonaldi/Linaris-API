package net.neferett.linaris.ghostplayers.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryInteractEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnInventoryInteract extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final InventoryInteractEvent e = (InventoryInteractEvent)event;
        return e.getWhoClicked() == null || !(e.getWhoClicked() instanceof Player) || !GhostManager.isGhost((Player)e.getWhoClicked());
    }
}
