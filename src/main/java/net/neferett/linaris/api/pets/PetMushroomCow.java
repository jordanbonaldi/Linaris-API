package net.neferett.linaris.api.pets;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityTeleport;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import net.neferett.linaris.utils.WrappedUtils;

public class PetMushroomCow extends Pet {

	int entityNameID;
	int entityID;
	Location lastLocation;
	int noMoveTicks;

	public PetMushroomCow(Player player) {
		super("Vache Champignon", "§bVache Champignons", player);
		Random rand = new Random();
		this.entityNameID = rand.nextInt(Integer.MAX_VALUE);
		this.entityID = rand.nextInt(Integer.MAX_VALUE);
		this.noMoveTicks = 0;
	}
	
	public void spawnPacket(Player target) {
		
		WrapperPlayServerSpawnEntityLiving spawnPacket;
		spawnPacket = new WrapperPlayServerSpawnEntityLiving();
		spawnPacket.setType(EntityType.MUSHROOM_COW);


		Location loc = getShoulder();
		
		WrappedDataWatcher watcher = WrappedUtils.getDefaultWatcher(loc.getWorld(), EntityType.MUSHROOM_COW);
		
		watcher.setObject(1, (short)1);

		watcher.setObject(4, (byte)1);
		
		watcher.setObject(12, (byte)-1);
		
		watcher.setObject(15, (byte)1);
		
		
		spawnPacket.setMetadata(watcher);
		spawnPacket.setX(loc.getX());
		spawnPacket.setY(loc.getY());
		spawnPacket.setZ(loc.getZ());
		spawnPacket.setHeadPitch(target.getLocation().getPitch());
		spawnPacket.setHeadYaw(target.getLocation().getYaw());
		spawnPacket.setYaw(target.getLocation().getYaw());
		spawnPacket.setEntityID(entityID);
		spawnPacket.sendPacket(target);
		
		WrapperPlayServerSpawnEntityLiving spawnNamePacket;
		spawnNamePacket = new WrapperPlayServerSpawnEntityLiving();
		spawnNamePacket.setType(EntityType.ARMOR_STAND);
		
		
		
		WrappedDataWatcher nameWatcher = WrappedUtils.getDefaultWatcher(loc.getWorld(), EntityType.ARMOR_STAND);
		
		nameWatcher.setObject(0, (byte)0x20);
		nameWatcher.setObject(1, (short)1);
		
		nameWatcher.setObject(2, getName());
		nameWatcher.setObject(3, (byte)1);
		nameWatcher.setObject(4, (byte)1);
		
		nameWatcher.setObject(10, (byte)0x08);
		

		spawnNamePacket.setMetadata(nameWatcher);
		spawnNamePacket.setX(loc.getX());
		spawnNamePacket.setY(loc.getY()-1.3D+0.5D);
		spawnNamePacket.setZ(loc.getZ());
		spawnNamePacket.setHeadPitch(target.getLocation().getPitch());
		spawnNamePacket.setHeadYaw(target.getLocation().getYaw());
		spawnNamePacket.setYaw(target.getLocation().getYaw());
		spawnNamePacket.setEntityID(entityNameID);
		spawnNamePacket.sendPacket(target);
		
	}
	
	public void dispawnPacket(Player target) {
		WrapperPlayServerEntityDestroy dispawnPacket;
		dispawnPacket = new WrapperPlayServerEntityDestroy();
		dispawnPacket.setEntityIds(new int[] { entityID, entityNameID });
		dispawnPacket.sendPacket(target);
	}

	@Override
	public boolean stayAlive() {
		if (player == null)
			return false;
		if (!player.isOnline())
			return false;
		return true;
	}

	@Override
	public void onSpawn(Player target) {
		spawnPacket(target);
	}

	@Override
	public void onDispawn(Player target) {
		dispawnPacket(target);
	}

	@Override
	public void onSpawn() {
		
	}

	@Override
	public void onDispawn() {
		
	}

	@Override
	public void onUpdate() {
		
		if (!isCrounched()) {

			
			Location loc = getShoulder();
			if (lastLocation == null)
				lastLocation = loc.clone();
			
		
			
			WrapperPlayServerEntityTeleport entityTeleport = new WrapperPlayServerEntityTeleport();
			entityTeleport.setEntityID(entityID);
			entityTeleport.setX(loc.getX());
			entityTeleport.setY(loc.getY());
			entityTeleport.setZ(loc.getZ());
			entityTeleport.setPitch(player.getLocation().getPitch());
			entityTeleport.setYaw(player.getLocation().getYaw());
			
			
			WrapperPlayServerEntityTeleport namerTeleport = new WrapperPlayServerEntityTeleport();
			namerTeleport.setEntityID(entityNameID);
			namerTeleport.setX(loc.getX());
			namerTeleport.setY(loc.getY()-1.3D+0.5D);
			namerTeleport.setZ(loc.getZ());
			namerTeleport.setPitch(player.getLocation().getPitch());
			namerTeleport.setYaw(player.getLocation().getYaw());
	
			
			if (!lastLocation.equals(loc))
				for (Player p : getSeePlayers()) {
					namerTeleport.sendPacket(p);
					entityTeleport.sendPacket(p);
				}
			lastLocation = loc.clone();
		}
	}

	@Override
	public void onCrounch() {}

	@Override
	public void onUnCrounch() {}
}
