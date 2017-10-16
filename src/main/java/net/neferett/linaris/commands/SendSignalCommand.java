package net.neferett.linaris.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.SocketEvent.SendMessage;
import net.neferett.linaris.utils.json.JsonCreator;

public class SendSignalCommand implements CommandExecutor {

	private final String	addr;
	private boolean			ban;
	private JsonCreator		js;
	private final int		port;
	private int				violation;

	public SendSignalCommand(final String addr, final int port) {
		this.addr = addr;
		this.port = port;
	}

	@Override
	public boolean onCommand(final CommandSender arg0, final Command arg1, final String arg2, final String[] arg3) {
		if (arg3.length != 4)
			return false;
		else if (arg0 instanceof Player)
			return false;

		this.violation = Integer.parseInt(arg3[2]);
		this.ban = Boolean.parseBoolean(arg3[3]);

		this.js = new JsonCreator(arg3[0], this.violation, arg3[1], BukkitAPI.get().getServerInfos().getServerName(),
				this.ban);

		new SendMessage(this.addr, this.port, this.js.build()).build();

		return true;
	}

}
