package net.neferett.linaris.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.ghostplayers.GhostManager;
import net.neferett.linaris.specialitems.SpecialItem;
import net.neferett.linaris.utils.tasksmanager.TaskManager;

public class InventorySpectatorClick extends PacketAdapter {

	public InventorySpectatorClick(BukkitAPI api) {
		super(api, PacketType.Play.Client.WINDOW_CLICK);
	}
	
	@Override
	public void onPacketReceiving(PacketEvent event) {
		
		Player player = event.getPlayer();
		
		 if (player.getName().contains("UNKNOWN[/"))
        	return;
        
    	if (GhostManager.isGhost(player.getName())) {
        	return;
        }
		if (!player.getGameMode().equals(GameMode.SPECTATOR)) return;

		ItemStack clickedItem = event.getPacket().getItemModifier().read(0);
		final SpecialItem item = SpecialItem.getSpecialItem(clickedItem);

		if (item == null || !item.isInventoryClickable()) return;

		TaskManager.runTask(new Runnable() {
			@Override
			public void run() {
				item.inventoryClickEvent(player);
			}
		});

	}
	
}
