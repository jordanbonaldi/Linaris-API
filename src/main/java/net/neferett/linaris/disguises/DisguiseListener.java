package net.neferett.linaris.disguises;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.DisguiseType;
import net.neferett.linaris.disguises.disguisetypes.PlayerDisguise;
import net.neferett.linaris.disguises.disguisetypes.TargetedDisguise;
import net.neferett.linaris.disguises.disguisetypes.watchers.LivingWatcher;
import net.neferett.linaris.disguises.utilities.DisguiseUtilities;
import net.neferett.linaris.disguises.utilities.ReflectionManager;

public class DisguiseListener implements Listener {

	private final HashMap<String, Boolean[]> disguiseClone = new HashMap<>();
	private final HashMap<String, Disguise> disguiseEntity = new HashMap<>();
	private final HashMap<String, BukkitRunnable> disguiseRunnable = new HashMap<>();
	private final BukkitAPI plugin;
	private BukkitTask updaterTask;

	public DisguiseListener(BukkitAPI libsDisguises) {
		this.plugin = libsDisguises;

	}

	private void checkPlayerCanBlowDisguise(Player entity) {
		final Disguise[] disguises = DisguiseAPI.getDisguises(entity);
		if (disguises.length > 0) {
			DisguiseAPI.undisguiseToAll(entity);
			if (DisguiseConfig.getDisguiseBlownMessage().length() > 0)
				entity.sendMessage(DisguiseConfig.getDisguiseBlownMessage());
		}
	}

