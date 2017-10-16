package net.neferett.linaris.PlayersHandler;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.api.PlayerLocal;
import net.neferett.linaris.commands.CommandHandler;
import net.neferett.linaris.inventory.PlayerInventory;
import net.neferett.linaris.utils.gui.GuiScreen;
import net.neferett.linaris.utils.tasksmanager.TaskManager;

public class Players extends PlayerProvider {

	private CommandHandler		cmd;
	private boolean				freeze;
	private final PlayerData	pd;
	private final PlayerLocal	pl;
	private Boolean				tp;

	public Players(final Player p) {
		this.tp = false;
		this.player = (CraftPlayer) p;
		this.pd = BukkitAPI.get().getPlayerDataManager().getPlayerData(p.getName());
		this.pl = BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(p.getName());
	}

	public void createTPWithDelay(final int time, final Callable<Location> call) {
		final AtomicInteger dectime = new AtomicInteger(time);
		this.tp = true;
		TaskManager.scheduleSyncRepeatingTask("tpdelay" + this.player.getName(), () -> {
			if (dectime.get() == 0) {
				this.tp = false;
				this.player.sendMessage("§7Téléportation en cours ...");
				try {
					this.player.teleport(call.call());
				} catch (final Exception e) {
					e.printStackTrace();
				}
				TaskManager.cancelTaskByName("tpdelay" + this.player.getName());
			} else if (this.tp)
				this.player.sendMessage("§7Téléportation dans §c" + dectime.getAndDecrement() + " seconde"
						+ (dectime.get() + 1 > 1 ? "s" : ""));
			else if (!this.tp) {
				this.player.sendMessage("§CTéléportation annulée !");
				TaskManager.cancelTaskByName("tpdelay" + this.player.getName());
			}
		}, time * 20, 20);
	}

	public void DisplayErrorMessage() {
		this.player.sendMessage(this.cmd.errorMessage());
	}

	public CommandHandler getCommdandHandler() {
		return this.cmd;
	}

	public PlayerData getPlayerData() {
		return this.pd;
	}

	public PlayerLocal getPlayerLocal() {
		return this.pl;
	}

	public boolean inTP() {
		return this.tp;
	}

	public boolean isFreeze() {
		return this.freeze;
	}

	public boolean isVanished() {
		return this.pd.contains("invisible") && this.pd.getBoolean("invisible");
	}

	public GuiScreen seeInventory(final Players p) {
		return new PlayerInventory(this, p);
	}

	public void setCommandHandler(final CommandHandler cmd) {
		this.cmd = cmd;
	}

	public void setFreeze(final boolean freeze) {
		this.freeze = freeze;
	}

	public void setTp(final Boolean tp) {
		this.tp = tp;
	}

}
