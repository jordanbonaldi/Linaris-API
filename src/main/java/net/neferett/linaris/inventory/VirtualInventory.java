package net.neferett.linaris.inventory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.neferett.linaris.specialitems.SpecialItem;

public abstract class VirtualInventory implements InventoryHolder {

	private static HashMap<Integer, VirtualInventory>	registeredVirtualInventories	= new HashMap<Integer, VirtualInventory>();
	private static int									autoIncrement;

	protected Inventory									inventory;
	private SpecialItem									inventorySpecialItem;
	protected String									title;
	protected int										id;
	public int											row;

	public Map<Integer, SpecialItem>					myItems							= new HashMap<Integer, SpecialItem>();

	public VirtualInventory(String title, int row) {
		this.title = title;
		this.row = row;
	}

	@Deprecated
	public Map<Integer, SpecialItem> getMyItems() {
		return myItems;
	}

	/**
	 * Recupère un inventaire par son id
	 * @param id
	 * @return
	 */
	public static VirtualInventory get(int id) {
		return registeredVirtualInventories.get(id);
	}

	/**
	 * Enregistre un nouveau inventaire virtuel
	 * @param id
	 * @param item
	 */
	public static int registerVirtualInventory(VirtualInventory virtualInventory) {
		registeredVirtualInventories.put(virtualInventory.id = autoIncrement++, virtualInventory);
		return virtualInventory.getId();
	}

	private Integer getId() {
		return id;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public abstract void open(Player player);

	public abstract void openWithExtraText(Player player, String extraText);

	/**
	 * Recupère le titre
	 * @return
	 */
	public String getTitle() {
		return trimTitle(this.title);
	}

	/**
	 * Le titre avec du texte en plus, exemple golds, selection du kit etc
	 * @param string
	 * @return
	 */
	public String getTitleWithExtraText(String string) {
		return trimTitle(title + " " + string);
	}

	/**
	 * Empêche l'affichage du titre à plus de 32 caractères
	 * @param title
	 * @return
	 */
	private String trimTitle(String title) {
		if (title.length() > 32) title = title.substring(0, 31);
		return title;
	}

	/**
	 * Rajoute un item au slot définit
	 * @param menuItem
	 * @param slot
	 */
	public void addItem(SpecialItem specialItem, int slot) {
		myItems.put(slot, specialItem);
	}

	/**
	 * Rajoute un item au slot définit
	 * @param l'id du specialItem
	 * @param slot
	 */
	public void addItem(int specialItemId, int slot) {
		myItems.put(slot, SpecialItem.get(specialItemId));
	}

	/**
	 * Rajoute un item au slot définit et la déclare
	 * @param menuItem
	 * @param slot
	 */
	public int addAndRegisterItem(SpecialItem specialItem, int slot) {
		int id = SpecialItem.registerItem(specialItem);
		myItems.put(slot, specialItem);
		return id;
	}

	/**
	 * Le dernier Slot d'un inventaire
	 * @return
	 */
	public int getIventorySlot() {
		return row * 9 - 1;
	}

	/**
	 * Rajoute un objet sur le dernier slot
	 * @param specialItem
	 * @return
	 */
	public void addItemOnLastSlot(SpecialItem specialItem) {
		addItem(specialItem, getIventorySlot());
	}

	/**
	 * Rajoute un objet sur le dernier slot
	 * @param l'id de l'item
	 * @return
	 */
	public void addItemOnLastSlot(int specialItemId) {
		addItem(SpecialItem.get(specialItemId), getIventorySlot());
	}

	/**
	 * Déclare et définit l'objet qui peut appeler le menu
	 * @param menuItem
	 * @param slot
	 */
	protected int setAndRegisterInventorySpecialItem(SpecialItem specialItem) {
		int id = SpecialItem.registerItem(specialItem);
		this.inventorySpecialItem = specialItem;
		return id;
	}

	public SpecialItem getInventorySpecialItem() {
		return inventorySpecialItem;
	}

	/**
	 * Recupère le numéro de slot
	 * @param line ligne
	 * @param pos position
	 * @return
	 */
	public int getPos(int line, int pos) {
		int result = (line - 1) * 9 + (pos - 1);
		return result;
	}

}
