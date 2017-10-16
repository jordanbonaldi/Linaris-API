package net.neferett.linaris.utils.tasksmanager;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import net.neferett.linaris.BukkitAPI;

public class TaskManager {

	static Plugin							plugin		= BukkitAPI.get();
	public static BukkitScheduler			scheduler	= Bukkit.getScheduler();
	private static HashMap<String, Integer>	taskList	= new HashMap<>();

	/**
	 * Rajout une task dans la list
	 * 
	 * @param name
	 * @param id
	 */
	public static void addTask(final String name, final int id) {
		taskList.put(name, id);
	}

	// Cancel all task
	public static void cancelAllTask() {
		for (final int taskId : taskList.values())
			scheduler.cancelTask(taskId);
	}

	// Annule une tâche par l'ID
	public static void cancelTaskById(final int id) {
		scheduler.cancelTask(id);
	}

	// Cancel de la task by name
	public static boolean cancelTaskByName(final String taskName) {
		if (taskExist(taskName)) {
			final int taskId = getTaskId(taskName);
			taskList.remove(taskName);
			scheduler.cancelTask(taskId);
			return true;
		}
		return false;
	}

	/**
	 * Useless autant appelé directement TaskManager.cancelTaskByName(taskName)
	 * qui en plus retourne le status de l'annulation
	 * 
	 * @deprecated
	 */
	@Deprecated
	public static void checkIfExist(final String taskName) {
		if (TaskManager.taskExist(taskName))
			TaskManager.cancelTaskByName(taskName);
	}

	// Récupération de l'id
	public static int getTaskId(final String taskName) {
		if (taskExist(taskName))
			return taskList.get(taskName);
		return 0;
	}

	/**
	 * Créer un nom de tâche unique basé sur un nom de tâche
	 * 
	 * @param string
	 * @return
	 */
	public static String getTaskName(final String string) {
		String taskName = string + "_" + new Random().nextInt(99999);
		while (taskExist(taskName))
			taskName = string + "_" + new Random().nextInt(99999);
		return taskName;
	}

	/**
	 * Tente de récupérer le nom de la task par l'id si elle existe encore et
	 * qu'elle a été déclaré dans ce manager
	 * 
	 * @param id
	 *            de la task
	 * @return null si non trouvé
	 */
	public static String getTaskNameById(final int id) {
		for (final Entry<String, Integer> entry : taskList.entrySet())
			if (entry.getValue() == id)
				return entry.getKey();
		return null;
	}

	public static void removeTaskByName(final String taskName) {
		taskList.remove(taskName);
	}

	// Run task now
	public static BukkitTask runTask(final Runnable runnable) {
		return scheduler.runTask(plugin, runnable);
	}

	// Run task later
	public static BukkitTask runTaskLater(final Runnable runnable, final int tick) {
		return scheduler.runTaskLater(plugin, runnable, tick);
	}

	/**
	 * Créer et enregistre une task, se retire de la liste toute seule à
	 * l'expiration, permet de l'annuler dans un plugin et éviter les mémory
	 * leaks
	 * 
	 * @param taskName
	 * @param task
	 * @param duration
	 */
	public static BukkitTask runTaskLater(final String taskName, final Runnable task, final int duration) {
		final BukkitTask bukkitTask = scheduler.runTaskLater(plugin, task, duration);
		final int id = bukkitTask.getTaskId();
		TaskManager.addTask(taskName, id);
		runTaskLater(() -> {
			// Toujours la même task ID pour éviter la suppression de task
			// renouvelées
			if (taskList.get(taskName) != null && taskList.get(taskName) == id)
				taskList.remove(taskName);
		}, duration);
		return bukkitTask;
	}

	/**
	 * Ajoute une tâche répétitive Annule la précédante du meme nom si existe.
	 * 
	 * @param runnable
	 * @param delay
	 * @param refresh
	 * @return
	 */
	public static BukkitTask scheduleSyncRepeatingTask(final String taskName, final Runnable runnable, final int delay,
			final int refresh) {
		cancelTaskByName(taskName);
		final BukkitTask task = scheduler.runTaskTimer(plugin, runnable, delay, refresh);
		taskList.put(taskName, task.getTaskId());
		return task;
	}

	public static BukkitTask scheduleSyncRepeatingTaskWithEnd(final String taskName, final Runnable runnable,
			final int delay, final int refresh, final int end) {
		cancelTaskByName(taskName);
		final BukkitTask task = scheduler.runTaskTimer(plugin, runnable, delay, refresh);
		taskList.put(taskName, task.getTaskId());
		runTaskLater(() -> cancelTaskByName(taskName), end * 20);
		return task;
	}

	// La tâche existe ?
	public static boolean taskExist(final String taskName) {
		if (taskList.containsKey(taskName))
			return true;
		return false;
	}

	public TaskManager() {

	}

	/**
	 * Recupère la task
	 * 
	 * @param taskName
	 * @return
	 */
	public BukkitTask getTask(final String taskName) {
		final BukkitTask task = null;
		final int id = getTaskId(taskName);
		if (id > 0)
			for (final BukkitTask pendingTask : scheduler.getPendingTasks())
				if (pendingTask.getTaskId() == id)
					return task;
		return null;
	}

}