	private void chunkMove(Player player, Location newLoc, Location oldLoc) {
		try {
			for (final PacketContainer packet : DisguiseUtilities.getBedChunkPacket(player, newLoc, oldLoc))
				ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet, false);
			if (newLoc != null)
				for (final HashSet<TargetedDisguise> list : DisguiseUtilities.getDisguises().values())
					for (final TargetedDisguise disguise : list)
						if (disguise.isPlayerDisguise() && disguise.canSee(player)
								&& ((PlayerDisguise) disguise).getWatcher().isSleeping()
								&& DisguiseUtilities.getPerverts(disguise).contains(player)) {
							final PacketContainer[] packets = DisguiseUtilities.getBedPackets(player,
									disguise.getEntity() == player ? newLoc : disguise.getEntity().getLocation(),
									newLoc, (PlayerDisguise) disguise);
							if (disguise.getEntity() == player)
								for (final PacketContainer packet : packets)
									packet.getIntegers().write(0, DisguiseAPI.getSelfDisguiseId());
							for (final PacketContainer packet : packets)
								ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
						}
		} catch (final InvocationTargetException e) {
			e.printStackTrace(System.out);
		}
	}

	public void cleanup() {
		for (final BukkitRunnable r : this.disguiseRunnable.values())
			r.cancel();
		for (final Disguise d : this.disguiseEntity.values())
			d.removeDisguise();
		this.disguiseClone.clear();
		this.updaterTask.cancel();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onAttack(EntityDamageByEntityEvent event) {
		if (DisguiseConfig.isDisguiseBlownOnAttack()) {
			if (event.getEntity() instanceof Player)
				this.checkPlayerCanBlowDisguise((Player) event.getEntity());
			if (event.getDamager() instanceof Player)
				this.checkPlayerCanBlowDisguise((Player) event.getDamager());
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();

		if (DisguiseConfig.isBedPacketsEnabled())
			this.chunkMove(p, p.getLocation(), null);
	}

	/**
	 * Most likely faster if we don't bother doing checks if he sees a player
	 * disguise
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMove(PlayerMoveEvent event) {
		if (DisguiseConfig.isBedPacketsEnabled()) {
			final Location to = event.getTo();
			final Location from = event.getFrom();
			final int x1 = (int) Math.floor(to.getX() / 16D) - 17;
			final int x2 = (int) Math.floor(from.getX() / 16D) - 17;
			final int z1 = (int) Math.floor(to.getZ() / 16D) - 17;
			final int z2 = (int) Math.floor(from.getZ() / 16D) - 17;
			if (x1 - (x1 % 8) != x2 - (x2 % 8) || z1 - (z1 % 8) != z2 - (z2 % 8))
				this.chunkMove(event.getPlayer(), to, from);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		ReflectionManager.removePlayer(event.getPlayer());
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		final Disguise[] disguises = DisguiseAPI.getDisguises(event.getPlayer());
		for (final Disguise disguise : disguises)
			if (disguise.isRemoveDisguiseOnDeath())
				disguise.removeDisguise();
	}

	@EventHandler
	public void onRightClick(PlayerInteractEntityEvent event) {
		if (this.disguiseEntity.containsKey(event.getPlayer().getName())
				|| this.disguiseClone.containsKey(event.getPlayer().getName())) {
			final Player p = event.getPlayer();
			event.setCancelled(true);
			this.disguiseRunnable.remove(p.getName()).cancel();
			final Entity entity = event.getRightClicked();
			String entityName;
			if (entity instanceof Player && !this.disguiseClone.containsKey(p.getName()))
				entityName = entity.getName();
			else
				entityName = DisguiseType.getType(entity).toReadable();
			if (this.disguiseClone.containsKey(p.getName())) {
				final Boolean[] options = this.disguiseClone.remove(p.getName());
				Disguise disguise = DisguiseAPI.getDisguise(p, entity);
				if (disguise == null)
					disguise = DisguiseAPI.constructDisguise(entity, options[0], options[1], options[2]);
				else
					disguise = disguise.clone();
				final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
				String reference = null;
				final int referenceLength = Math.max(2,
						(int) Math.ceil((0.1D + DisguiseConfig.getMaxClonedDisguises()) / 26D));
				int attempts = 0;
				while (reference == null && attempts++ < 1000) {
					reference = "@";
					for (int i = 0; i < referenceLength; i++)
						reference += alphabet[new Random().nextInt(alphabet.length)];
					if (DisguiseUtilities.getClonedDisguise(reference) != null)
						reference = null;
				}
				if (reference != null && DisguiseUtilities.addClonedDisguise(reference, disguise)) {
					p.sendMessage(ChatColor.RED + "Constructed a " + entityName + " disguise! Your reference is "
							+ reference);
					p.sendMessage(ChatColor.RED + "Example usage: /disguise " + reference);
				} else
					p.sendMessage(ChatColor.RED
							+ "Failed to store the reference due to lack of size. Please set this in the config");
			} else if (this.disguiseEntity.containsKey(p.getName())) {
				final Disguise disguise = this.disguiseEntity.remove(p.getName());
				if (disguise != null) {
					if (disguise.isMiscDisguise() && !DisguiseConfig.isMiscDisguisesForLivingEnabled()
							&& entity instanceof LivingEntity)
						p.sendMessage(ChatColor.RED
								+ "Can't disguise a living entity as a misc disguise. This has been disabled in the config!");
					else {
						if (entity instanceof Player && DisguiseConfig.isNameOfPlayerShownAboveDisguise())
							if (disguise.getWatcher() instanceof LivingWatcher) {
								disguise.getWatcher().setCustomName(((Player) entity).getDisplayName());
								if (DisguiseConfig.isNameAboveHeadAlwaysVisible())
									disguise.getWatcher().setCustomNameVisible(true);
							}
						DisguiseAPI.disguiseToAll(entity, disguise);
						String disguiseName = "a ";
						if (disguise instanceof PlayerDisguise)
							disguiseName = "the player " + ((PlayerDisguise) disguise).getName();
						else
							disguiseName += disguise.getType().toReadable();
						if (disguise.isDisguiseInUse())
							p.sendMessage(ChatColor.RED + "Disguised " + (entity instanceof Player ? "" : "the ")
									+ entityName + " as " + disguiseName + "!");
						else
							p.sendMessage(
									ChatColor.RED + "Failed to disguise " + (entity instanceof Player ? "" : "the ")
											+ entityName + " as " + disguiseName + "!");
					}
				} else if (DisguiseAPI.isDisguised(entity)) {
					DisguiseAPI.undisguiseToAll(entity);
					p.sendMessage(
							ChatColor.RED + "Undisguised " + (entity instanceof Player ? "" : "the ") + entityName);
				} else
					p.sendMessage(
							ChatColor.RED + (entity instanceof Player ? "" : "the") + entityName + " isn't disguised!");
			}
		}
	}

	@EventHandler
	public void onTarget(EntityTargetEvent event) {
		if (DisguiseConfig.isMonstersIgnoreDisguises() && event.getTarget() != null
				&& event.getTarget() instanceof Player && DisguiseAPI.isDisguised(event.getTarget()))
			switch (event.getReason()) {
			case TARGET_ATTACKED_ENTITY:
			case TARGET_ATTACKED_OWNER:
			case OWNER_ATTACKED_TARGET:
			case CUSTOM:
				break;
			default:
				event.setCancelled(true);
				break;
			}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onTeleport(final PlayerTeleportEvent event) {
		if (!DisguiseAPI.isDisguised(event.getPlayer()))
			return;
		final Location to = event.getTo();
		final Location from = event.getFrom();
		if (DisguiseConfig.isBedPacketsEnabled()) {
			final int x1 = (int) Math.floor(to.getX() / 16D) - 17;
			final int x2 = (int) Math.floor(from.getX() / 16D) - 17;
			final int z1 = (int) Math.floor(to.getZ() / 16D) - 17;
			final int z2 = (int) Math.floor(from.getZ() / 16D) - 17;
			if (x1 - (x1 % 8) != x2 - (x2 % 8) || z1 - (z1 % 8) != z2 - (z2 % 8)) {
				this.chunkMove(event.getPlayer(), null, from);
				Bukkit.getScheduler().runTask(this.plugin, () -> {
					if (!event.isCancelled())
						DisguiseListener.this.chunkMove(event.getPlayer(), event.getTo(), null);
					else
						DisguiseListener.this.chunkMove(event.getPlayer(), event.getPlayer().getLocation(), null);
				});
			}
		}
		if (DisguiseConfig.isUndisguiseOnWorldChange() && to.getWorld() != null && from.getWorld() != null
				&& to.getWorld() != from.getWorld())
			for (final Disguise disguise : DisguiseAPI.getDisguises(event.getPlayer()))
				disguise.removeDisguise();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onVehicleEnter(VehicleEnterEvent event) {
		if (event.getEntered() instanceof Player
				&& DisguiseAPI.isDisguised((Player) event.getEntered(), event.getEntered())) {
			DisguiseUtilities.removeSelfDisguise((Player) event.getEntered());
			((Player) event.getEntered()).updateInventory();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onVehicleLeave(VehicleExitEvent event) {
		if (event.getExited() instanceof Player) {
			final Disguise disguise = DisguiseAPI.getDisguise((Player) event.getExited(), event.getExited());
			if (disguise != null)
				Bukkit.getScheduler().runTask(this.plugin, () -> {
					DisguiseUtilities.setupFakeDisguise(disguise);
					((Player) disguise.getEntity()).updateInventory();
				});
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWorldSwitch(final PlayerChangedWorldEvent event) {
		if (!DisguiseAPI.isDisguised(event.getPlayer()))
			return;
		if (DisguiseConfig.isBedPacketsEnabled())
			this.chunkMove(event.getPlayer(), event.getPlayer().getLocation(), null);
		if (DisguiseConfig.isUndisguiseOnWorldChange())
			for (final Disguise disguise : DisguiseAPI.getDisguises(event.getPlayer()))
				disguise.removeDisguise();
		else {
			// Stupid hack to fix worldswitch invisibility bug
			final boolean viewSelfToggled = DisguiseAPI.isViewSelfToggled(event.getPlayer());
			if (viewSelfToggled) {
				final Disguise disguise = DisguiseAPI.getDisguise(event.getPlayer());
				disguise.setViewSelfDisguise(false);
				Bukkit.getScheduler().runTaskLater(this.plugin, () -> disguise.setViewSelfDisguise(true), 20L); // I
																												// wish
																												// I
																												// could
																												// use
																												// lambdas
																												// here,
																												// so
																												// badly
			}
		}
	}

	public void setDisguiseClone(final String player, Boolean[] options) {
		if (this.disguiseRunnable.containsKey(player)) {
			final BukkitRunnable run = this.disguiseRunnable.remove(player);
			run.cancel();
			run.run();
		}
		final BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				DisguiseListener.this.disguiseClone.remove(player);
				DisguiseListener.this.disguiseRunnable.remove(player);
			}
		};
		runnable.runTaskLater(this.plugin, 20 * DisguiseConfig.getDisguiseCloneExpire());
		this.disguiseRunnable.put(player, runnable);
		this.disguiseClone.put(player, options);
	}

	public void setDisguiseEntity(final String player, Disguise disguise) {
		if (this.disguiseRunnable.containsKey(player)) {
			final BukkitRunnable run = this.disguiseRunnable.remove(player);
			run.cancel();
			run.run();
		}
		final BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				DisguiseListener.this.disguiseEntity.remove(player);
				DisguiseListener.this.disguiseRunnable.remove(player);
			}
		};
		runnable.runTaskLater(this.plugin, 20 * DisguiseConfig.getDisguiseEntityExpire());
		this.disguiseRunnable.put(player, runnable);
		this.disguiseEntity.put(player, disguise);
	}

}
