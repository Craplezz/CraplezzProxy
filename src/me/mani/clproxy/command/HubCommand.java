package me.mani.clproxy.command;

import me.mani.clproxy.Proxy;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {

	public HubCommand() {
		super("hub", "proxy.hub", "l", "lobby");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer))
			return;
		ProxiedPlayer player = (ProxiedPlayer) sender;
		
		ServerInfo emptiestServer = null;
		for (ServerInfo server : Proxy.getInstance().getLobbies())
			if (emptiestServer == null || emptiestServer.getPlayers().size() > server.getPlayers().size())
				emptiestServer = server;
		
		player.connect(emptiestServer);
	}	
	
}
