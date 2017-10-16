package net.neferett.linaris.listeners;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.ghostplayers.GhostManager;

public class NewDamagerListener extends PacketAdapter {

	public NewDamagerListener(final BukkitAPI api) {
		super(api, PacketType.Play.Client.USE_ENTITY);
	}

	public boolean exist(final World w, final int id) {
		for (final Entity ent : w.getLivingEntities())
			if (ent.getEntityId() == id)
				return true;
		return false;
	}

	public LivingEntity getLookingAtEntity(final Player p, final int maxDistance) {
		final List<LivingEntity> entities = p.getNearbyEntities(maxDistance, maxDistance, maxDistance).stream()
				.filter(e -> e instanceof LivingEntity).map(e -> (LivingEntity) e).collect(Collectors.toList());

		final Iterator<Block> itr = new BlockIterator(p, maxDistance);

		final Location entityLoc = new Location(null, 0, 0, 0);
		final Location playerLoc = p.getLocation();
		Block block;
		int bx, by, bz;
		double ex, ey, ez;

		final double blockOffset = 0.5;
		final double vectorOffset = 0.325;
		final double heightOffset = 1;
		final double heightCheckOffset = 1;
		while (itr.hasNext()) {
			block = itr.next();
			if (block.getType().isSolid())
				break;
			bx = block.getX();
			by = block.getY();
			bz = block.getZ();
			for (final LivingEntity entity : entities) {
				entity.getLocation(entityLoc);

				ex = entityLoc.getX();
				ey = entityLoc.getY();
				ez = entityLoc.getZ();

				if (bx + 0.5 - blockOffset <= ex && bx + 0.5 + blockOffset >= ex && bz + 0.5 - blockOffset <= ez
						&& bz + 0.5 + blockOffset >= ez && by - heightCheckOffset <= ey
						&& by + heightCheckOffset >= ey) {
					entityLoc.add(0, heightOffset, 0);

					if (playerLoc.add(0, p.getEyeHeight(), 0).subtract(entityLoc).toVector().normalize()
							.crossProduct(playerLoc.getDirection().normalize()).length() < vectorOffset)
						return entity;
				}
			}
		}

		return null;
	}

	@Override
	public void onPacketReceiving(final PacketEvent event) {
		final WrapperPlayClientUseEntity use = new WrapperPlayClientUseEntity(event.getPacket());

		if (use.getType() == EntityUseAction.ATTACK) {

			final Player player = event.getPlayer();

			if (player.getName().contains("UNKNOWN[/"))
				return;

			if (GhostManager.isGhost(player.getName()))
				return;

			if (this.exist(player.getWorld(), use.getTarget()))
				return;

			final LivingEntity entity = this.getLookingAtEntity(player, 4);

			if (entity != null) {
				use.setTarget(entity.getEntityId());
				event.setPacket(use.getHandle());
			}

		}

	}

}
