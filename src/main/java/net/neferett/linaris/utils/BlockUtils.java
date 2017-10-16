package net.neferett.linaris.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import net.neferett.linaris.metadatas.Flags;
import net.neferett.linaris.utils.tasksmanager.TaskManager;

public class BlockUtils {
	private static final String temporaryChangeBlockFlag = "TemporaryChangeBlockFlag";
	private static final List<Material> nonChangeable = Arrays.asList(Material.CHEST,Material.FURNACE,Material.BREWING_STAND,Material.HOPPER);
	/**
	 * Transforme temporairement un block en un autre.
	 * N'Override pas le changement précédant !
	 * 
	 * Prévoir méthode d'Override et/ou Override priority et/ou Clear
	 * (taskName/anciens type/data en flag, cancel de task)
	 * (pour la priorité, ça pourrait etre pokemon like : plante < feu < eau < plante => Donc un entier ne suffirait pas = a gérer au cas par cas dans les plugins necessiteux)
	 * 
	 * 
	 * @param block
	 * @param newtype
	 * @param newdata
	 * @param duration
	 */
	@SuppressWarnings("deprecation")
	public static void temporaryChangeBlock(final Block block, Material newtype, Byte newdata, int duration) {
		if(Flags.hasFlag(block, temporaryChangeBlockFlag))return;
		if(nonChangeable.contains(block.getType()))return;//On va eviter de faire pop le contenu des coffres au sol.
		Flags.setFlag(block, temporaryChangeBlockFlag);
		final Material type = block.getType();
		final Byte data = block.getData();
		//LoggerUtils.debug("Old block : "+type+" : "+data);
		block.setTypeIdAndData(newtype.getId(), newdata, false);
		TaskManager.runTaskLater(new Runnable() {
			@Override
			public void run() {
				block.setTypeIdAndData(type.getId(), data, false);
				Flags.removeFlag(block, temporaryChangeBlockFlag);
			}
		}, duration);
	}
	/**
	 * Recupère une liste de block dans le rayon donné
	 * @param center
	 * @param range
	 * @deprecated use vectorUtils
	 * @return
	 */
	public static List<Block> getBlockInRange(Location center,int range) {
		return getBlockInRange(center,range,range);
	}
	/**
	 * Recupère une liste de block dans le rayon donné avec une range différente en y
	 * @param center
	 * @param range
	 * @param yRange
	 * @return
	 * @deprecated use vectorUtils
	 */
	public static List<Block> getBlockInRange(Location center,int range,int yRange) {
		List<Block> list = new ArrayList<Block>();
		Block b = center.getBlock();
		for(Vector v: VectorUtils.getVectorRange(range, yRange)) {
			list.add(b.getRelative(v.getBlockX(),v.getBlockY(),v.getBlockZ()));
		}
		/*
		 * old :
		World world = center.getWorld();
		int locX = center.getBlockX();
		int locY = center.getBlockY();
		int locZ = center.getBlockZ();
		for (int x = locX - range; x < locX + range; x++) {
			for (int z = locZ - range; z < locZ + range; z++) {
				for (int y = locY - yRange; y < locY + yRange; y++) {
					final Block block = world.getBlockAt(x, y, z);
					if (block.getLocation().distanceSquared(center) < range * range)list.add(block);
				}
			}
		}*/
		return list;
	}
}
