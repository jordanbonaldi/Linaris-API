package net.neferett.linaris.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class ScoreboardSign {

	static Map<String, ScoreboardSign> assigned = new HashMap<>();

	public static ScoreboardSign get(final Player player) {
		return assigned.get(player.getName());
	}

	private boolean			created	= false;

	private final String[]	lines	= new String[100];
	private String			objectiveName;
	private final Player	player;

	public ScoreboardSign(final Player player, final String objectiveName) {
		this.player = player;
		this.objectiveName = objectiveName;
		this.assign(player);
	}

	public void assign(final Player player) {
		final ScoreboardSign old = get(player);
		if (old != null)
			old.destroy();
		assigned.put(player.getName(), this);
		this.create();
	}

	public void create() {
		if (this.created)
			return;

		final PlayerConnection player = this.getPlayer();
		player.sendPacket(this.createObjectivePacket(0, this.objectiveName));
		player.sendPacket(this.setObjectiveSlot());
		int i = 0;
		while (i < this.lines.length)
			this.sendLine(i++);

		this.created = true;
	}

	/*
	 * Factories
	 */
	private PacketPlayOutScoreboardObjective createObjectivePacket(final int mode, final String displayName) {
		final PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
		try {
			// Nom de l'objectif
			final Field name = packet.getClass().getDeclaredField("a");
			name.setAccessible(true);
			name.set(packet, this.player.getName());

			// Mode
			// 0 : cr�er
			// 1 : Supprimer
			// 2 : Mettre � jour
			final Field modeField = packet.getClass().getDeclaredField("d");
			modeField.setAccessible(true);
			modeField.set(packet, mode);

			if (mode == 0 || mode == 2) {
				final Field displayNameField = packet.getClass().getDeclaredField("b");
				displayNameField.setAccessible(true);
				displayNameField.set(packet, displayName);

				final Field display = packet.getClass().getDeclaredField("c");
				display.setAccessible(true);
				display.set(packet, IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
			}
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return packet;
	}

	public void destroy() {
		if (!this.created)
			return;

		this.getPlayer().sendPacket(this.createObjectivePacket(1, null));

		this.created = false;

		assigned.remove(this.player.getName());
	}

	public String getLine(final int line) {
		return this.lines[line];
	}

	public int getLine(final String line) {
		for (int i = 0; i < this.lines.length; i++)
			if (this.lines[i] != null && this.lines[i].equals(line))
				return i;
		return -1;

	}

	private PlayerConnection getPlayer() {
		return ((CraftPlayer) this.player).getHandle().playerConnection;
	}

	public void removeLine(final int line) {
		final String oldLine = this.getLine(line);
		if (oldLine != null && this.created)
			this.getPlayer().sendPacket(this.removeLine(oldLine));

		this.lines[line] = null;
	}

	private PacketPlayOutScoreboardScore removeLine(final String line) {
		return new PacketPlayOutScoreboardScore(line);
	}

	private void sendLine(final int line) {
		if (!this.created)
			return;

		final int score = line;
		final String val = this.lines[line];
		this.getPlayer().sendPacket(this.sendScore(val, score));
	}

	private PacketPlayOutScoreboardScore sendScore(final String line, final int score) {
		final PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line);
		try {
			final Field name = packet.getClass().getDeclaredField("b");
			name.setAccessible(true);
			name.set(packet, this.player.getName());

			final Field scoreField = packet.getClass().getDeclaredField("c");
			scoreField.setAccessible(true);
			scoreField.set(packet, score); // SideBar

			final Field action = packet.getClass().getDeclaredField("d");
			action.setAccessible(true);
			action.set(packet, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return packet;
	}

	public void setLine(final int line, final String value) {
		final String oldLine = this.getLine(line);
		if (oldLine != null && this.created)
			this.getPlayer().sendPacket(this.removeLine(oldLine));

		this.lines[line] = value;
		this.sendLine(line);
	}

	public void setObjectiveName(final String name) {
		this.objectiveName = name;
		if (this.created)
			this.getPlayer().sendPacket(this.createObjectivePacket(2, name));
	}

	private PacketPlayOutScoreboardDisplayObjective setObjectiveSlot() {
		final PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();
		try {
			// Slot de l'objectif
			final Field position = packet.getClass().getDeclaredField("a");
			position.setAccessible(true);
			position.set(packet, 1); // SideBar

			final Field name = packet.getClass().getDeclaredField("b");
			name.setAccessible(true);
			name.set(packet, this.player.getName());
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return packet;
	}
}