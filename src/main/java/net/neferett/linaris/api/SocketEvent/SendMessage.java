package net.neferett.linaris.api.SocketEvent;

import java.io.IOException;

import net.neferett.socket.SockClient;
import net.neferett.socket.packet.Packet;
import net.neferett.socket.packet.PacketAction;

public class SendMessage {

	private SockClient		client;
	private final String	message;
	private final Packet	p;

	public SendMessage(final String addr, final int port, final String message) {
		this.client = null;
		try {
			this.client = new SockClient(addr, port, new MsgEvents());
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.message = message;
		this.p = new Packet(PacketAction.SEND);
		this.p.setMessage(this.message);
	}

	public void build() {
		this.client.addPacket(this.p);
		this.client.buildThread().start();
	}

	public SockClient getClient() {
		return this.client;
	}

	public String getMessage() {
		return this.message;
	}

	public Packet getPacket() {
		return this.p;
	}

}
