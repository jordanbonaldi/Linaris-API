package net.neferett.linaris.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.comphenix.protocol.ProtocolLibrary;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.GameServer;
import net.neferett.linaris.api.Games;
import net.neferett.linaris.events.ReturnToLobbyEvent;
import net.neferett.linaris.metadatas.Flags;
import net.neferett.linaris.rabbitmq.messaging.RabbitMQMessagingClient;
import net.neferett.linaris.specialitems.SpecialItem;
import net.neferett.linaris.utils.json.JSONObject;

public class PlayerUtils {

	/**
	 * Change le tueur du joueur par la dernière personne qui l'a touché en
	 * VANILLA, dépend du flag lastDamager qui doit être posé sur les event de
	 * dégâts ou sorts, ignore le teamkill
	 *
	 * @param player
	 */
	public static void changeKillerToLastDammager(final Player player) {
		final String lastDamagerName = Flags.readStringFlag(player, Flags.lastDamager);
		if (lastDamagerName != null) {
			final Player killer = Bukkit.getPlayer(lastDamagerName);
			if (killer != null && !killer.getName().equals(player.getName())) {
				final CraftPlayer craftPlayer = (CraftPlayer) player;
				craftPlayer.getHandle().killer = ((CraftPlayer) killer).getHandle();
			}
			Flags.removeFlag(player, Flags.lastDamager);
		}
	}

	/**
	 * Vide l'inventaire du joueur + armure
	 *
	 * @param player
	 */
	public static void clearFullInventory(final Player player) {
		player.closeInventory();
		player.getInventory().clear();
		player.setItemInHand(new ItemStack(Material.AIR));
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
	}

	/**
	 * Vide l'inventaire du joueur
	 *
	 * @param player
	 */
	public static void clearInventory(final Player player) {
		player.closeInventory();
		player.getInventory().clear();
		player.setItemInHand(new ItemStack(Material.AIR));
	}

	/**
	 * Consummer l'objet en main, réduit de 1 le nombre de stack et détruit si 0
	 *
	 * @param player
	 */
	public static void consumItemInHand(final Player player) {
		final PlayerInventory inv = player.getInventory();
		int amount = inv.getItemInHand().getAmount();
		if (amount == 1) {
			inv.setItemInHand(new ItemStack(Material.AIR));
			return;
		}
		amount--;
		inv.getItemInHand().setAmount(amount);
	}

