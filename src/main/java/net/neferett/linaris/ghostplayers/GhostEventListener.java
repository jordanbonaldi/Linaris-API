package net.neferett.linaris.ghostplayers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.world.StructureGrowEvent;

import net.neferett.linaris.events.MultiConnectionEvent;
import net.neferett.linaris.events.SoloConnectionEvent;
import net.neferett.linaris.ghostplayers.events.ActionOnEvent;
import net.neferett.linaris.ghostplayers.events.ActionOnInventoryClose;
import net.neferett.linaris.ghostplayers.events.ActionOnInventoryInteract;
import net.neferett.linaris.ghostplayers.events.ActionOnInventoryOpen;
import net.neferett.linaris.ghostplayers.events.ActionOnMultiConnection;
import net.neferett.linaris.ghostplayers.events.ActionOnPlayerDie;
import net.neferett.linaris.ghostplayers.events.ActionOnPlayerEvent;
import net.neferett.linaris.ghostplayers.events.ActionOnPlayerJoin;
import net.neferett.linaris.ghostplayers.events.ActionOnPlayerKick;
import net.neferett.linaris.ghostplayers.events.ActionOnPlayerLogin;
import net.neferett.linaris.ghostplayers.events.ActionOnPlayerQuit;
import net.neferett.linaris.ghostplayers.events.ActionOnPlayerRespawn;
import net.neferett.linaris.ghostplayers.events.ActionOnSoloConnection;
import net.neferett.linaris.ghostplayers.events.ActionOnStructureGrow;

@SuppressWarnings("deprecation")
public class GhostEventListener {
	public enum GhostEventHandler {
		ACHIEVEMENT(PlayerAchievementAwardedEvent.class, new ActionOnPlayerEvent()),
		ANIMATION(PlayerAnimationEvent.class, new ActionOnPlayerEvent()),
		CRAFT_ITEM(CraftItemEvent.class, new ActionOnInventoryInteract()),
		INVENTORY_CLICK(InventoryClickEvent.class, new ActionOnInventoryInteract()),
		INVENTORY_CLOSE(InventoryCloseEvent.class, new ActionOnInventoryClose()),
		INVENTORY_CREATIVE(InventoryCreativeEvent.class, new ActionOnInventoryInteract()),
		INVENTORY_DRAG(InventoryDragEvent.class, new ActionOnInventoryInteract()),
		INVENTORY_INTERACT(InventoryInteractEvent.class, new ActionOnInventoryInteract()),
		INVENTORY_OPEN(InventoryOpenEvent.class, new ActionOnInventoryOpen()),
		MULTI_CONNECTION(MultiConnectionEvent.class, new ActionOnMultiConnection()),
		PLAYER_ARMORSTAND_MANIPULATE(PlayerArmorStandManipulateEvent.class, new ActionOnPlayerEvent()),
		PLAYER_BED_ENTER(PlayerBedLeaveEvent.class, new ActionOnPlayerEvent()),
		PLAYER_BED_EXIT(PlayerBedEnterEvent.class, new ActionOnPlayerEvent()),
		PLAYER_CHAT(AsyncPlayerChatEvent.class, new ActionOnPlayerEvent()),
		PLAYER_CHAT_SYNC(PlayerChatEvent.class, new ActionOnPlayerEvent()),
		PLAYER_DIE(PlayerDeathEvent.class, new ActionOnPlayerDie()),
		PLAYER_FLY(PlayerToggleFlightEvent.class, new ActionOnPlayerEvent()),
		PLAYER_GAMEMODE(PlayerGameModeChangeEvent.class, new ActionOnPlayerEvent()),
		PLAYER_INTERACT(PlayerInteractEvent.class, new ActionOnPlayerEvent()),
		PLAYER_INTERACT_AT_ENTITY(PlayerInteractAtEntityEvent.class, new ActionOnPlayerEvent()),
		PLAYER_INTERACT_ENTITY(PlayerInteractEntityEvent.class, new ActionOnPlayerEvent()),
		PLAYER_INVENTORY(PlayerInventoryEvent.class, new ActionOnPlayerEvent()),
		PLAYER_ITEM_HELD(PlayerItemHeldEvent.class, new ActionOnPlayerEvent()),
		PLAYER_JOIN(PlayerJoinEvent.class, new ActionOnPlayerJoin()),
		PLAYER_KICK(PlayerKickEvent.class, new ActionOnPlayerKick()),
		PLAYER_LOGIN(PlayerLoginEvent.class, new ActionOnPlayerLogin()),
		PLAYER_MOVE(PlayerMoveEvent.class, new ActionOnPlayerEvent()),
		PLAYER_PORTAL(PlayerPortalEvent.class, new ActionOnPlayerEvent()),
		PLAYER_QUIT(PlayerQuitEvent.class, new ActionOnPlayerQuit()),
		PLAYER_RESPAWN(PlayerRespawnEvent.class, new ActionOnPlayerRespawn()),
		PLAYER_SNEAK(PlayerToggleSneakEvent.class, new ActionOnPlayerEvent()),
		PLAYER_SPRINT(PlayerToggleSprintEvent.class, new ActionOnPlayerEvent()),
		PLAYER_TELEPORT(PlayerTeleportEvent.class, new ActionOnPlayerEvent()),
		PLAYER_VELOCITY(PlayerVelocityEvent.class, new ActionOnPlayerEvent()),
		SOLO_CONNECTION(SoloConnectionEvent.class, new ActionOnSoloConnection()),
		STRUCTURE_GROW(StructureGrowEvent.class, new ActionOnStructureGrow()),
		WORLD_CHANGE(PlayerChangedWorldEvent.class, new ActionOnPlayerEvent());
		;

		private static Map<Class<? extends Event>, GhostEventHandler> mapping;
		static {
			GhostEventHandler.mapping = new HashMap<>();
			for (final GhostEventHandler evenHandler : values())
				GhostEventHandler.mapping.put(evenHandler.type, evenHandler);
		}

		public static GhostEventHandler getGhostEventHandler(final Event event) {
			return GhostEventHandler.mapping.get(event.getClass());
		}

		private final ActionOnEvent				action;

		private final Class<? extends Event>	type;

		private GhostEventHandler(final Class<? extends Event> type, final ActionOnEvent action) {
			this.action = action;
			this.type = type;
		}

		public ActionOnEvent getAction() {
			return this.action;
		}
	}

	public static boolean shouldFire(final Event event) {
		final GhostEventHandler geh = GhostEventHandler.getGhostEventHandler(event);
		return geh == null || geh.getAction().shouldFire(event);
	}
}
