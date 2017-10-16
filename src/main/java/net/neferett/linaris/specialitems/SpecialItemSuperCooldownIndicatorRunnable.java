package net.neferett.linaris.specialitems;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.utils.tasksmanager.TaskManager;

/**
 * Du HG de Trif, permet d'updater l'item pour y afficher le cooldown dans le nom toutes les secondes
 * @author Likaos
 */
public class SpecialItemSuperCooldownIndicatorRunnable implements Runnable {

	private Player		player;
	private SpecialItem	special;
	private String		taskName;
	private int			cooldown	= 0;
	// Message de cooldown ajouté
	private String		cdPrefix	= "§7(";
	private ChatColor	cdColor		= ChatColor.AQUA;
	private String		cdSuffix	= "§7)";
	// Action à exécuter pendant ou à la fin de la recharge (son/effet/messages ?)
	private Runnable	reloadingAction;
	private Runnable	readyAction;

	public SpecialItemSuperCooldownIndicatorRunnable(Player player, SpecialItem specialItem, int cooldown) {
		this.player = player;
		this.special = specialItem;
		this.cooldown = cooldown;
	}

	public SpecialItemSuperCooldownIndicatorRunnable(Player player, SpecialItem specialItem, int cooldown, String cdPrefix, ChatColor cdColor, String cdSuffix) {
		this(player, specialItem, cooldown);
		if (cdPrefix != null) this.cdPrefix = cdPrefix;
		if (cdColor != null) this.cdColor = cdColor;
		if (cdSuffix != null) this.cdSuffix = cdSuffix;
	}

	public SpecialItemSuperCooldownIndicatorRunnable(Player player, SpecialItem specialItem, int cooldown, String cdPrefix, ChatColor cdColor, String cdSuffix, Runnable reloadingAction,
			Runnable readyAction) {
		this(player, specialItem, cooldown, cdPrefix, cdColor, cdSuffix);
		if (reloadingAction != null) this.reloadingAction = reloadingAction;
		if (readyAction != null) this.readyAction = readyAction;
	}

	/**
	 * Démarrer le scheduler, sorti de la méthode pour utiliser les appels d'autre constructeurs
	 */
	public void start() {
		taskName = "SpecialItemCooldownIndicatorRunnable" + player.getName() + special.getName();
		// Si tout le monde fait n'importe quoi...
		// if (TaskManager.taskExist(taskName)) return;
		TaskManager.scheduleSyncRepeatingTask(taskName, this, 0, 20);
	}

	@Override
	public void run() {
		// Hors ligne fin
		if (!player.isOnline()) {
			TaskManager.cancelTaskByName(taskName);
			return;
		}
		ItemStack item = special.getFirstInInventory(player.getInventory());
		if (item != null) {
			StringBuilder itemName = new StringBuilder();
			itemName.append(special.getName());
			// Encore du cooldown
			if (cooldown > 0) {
				itemName.append(" ").append(cdPrefix).append(cdColor).append(cooldown).append(" seconde");
				if (cooldown > 1) itemName.append("s");
				itemName.append(cdSuffix);
				if (reloadingAction != null) reloadingAction.run();
				item.setAmount(-cooldown);
			} else {
				item.setAmount(1);
			}
			SpecialItem.rename(item, itemName.toString());
		}
		// Pas trouvé, fin
		else {
			TaskManager.cancelTaskByName(taskName);
			return;
		}

		// Fin du CD
		if (cooldown <= 0) {
			if (readyAction != null) readyAction.run();
			TaskManager.cancelTaskByName(taskName);
			return;
		}
		cooldown--;
	}
}
