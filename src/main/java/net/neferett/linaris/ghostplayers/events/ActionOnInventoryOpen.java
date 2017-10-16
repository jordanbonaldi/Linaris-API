package net.neferett.linaris.ghostplayers.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryOpenEvent;

import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnInventoryOpen extends ActionOnEvent
{
    @Override
    public boolean shouldFire(final Event event) {
        final InventoryOpenEvent e = (InventoryOpenEvent)event;
        return e.getPlayer() == null || !(e.getPlayer() instanceof Player) || !GhostManager.isGhost((Player)e.getPlayer());
    }
}
