package me.mani.clproxy;

import me.mani.clproxy.server.ServerInfoBroadcastServer;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class ServerManager {

    private ServerInfoBroadcastServer broadcastServer;

    public ServerManager() {
        broadcastServer = new ServerInfoBroadcastServer();
    }

}
