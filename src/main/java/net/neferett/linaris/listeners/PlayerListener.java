package net.neferett.linaris.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.neferett.linaris.PlayersHandler.PlayerManager;
import net.neferett.linaris.PlayersHandler.Players;
import net.neferett.linaris.events.PlayerDamageEvent;
import net.neferett.linaris.utils.gui.GuiManager;

public class PlayerListener implements Listener {

	@EventHandler
	public void onDamage(final EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player)
			Bukkit.getPluginManager().callEvent(new PlayerDamageEvent(e));
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerClick(final InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player))
			return;
		final Players player = PlayerManager.get().getPlayer((Player) e.getWhoClicked());
		if (player.isFreeze())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamage(final EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player))
			return;
		final Players player = PlayerManager.get().getPlayer((Player) e.getDamager());
		if (player == null) {
			e.setCancelled(true);
			return;
		}
		if (player.isFreeze())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamage(final EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		final Players player = PlayerManager.get().getPlayer((Player) e.getEntity());
		if (player == null) {
			e.setCancelled(true);
			return;
		}
		if (player.isFreeze())
			e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDamage(final PlayerDamageEvent e) {
		final Players p = e.getPlayer();

		if (p.getPlayerData().contains("invisible") && p.getPlayerData().getBoolean("invisible"))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDropItem(final PlayerDropItemEvent e) {
		final Players player = PlayerManager.get().getPlayer(e.getPlayer());
		if (player.isFreeze())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerHeldItem(final PlayerItemHeldEvent e) {
		final Players player = PlayerManager.get().getPlayer(e.getPlayer());
		if (player.isFreeze())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(final PlayerInteractEvent e) {
		final Players player = PlayerManager.get().getPlayer(e.getPlayer());
		if (player.isFreeze())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(final PlayerPickupItemEvent e) {
		final Players player = PlayerManager.get().getPlayer(e.getPlayer());
		if (player == null) {
			e.setCancelled(true);
			return;
		}
		if (player.isFreeze())
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(final PlayerMoveEvent e) {
		final Players player = PlayerManager.get().getPlayer(e.getPlayer());
		if (player == null) {
			e.setCancelled(true);
			return;
		}
		if (player.isFreeze()) {
			final Location from = e.getFrom();
			final Location to = e.getTo();
			if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()
					|| from.getBlockY() != to.getBlockY())
				player.teleport(from);
		} else if (player.inTP()) {
			final Location from = new Location(player.getWorld(), (int) e.getFrom().getX(), (int) e.getFrom().getY(),
					(int) e.getFrom().getZ());
			final Location to = new Location(player.getWorld(), (int) e.getTo().getX(), (int) e.getTo().getY(),
					(int) e.getTo().getZ());

			if (!from.equals(to))
				player.setTp(false);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(final PlayerQuitEvent e) {
		final Players player = PlayerManager.get().getPlayer(e.getPlayer());

		if (player == null)
			return;
		if (player.isFreeze())
			PlayerManager.get().getModerator().forEach(p -> p.sendMessage(
					"§b" + player.getName() + "§c a d\u00e9connect\u00e9 alors qu'il \u00e9tait freeze !"));
		PlayerManager.get().removePlayer(player.getPlayer());
	}

	@EventHandler
	public void onPlayerRightClick(final PlayerInteractEntityEvent e) {
		if (!(e.getRightClicked() instanceof Player))
			return;
		final Players p = PlayerManager.get().getPlayer(e.getPlayer());

		if (p.isVanished())
			GuiManager.openGui(PlayerManager.get().getPlayer((Player) e.getRightClicked()).seeInventory(p));
	}

}
