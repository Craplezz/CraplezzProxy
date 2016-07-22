package me.mani.clproxy.listener;

import me.mani.clproxy.Proxy;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class ServerConnectListener implements Listener {

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {

        Proxy.getServerManager().sendServerInfos();

    }

}
