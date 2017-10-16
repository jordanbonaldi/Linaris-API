package net.neferett.linaris.metadatas;

import org.bukkit.metadata.Metadatable;

import net.neferett.linaris.utils.tasksmanager.TaskManager;

public class MetaFlagRunnable implements Runnable {

	protected String		taskName;
	protected String		flag;
	protected Metadatable	metadatable;

	public MetaFlagRunnable(Metadatable metadatable, String taskName, String flag) {
		this.taskName = taskName;
		this.flag = flag;
		this.metadatable = metadatable;
	}

	@Override
	public void run() {
		Flags.removeFlag(metadatable, flag);
		TaskManager.removeTaskByName(taskName);
	}

}
