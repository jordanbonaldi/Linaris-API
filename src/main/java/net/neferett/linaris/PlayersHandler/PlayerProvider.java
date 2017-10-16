package net.neferett.linaris.PlayersHandler;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class PlayerProvider {
	private int				hash;
	protected CraftPlayer	player;

	public PlayerProvider() {
		this.hash = 0;
	}

	public void abandonConversation(final Conversation conversation) {
		this.player.abandonConversation(conversation);
	}

	public void abandonConversation(final Conversation conversation, final ConversationAbandonedEvent details) {
		this.player.abandonConversation(conversation, details);
	}

	public void acceptConversationInput(final String input) {
		this.player.acceptConversationInput(input);
	}

	public PermissionAttachment addAttachment(final Plugin plugin) {
		return this.player.addAttachment(plugin);
	}

	public PermissionAttachment addAttachment(final Plugin plugin, final int ticks) {
		return this.player.addAttachment(plugin, ticks);
	}

	public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value) {
		return this.player.addAttachment(plugin, name, value);
	}

	public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value,
			final int ticks) {
		return this.player.addAttachment(plugin, name, value, ticks);
	}

	public boolean addPotionEffect(final PotionEffect effect) {
		return this.player.addPotionEffect(effect);
	}

	public boolean addPotionEffect(final PotionEffect effect, final boolean force) {
		return this.player.addPotionEffect(effect, force);
	}

	public boolean addPotionEffects(final Collection<PotionEffect> effects) {
		return this.player.addPotionEffects(effects);
	}

	public void awardAchievement(final Achievement achievement) {
		this.player.awardAchievement(achievement);
	}

	public boolean beginConversation(final Conversation conversation) {
		return this.player.beginConversation(conversation);
	}

	public boolean canSee(final Player player) {
		return this.player.canSee(player);
	}

	public void chat(final String msg) {
		this.player.chat(msg);
	}

	public void closeInventory() {
		this.player.closeInventory();
	}

	public void damage(final double amount) {
		this.player.damage(amount);
	}

	public void damage(final double amount, final Entity source) {
		this.player.damage(amount, source);
	}

	public void damage(final int arg0) {
		this.player.damage(arg0);
	}

	public void damage(final int arg0, final Entity arg1) {
		this.player.damage(arg0, arg1);
	}

	public void decrementStatistic(final Statistic statistic) {
		this.player.decrementStatistic(statistic);
	}

	public void decrementStatistic(final Statistic statistic, final EntityType entityType) {
		this.player.decrementStatistic(statistic, entityType);
	}

	public void decrementStatistic(final Statistic statistic, final EntityType entityType, final int amount) {
		this.player.decrementStatistic(statistic, entityType, amount);
	}

	public void decrementStatistic(final Statistic statistic, final int amount) {
		this.player.decrementStatistic(statistic, amount);
	}

	public void decrementStatistic(final Statistic statistic, final Material material) {
		this.player.decrementStatistic(statistic, material);
	}

	public void decrementStatistic(final Statistic statistic, final Material material, final int amount) {
		this.player.decrementStatistic(statistic, material, amount);
	}

	public boolean eject() {
		return this.player.eject();
	}

	public Collection<PotionEffect> getActivePotionEffects() {
		return this.player.getActivePotionEffects();
	}

	public InetSocketAddress getAddress() {
		return this.player.getAddress();
	}

	public boolean getAllowFlight() {
		return this.player.getAllowFlight();
	}

	public Location getBedSpawnLocation() {
		return this.player.getBedSpawnLocation();
	}

	public boolean getCanPickupItems() {
		return this.player.getCanPickupItems();
	}

	public Location getCompassTarget() {
		return this.player.getCompassTarget();
	}

	public String getCustomName() {
		return this.player.getCustomName();
	}

	public String getDisplayName() {
		return this.player.getDisplayName();
	}

	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return this.player.getEffectivePermissions();
	}

	public Inventory getEnderChest() {
		return this.player.getEnderChest();
	}

	public int getEntityId() {
		return this.player.getEntityId();
	}

	public EntityEquipment getEquipment() {
		return this.player.getEquipment();
	}

	public float getExhaustion() {
		return this.player.getExhaustion();
	}

	public float getExp() {
		return this.player.getExp();
	}

	public int getExpToLevel() {
		return this.player.getExpToLevel();
	}

	public double getEyeHeight() {
		return this.player.getEyeHeight();
	}

	public double getEyeHeight(final boolean ignoreSneaking) {
		return this.player.getEyeHeight(ignoreSneaking);
	}

	public Location getEyeLocation() {
		return this.player.getEyeLocation();
	}

	public float getFallDistance() {
		return this.player.getFallDistance();
	}

	public int getFireTicks() {
		return this.player.getFireTicks();
	}

	public long getFirstPlayed() {
		return this.player.getFirstPlayed();
	}

	public float getFlySpeed() {
		return this.player.getFlySpeed();
	}

	public int getFoodLevel() {
		return this.player.getFoodLevel();
	}

	public GameMode getGameMode() {
		return this.player.getGameMode();
	}

	public EntityPlayer getHandle() {
		return this.player.getHandle();
	}

	public double getHealth() {
		return this.player.getHealth();
	}

	public double getHealthScale() {
		return this.player.getHealthScale();
	}

	public PlayerInventory getInventory() {
		return this.player.getInventory();
	}

	public ItemStack getItemInHand() {
		return this.player.getItemInHand();
	}

	public ItemStack getItemOnCursor() {
		return this.player.getItemOnCursor();
	}

	public Player getKiller() {
		return this.player.getKiller();
	}

	public double getLastDamage() {
		return this.player.getHealth();
	}

	public EntityDamageEvent getLastDamageCause() {
		return this.player.getLastDamageCause();
	}

	public long getLastPlayed() {
		return this.player.getLastPlayed();
	}

	public List<Block> getLastTwoTargetBlocks(final HashSet<Byte> transparent, final int maxDistance) {
		return this.player.getLastTwoTargetBlocks(transparent, maxDistance);
	}

	public Entity getLeashHolder() throws IllegalStateException {
		return this.player.getLeashHolder();
	}

	public int getLevel() {
		return this.player.getLevel();
	}

	public List<Block> getLineOfSight(final HashSet<Byte> transparent, final int maxDistance) {
		return this.player.getLineOfSight(transparent, maxDistance);
	}

	public Set<String> getListeningPluginChannels() {
		return this.player.getListeningPluginChannels();
	}

	public Location getLocation() {
		return this.player.getLocation();
	}

	public Location getLocation(final Location loc) {
		return this.player.getLocation(loc);
	}

	public int getMaxFireTicks() {
		return this.player.getMaxFireTicks();
	}

	public double getMaxHealth() {
		return this.player.getMaxHealth();
	}

	public int getMaximumAir() {
		return this.player.getMaximumAir();
	}

	public int getMaximumNoDamageTicks() {
		return this.player.getMaximumNoDamageTicks();
	}

	public String getName() {
		return this.player.getName();
	}

	public List<Entity> getNearbyEntities(final double x, final double y, final double z) {
		return this.player.getNearbyEntities(x, y, z);
	}

	public int getNoDamageTicks() {
		return this.player.getNoDamageTicks();
	}

	public InventoryView getOpenInventory() {
		return this.player.getOpenInventory();
	}

	public Entity getPassenger() {
		return this.player.getPassenger();
	}

	public Player getPlayer() {
		return this.player.getPlayer();
	}

	public String getPlayerListName() {
		return this.player.getPlayerListName();
	}

	public long getPlayerTime() {
		return this.player.getPlayerTime();
	}

	public long getPlayerTimeOffset() {
		return this.player.getPlayerTimeOffset();
	}

	public WeatherType getPlayerWeather() {
		return this.player.getPlayerWeather();
	}

	public int getRemainingAir() {
		return this.player.getRemainingAir();
	}

	public boolean getRemoveWhenFarAway() {
		return this.player.getRemoveWhenFarAway();
	}

	public float getSaturation() {
		return this.player.getSaturation();
	}

	public CraftScoreboard getScoreboard() {
		return this.player.getScoreboard();
	}

	public Server getServer() {
		return this.player.getServer();
	}

	public int getSleepTicks() {
		return this.player.getSleepTicks();
	}

	public int getStatistic(final Statistic statistic) {
		return this.player.getStatistic(statistic);
	}

	public int getStatistic(final Statistic statistic, final EntityType entityType) {
		return this.player.getStatistic(statistic, entityType);
	}

	public int getStatistic(final Statistic statistic, final Material material) {
		return this.player.getStatistic(statistic, material);
	}

	public Block getTargetBlock(final HashSet<Byte> transparent, final int maxDistance) {
		return this.player.getTargetBlock(transparent, maxDistance);
	}

	public int getTicksLived() {
		return this.player.getTicksLived();
	}

	public int getTotalExperience() {
		return this.player.getTotalExperience();
	}

	public EntityType getType() {
		return this.player.getType();
	}

	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}

	public Entity getVehicle() {
		return this.player.getVehicle();
	}

	public Vector getVelocity() {
		return this.player.getVelocity();
	}

	public float getWalkSpeed() {
		return this.player.getWalkSpeed();
	}

	public World getWorld() {
		return this.player.getWorld();
	}

	public void giveExp(final int exp) {
		this.player.giveExp(exp);
	}

	public void giveExpLevels(final int levels) {
		this.player.giveExpLevels(levels);
	}

	public boolean hasAchievement(final Achievement achievement) {
		return this.player.hasAchievement(achievement);
	}

	@Override
	public int hashCode() {
		if (this.hash == 0 || this.hash == 485)
			this.hash = 485 + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
		return this.hash;
	}

	public boolean hasLineOfSight(final Entity other) {
		return this.player.hasLineOfSight(other);
	}

	public boolean hasPermission(final Permission perm) {
		return this.player.hasPermission(perm);
	}

	public boolean hasPermission(final String name) {
		return this.player.hasPermission(name);
	}

	public boolean hasPlayedBefore() {
		return this.player.hasPlayedBefore();
	}

	public boolean hasPotionEffect(final PotionEffectType type) {
		return this.player.hasPotionEffect(type);
	}

	public void hidePlayer(final Player player) {
		this.player.hidePlayer(player);
	}

	public void incrementStatistic(final Statistic statistic) {
		this.player.incrementStatistic(statistic);
	}

	public void incrementStatistic(final Statistic statistic, final EntityType entityType) {
		this.player.incrementStatistic(statistic, entityType);
	}

	public void incrementStatistic(final Statistic statistic, final EntityType entityType, final int amount) {
		this.player.incrementStatistic(statistic, entityType, amount);
	}

	public void incrementStatistic(final Statistic statistic, final int amount) {
		this.player.incrementStatistic(statistic, amount);
	}

	public void incrementStatistic(final Statistic statistic, final Material material) {
		this.player.incrementStatistic(statistic, material);
	}

	public void incrementStatistic(final Statistic statistic, final Material material, final int amount) {
		this.player.incrementStatistic(statistic, material, amount);
	}

	public boolean isBanned() {
		return this.player.isBanned();
	}

	public boolean isBlocking() {
		return this.player.isBlocking();
	}

	public boolean isConversing() {
		return this.player.isConversing();
	}

	public boolean isCustomNameVisible() {
		return this.player.isCustomNameVisible();
	}

	public boolean isDead() {
		return this.player.isDead();
	}

	public boolean isEmpty() {
		return this.player.isEmpty();
	}

	public boolean isFlying() {
		return this.player.isFlying();
	}

	public boolean isHealthScaled() {
		return this.player.isHealthScaled();
	}

	public boolean isInsideVehicle() {
		return this.player.isInsideVehicle();
	}

	public boolean isLeashed() {
		return this.player.isLeashed();
	}

	public boolean isOnGround() {
		return this.player.isOnGround();
	}

	public boolean isOnline() {
		return this.player.getUniqueId() != null && Bukkit.getPlayer(this.player.getUniqueId()) != null;
	}

	public boolean isOp() {
		return this.player.isOp();
	}

	public boolean isPermissionSet(final Permission perm) {
		return this.player.isPermissionSet(perm);
	}

	public boolean isPermissionSet(final String name) {
		return this.player.isPermissionSet(name);
	}

	public boolean isPlayerTimeRelative() {
		return this.player.isPlayerTimeRelative();
	}

	public boolean isSleeping() {
		return this.player.isSleeping();
	}

	public boolean isSleepingIgnored() {
		return this.player.isSleepingIgnored();
	}

	public boolean isSneaking() {
		return this.player.isSneaking();
	}

	public boolean isSprinting() {
		return this.player.isSprinting();
	}

	public boolean isValid() {
		return this.player != null && this.player.isValid();
	}

	public boolean isWhitelisted() {
		return this.player.isWhitelisted();
	}

	public void kickPlayer(final String message) {
		this.player.kickPlayer(message);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Projectile> T launchProjectile(final Class<? extends T> projectile) {
		return (T) this.player.launchProjectile((Class) projectile);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Projectile> T launchProjectile(final Class<? extends T> projectile, final Vector velocity) {
		return (T) this.player.launchProjectile((Class) projectile, velocity);
	}

	public boolean leaveVehicle() {
		return this.player.leaveVehicle();
	}

	public void loadData() {
		this.player.loadData();
	}

	public InventoryView openEnchanting(final Location location, final boolean force) {
		return this.player.openEnchanting(location, force);
	}

	public InventoryView openInventory(final Inventory inventory) {
		return this.player.openInventory(inventory);
	}

	public void openInventory(final InventoryView inventory) {
		this.player.openInventory(inventory);
	}

	public InventoryView openWorkbench(final Location location, final boolean force) {
		return this.player.openWorkbench(location, force);
	}

	public boolean performCommand(final String command) {
		return this.player.performCommand(command);
	}

	public void playEffect(final EntityEffect type) {
		this.player.playEffect(type);
	}

	public void playEffect(final Location loc, final Effect effect, final int data) {
		this.player.playEffect(loc, effect, data);
	}

	public <T> void playEffect(final Location loc, final Effect effect, final T data) {
		this.player.playEffect(loc, effect, (Object) data);
	}

	public void playNote(final Location loc, final byte instrument, final byte note) {
		this.player.playNote(loc, instrument, note);
	}

	public void playNote(final Location loc, final Instrument instrument, final Note note) {
		this.player.playNote(loc, instrument, note);
	}

	public void playSound(final Location loc, final Sound sound, final float volume, final float pitch) {
		this.player.playSound(loc, sound, volume, pitch);
	}

	public void playSound(final Location loc, final String sound, final float volume, final float pitch) {
		this.player.playSound(loc, sound, volume, pitch);
	}

	public void recalculatePermissions() {
		this.player.recalculatePermissions();
	}

	public void remove() {
		this.player.remove();
	}

	public void removeAchievement(final Achievement achievement) {
		this.player.removeAchievement(achievement);
	}

	public void removeAttachment(final PermissionAttachment attachment) {
		this.player.removeAttachment(attachment);
	}

	public void removePotionEffect(final PotionEffectType type) {
		this.player.removePotionEffect(type);
	}

	public void resetMaxHealth() {
		this.player.resetMaxHealth();
	}

	public void resetPlayerTime() {
		this.player.resetPlayerTime();
	}

	public void resetPlayerWeather() {
		this.player.resetPlayerWeather();
	}

	public void saveData() {
		this.player.saveData();
	}

	public void sendBlockChange(final Location loc, final int material, final byte data) {
		this.player.sendBlockChange(loc, material, data);
	}

	public void sendBlockChange(final Location loc, final Material material, final byte data) {
		this.player.sendBlockChange(loc, material, data);
	}

	public boolean sendChunkChange(final Location loc, final int sx, final int sy, final int sz, final byte[] data) {
		return this.player.sendChunkChange(loc, sx, sy, sz, data);
	}

	public void sendMap(final MapView map) {
		this.player.sendMap(map);
	}

	public void sendMessage(final String message) {
		this.player.sendMessage(message);
	}

	public void sendMessage(final String[] messages) {
		this.player.sendMessage(messages);
	}

	public void sendMessage(final TextComponent message) {
		this.player.spigot().sendMessage(message);
	}

	public void sendPluginMessage(final Plugin source, final String channel, final byte[] message) {
		this.player.sendPluginMessage(source, channel, message);
	}

	public void sendRawMessage(final String message) {
		this.player.sendRawMessage(message);
	}

	public void sendSignChange(final Location loc, final String[] lines) {
		this.player.sendSignChange(loc, lines);
	}

	public Map<String, Object> serialize() {
		return this.player.serialize();
	}

	public void setAllowFlight(final boolean value) {
		this.player.setAllowFlight(value);
	}

	public void setBanned(final boolean value) {
		this.player.setBanned(value);
	}

	public void setBedSpawnLocation(final Location location) {
		this.player.setBedSpawnLocation(location);
	}

	public void setBedSpawnLocation(final Location location, final boolean override) {
		this.player.setBedSpawnLocation(location, override);
	}

	public void setCanPickupItems(final boolean pickup) {
		this.player.setCanPickupItems(pickup);
	}

	public void setCompassTarget(final Location loc) {
		this.player.setCompassTarget(loc);
	}

	public void setCustomName(final String name) {
		this.player.setCustomName(name);
	}

	public void setCustomNameVisible(final boolean flag) {
		this.player.setCustomNameVisible(flag);
	}

	public void setDisplayName(final String name) {
		this.player.setDisplayName(name);
	}

	public void setExhaustion(final float value) {
		this.player.setExhaustion(value);
	}

	public void setExp(final float exp) {
		this.player.setExp(exp);
	}

	public void setFallDistance(final float distance) {
		this.player.setFallDistance(distance);
	}

	public void setFireTicks(final int ticks) {
		this.player.setFireTicks(ticks);
	}

	public void setFlying(final boolean value) {
		this.player.setFlying(value);
	}

	public void setFlySpeed(final float value) {
		this.player.setFlySpeed(value);
	}

	public void setFoodLevel(final int value) {
		this.player.setFoodLevel(value);
	}

	public void setGameMode(final GameMode mode) {
		this.player.setGameMode(mode);
	}

	public void setHealth(final double health) {
		this.player.setHealth(health);
	}

	public void setHealth(final int arg0) {
		this.player.setHealth(arg0);
	}

	public void setHealthScale(final double value) {
		this.player.setHealthScale(value);
	}

	public void setHealthScaled(final boolean scale) {
		this.player.setHealthScaled(scale);
	}

	public void setItemInHand(final ItemStack item) {
		this.player.setItemInHand(item);
	}

	public void setItemOnCursor(final ItemStack item) {
		this.player.setItemOnCursor(item);
	}

	public void setLastDamage(final double damage) {
		this.player.setLastDamage(damage);
	}

	public void setLastDamage(final int arg0) {
		this.setLastDamage(arg0);
	}

	public void setLastDamageCause(final EntityDamageEvent event) {
		this.player.setLastDamageCause(event);
	}

	public boolean setLeashHolder(final Entity holder) {
		return this.player.setLeashHolder(holder);
	}

	public void setLevel(final int level) {
		this.player.setLevel(level);
	}

	public void setMaxHealth(final double amount) {
		this.player.setMaxHealth(amount);
	}

	public void setMaxHealth(final int arg0) {
		this.player.setMaxHealth(arg0);
	}

	public void setMaximumAir(final int ticks) {
		this.player.setMaximumAir(ticks);
	}

	public void setMaximumNoDamageTicks(final int ticks) {
		this.player.setMaximumNoDamageTicks(ticks);
	}

	public void setNoDamageTicks(final int ticks) {
		this.player.setNoDamageTicks(ticks);
	}

	public void setOp(final boolean value) {
		this.player.setOp(value);
	}

	public boolean setPassenger(final Entity passenger) {
		return this.player.setPassenger(passenger);
	}

	public void setPlayerListName(final String name) {
		this.player.setPlayerListName(name);
	}

	public void setPlayerTime(final long time, final boolean relative) {
		this.player.setPlayerTime(time, relative);
	}

	public void setPlayerWeather(final WeatherType type) {
		this.player.setPlayerWeather(type);
	}

	public void setRemainingAir(final int ticks) {
		this.player.setRemainingAir(ticks);
	}

	public void setRemoveWhenFarAway(final boolean remove) {
		this.player.setRemoveWhenFarAway(remove);
	}

	public void setResourcePack(final String url) {
		this.player.setResourcePack(url);
	}

	public void setSaturation(final float value) {
		this.player.setSaturation(value);
	}

	public void setScoreboard(final Scoreboard scoreboard) {
		this.player.setScoreboard(scoreboard);
	}

	public void setSleepingIgnored(final boolean isSleeping) {
		this.player.setSleepingIgnored(isSleeping);
	}

	public void setSneaking(final boolean sneak) {
		this.player.setSneaking(sneak);
	}

	public void setSprinting(final boolean sprinting) {
		this.player.setSprinting(sprinting);
	}

	public void setStatistic(final Statistic statistic, final EntityType entityType, final int newValue) {
		this.player.setStatistic(statistic, entityType, newValue);
	}

	public void setStatistic(final Statistic statistic, final int newValue) {
		this.player.setStatistic(statistic, newValue);
	}

	public void setStatistic(final Statistic statistic, final Material material, final int newValue) {
		this.player.setStatistic(statistic, material, newValue);
	}

	public void setTexturePack(final String url) {
		this.player.setTexturePack(url);
	}

	public void setTicksLived(final int value) {
		this.player.setTicksLived(value);
	}

	public void setTotalExperience(final int exp) {
		this.player.setTotalExperience(exp);
	}

	public void setVelocity(final Vector vel) {
		this.player.setVelocity(vel);
	}

	public void setWalkSpeed(final float value) {
		this.player.setWalkSpeed(value);
	}

	public void setWhitelisted(final boolean value) {
		this.player.setWhitelisted(value);
	}

	@Deprecated
	public Arrow shootArrow() {
		return this.player.shootArrow();
	}

	public void showPlayer(final Player player) {
		this.player.showPlayer(player);
	}

	@Deprecated
	public Player.Spigot spigot() {
		return this.player.spigot();
	}

	public boolean teleport(final Entity destination) {
		return this.player.teleport(destination);
	}

	public boolean teleport(final Entity destination, final PlayerTeleportEvent.TeleportCause cause) {
		return this.player.teleport(destination, cause);
	}

	public boolean teleport(final Location location) {
		return this.player.teleport(location);
	}

	public boolean teleport(final Location location, final PlayerTeleportEvent.TeleportCause cause) {
		return this.player.teleport(location, cause);
	}

	@Deprecated
	public Egg throwEgg() {
		return this.player.throwEgg();
	}

	@Deprecated
	public Snowball throwSnowball() {
		return this.player.throwSnowball();
	}

	@Override
	public String toString() {
		return this.player.toString();
	}

	@Deprecated
	public void updateInventory() {
		this.player.updateInventory();
	}
}
