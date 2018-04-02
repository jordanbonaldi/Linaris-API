package net.neferett.linaris.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import lombok.Getter;
import lombok.Setter;
import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.utils.ScoreBoardModule;

public abstract class API extends JavaPlugin {

	static API instance;

	public static API getInstance() {
		return instance;
	}

	private boolean			annonce;
	private final String	game;
	@Getter
	@Setter
	private GameMode		gamemode;
	private boolean			handleranks;
	private ServerInfo		info;
	private final String	mapname;
	private final int		maxplayer;

	protected World			w;

	public API(final String game, final String mapname, final int maxplayer) {
		instance = this;
		this.annonce = false;
		this.handleranks = false;
		this.game = game;
		this.mapname = mapname;
		this.maxplayer = maxplayer;
	}

	public void addHealthNameTag() {
		final ScoreboardManager manager = Bukkit.getScoreboardManager();
		final Scoreboard sb = manager.getMainScoreboard();

		final Objective objective21 = sb.registerNewObjective("healthabove", "health");
		objective21.setDisplaySlot(DisplaySlot.BELOW_NAME);
		objective21.setDisplayName("§c❤");
	}

	public abstract void addRanks();

	public void closeServer() {
		this.info.setCanJoin(false, false);
		this.info.setCanSee(false, false);
	}

	public String getGame() {
		return this.game;
	}

	public BukkitAPI getGeneralInstance() {
		return BukkitAPI.get();
	}

	public boolean getHandleRank() {
		return this.handleranks;
	}

	public ServerInfo getInfo() {
		return this.info;
	}

	public void handleWorld() {
		this.w = Bukkit.getWorld("world");
		this.w.setGameRuleValue("doDaylightCycle", "false");
		this.w.setGameRuleValue("doFireTick", "false");
		this.w.setTime(6000);
	}

	public boolean isAnnonce() {
		return this.annonce;
	}

	public abstract void onClose();

	@Override
	public void onDisable() {
		this.onClose();
	}

	@Override
	public void onEnable() {
		this.addRanks();
		this.RegisterCommands();
		this.onOpen();
	}

	@Override
	public void onLoad() {
		this.info = BukkitAPI.get().getServerInfos();
		this.info.setGameName(this.game);
		this.info.setMapName(this.mapname);
		BukkitAPI.get().setMaxPlayers(this.maxplayer);
		this.onLoading();
	}

	public abstract void onLoading();

	public abstract void onOpen();

	public void openServer() {
		this.info.setCanJoin(true, true);
		this.info.setCanSee(true, true);
	}

	public void RegisterAllEvents(final Listener... events) {
		Arrays.asList(events).forEach((e) -> {
			BukkitAPI.get().getServer().getPluginManager().registerEvents(e, BukkitAPI.get());
		});
	}

	public abstract void RegisterCommands();

	public void setAnnounce() {
		this.annonce = true;
	}

	public void setAPIMode(final boolean a) {
		BukkitAPI.get().setApi(a);
	}

	public void setGameName(final String name) {
		this.info.setGameName(name);
	}

	public void sethandleRank() {
		this.handleranks = true;
	}

	public void setScoreBoard(final Class<? extends ScoreBoardModule> clazz) {
		try {
			@SuppressWarnings("rawtypes")
			final Constructor constuctor = clazz.getDeclaredConstructor(BukkitAPI.class);
			constuctor.newInstance(BukkitAPI.get());
		} catch (final NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
