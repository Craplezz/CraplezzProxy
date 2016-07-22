package me.mani.clproxy;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import me.mani.clproxy.command.HubCommand;
import me.mani.clproxy.command.TeamChatCommand;
import me.mani.clproxy.listener.ServerConnectListener;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class Proxy extends Plugin {

	private static Proxy instance;
	private static LocaleManager localeManager;
	private static ServerManager serverManager;

	private MongoClient mongoClient;
	private List<ServerInfo> lobbies = new ArrayList<>();
	
	@Override
	public void onEnable() {
		instance = this;
		
		getProxy().getPluginManager().registerCommand(this, new HubCommand());
		getProxy().getPluginManager().registerCommand(this, new TeamChatCommand());

		getProxy().getPluginManager().registerListener(this, new ServerConnectListener());

		mongoClient = new MongoClient(new ServerAddress("craplezz.de", 27017), Arrays.asList(MongoCredential.createCredential("Overload", "admin", "1999mani123".toCharArray())));

		// TODO: Remove hard coded login
		localeManager = new LocaleManager(mongoClient, "todo", "general");
		for (Entry<String, ServerInfo> server : getProxy().getServers().entrySet())
			if (server.getKey().startsWith("lobby"))
				lobbies.add(server.getValue());
		serverManager = new ServerManager(mongoClient, "todo", "general");

		getProxy().getScheduler().schedule(this, () -> {

			serverManager.sendServerInfos();

		}, 0L, 30L, TimeUnit.SECONDS);
	}

	public List<ServerInfo> getLobbies() {
		return lobbies;
	}
	
	public static Proxy getInstance() {
		return instance;
	}

	public static LocaleManager getLocaleManager() {
		return localeManager;
	}

	public static ServerManager getServerManager() {
		return serverManager;
	}

}
