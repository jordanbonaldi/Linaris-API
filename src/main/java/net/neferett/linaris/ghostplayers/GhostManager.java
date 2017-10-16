package net.neferett.linaris.ghostplayers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.utils.LoggerUtils;
import net.neferett.linaris.utils.Reflector;
import net.neferett.linaris.utils.ServerUtils;

import com.comphenix.protocol.wrappers.PlayerInfoData;

public class GhostManager {
	private static Map<String, Player> ghostPlayers;
	private static Set<String> waitedGhostPlayers;
	private static List<CraftPlayer> fakePlayerView;
	private static List<CraftPlayer> playerView;
	private static String prefix;
	public static boolean enabled;
	private static boolean init;

	public static void init() {
		if (!GhostManager.enabled) {
			return;
		}
		if (GhostManager.init) {
			return;
		}
		GhostManager.init = true;
		final CraftServer cs = (CraftServer) Bukkit.getServer();
		new GhostPluginManager((Server) cs, cs.getCommandMap());
		GhostManager.fakePlayerView = new ArrayList<CraftPlayer>();
		GhostManager.playerView = Collections
				.unmodifiableList((List<? extends CraftPlayer>) GhostManager.fakePlayerView);
		final Field fPlayerView = Reflector.getField(CraftServer.class, "playerView");
		try {
			Reflector.setFieldFinalModifiable(fPlayerView);
			fPlayerView.set(cs, GhostManager.playerView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final PluginManager pm = BukkitAPI.get().getServer().getPluginManager();
		pm.registerEvents((Listener) new JoinAndQuitListener(), BukkitAPI.get());
		
		ProtocolLibrary.getProtocolManager()
				.addPacketListener(new PacketAdapter(BukkitAPI.get(), ListenerPriority.HIGHEST, PacketType.Play.Server.PLAYER_INFO) {

					@Override
					public void onPacketSending(PacketEvent event) {
						if (GhostManager.ghostPlayers.size() > 0) {
							final WrapperPlayServerPlayerInfo p = new WrapperPlayServerPlayerInfo(event.getPacket());
							
							if (p.getAction() == PlayerInfoAction.ADD_PLAYER) {
								final Player ep = event.getPlayer();
								if (ep == null) {
									return;
								}
								LoggerUtils.info("Name: " + ep.getAddress());
								if (GhostManager.ghostPlayers.get(ep.getName()) != null) {
									return;
								}
//								
								List<PlayerInfoData> it = new ArrayList<>();
								
								final Iterator<PlayerInfoData> ite = p.getData().iterator();
								while (ite.hasNext()) {
									PlayerInfoData pid = ite.next();
									if (pid.getProfile().getName() == null || GhostManager.ghostPlayers.get(pid.getProfile().getName().toLowerCase()) == null) {
										it.add(pid);
									}
								}
								p.setData(it);
								for (PlayerInfoData pid : it) {
									LoggerUtils.debug(pid.getProfile().getName());
								}
							}
						}
					}

				});

	}

	public static void addWaitedGhostPlayer(final String uuid) {
		if (GhostManager.waitedGhostPlayers.add(uuid.toLowerCase())) {
			ServerUtils.changeMaxPlayers(Bukkit.getMaxPlayers() + 1);
			LoggerUtils.info(GhostManager.prefix + "Adding new waited ghost player > " + uuid.toString());
		}
	}

	public static boolean isGhost(final Player player) {
		return player != null && (GhostManager.ghostPlayers.get(player.getName().toLowerCase()) != null
				|| GhostManager.waitedGhostPlayers.contains(player.getName().toLowerCase()));
	}
	
	public static boolean isGhost(final String player) {
		return player != null && (GhostManager.ghostPlayers.get(player.toLowerCase()) != null
				|| GhostManager.waitedGhostPlayers.contains(player.toLowerCase()));
	}

	public static int getGhostCount() {
		return GhostManager.ghostPlayers.size();
	}

	public static Collection<Player> getAllGhostPlayers() {
		return GhostManager.ghostPlayers.values();
	}

	public static void addGhost(final Player player) {
		if (GhostManager.waitedGhostPlayers.contains(player.getName().toLowerCase())) {
			GhostManager.waitedGhostPlayers.remove(player.getName().toLowerCase());
			GhostManager.ghostPlayers.put(player.getName().toLowerCase(), player);
			player.addAttachment(BukkitAPI.get(), "minecraft.command.tp", true);
			player.setGameMode(GameMode.SPECTATOR);
			GhostManager.fakePlayerView.remove(player);
			LoggerUtils.info(GhostManager.prefix + "GhostPlayer Join > " + player.getName());
		} else {
			LoggerUtils.warning("§cThis function is for ghostplayers only !");
		}
	}

	public static void removeGhost(final Player player) {
		if (isGhost(player)) {
			player.setGameMode(GameMode.SURVIVAL);
			ServerUtils.changeMaxPlayers(Bukkit.getMaxPlayers() - 1);
			GhostManager.ghostPlayers.remove(player.getName().toLowerCase());
			LoggerUtils.info(GhostManager.prefix + "GhostPlayer Quit > " + player.getName());
			BukkitAPI.get().getPlayerDataManager().unload(player.getName().toLowerCase());
		} else {
			LoggerUtils.warning("§cThis function is for ghostplayers only !");
		}
	}

	static {
		GhostManager.ghostPlayers = new HashMap<String, Player>();
		GhostManager.waitedGhostPlayers = new TreeSet<String>();
		GhostManager.prefix = "[GHOST] ";
		GhostManager.enabled = true;
		GhostManager.init = false;
	}

	private static class JoinAndQuitListener implements Listener {
		@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
		public void onPlayerLogin(final PlayerLoginEvent event) {
			if (event.getResult().equals((Object) PlayerLoginEvent.Result.ALLOWED)) {
				GhostManager.fakePlayerView.add(((CraftPlayer) event.getPlayer()));
			}
		}

		@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
		public void onPlayerQuit(final PlayerQuitEvent event) {
			final Player player = event.getPlayer();
			GhostManager.fakePlayerView.remove(player);
		}
	}
}
