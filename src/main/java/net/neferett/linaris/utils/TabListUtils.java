package net.neferett.linaris.utils;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class TabListUtils {

	/**
	 * Modifie le header/footer du tab, en l'attente de l'API officielle
	 * 
	 * @param player
	 * @param header
	 * @param footer
	 */
	public static void sendTablist(final Player p, String header, String footer) {
		if (header == null) {
			header = "";
		}
		if (footer == null) {
			footer = "";
		}
		final PlayerConnection Connection = ((CraftPlayer) p).getHandle().playerConnection;
		final IChatBaseComponent tabheader = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
		final IChatBaseComponent tabfooter = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
		final PacketPlayOutPlayerListHeaderFooter THpacket = new PacketPlayOutPlayerListHeaderFooter(tabheader);
		try {
			final Field f = THpacket.getClass().getDeclaredField("b");
			f.setAccessible(true);
			f.set(THpacket, tabfooter);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			Connection.sendPacket(THpacket);
		}
		Connection.sendPacket(THpacket);
	}
}
