package net.neferett.linaris.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.utils.tasksmanager.TaskManager;

public class RestartTask implements Runnable{

	public static void reboot() {
		TaskManager.scheduleSyncRepeatingTask("RebootServer", new RestartTask(), 0, 20);
	}
	
	int time;
	
	public RestartTask() {
		this.time = 10;
		BukkitAPI.get().getServerInfos().setCanJoin(false, false);
		BukkitAPI.get().getServerInfos().setCanSee(false, true);
	}
	
	@Override
	public void run() {
		if (time == 15) {
			for (Player p : Bukkit.getOnlinePlayers()) {
	          	p.sendMessage("§cUn redémarrage du serveur est ncessaire vous allez être téléportés dans un hub !");
	          }
		}
		if (time == 10) {
			for (Player p : Bukkit.getOnlinePlayers()) {
	          	p.sendMessage("§cVous avez été téléportés dans un hub !");
	        }
			try {
				PlayerUtils.returnToHub();
			} catch (Exception e) {
			}
		}
		
		if (time == 0) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
			TaskManager.cancelTaskByName("RebootServer");
			return;
		}
		time--;
		 
	}

}
