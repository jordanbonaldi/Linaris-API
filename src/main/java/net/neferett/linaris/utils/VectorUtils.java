package net.neferett.linaris.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.util.Vector;


public class VectorUtils {
	private static HashMap<String, ArrayList<Vector>> vectorRanges;
	/**
	 * Retourne une liste de vector en fonction du range; Peut etre utilisé pour la suite sur des block ou des location
	 * La liste est gardé en memoir : prevu pour utilisation fréquente.
	 * @param center
	 * @param range
	 * @return
	 */
	public static ArrayList<Vector> getVectorRange(int range) {
		return getVectorRange(range,range);
	}
	/**
	 * Retourne une liste de vector en fonction du range; 
	 * Peut etre utilisé pour la suite sur des block ou des location
	 * La liste est gardé en memoir : prevu pour utilisation fréquente.
	 * @param center
	 * @param range
	 * @param yRange
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Vector> getVectorRange(int range,int yRange) {
		if(range<0)range = -range;
		if(yRange<0)yRange = -yRange;
		String key = range+";"+yRange;
		if(vectorRanges==null)vectorRanges = new HashMap<String,ArrayList<Vector>>();
		if(!vectorRanges.containsKey(key)) {
			ArrayList<Vector> list = new ArrayList<Vector>();
			Vector center = new Vector(0,0,0);
			Vector v;
			int rangeSquared =  (1+range) * (1+range);//Pour que range puisse commencer à 1
			for (int x = - range; x <= range; x++) {
				for (int z = - range; z <= range; z++) {
					for (int y = - yRange; y <= yRange; y++) {
						v = new Vector(x,y,z);
						if (v.distanceSquared(center) < rangeSquared)list.add(v);
					}
				}
			}
			vectorRanges.put(key,list);
			return (ArrayList<Vector>)list.clone();
		}
		return (ArrayList<Vector>)vectorRanges.get(key).clone();
	}

	/**
	 * Experimental
	 * Retourne les vecteurs situés dans la range max moins ceux de la range min
	 * @param minRange
	 * @param minYRange
	 * @param maxRange
	 * @param maxYRange
	 * @return
	 */
	public static ArrayList<Vector> getVectorRangeMinMax(int minRange,int maxRange) {
		return getVectorRangeMinMax(minRange,minRange,maxRange,maxRange);
	}
	/**
	 * Experimental
	 * Retourne les vecteurs situés dans la range max moins ceux de la range min
	 * @param minRange
	 * @param minYRange
	 * @param maxRange
	 * @param maxYRange
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Vector> getVectorRangeMinMax(int minRange,int minYRange,int maxRange,int maxYRange) {
		String key = minRange+";"+minYRange+"-"+maxRange+";"+maxYRange;
		if(vectorRanges==null)vectorRanges = new HashMap<String,ArrayList<Vector>>();
		if(!vectorRanges.containsKey(key)) {
			ArrayList<Vector> list = getVectorRange(maxRange,maxYRange);
			list.removeAll(getVectorRange(minRange,minYRange));
			vectorRanges.put(key,list);
			return (ArrayList<Vector>)list.clone();
		}
		return (ArrayList<Vector>)vectorRanges.get(key).clone();
	}
	

	/**
	 * Random vector
	 */
	public static Vector randVector()
	{
		Random random = new Random();
		double x, y, z;
		x = random.nextDouble() * 2 - 1;
		y = random.nextDouble() * 2 - 1;
		z = random.nextDouble() * 2 - 1;
		return new Vector(x, y, z).normalize();
	}
}
