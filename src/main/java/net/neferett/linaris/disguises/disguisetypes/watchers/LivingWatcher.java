package net.neferett.linaris.disguises.disguisetypes.watchers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedAttribute;
import com.comphenix.protocol.wrappers.WrappedAttribute.Builder;

import net.neferett.linaris.disguises.DisguiseAPI;
import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.FlagWatcher;
import net.neferett.linaris.disguises.utilities.DisguiseUtilities;
import net.neferett.linaris.disguises.utilities.ReflectionManager;

public class LivingWatcher extends FlagWatcher {

	static Object[] list;
	static Method potionNo;

	static {
		try {
			list = (Object[]) ReflectionManager.getNmsField("MobEffectList", "byId").get(null);
			for (final Object obj : list)
				if (obj != null)
					for (final Method field : obj.getClass().getMethods())
						if (field.getReturnType() == int.class)
							if ((Integer) field.invoke(obj) > 10000) {
								potionNo = field;
								break;
							}
		} catch (final Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
	private double maxHealth;
	private boolean maxHealthSet;
	private HashSet<Integer> potionEffects = new HashSet<>();

	public LivingWatcher(Disguise disguise) {
		super(disguise);
	}

	@SuppressWarnings("deprecation")
	public void addPotionEffect(PotionEffectType potionEffect) {
		if (!this.hasPotionEffect(potionEffect)) {
			this.removePotionEffect(potionEffect);
			this.potionEffects.add(potionEffect.getId());
			this.sendPotionEffects();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public LivingWatcher clone(Disguise disguise) {
		final LivingWatcher clone = (LivingWatcher) super.clone(disguise);
		clone.potionEffects = (HashSet<Integer>) this.potionEffects.clone();
		clone.maxHealth = this.maxHealth;
		clone.maxHealthSet = this.maxHealthSet;
		return clone;
	}

	public float getHealth() {
		return (Float) this.getValue(6, 0F);
	}

	public double getMaxHealth() {
		return this.maxHealth;
	}

	public boolean getPotionParticlesRemoved() {
		return (Byte) this.getValue(8, (byte) 0) == 1;
	}

	private int getPotions() {
		final int m = 3694022;

		if (this.potionEffects.isEmpty())
			return m;

		float f1 = 0.0F;
		float f2 = 0.0F;
		float f3 = 0.0F;
		float f4 = 0.0F;
		try {
			for (final int localMobEffect : this.potionEffects) {
				final int n = (Integer) potionNo.invoke(list[localMobEffect]);
				f1 += (n >> 16 & 0xFF) / 255.0F;
				f2 += (n >> 8 & 0xFF) / 255.0F;
				f3 += (n >> 0 & 0xFF) / 255.0F;
				f4 += 1.0F;
			}
		} catch (final Exception ex) {
			ex.printStackTrace(System.out);
		}

		f1 = f1 / f4 * 255.0F;
		f2 = f2 / f4 * 255.0F;
		f3 = f3 / f4 * 255.0F;

		return (int) f1 << 16 | (int) f2 << 8 | (int) f3;
	}

	@SuppressWarnings("deprecation")
	public boolean hasPotionEffect(PotionEffectType type) {
		return this.potionEffects.contains(type.getId());
	}

	public boolean isMaxHealthSet() {
		return this.maxHealthSet;
	}

	@SuppressWarnings("deprecation")
	public void removePotionEffect(PotionEffectType type) {
		if (this.potionEffects.contains(type.getId())) {
			this.potionEffects.remove(type.getId());
			this.sendPotionEffects();
		}
	}

	public void removePotionParticles(boolean particles) {
		this.setValue(8, (byte) (particles ? 1 : 0));
		this.sendData(8);
	}

	private void sendPotionEffects() {
		this.setValue(7, this.getPotions());
		this.sendData(7);
	}

	public void setHealth(float health) {
		this.setValue(6, health);
		this.sendData(6);
	}

	public void setMaxHealth(double newHealth) {
		this.maxHealth = newHealth;
		this.maxHealthSet = true;
		if (DisguiseAPI.isDisguiseInUse(this.getDisguise()) && this.getDisguise().getWatcher() == this) {
			final PacketContainer packet = new PacketContainer(PacketType.Play.Server.UPDATE_ATTRIBUTES);
			final List<WrappedAttribute> attributes = new ArrayList<>();
			Builder builder;
			builder = WrappedAttribute.newBuilder();
			builder.attributeKey("generic.maxHealth");
			builder.baseValue(this.getMaxHealth());
			builder.packet(packet);
			attributes.add(builder.build());
			final Entity entity = this.getDisguise().getEntity();
			packet.getIntegers().write(0, entity.getEntityId());
			packet.getAttributeCollectionModifier().write(0, attributes);
			for (final Player player : DisguiseUtilities.getPerverts(this.getDisguise()))
				try {
					ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet, false);
				} catch (final InvocationTargetException e) {
					e.printStackTrace(System.out);
				}
		}
	}

}
