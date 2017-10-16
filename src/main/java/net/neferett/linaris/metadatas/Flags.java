package net.neferett.linaris.metadatas;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.utils.tasksmanager.TaskManager;

public class Flags {

	public static JavaPlugin	plugin		= BukkitAPI.get();

	/*
	 * Les Flags communs
	 */

	/**
	 * Une entité avec ce flag est intuable
	 */
	public static String		godMode		= "API_godMode";
	/**
	 * Le nom du dernier joueur a avoir touché l'entité
	 */
	public static String		lastDamager	= "API_lastDamager";
	/**
	 * Flag de mort, l'entité vient de mourir
	 */
	public static String		died		= "API_died";
	/**
	 * L'entité est en mode fantôme/spectateur
	 */
	public static String		ghost		= "API_ghost";

	/*
	 * Entity
	 */

	public static boolean hasFlag(Metadatable metadatable, String flag) {
		return metadatable.hasMetadata(flag);
	}

	public static void removeFlag(Metadatable metadatable, String flag) {
		metadatable.removeMetadata(flag, plugin);
	}

	public static void setFlag(Metadatable metadatable, String flag) {
		metadatable.setMetadata(flag, new FixedMetadataValue(plugin, 1));
	}

	public static void setStringFlag(Metadatable metadatable, String flag, String value) {
		metadatable.setMetadata(flag, new FixedMetadataValue(plugin, value));
	}

	public static void setIntegerFlag(Metadatable metadatable, String flag, Integer value) {
		metadatable.setMetadata(flag, new FixedMetadataValue(plugin, value));
	}

	public static String readStringFlag(Metadatable metadatable, String flag) {
		if (hasFlag(metadatable, flag)) return metadatable.getMetadata(flag).get(0).asString();
		else return null;
	}

	public static int readIntegerFlag(Metadatable metadatable, String flag) {
		if (hasFlag(metadatable, flag)) return metadatable.getMetadata(flag).get(0).asInt();
		else return -1;
	}

	// Temporary flag
	public static void setTemporaryFlag(final Metadatable metadatable, final String flag, int duration) {
		String taskName = "TempFlag_" + flag + metadatable.toString();
		// Le flag temporaire existe déjà, retrait du flag + annulation de la tâche
		if (hasFlag(metadatable, flag)) {
			removeFlag(metadatable, flag);
			TaskManager.cancelTaskByName(taskName);
		}
		// On repose un flag
		setFlag(metadatable, flag);
		// La tâche de retrait planifié sur une tâche qui se remove toute seule
		TaskManager.runTaskLater(taskName, new Runnable() {
			@Override
			public void run() {
				removeFlag(metadatable, flag);
			}
		}, duration);
	}
	// Temporary String flag
	public static void setTemporaryStringFlag(Metadatable metadatable, String flag, String value, int duration) {
		String taskName = "TempFlag_" + flag + metadatable.toString();
		// Le flag temporaire existe déjà, retrait du flag + annulation de la tâche
		if (hasFlag(metadatable, flag)) {
			removeFlag(metadatable, flag);
			TaskManager.cancelTaskByName(taskName);
		}
		// On repose un flag
		setStringFlag(metadatable,flag,value);
		// La tâche de retrait planifié sur une tâche qui se remove toute seule
		TaskManager.runTaskLater(new MetaFlagRunnable(metadatable, taskName, flag), duration);
	}

	/**
	 * Augmente la valeur d'un flag temporaire d'un flag en annulant la tâche s'il y a et en remettant le compteur au
	 * maximum
	 * @param entity
	 * @param flag
	 * @param durationInTicks
	 * @param value
	 */
	public static void incrementAndRefreshTemporaryFlag(Metadatable metadatable, String flag, int durationInTicks, int value) {
		int flagValue = readIntegerFlag(metadatable, flag);
		// Pas de flag on part de 0
		if (flagValue == -1) flagValue = 0;
		setIntegerFlag(metadatable, flag, flagValue + value);
		runMetaFlagIncrementAndRefreshRunnable(metadatable, flag, durationInTicks);
	}

	/**
	 * La tâche qui va gére l'increment
	 * @param entity
	 * @param flag
	 * @param ticks
	 */
	private static void runMetaFlagIncrementAndRefreshRunnable(Metadatable metadatable, String flag, int ticks) {
		String taskName = metadatable + flag;
		TaskManager.cancelTaskByName(taskName);
		BukkitTask task = TaskManager.scheduler.runTaskLater(plugin, new MetaFlagRunnable(metadatable, taskName, flag), ticks);
		TaskManager.addTask(taskName, task.getTaskId());
	}

}
