package net.neferett.linaris.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.neferett.linaris.PlayersHandler.Players;
import net.neferett.linaris.api.PlayerData;

public abstract class CommandHandler {

	static List<CommandHandler>	cmds	= new ArrayList<>();

	static CommandHandler		instance;

	public static CommandHandler getCmdByName(final String name) {
		return cmds.stream()
				.filter(cmd -> cmd.getCmd().equals(name) || (cmd.getAlias().stream().filter(str -> str.equals(name))
						.collect(Collectors.toList()).size() == 0 ? false : true))
				.collect(Collectors.toList())
				.size() == 0 ? null : cmds.stream().filter(cmd -> cmd.getCmd().equals(name) || (cmd.getAlias().stream()
						.filter(str -> str.equals(name)).collect(Collectors.toList()).size() == 0 ? false : true))
						.collect(Collectors.toList()).get(0);
	}

	public static List<CommandHandler> getCmds() {
		return cmds;
	}

	private final List<String>			alias;

	private final String				cmd;

	private String						errormsg;

	private final Predicate<PlayerData>	PredicateOnPLayerData;

	public CommandHandler(final String cmd, final Predicate<PlayerData> p, final String... alias) {
		this.cmd = cmd;
		this.PredicateOnPLayerData = p;
		this.alias = Arrays.asList(alias);
		this.setErrorMsg("§cVous n'avez pas la permission de faire cela");
		cmds.add(this);
	}

	public abstract void cmd(Players player, String cmd, List<String> args);

	public String errorMessage() {
		return this.errormsg;
	}

	public List<String> getAlias() {
		return this.alias;
	}

	public String getCmd() {
		return this.cmd;
	}

	public Predicate<PlayerData> getPredicateOnPLayerData() {
		return this.PredicateOnPLayerData;
	}

	public abstract void onError(Players p);

	public void setErrorMsg(final String msg) {
		this.errormsg = msg;
	}
}
