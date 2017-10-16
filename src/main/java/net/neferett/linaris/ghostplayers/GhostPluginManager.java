package net.neferett.linaris.ghostplayers;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.TimedRegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.util.FileUtil;

import com.google.common.collect.ImmutableSet;

import net.neferett.linaris.utils.LoggerUtils;
import net.neferett.linaris.utils.Reflector;

public final class GhostPluginManager implements PluginManager {
	private static File updateDirectory;
	static {
		GhostPluginManager.updateDirectory = null;
	}
	private SimpleCommandMap commandMap;
	private Map<Boolean, Set<Permission>> defaultPerms;
	private Map<Boolean, Map<Permissible, Boolean>> defSubs;
	private Map<Pattern, PluginLoader> fileAssociations;
	private Map<String, Plugin> lookupNames;
	private Map<String, Permission> permissions;
	private Map<String, Map<Permissible, Boolean>> permSubs;
	private List<Plugin> plugins;
	private final Server server;

	private boolean useTimings;

	@SuppressWarnings("unchecked")
	public GhostPluginManager(final Server instance, final SimpleCommandMap commandMap) {
		this.fileAssociations = null;
		this.plugins = null;
		this.lookupNames = null;
		this.commandMap = null;
		this.permissions = null;
		this.defaultPerms = null;
		this.permSubs = null;
		this.defSubs = null;
		this.useTimings = false;
		this.server = instance;
		this.commandMap = commandMap;
		final PluginManager pm = instance.getPluginManager();
		try {
			Field f = Reflector.getField(SimplePluginManager.class, "fileAssociations");
			Reflector.setFieldFinalModifiable(f);
			this.fileAssociations = (Map<Pattern, PluginLoader>) f.get(pm);
			f = Reflector.getField(SimplePluginManager.class, "plugins");
			Reflector.setFieldFinalModifiable(f);
			this.plugins = (List<Plugin>) f.get(pm);
			f = Reflector.getField(SimplePluginManager.class, "lookupNames");
			Reflector.setFieldFinalModifiable(f);
			this.lookupNames = (Map<String, Plugin>) f.get(pm);
			f = Reflector.getField(SimplePluginManager.class, "permissions");
			Reflector.setFieldFinalModifiable(f);
			this.permissions = (Map<String, Permission>) f.get(pm);
			f = Reflector.getField(SimplePluginManager.class, "defaultPerms");
			Reflector.setFieldFinalModifiable(f);
			this.defaultPerms = (Map<Boolean, Set<Permission>>) f.get(pm);
			f = Reflector.getField(SimplePluginManager.class, "permSubs");
			Reflector.setFieldFinalModifiable(f);
			this.permSubs = (Map<String, Map<Permissible, Boolean>>) f.get(pm);
			f = Reflector.getField(SimplePluginManager.class, "defSubs");
			Reflector.setFieldFinalModifiable(f);
			this.defSubs = (Map<Boolean, Map<Permissible, Boolean>>) f.get(pm);
			f = Reflector.getField(SimplePluginManager.class, "useTimings");
			Reflector.setFieldFinalModifiable(f);
			this.useTimings = (boolean) f.get(pm);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		this.defaultPerms.put(true, new HashSet<Permission>());
		this.defaultPerms.put(false, new HashSet<Permission>());
		final Field fPluginManager = Reflector.getField(CraftServer.class, "pluginManager");
		try {
			Reflector.setFieldFinalModifiable(fPluginManager);
			fPluginManager.set(instance, this);
		} catch (final Exception e2) {
			e2.printStackTrace();
		}
		LoggerUtils.info("GhostPluginManager Loaded");
	}

	@Override
	public void addPermission(final Permission perm) {
		final String name = perm.getName().toLowerCase();
		if (this.permissions.containsKey(name))
			throw new IllegalArgumentException("The permission " + name + " is already defined!");
		this.permissions.put(name, perm);
		this.calculatePermissionDefault(perm);
	}

	private void calculatePermissionDefault(final Permission perm) {
		if (perm.getDefault() == PermissionDefault.OP || perm.getDefault() == PermissionDefault.TRUE) {
			this.defaultPerms.get(true).add(perm);
			this.dirtyPermissibles(true);
		}
		if (perm.getDefault() == PermissionDefault.NOT_OP || perm.getDefault() == PermissionDefault.TRUE) {
			this.defaultPerms.get(false).add(perm);
			this.dirtyPermissibles(false);
		}
	}

	@Override
	public void callEvent(final Event event) {
		if (event.isAsynchronous()) {
			if (Thread.holdsLock(this))
				throw new IllegalStateException(
						event.getEventName() + " cannot be triggered asynchronously from inside synchronized code.");
			if (this.server.isPrimaryThread())
				throw new IllegalStateException(
						event.getEventName() + " cannot be triggered asynchronously from primary server thread.");
			this.fireEvent(event);
		} else
			synchronized (this) {
				this.fireEvent(event);
			}
	}

	private void checkUpdate(final File file) {
		if (GhostPluginManager.updateDirectory == null || !GhostPluginManager.updateDirectory.isDirectory())
			return;
		final File updateFile = new File(GhostPluginManager.updateDirectory, file.getName());
		if (updateFile.isFile() && FileUtil.copy(updateFile, file))
			updateFile.delete();
	}

	@Override
	public void clearPlugins() {
		synchronized (this) {
			this.disablePlugins();
			this.plugins.clear();
			this.lookupNames.clear();
			HandlerList.unregisterAll();
			this.fileAssociations.clear();
			this.permissions.clear();
			this.defaultPerms.get(true).clear();
			this.defaultPerms.get(false).clear();
		}
	}

	private void dirtyPermissibles(final boolean op) {
		final Set<Permissible> permissibles = this.getDefaultPermSubscriptions(op);
		for (final Permissible p : permissibles)
			p.recalculatePermissions();
	}

	@Override
	public void disablePlugin(final Plugin plugin) {
		if (plugin.isEnabled()) {
			try {
				plugin.getPluginLoader().disablePlugin(plugin);
			} catch (final Throwable ex) {
				this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while disabling "
						+ plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
			}
			try {
				this.server.getScheduler().cancelTasks(plugin);
			} catch (final Throwable ex) {
				this.server.getLogger().log(Level.SEVERE,
						"Error occurred (in the plugin loader) while cancelling tasks for "
								+ plugin.getDescription().getFullName() + " (Is it up to date?)",
						ex);
			}
			try {
				this.server.getServicesManager().unregisterAll(plugin);
			} catch (final Throwable ex) {
				this.server.getLogger().log(Level.SEVERE,
						"Error occurred (in the plugin loader) while unregistering services for "
								+ plugin.getDescription().getFullName() + " (Is it up to date?)",
						ex);
			}
			try {
				HandlerList.unregisterAll(plugin);
			} catch (final Throwable ex) {
				this.server.getLogger().log(Level.SEVERE,
						"Error occurred (in the plugin loader) while unregistering events for "
								+ plugin.getDescription().getFullName() + " (Is it up to date?)",
						ex);
			}
			try {
				this.server.getMessenger().unregisterIncomingPluginChannel(plugin);
				this.server.getMessenger().unregisterOutgoingPluginChannel(plugin);
			} catch (final Throwable ex) {
				this.server.getLogger().log(Level.SEVERE,
						"Error occurred (in the plugin loader) while unregistering plugin channels for "
								+ plugin.getDescription().getFullName() + " (Is it up to date?)",
						ex);
			}
		}
	}

	@Override
	public void disablePlugins() {
		final Plugin[] plugins = this.getPlugins();
		for (int i = plugins.length - 1; i >= 0; --i)
			this.disablePlugin(plugins[i]);
	}

	@Override
	public void enablePlugin(final Plugin plugin) {
		if (!plugin.isEnabled()) {
			final List<Command> pluginCommands = PluginCommandYamlParser.parse(plugin);
			if (!pluginCommands.isEmpty())
				this.commandMap.registerAll(plugin.getDescription().getName(), pluginCommands);
			try {
				plugin.getPluginLoader().enablePlugin(plugin);
			} catch (final Throwable ex) {
				this.server.getLogger().log(Level.SEVERE, "Error occurred (in the plugin loader) while enabling "
						+ plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
			}
			HandlerList.bakeAll();
		}
	}

	private void fireEvent(final Event event) {
		if (!GhostEventListener.shouldFire(event))
			return;
		final HandlerList handlers = event.getHandlers();
		final RegisteredListener[] listeners = handlers.getRegisteredListeners();
		RegisteredListener[] arrayOfRegisteredListener1;
		for (int i = (arrayOfRegisteredListener1 = listeners).length, j = 0; j < i; ++j) {
			final RegisteredListener registration = arrayOfRegisteredListener1[j];
			if (registration.getPlugin().isEnabled())
				try {
					registration.callEvent(event);
				} catch (final AuthorNagException ex) {
					final Plugin plugin = registration.getPlugin();
					if (plugin.isNaggable()) {
						plugin.setNaggable(false);
						this.server.getLogger().log(Level.SEVERE,
								String.format("Nag author(s): '%s' of '%s' about the following: %s",
										plugin.getDescription().getAuthors(), plugin.getDescription().getFullName(),
										ex.getMessage()));
					}
				} catch (final Throwable ex2) {
					this.server.getLogger().log(Level.SEVERE, "Could not pass event " + event.getEventName() + " to "
							+ registration.getPlugin().getDescription().getFullName(), ex2);
				}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set<Permission> getDefaultPermissions(final boolean op) {
		return ImmutableSet.copyOf((Collection) this.defaultPerms.get(op));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set<Permissible> getDefaultPermSubscriptions(final boolean op) {
		final Map<Permissible, Boolean> map = this.defSubs.get(op);
		if (map == null)
			return new HashSet<>();
		return ImmutableSet.copyOf((Collection) map.keySet());
	}

	private HandlerList getEventListeners(final Class<? extends Event> type) {
		try {
			final Method method = this.getRegistrationClass(type).getDeclaredMethod("getHandlerList", new Class[0]);
			method.setAccessible(true);
			return (HandlerList) method.invoke(null, new Object[0]);
		} catch (final Exception e) {
			throw new IllegalPluginAccessException(e.toString());
		}
	}

	@Override
	public Permission getPermission(final String name) {
		return this.permissions.get(name.toLowerCase());
	}

	@Override
	public Set<Permission> getPermissions() {
		return new HashSet<>(this.permissions.values());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set<Permissible> getPermissionSubscriptions(final String permission) {
		final String name = permission.toLowerCase();
		final Map<Permissible, Boolean> map = this.permSubs.get(name);
		if (map == null)
			return new HashSet<>();
		return ImmutableSet.copyOf((Collection) map.keySet());
	}

	@Override
	public synchronized Plugin getPlugin(final String name) {
		return this.lookupNames.get(name.replace(' ', '_'));
	}

	@Override
	public synchronized Plugin[] getPlugins() {
		return this.plugins.toArray(new Plugin[0]);
	}

	private Class<? extends Event> getRegistrationClass(final Class<? extends Event> clazz) {
		try {
			clazz.getDeclaredMethod("getHandlerList", new Class[0]);
			return clazz;
		} catch (final NoSuchMethodException localNoSuchMethodException) {
			if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class)
					&& Event.class.isAssignableFrom(clazz.getSuperclass()))
				return this.getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
			throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName());
		}
	}

	@Override
	public boolean isPluginEnabled(final Plugin plugin) {
		return plugin != null && this.plugins.contains(plugin) && plugin.isEnabled();
	}

	@Override
	public boolean isPluginEnabled(final String name) {
		final Plugin plugin = this.getPlugin(name);
		return this.isPluginEnabled(plugin);
	}

	@Override
	public synchronized Plugin loadPlugin(final File file) throws InvalidPluginException, UnknownDependencyException {
		Validate.notNull(file, "File cannot be null");
		this.checkUpdate(file);
		final Set<Pattern> filters = this.fileAssociations.keySet();
		Plugin result = null;
		for (final Pattern filter : filters) {
			final String name = file.getName();
			final Matcher match = filter.matcher(name);
			if (match.find()) {
				final PluginLoader loader = this.fileAssociations.get(filter);
				result = loader.loadPlugin(file);
			}
		}
		if (result != null) {
			this.plugins.add(result);
			this.lookupNames.put(result.getDescription().getName(), result);
		}
		return result;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public Plugin[] loadPlugins(final File directory) {
		Validate.notNull(directory, "Directory cannot be null");
		Validate.isTrue(directory.isDirectory(), "Directory must be a directory");
		final List<Plugin> result = new ArrayList<>();
		final Set<Pattern> filters = this.fileAssociations.keySet();
		if (!this.server.getUpdateFolder().equals(""))
			GhostPluginManager.updateDirectory = new File(directory, this.server.getUpdateFolder());
		final Map<String, File> plugins = new HashMap<>();
		final Set<String> loadedPlugins = new HashSet<>();
		final Map<String, Collection<String>> dependencies = new HashMap<>();
		final Map<String, Collection<String>> softDependencies = new HashMap<>();
		File[] arrayOfFile;
		for (int i = (arrayOfFile = directory.listFiles()).length, j = 0; j < i; ++j) {
			final File file = arrayOfFile[j];
			PluginLoader loader = null;
			for (final Pattern filter : filters) {
				final Matcher match = filter.matcher(file.getName());
				if (match.find())
					loader = this.fileAssociations.get(filter);
			}
			if (loader != null) {
				PluginDescriptionFile description = null;
				try {
					description = loader.getPluginDescription(file);
					final String name = description.getName();
					if (name.equalsIgnoreCase("bukkit") || name.equalsIgnoreCase("minecraft")
							|| name.equalsIgnoreCase("mojang")) {
						this.server.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '"
								+ directory.getPath() + "': Restricted Name");
						continue;
					}
					if (description.getRawName().indexOf(32) != -1)
						this.server.getLogger()
								.warning(String.format(
										"Plugin `%s' uses the space-character (0x20) in its name `%s' - this is discouraged",
										description.getFullName(), description.getRawName()));
				} catch (final InvalidDescriptionException ex) {
					this.server.getLogger().log(Level.SEVERE,
							"Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "'", ex);
					continue;
				}
				final File replacedFile = plugins.put(description.getName(), file);
				if (replacedFile != null)
					this.server.getLogger()
							.severe(String.format("Ambiguous plugin name `%s' for files `%s' and `%s' in `%s'",
									description.getName(), file.getPath(), replacedFile.getPath(),
									directory.getPath()));
				final Collection<String> softDependencySet = description.getSoftDepend();
				if (softDependencySet != null && !softDependencySet.isEmpty())
					if (softDependencies.containsKey(description.getName()))
						softDependencies.get(description.getName()).addAll(softDependencySet);
					else
						softDependencies.put(description.getName(), new LinkedList<>(softDependencySet));
				final Collection<String> dependencySet = description.getDepend();
				if (dependencySet != null && !dependencySet.isEmpty())
					dependencies.put(description.getName(), new LinkedList<>(dependencySet));
				final Collection<String> loadBeforeSet = description.getLoadBefore();
				if (loadBeforeSet != null && !loadBeforeSet.isEmpty())
					for (final String loadBeforeTarget : loadBeforeSet)
						if (softDependencies.containsKey(loadBeforeTarget))
							softDependencies.get(loadBeforeTarget).add(description.getName());
						else {
							final Collection<String> shortSoftDependency = new LinkedList<>();
							shortSoftDependency.add(description.getName());
							softDependencies.put(loadBeforeTarget, shortSoftDependency);
						}
			}
		}
		while (!plugins.isEmpty()) {
			boolean missingDependency = true;
			Object pluginIterator = plugins.keySet().iterator();
			while (((Iterator) pluginIterator).hasNext()) {
				final String plugin = (String) ((Iterator) pluginIterator).next();
				if (dependencies.containsKey(plugin)) {
					final Object dependencyIterator = dependencies.get(plugin).iterator();
					while (((Iterator) dependencyIterator).hasNext()) {
						final String dependency = (String) ((Iterator) dependencyIterator).next();
						if (loadedPlugins.contains(dependency))
							((Iterator) dependencyIterator).remove();
						else {
							if (!plugins.containsKey(dependency)) {
								missingDependency = false;
								final File file2 = plugins.get(plugin);
								((Iterator) pluginIterator).remove();
								softDependencies.remove(plugin);
								dependencies.remove(plugin);
								this.server.getLogger().log(
										Level.SEVERE, "Could not load '" + file2.getPath() + "' in folder '"
												+ directory.getPath() + "'",
										new UnknownDependencyException(dependency));
								break;
							}
							continue;
						}
					}
					if (dependencies.containsKey(plugin) && dependencies.get(plugin).isEmpty())
						dependencies.remove(plugin);
				}
				if (softDependencies.containsKey(plugin)) {
					final Object softDependencyIterator = softDependencies.get(plugin).iterator();
					while (((Iterator) softDependencyIterator).hasNext()) {
						final String softDependency = (String) ((Iterator) softDependencyIterator).next();
						if (!plugins.containsKey(softDependency))
							((Iterator) softDependencyIterator).remove();
					}
					if (softDependencies.get(plugin).isEmpty())
						softDependencies.remove(plugin);
				}
				if (!dependencies.containsKey(plugin) && !softDependencies.containsKey(plugin)
						&& plugins.containsKey(plugin)) {
					final File file3 = plugins.get(plugin);
					((Iterator) pluginIterator).remove();
					missingDependency = false;
					try {
						result.add(this.loadPlugin(file3));
						loadedPlugins.add(plugin);
					} catch (final InvalidPluginException ex2) {
						this.server.getLogger().log(Level.SEVERE,
								"Could not load '" + file3.getPath() + "' in folder '" + directory.getPath() + "'",
								ex2);
					}
				}
			}
			if (missingDependency) {
				pluginIterator = plugins.keySet().iterator();
				while (((Iterator) pluginIterator).hasNext()) {
					final String plugin = (String) ((Iterator) pluginIterator).next();
					if (!dependencies.containsKey(plugin)) {
						softDependencies.remove(plugin);
						missingDependency = false;
						final File file3 = plugins.get(plugin);
						((Iterator) pluginIterator).remove();
						try {
							result.add(this.loadPlugin(file3));
							loadedPlugins.add(plugin);
						} catch (final InvalidPluginException ex2) {
							this.server.getLogger().log(Level.SEVERE,
									"Could not load '" + file3.getPath() + "' in folder '" + directory.getPath() + "'",
									ex2);
						}
					}
				}
				if (!missingDependency)
					continue;
				softDependencies.clear();
				dependencies.clear();
				final Object failedPluginIterator = plugins.values().iterator();
				while (((Iterator) failedPluginIterator).hasNext()) {
					final File file3 = (File) ((Iterator) failedPluginIterator).next();
					((Iterator) failedPluginIterator).remove();
					this.server.getLogger().log(Level.SEVERE, "Could not load '" + file3.getPath() + "' in folder '"
							+ directory.getPath() + "': circular dependency detected");
				}
			}
		}
		return result.toArray(new Plugin[result.size()]);
	}

	@Override
	public void recalculatePermissionDefaults(final Permission perm) {
		if (this.permissions.containsValue(perm)) {
			this.defaultPerms.get(true).remove(perm);
			this.defaultPerms.get(false).remove(perm);
			this.calculatePermissionDefault(perm);
		}
	}

	@Override
	public void registerEvent(final Class<? extends Event> event, final Listener listener, final EventPriority priority,
			final EventExecutor executor, final Plugin plugin) {
		this.registerEvent(event, listener, priority, executor, plugin, false);
	}

	@Override
	public void registerEvent(final Class<? extends Event> event, final Listener listener, final EventPriority priority,
			final EventExecutor executor, final Plugin plugin, final boolean ignoreCancelled) {
		Validate.notNull(listener, "Listener cannot be null");
		Validate.notNull(priority, "Priority cannot be null");
		Validate.notNull(executor, "Executor cannot be null");
		Validate.notNull(plugin, "Plugin cannot be null");
		if (!plugin.isEnabled())
			throw new IllegalPluginAccessException("Plugin attempted to register " + event + " while not enabled");
		if (this.useTimings)
			this.getEventListeners(event)
					.register(new TimedRegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
		else
			this.getEventListeners(event)
					.register(new RegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
	}

	@Override
	public void registerEvents(final Listener listener, final Plugin plugin) {
		if (!plugin.isEnabled())
			throw new IllegalPluginAccessException("Plugin attempted to register " + listener + " while not enabled");
		for (final Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader()
				.createRegisteredListeners(listener, plugin).entrySet())
			this.getEventListeners(this.getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
	}

	@Override
	public void registerInterface(final Class<? extends PluginLoader> loader) throws IllegalArgumentException {
		if (PluginLoader.class.isAssignableFrom(loader)) {
			PluginLoader instance = null;
			Label_0140: {
				try {
					final Constructor<? extends PluginLoader> constructor = loader.getConstructor(Server.class);
					instance = constructor.newInstance(this.server);
					break Label_0140;
				} catch (final NoSuchMethodException ex) {
					final String className = loader.getName();
					throw new IllegalArgumentException(String.format(
							"Class %s does not have a public %s(Server) constructor", className, className), ex);
				} catch (final Exception ex2) {
					throw new IllegalArgumentException(
							String.format("Unexpected exception %s while attempting to construct a new instance of %s",
									ex2.getClass().getName(), loader.getName()),
							ex2);
				}
			}
			final Pattern[] patterns = instance.getPluginFileFilters();
			synchronized (this) {
				Pattern[] arrayOfPattern1;
				for (int i = (arrayOfPattern1 = patterns).length, j = 0; j < i; ++j) {
					final Pattern pattern = arrayOfPattern1[j];
					this.fileAssociations.put(pattern, instance);
				}
			}
			return;
		}
		throw new IllegalArgumentException(
				String.format("Class %s does not implement interface PluginLoader", loader.getName()));
	}

	@Override
	public void removePermission(final Permission perm) {
		this.removePermission(perm.getName());
	}

	@Override
	public void removePermission(final String name) {
		this.permissions.remove(name.toLowerCase());
	}

	@Override
	public void subscribeToDefaultPerms(final boolean op, final Permissible permissible) {
		Map<Permissible, Boolean> map = this.defSubs.get(op);
		if (map == null) {
			map = new WeakHashMap<>();
			this.defSubs.put(op, map);
		}
		map.put(permissible, true);
	}

	@Override
	public void subscribeToPermission(final String permission, final Permissible permissible) {
		final String name = permission.toLowerCase();
		Map<Permissible, Boolean> map = this.permSubs.get(name);
		if (map == null) {
			map = new WeakHashMap<>();
			this.permSubs.put(name, map);
		}
		map.put(permissible, true);
	}

	@Override
	public void unsubscribeFromDefaultPerms(final boolean op, final Permissible permissible) {
		final Map<Permissible, Boolean> map = this.defSubs.get(op);
		if (map != null) {
			map.remove(permissible);
			if (map.isEmpty())
				this.defSubs.remove(op);
		}
	}

	@Override
	public void unsubscribeFromPermission(final String permission, final Permissible permissible) {
		final String name = permission.toLowerCase();
		final Map<Permissible, Boolean> map = this.permSubs.get(name);
		if (map != null) {
			map.remove(permissible);
			if (map.isEmpty())
				this.permSubs.remove(name);
		}
	}

	@Override
	public boolean useTimings() {
		return this.useTimings;
	}

	public void useTimings(final boolean use) {
		this.useTimings = use;
	}
}
