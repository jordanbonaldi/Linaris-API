package net.neferett.linaris.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;

public class WrappedUtils {

	public static WrappedDataWatcher getDefaultWatcher(World world, EntityType type) {
		
        Entity entity = world.spawnEntity(new Location(world, 0, 256, 0), type);
        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(entity).deepClone();
       
        entity.remove();
        return watcher;
    }
   
	
}
