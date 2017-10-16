package net.neferett.linaris.utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class UtilParticles {

	private final static int DEF_RADIUS = 128;

	public static void display(final int red, final int green, final int blue, final Location location) {
		display(Particles.REDSTONE, red, green, blue, location, 1);
	}

	public static void display(final Particles effect, final double x, final double y, final double z,
			final Location location, final int amount) {
		effect.display((float) x, (float) y, (float) z, 0f, amount, location, 128);
	}

	public static void display(final Particles effect, final int red, final int green, final int blue,
			final Location location) {
		display(effect, red, green, blue, location, 1);
	}

	public static void display(final Particles effect, final int red, final int green, final int blue,
			final Location location, final int amount) {
		for (int i = 0; i < amount; i++)
			effect.display(new Particles.OrdinaryColor(red, green, blue), location, DEF_RADIUS);
	}

	public static void display(final Particles effect, final Location location) {
		display(effect, location, 1);
	}

	public static void display(final Particles effect, final Location location, final int amount) {
		effect.display(0, 0, 0, 0, amount, location, 128);
	}

	public static void display(final Particles effect, final Location location, final int amount, final float speed) {
		effect.display(0, 0, 0, speed, amount, location, 128);
	}

	public static void drawParticleLine(final Location from, final Location to, final Particles effect,
			final int particles, final int r, final int g, final int b) {
		final Location location = from.clone();
		final Location target = to.clone();
		final double amount = particles;
		final Vector link = target.toVector().subtract(location.toVector());
		final float length = (float) link.length();
		link.normalize();

		final float ratio = length / particles;
		final Vector v = link.multiply(ratio);
		final Location loc = location.clone().subtract(v);
		int step = 0;
		for (int i = 0; i < particles; i++) {
			if (step >= amount)
				step = 0;
			step++;
			loc.add(v);
			if (effect == Particles.REDSTONE)
				effect.display(new Particles.OrdinaryColor(r, g, b), loc, 128);
			else
				effect.display(0, 0, 0, 0, 1, loc, 128);
		}
	}

	public static void generateParticleArroundPlayer(final Particles p, final Location l, final float rayon,
			final int vitesse, final int ecart) {

		getCircle(l, rayon, ecart).forEach(e -> {
			display(p, e, 100, vitesse);
		});
	}

	public static ArrayList<Location> getCircle(final Location center, final double radius, final int amount) {
		final World world = center.getWorld();
		final double increment = 2 * Math.PI / amount;
		final ArrayList<Location> locations = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			final double angle = i * increment;
			final double x = center.getX() + radius * Math.cos(angle);
			final double z = center.getZ() + radius * Math.sin(angle);
			locations.add(new Location(world, x, center.getY(), z));
		}
		return locations;
	}

}