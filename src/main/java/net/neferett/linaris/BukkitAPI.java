package net.neferett.linaris;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;

import net.neferett.socket.api.FastSendMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Zombie;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.neferett.linaris.PlayersHandler.PlayerManager;
import net.neferett.linaris.api.FriendsManagement;
import net.neferett.linaris.api.Games;
import net.neferett.linaris.api.PlayerDataManager;
import net.neferett.linaris.api.PlayerLocalManager;
import net.neferett.linaris.api.ServerInfo;
import net.neferett.linaris.api.StatsManager;
import net.neferett.linaris.api.pets.PetListener;
import net.neferett.linaris.api.ranks.RankManager;
import net.neferett.linaris.api.server.ProxyDataManager;
import net.neferett.linaris.api.server.RPCServersManager;
import net.neferett.linaris.commands.ColorCommand;
import net.neferett.linaris.commands.FreezeCommand;
import net.neferett.linaris.commands.HubCommand;
import net.neferett.linaris.commands.InvseeCommand;
import net.neferett.linaris.commands.LagCommand;
import net.neferett.linaris.commands.LogoCommand;
import net.neferett.linaris.commands.RankCommand;
import net.neferett.linaris.commands.RebootCommand;
import net.neferett.linaris.commands.SendSignalCommand;
import net.neferett.linaris.commands.TPCommands;
import net.neferett.linaris.commands.TestCommand;
import net.neferett.linaris.commands.VanishCommand;
import net.neferett.linaris.customevents.callers.ArrowHitBlockCaller;
import net.neferett.linaris.customevents.listeners.AnvilItemRenameListener;
import net.neferett.linaris.db.SingleDatabaseConnector;
import net.neferett.linaris.disguises.DisguiseListener;
import net.neferett.linaris.disguises.disguisetypes.DisguiseType;
import net.neferett.linaris.disguises.disguisetypes.FlagWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.AgeableWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.GuardianWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.LivingWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.MinecartWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.SlimeWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.TameableWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.ZombieWatcher;
import net.neferett.linaris.disguises.utilities.DisguiseSound;
import net.neferett.linaris.disguises.utilities.DisguiseUtilities;
import net.neferett.linaris.disguises.utilities.DisguiseValues;
import net.neferett.linaris.disguises.utilities.FakeBoundingBox;
import net.neferett.linaris.disguises.utilities.PacketsManager;
import net.neferett.linaris.disguises.utilities.ReflectionManager;
import net.neferett.linaris.ghostplayers.GhostManager;
import net.neferett.linaris.listeners.InventorySpectatorClick;
import net.neferett.linaris.listeners.NewDamagerListener;
import net.neferett.linaris.listeners.PlayerChatEvent;
import net.neferett.linaris.listeners.PlayerConnectionListener;
import net.neferett.linaris.listeners.PlayerListener;
import net.neferett.linaris.mistery.MysteryItemsManager;
import net.neferett.linaris.rabbitmq.PlayerEffectMessaging;
import net.neferett.linaris.rabbitmq.ServerManagersClient;
import net.neferett.linaris.rabbitmq.messaging.RabbitMQMessagingClient;
import net.neferett.linaris.rabbitmq.utils.RabbitMQUtils;
import net.neferett.linaris.spectator.GamePosHider;
import net.neferett.linaris.spectator.SpectatorManager;
import net.neferett.linaris.utils.LoggerUtils;
import net.neferett.linaris.utils.RedisServer;
import net.neferett.linaris.utils.TasksExecutor;
import net.neferett.linaris.utils.json.JSONObject;
import net.neferett.linaris.utils.tasksmanager.TaskManager;
import redis.clients.jedis.Jedis;

public class BukkitAPI extends JavaPlugin {

	private static BukkitAPI	instance;
	public static String		ip;

	public static BukkitAPI get() {
		return instance;
	}

	private boolean											api;
	private List<Predicate<PlayerCommandPreprocessEvent>>	commandProcess;
	private SingleDatabaseConnector							connector;
	private FriendsManagement								friendsManager;
	private DisguiseListener								listener;
	private int												maxPlayers;
	private PlayerDataManager								playerDataManager;
	private PlayerLocalManager								playerLocalManager;
	private PlayerManager									pm;
	private ProxyDataManager								proxyDataManager;
	private RankManager										rm;
	private boolean											scoreload;

