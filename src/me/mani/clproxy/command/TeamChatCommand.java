package me.mani.clproxy.command;

import me.mani.clproxy.Proxy;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TeamChatCommand extends Command {
	
	public TeamChatCommand() {
		super("teamchat", "proxy.teamchat.send", "t", "tc");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer))
			return;
		ProxiedPlayer player = (ProxiedPlayer) sender;
		
		for (ProxiedPlayer onlinePlayer : ProxyServer.getInstance().getPlayers())
			if (onlinePlayer.hasPermission("proxy.teamchat.recieve"))
				onlinePlayer.sendMessage(Proxy.getInstance().getLocaleManager().translate("teamchat-prefix", player.getName(), String.join(" ", args)));
	}

}
