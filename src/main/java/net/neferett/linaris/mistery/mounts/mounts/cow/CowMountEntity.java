package net.neferett.linaris.mistery.mounts.mounts.cow;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Cow;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.server.v1_8_R3.EntityCow;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;
import net.neferett.linaris.mistery.mounts.Mount;

public class CowMountEntity extends EntityCow {

	Mount mount;
	
    public CowMountEntity(World world) {
        super(world);
    }
    
    public CowMountEntity(World world,Mount mount) {
        super(world);
        this.mount = mount;
    }

    public static Cow spawn(Location location) {
        World mcWorld = (World) ((CraftWorld) location.getWorld()).getHandle();
        final CowMountEntity customEntity = new CowMountEntity(mcWorld);
        customEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        customEntity.setCustomNameVisible(false);
        ((CraftLivingEntity) customEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        mcWorld.addEntity(customEntity, SpawnReason.CUSTOM);
        return (CraftCow) customEntity.getBukkitEntity();
    }

    @Override
    public void g(float sideMot, float forMot) {
        if (this.passenger != null && this.passenger instanceof EntityHuman) {
        	
    		this.lastYaw = this.yaw = this.passenger.yaw;
    		this.pitch = this.passenger.pitch * 0.5F;
    		
    		setYawPitch(this.yaw, this.pitch);
            this.aI = this.aG = this.yaw;
            
            
            sideMot = ((EntityLiving) this.passenger).aZ * 0.5F;
            forMot = ((EntityLiving) this.passenger).ba;
            if (forMot <= 0.0F) {
                forMot *= 0.25F;
            }
 
            Field jump = null;
            try {
                jump = EntityLiving.class.getDeclaredField("aY");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            } catch (SecurityException e1) {
                e1.printStackTrace();
            }
            jump.setAccessible(true);
            
            if (jump != null && this.onGround) {
                try {
                    if (jump.getBoolean(this.passenger)) {
                        double jumpHeight = 0.5D;
                        this.motY = jumpHeight;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
 
            this.S = 1.0F;
            this.aK = this.bI() * 0.1F;
            if (!this.world.isClientSide) {
                this.k((float) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
                super.g(sideMot, forMot);
            }
 
            this.ay = this.az;
            double d0 = this.locX - this.lastX;
            double d1 = this.locZ - this.lastZ;
            float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
            if (f4 > 1.0F) {
                f4 = 1.0F;
            }
 
            this.az += (f4 - this.az) * 0.4F;
            this.aA += this.az;
        } else {
        	die();
        }
    }
    
}