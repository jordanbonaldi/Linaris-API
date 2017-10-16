package net.neferett.linaris.api.SocketEvent;

import net.neferett.socket.events.manager.EventListener;
import net.neferett.socket.events.manager.SocketEvent;
import net.neferett.socket.packet.Packet;
import net.neferett.socket.packet.event.SendMessageEvent;

public class MsgEvents implements EventListener {

	@SocketEvent
	public void onSend(final SendMessageEvent e) {
		final Packet p = e.getPacket();
		p.sendMessage();
	}

}
