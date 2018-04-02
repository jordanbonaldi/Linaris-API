package net.neferett.linaris.specialitems;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.utils.tasksmanager.TaskManager;

public class SpecialItemSuperCooldownIndicatorRunnable implements Runnable {

	private ChatColor			cdColor		= ChatColor.AQUA;
	// Message de cooldown ajouté
	private String				cdPrefix	= "§7(";
	private String				cdSuffix	= "§7)";
	private int					cooldown	= 0;
	private final Player		player;
	private Runnable			readyAction;
	// Action à exécuter pendant ou à la fin de la recharge (son/effet/messages
	// ?)
	private Runnable			reloadingAction;
	private final SpecialItem	special;
	private String				taskName;

	public SpecialItemSuperCooldownIndicatorRunnable(final Player player, final SpecialItem specialItem,
			final int cooldown) {
		this.player = player;
		this.special = specialItem;
		this.cooldown = cooldown;
	}

	public SpecialItemSuperCooldownIndicatorRunnable(final Player player, final SpecialItem specialItem,
			final int cooldown, final String cdPrefix, final ChatColor cdColor, final String cdSuffix) {
		this(player, specialItem, cooldown);
		if (cdPrefix != null)
			this.cdPrefix = cdPrefix;
		if (cdColor != null)
			this.cdColor = cdColor;
		if (cdSuffix != null)
			this.cdSuffix = cdSuffix;
	}

	public SpecialItemSuperCooldownIndicatorRunnable(final Player player, final SpecialItem specialItem,
			final int cooldown, final String cdPrefix, final ChatColor cdColor, final String cdSuffix,
			final Runnable reloadingAction, final Runnable readyAction) {
		this(player, specialItem, cooldown, cdPrefix, cdColor, cdSuffix);
		if (reloadingAction != null)
			this.reloadingAction = reloadingAction;
		if (readyAction != null)
			this.readyAction = readyAction;
	}

	@Override
	public void run() {
		// Hors ligne fin
		if (!this.player.isOnline()) {
			TaskManager.cancelTaskByName(this.taskName);
			return;
		}
		final ItemStack item = this.special.getFirstInInventory(this.player.getInventory());
		if (item != null) {
			final StringBuilder itemName = new StringBuilder();
			itemName.append(this.special.getName());
			// Encore du cooldown
			if (this.cooldown > 0) {
				itemName.append(" ").append(this.cdPrefix).append(this.cdColor).append(this.cooldown)
						.append(" seconde");
				if (this.cooldown > 1)
					itemName.append("s");
				itemName.append(this.cdSuffix);
				if (this.reloadingAction != null)
					this.reloadingAction.run();
				item.setAmount(-this.cooldown);
			} else
				item.setAmount(1);
			SpecialItem.rename(item, itemName.toString());
		}
		// Pas trouvé, fin
		else {
			TaskManager.cancelTaskByName(this.taskName);
			return;
		}

		// Fin du CD
		if (this.cooldown <= 0) {
			if (this.readyAction != null)
				this.readyAction.run();
			TaskManager.cancelTaskByName(this.taskName);
			return;
		}
		this.cooldown--;
	}

	/**
	 * Démarrer le scheduler, sorti de la méthode pour utiliser les appels
	 * d'autre constructeurs
	 */
	public void start() {
		this.taskName = "SpecialItemCooldownIndicatorRunnable" + this.player.getName() + this.special.getName();
		// Si tout le monde fait n'importe quoi...
		// if (TaskManager.taskExist(taskName)) return;
		TaskManager.scheduleSyncRepeatingTask(this.taskName, this, 0, 20);
	}
}