	/**
	 * Inflige des dégâts à l'entité (sans tenir compte de l'armure) sans
	 * alarmer l'AntiCheat et en taggant cette dernière comme lastDamager
	 */
	@SuppressWarnings("deprecation")
	public static void damage(final Player damager, final LivingEntity livingEntity, final double amount) {
		if (Flags.hasFlag(livingEntity, Flags.godMode))
			return;
		if (livingEntity.isDead())
			return;
		final EntityDamageEvent event = new EntityDamageEvent(livingEntity, null, amount);
		// Need Java 8
		// Map<DamageModifier, Double> modifiers = new EnumMap<DamageModifier,
		// Double>(ImmutableMap.of(DamageModifier.BASE,
		// Double.valueOf(amount)));
		// Map<DamageModifier, ? extends Function<? super Double, Double>>
		// modifierFunctions = new
		// EnumMap<DamageModifier,? extends Function<? super Double,
		// Double>(ImmutableMap.of(DamageModifier.BASE,
		// 0.0D));
		// event = new EntityDamageEvent(livingEntity, null, modifiers,
		// modifierFunctions);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			Flags.setStringFlag(livingEntity, Flags.lastDamager, damager.getName());
			EntityUtils.damage(livingEntity, amount);
		}
	}

	/**
	 * Même chose que damage mais avec un effet de knockBack
	 *
	 * @param damager
	 * @param livingEntity
	 * @param amount
	 * @param knockBackPower
	 * @deprecated pas terminé
	 */
	@Deprecated
	public static void damage(final Player damager, final LivingEntity livingEntity, final double amount,
			final double knockBackPower) {
		damage(damager, livingEntity, amount);
		livingEntity.setVelocity(new Vector(0, 0, 0));
	}

	public static void executeCommand(final Player p, final String command) {

		try {
			final JSONObject json = new JSONObject();
			json.put("player", p.getName());
			json.put("command", command);
			new RabbitMQMessagingClient("cmdExecutor", json);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recupère le nom de la dernière entité a voir attaqué cette entité,
	 * utilisé actuellement pour attribué les kills dans certains jeux
	 *
	 * @param entity
	 * @return
	 */
	public static String getLastDamagerName(final Entity entity) {
		return Flags.readStringFlag(entity, Flags.lastDamager);
	}

	public static void givePlayerBackToHubItem(final Player player) {
		if (MenuItemBackToHub.id == 0)
			MenuItemBackToHub.id = SpecialItem.registerItem(new MenuItemBackToHub());
		player.getInventory().setItem(8, SpecialItem.get(MenuItemBackToHub.id).getStaticItem());
	}

	public static void goToServer(final Player p, final GameServer server) {
		goToServer(p.getName(), server.getServName());
	}

	public static void goToServer(final String p, final String server) {

		try {
			final JSONObject json = new JSONObject();
			json.put("player", p);
			json.put("server", server);
			new RabbitMQMessagingClient("servermoving", json);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Met un joueur à 0 comme s'il venait de se connecter Remet aussi la vie
	 * maximale à 20, levels et xp
	 *
	 * @param player
	 */
	public static void razPlayer(final Player player) {
		player.setMaxHealth(20);
		player.setExp(0);
		player.setLevel(0);
		player.setFallDistance(0);
		player.setFireTicks(0);
		clearFullInventory(player);
		resetPlayer(player);
	}

	/**
	 * Retire tous les effets de potion du joueur
	 *
	 * @param player
	 */
	public static void removePotionEffects(final Player player) {
		for (final PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
	}

	/**
	 * Répare toutes les armures du joueur
	 *
	 * @param player
	 */
	public static void resetArmorDurability(final Player player) {
		for (final ItemStack itemStack : player.getInventory().getArmorContents())
			if (itemStack != null && itemStack.getType() != Material.AIR)
				itemStack.setDurability((short) -1);
	}

	/**
	 * Remet à fond la dura de l'objet en main
	 *
	 * @param player
	 */
	public static void resetItemInHandDurability(final Player player) {
		final ItemStack itemStack = player.getItemInHand();
		if (ItemStackUtils.isValid(itemStack)) {
			itemStack.setDurability((short) 1); // 1 mieux que 0 pour éviter le
												// spam barre pleine
			player.updateInventory(); // Si pas update le client considère
										// l'item endommagée
		}
	}

	/**
	 * Remet le joueur full life/food sans effets de potions
	 *
	 * @param player
	 */
	public static void resetPlayer(final Player player) {
		removePotionEffects(player);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setSaturation(20);
	}

	public static void returnToHub() {

		for (final Player p : Bukkit.getOnlinePlayers())
			try {
				final JSONObject json = new JSONObject();
				json.put("player", p.getName());
				new RabbitMQMessagingClient("returntohub", json);
			} catch (final Exception e) {
				e.printStackTrace();
			}
	}

	public static void returnToHub(final Player p) {

		try {
			Bukkit.getPluginManager().callEvent(new ReturnToLobbyEvent(p));
			if (BukkitAPI.get().getServerInfos().getGameName().equals(Games.LOBBY.getDisplayName()))
				return;
			final JSONObject json = new JSONObject();
			json.put("player", p.getName());
			new RabbitMQMessagingClient("returntohub", json);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendActionMessage(final Player p, final String msg) {
		final CraftPlayer cp = (CraftPlayer) p;
		if (ProtocolLibrary.getProtocolManager().getProtocolVersion(p) < 47)
			return;
		final IChatBaseComponent message = ChatSerializer.a("{\"text\": \"" + msg + "\"}");
		final PacketPlayOutChat packet = new PacketPlayOutChat(message, (byte) 2);
		cp.getHandle().playerConnection.sendPacket(packet);

	}

	/**
	 * Force le respawn du joueur au bout de x ticks
	 *
	 * @param player
	 */
	public static void sendForceRespawn(final Player player, final int ticks) {
		final String playerName = player.getName();
		Bukkit.getScheduler().runTaskLater(BukkitAPI.get(), () -> {
			final Player player1 = Bukkit.getPlayer(playerName);
			if (player1 != null && player1.isDead() && player1.isOnline()) {
				final PacketPlayInClientCommand packet = new PacketPlayInClientCommand(
						EnumClientCommand.PERFORM_RESPAWN);
				final EntityPlayer ep = ((CraftPlayer) player1).getHandle();
				if (ep.playerConnection != null && !ep.playerConnection.isDisconnected())
					ep.playerConnection.a(packet);
			}
		}, ticks);
	}

	/**
	 * Active ou non la collision entre le joueur est les autres entitées
	 *
	 * @param player
	 * @param value
	 */
	public static void setEntityCollision(final Player player, final Boolean value) {
		((CraftPlayer) player).getHandle().collidesWithEntities = value;
	}

	/**
	 * Force le nom de l'entité qui aura attaqué en dernier cette entité
	 *
	 * @param entity
	 * @return
	 */
	public static void setLastDamagerName(final Entity entity, final String lastDamagerName) {
		Flags.setStringFlag(entity, Flags.lastDamager, lastDamagerName);
	}

}