	private ServerInfo										serverInfos;

	private boolean											serverRegistered;

	private StatsManager									statsManager;

	private TasksExecutor									tasksManager;

	public void addProcessPredicate(final Predicate<PlayerCommandPreprocessEvent> a) {
		this.commandProcess.add(a);
	}

	public void enableDisguise() {
		PacketsManager.init(this);
		DisguiseUtilities.init(this);

		PacketsManager.addPacketListeners();
		this.listener = new DisguiseListener(this);
		Bukkit.getPluginManager().registerEvents(this.listener, this);

		this.registerValues();
		instance = this;
		PacketsManager.setViewDisguisesListener(true);
	}

	public List<Predicate<PlayerCommandPreprocessEvent>> getCommandProcess() {
		return this.commandProcess;
	}

	public SingleDatabaseConnector getConnector() {
		return this.connector;
	}

	public FriendsManagement getFriendsManager() {
		return this.friendsManager;
	}

	public DisguiseListener getListener() {
		return this.listener;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public List<String> getOnlineNames() {
		final List<String> names = new ArrayList<>();
		for (final Player pp : this.getServer().getOnlinePlayers())
			names.add(pp.getName());
		return names;

	}

	public PlayerDataManager getPlayerDataManager() {
		return this.playerDataManager;
	}

	public PlayerLocalManager getPlayerLocalManager() {
		return this.playerLocalManager;
	}

	public PlayerManager getPlayerManager() {
		return this.pm;
	}

	public ProxyDataManager getProxyDataManager() {
		return this.proxyDataManager;
	}

	public RankManager getRankManager() {
		return this.rm;
	}

	public ServerInfo getServerInfos() {
		return this.serverInfos;
	}

	public String getServName() {
		return Games.getIDByDisplayName(this.getServerInfos().getGameName()) + Arrays
				.asList(this.getDataFolder().getAbsolutePath().replace(this.getDataFolder().getPath(), "").split("/"))
				.get(4);
	}

	public StatsManager getStatsManager() {
		return this.statsManager;
	}

	public TasksExecutor getTasksManager() {
		return this.tasksManager;
	}

	public void heartbeat() {
		try {
			final String bungeename = this.getServerInfos().getServerName();
			final Jedis rb_jedis = this.getConnector().getBungeeResource();
			rb_jedis.hset("servers", bungeename,
					ip + ":" + this.getServer().getPort() + ":" + this.getServerInfos().getGameName() + ":"
							+ this.getServerInfos().getMapName() + ":" + this.getMaxPlayers() + ":"
							+ Bukkit.getOnlinePlayers().size() + ":" + this.getServerInfos().canJoin() + ":"
							+ this.getServerInfos().canSee());
			rb_jedis.close();

			final JSONObject object = new JSONObject();
			object.put("type", "heartbeat");
			object.put("servName", this.getServerInfos().getServerName());
			object.put("gameName", this.getServerInfos().getGameName());
			object.put("mapName", this.getServerInfos().getMapName());
			object.put("players", String.valueOf(Bukkit.getOnlinePlayers().size()));
			object.put("maxPlayers", String.valueOf(this.getMaxPlayers()));
			object.put("canJoin", String.valueOf(this.getServerInfos().canJoin()));
			object.put("canSee", String.valueOf(this.getServerInfos().canSee()));
			object.put("ip", ip);
			object.put("port", String.valueOf(this.getServer().getPort()));
			new RabbitMQMessagingClient("servers", object);
			final Jedis jedis = this.getConnector().getBungeeResource();
			try {
				for (final Player player : Bukkit.getOnlinePlayers())
					jedis.sadd("connectedonserv:" + this.getServerInfos().getServerName(),
							player.getUniqueId().toString());
				jedis.close();
			} catch (final Exception ignored) {}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void initServer() {
		this.getTasksManager().addTask(() -> {
			try {
				new ServerManagersClient();

				this.registerServer();

				new RPCServersManager(this.getServerInfos().getServerName());
				new PlayerEffectMessaging();

				this.rm = new RankManager();


			} catch (final Exception e) {
				e.printStackTrace();
				this.reboot();
			}
		});

	}

	public boolean isApi() {
		return this.api;
	}

	public boolean isScoreload() {
		return this.scoreload;
	}

	public void load() {
		this.api = false;
		this.scoreload = false;

		this.serverInfos = new ServerInfo();

		// connector = RedisDatabase.create(new Credentials(RedisServer.host,
		// RedisServer.auth, 0));
		// connector.setup();

		this.connector = new SingleDatabaseConnector(this, RedisServer.host + ":" + RedisServer.port, System.getenv("REDIS_PASSWORD"));

		this.tasksManager = new TasksExecutor();
		new Thread(this.tasksManager, "ExecutorThread").start();

		RabbitMQUtils.inits();

		this.playerLocalManager = new PlayerLocalManager();
		this.playerDataManager = new PlayerDataManager(this);
		this.friendsManager = new FriendsManagement(this);
		this.statsManager = new StatsManager(this);

	}

	@Override
	public void onDisable() {

		try {
			final JSONObject object = new JSONObject();
			object.put("type", "stop");
			object.put("servName", this.getServerInfos().getServerName());
			new FastSendMessage("127.0.0.1", 12000, "stop " + this.getDataFolder()
					.getAbsolutePath().replace(this.getDataFolder().getPath(), "\n")).build();
			new RabbitMQMessagingClient("servers", object);
			RabbitMQUtils.getConnection().close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEnable() {
		this.initServer();

		this.scheduleAutoReboot();

		this.commandProcess = new ArrayList<>();

		this.getServer().getPluginManager().registerEvents(new PlayerChatEvent(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new AnvilItemRenameListener(), this);
		this.getServer().getPluginManager().registerEvents(new PetListener(), this);
		this.getServer().getPluginManager().registerEvents(new GamePosHider(), this);
//		this.getCommand("sendsignal").setExecutor(new SendSignalCommand("149.202.65.5", 17005));

		this.getServer().getPluginManager().registerEvents(new PlayerConnectionListener(), this);

		ArrowHitBlockCaller.register();
		this.getCommand("lag").setExecutor(new LagCommand());
		this.getCommand("reboot").setExecutor(new RebootCommand());
		final HubCommand hubCommand = new HubCommand();
		this.getCommand("hub").setExecutor(hubCommand);
		this.getCommand("lobby").setExecutor(hubCommand);
		this.getCommand("test").setExecutor(new TestCommand());
		new VanishCommand();
		new TPCommands();
		new ColorCommand();
		new LogoCommand();
		new InvseeCommand();
		new FreezeCommand();
		new RankCommand();
		this.pm = new PlayerManager();

		this.enableDisguise();

		SpectatorManager.init();

		GhostManager.init();

		MysteryItemsManager.getInstance().inits();

		ProtocolLibrary.getProtocolManager().addPacketListener(new NewDamagerListener(this));
		ProtocolLibrary.getProtocolManager().addPacketListener(new InventorySpectatorClick(this));

		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "WDL|CONTROL");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "WDL|INIT", (s, player, bytes) -> {
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeInt(1);
			out.writeBoolean(false);
			out.writeInt(1);
			out.writeBoolean(false);
			out.writeBoolean(false);
			out.writeBoolean(false);
			out.writeBoolean(false);
			Bukkit.getLogger().info("Blocking WorldDownloader for " + player.getDisplayName());
			player.sendPluginMessage(this, "WDL|CONTROL", out.toByteArray());
		});
		LoggerUtils.info("ServerID: " + Bukkit.getServerName());
		this.proxyDataManager = new ProxyDataManager(this);
		System.out.println("[GameServer] Loaded");
		// ProtocolLibrary.getProtocolManager().addPacketListener(new
		// AntiFlyPacket(this));
		// ProtocolLibrary.getProtocolManager().addPacketListener(new
		// AntiFlyPacketServer(this));
		// //ProtocolLibrary.getProtocolManager().addPacketListener(new
		// AntiSpeedHackPacket(this));

		this.heartbeat();

	}

	@Override
	public void onLoad() {
		this.api = false;
		instance = this;

		this.getConfig().options().copyDefaults(true);
		this.saveConfig();

		if (this.getConfig().contains("ip"))
			ip = this.getConfig().getString("ip");
		else
			ip = "127.0.0.1";
		this.maxPlayers = Bukkit.getMaxPlayers();
		this.load();
	}

	public void reboot() {
		this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "reboot");
	}

	public void registerServer() {
		if (this.serverRegistered)
			return;

		LoggerUtils.info("Trying to register server to the proxy");
		this.heartbeat();

		TaskManager.scheduleSyncRepeatingTask("heart", this::heartbeat, 0, this.getConfig().getInt("refresh"));

		this.serverRegistered = true;
	}

	@SuppressWarnings("unchecked")
	private void registerValues() {
		for (final DisguiseType disguiseType : DisguiseType.values()) {
			if (disguiseType.getEntityType() == null)
				continue;
			@SuppressWarnings("rawtypes")
			Class watcherClass;
			try {
				switch (disguiseType) {
					case MINECART_CHEST:
					case MINECART_COMMAND:
					case MINECART_FURNACE:
					case MINECART_HOPPER:
					case MINECART_MOB_SPAWNER:
					case MINECART_TNT:
						watcherClass = MinecartWatcher.class;
						break;
					case DONKEY:
					case MULE:
					case UNDEAD_HORSE:
					case ZOMBIE_VILLAGER:
					case PIG_ZOMBIE:
						watcherClass = ZombieWatcher.class;
						break;
					case MAGMA_CUBE:
						watcherClass = SlimeWatcher.class;
						break;
					case ELDER_GUARDIAN:
						watcherClass = GuardianWatcher.class;
						break;
					case ENDERMITE:
						watcherClass = LivingWatcher.class;
						break;
					default:
						watcherClass = Class.forName("net.neferett.linaris.disguise.disguisetypes.watchers."
								+ this.toReadable(disguiseType.name()) + "Watcher");
						break;
				}
			} catch (final ClassNotFoundException ex) {
				// There is no explicit watcher for this entity.
				@SuppressWarnings("rawtypes")
				final Class entityClass = disguiseType.getEntityType().getEntityClass();
				if (entityClass != null) {
					if (Tameable.class.isAssignableFrom(entityClass))
						watcherClass = TameableWatcher.class;
					else if (Ageable.class.isAssignableFrom(entityClass))
						watcherClass = AgeableWatcher.class;
					else if (LivingEntity.class.isAssignableFrom(entityClass))
						watcherClass = LivingWatcher.class;
					else
						watcherClass = FlagWatcher.class;
				} else
					watcherClass = FlagWatcher.class; // Disguise is unknown
														// type
			}
			disguiseType.setWatcherClass(watcherClass);
			if (DisguiseValues.getDisguiseValues(disguiseType) != null)
				continue;
			String nmsEntityName = this.toReadable(disguiseType.name());
			switch (disguiseType) {
				case WITHER_SKELETON:
				case ZOMBIE_VILLAGER:
				case DONKEY:
				case MULE:
				case UNDEAD_HORSE:
				case SKELETON_HORSE:
					continue;
				case PRIMED_TNT:
					nmsEntityName = "TNTPrimed";
					break;
				case MINECART_TNT:
					nmsEntityName = "MinecartTNT";
					break;
				case MINECART:
					nmsEntityName = "MinecartRideable";
					break;
				case FIREWORK:
					nmsEntityName = "Fireworks";
					break;
				case SPLASH_POTION:
					nmsEntityName = "Potion";
					break;
				case GIANT:
					nmsEntityName = "GiantZombie";
					break;
				case DROPPED_ITEM:
					nmsEntityName = "Item";
					break;
				case FIREBALL:
					nmsEntityName = "LargeFireball";
					break;
				case LEASH_HITCH:
					nmsEntityName = "Leash";
					break;
				case ELDER_GUARDIAN:
					nmsEntityName = "Guardian";
					break;
				default:
					break;
			}
			try {
				if (nmsEntityName.equalsIgnoreCase("Unknown")) {
					final DisguiseValues disguiseValues = new DisguiseValues(disguiseType, null, 0, 0);
					disguiseValues.setAdultBox(new FakeBoundingBox(0, 0, 0));
					final DisguiseSound sound = DisguiseSound.getType(disguiseType.name());
					if (sound != null)
						sound.setDamageAndIdleSoundVolume(1f);
					continue;
				}
				final Object nmsEntity = ReflectionManager.createEntityInstance(nmsEntityName);
				if (nmsEntity == null)
					continue;
				final Entity bukkitEntity = ReflectionManager.getBukkitEntity(nmsEntity);
				int entitySize = 0;
				for (final Field field : ReflectionManager.getNmsClass("Entity").getFields())
					if (field.getType().getName().equals("EnumEntitySize")) {
						@SuppressWarnings("rawtypes")
						final Enum enumEntitySize = (Enum) field.get(nmsEntity);
						entitySize = enumEntitySize.ordinal();
						break;
					}
				final DisguiseValues disguiseValues = new DisguiseValues(disguiseType, nmsEntity.getClass(), entitySize,
						bukkitEntity instanceof Damageable ? ((Damageable) bukkitEntity).getMaxHealth() : 0);
				for (final WrappedWatchableObject watch : WrappedDataWatcher.getEntityWatcher(bukkitEntity)
						.getWatchableObjects())
					disguiseValues.setMetaValue(watch.getIndex(), watch.getValue());
				// Uncomment when I need to find the new datawatcher values
				// for a class..
				final DisguiseSound sound = DisguiseSound.getType(disguiseType.name());
				if (sound != null) {
					final Float soundStrength = ReflectionManager.getSoundModifier(nmsEntity);
					if (soundStrength != null)
						sound.setDamageAndIdleSoundVolume(soundStrength);
				}

				// Get the bounding box
				disguiseValues.setAdultBox(ReflectionManager.getBoundingBox(bukkitEntity));
				if (bukkitEntity instanceof Ageable) {
					((Ageable) bukkitEntity).setBaby();
					disguiseValues.setBabyBox(ReflectionManager.getBoundingBox(bukkitEntity));
				} else if (bukkitEntity instanceof Zombie) {
					((Zombie) bukkitEntity).setBaby(true);
					disguiseValues.setBabyBox(ReflectionManager.getBoundingBox(bukkitEntity));
				}
				disguiseValues.setEntitySize(ReflectionManager.getSize(bukkitEntity));
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException | FieldAccessException ex) {
				System.out.print("[LibsDisguises] Uh oh! Trouble while making values for the disguise "
						+ disguiseType.name() + "!");
				System.out.print("[LibsDisguises] Before reporting this error, "
						+ "please make sure you are using the latest version of LibsDisguises and ProtocolLib.");
				if (ReflectionManager.isForge())
					System.out.print("[LibsDisguises] Development builds are available at (ProtocolLib) "
							+ "http://assets.comphenix.net/job/ProtocolLib%20-%20Cauldron/ and (LibsDisguises) http://ci.md-5.net/job/LibsDisguises/");
				else
					System.out.print("[LibsDisguises] Development builds are available at (ProtocolLib) "
							+ "http://assets.comphenix.net/job/ProtocolLib/ and (LibsDisguises) http://ci.md-5.net/job/LibsDisguises/");

				ex.printStackTrace(System.out);
			}
		}
	}

	/**
	 * Reloads the config with new config options.
	 */
	public void reload() {
		HandlerList.unregisterAll(this.listener);
		this.reloadConfig();
	}

	public void scheduleAutoReboot() {
		TaskManager.scheduler.runTaskAsynchronously(instance, () -> {
			final int rebootHour = 5;
			final int rebootMinute = 0;
			final int rebootSeconds = 0;

			final Calendar now = Calendar.getInstance();

			final Calendar nextReboot = (Calendar) now.clone();
			nextReboot.set(Calendar.HOUR_OF_DAY, rebootHour);
			nextReboot.set(Calendar.MINUTE, rebootMinute);
			nextReboot.set(Calendar.SECOND, rebootSeconds);

			final long diff = nextReboot.getTimeInMillis() - now.getTimeInMillis();

			System.out.println("Now = " + now.getTime().toString());

			if (diff <= 0)
				nextReboot.add(Calendar.DAY_OF_YEAR, 1);

			System.out.println("Reboot Scheduled = " + nextReboot.getTime().toString());

			final Timer timer = new Timer();
			now.getTime();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Bukkit.getScheduler().runTask(instance, () -> {
						BukkitAPI.this.reboot();
						timer.cancel();
					});
				}
			}, nextReboot.getTime());
		});
	}

	public void setApi(final boolean api) {
		this.api = api;
	}

	public void setMaxPlayers(final int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public void setScoreload(final boolean scoreload) {
		this.scoreload = scoreload;
	}

	private String toReadable(final String string) {
		final StringBuilder builder = new StringBuilder();
		for (final String s : string.split("_"))
			builder.append(s.substring(0, 1)).append(s.substring(1).toLowerCase());
		return builder.toString();
	}

}
