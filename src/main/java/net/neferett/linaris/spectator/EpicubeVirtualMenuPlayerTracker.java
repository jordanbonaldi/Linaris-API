package net.neferett.linaris.spectator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.neferett.linaris.inventory.VirtualMenu;

public class EpicubeVirtualMenuPlayerTracker extends VirtualMenu {
	public static int								id;
	private static EpicubeVirtualMenuPlayerTracker	instance;

	public static EpicubeVirtualMenuPlayerTracker get() {
		return EpicubeVirtualMenuPlayerTracker.instance;
	}

	private List<Inventory>									inventories;
	private boolean											shouldUpdate;

	private final Map<String, EpicubeMenuItemTrackedPlayer>	trackedPlayers;

	public EpicubeVirtualMenuPlayerTracker() {
		super("Menu Spectateur", 3);
		this.shouldUpdate = true;
		this.trackedPlayers = new HashMap<>();
		EpicubeVirtualMenuPlayerTracker.instance = this;
	}

	public void addTrackedPlayer(final Player player) {
		if (this.trackedPlayers.get(player.getName()) != null) {
			SpectatorManager.warning("Impossible de rajouter " + player.getName() + " d\u00e9j\u00e0 pr\u00e9sent !");
			return;
		}
		this.tryToGrow();
		this.trackedPlayers.put(player.getName(), new EpicubeMenuItemTrackedPlayer(player));
		this.shouldUpdate = true;
	}

	@Override
	public Inventory getInventory() {
		return this.getInventory(0);
	}

	public Inventory getInventory(int page) {
		if (page < 0)
			page = 0;
		if (this.inventories == null || this.shouldUpdate) {
			this.inventories = new ArrayList<>();
			int current_page = 0;
			int i = 0;
			this.inventory = Bukkit.createInventory(this, this.row * 9, this.getTitle());
			this.inventories.add(this.inventory);
			final Iterator<Map.Entry<String, EpicubeMenuItemTrackedPlayer>> it = this.trackedPlayers.entrySet()
					.iterator();
			while (it.hasNext()) {
				if (i >= (this.row - 1) * 9) {
					i = 0;
					this.inventory.setItem(this.row * 9 - 1, EpicubeMenuItemNext.getItem(current_page + 1));
					(this.inventory = Bukkit.createInventory(this, this.row * 9, this.getTitle()))
							.setItem(this.row * 9 - 9, EpicubeMenuItemPrevious.getItem(current_page));
					this.inventories.add(this.inventory);
					++current_page;
				}
				this.inventory.addItem(it.next().getValue().getClonedItem());
				++i;
			}
			this.shouldUpdate = false;
		}
		return this.inventories.size() > page ? this.inventories.get(page)
				: this.inventories.get(this.inventories.size() - 1);
	}

	public boolean isTrackable(final String name) {
		return this.trackedPlayers.get(name) != null;
	}

	private int maxSlot() {
		return this.row * 9;
	}

	@Override
	public void open(final Player player) {
		player.openInventory(this.getInventory());
	}

	public void open(final Player player, final int page) {
		player.openInventory(this.getInventory(page));
	}

	@Override
	public void openWithExtraText(final Player player, final String extraText) {
		player.openInventory(this.getInventory());
	}

	public void removeTrackedPlayer(final Player player) {
		if (this.trackedPlayers.get(player.getName()) == null) {
			SpectatorManager.warning("Impossible de retirer " + player.getName() + " n'est pas pr\u00e9sent");
			return;
		}
		this.trackedPlayers.remove(player.getName()).remove();
		this.shouldUpdate = true;
	}

	public boolean tryToGrow() {
		final int size = this.trackedPlayers.entrySet().size();
		if (size + 1 > this.maxSlot()) {
			if (this.row >= 6)
				return false;
			++this.row;
		}
		return true;
	}
}
