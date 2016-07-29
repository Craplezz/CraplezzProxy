package me.mani.clproxy;

import me.mani.clapi.connection.server.ClientConnection;
import me.mani.clproxy.server.ServerInfoBroadcastServer;
import me.mani.clproxy.server.packet.ServerInfoDataPacket;
import me.mani.clproxy.util.CachedServerInfo;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class ServerManager {

    private ServerInfoBroadcastServer broadcastServer;
    private Map<String, CachedServerInfo> serverInfos = new HashMap<>();

    public ServerManager() {
        broadcastServer = new ServerInfoBroadcastServer();
    }

    public void updateServerInfo(InetAddress senderAddress, CachedServerInfo serverInfo) {
        if (Proxy.getInstance().getProxy().getServerInfo(serverInfo.getServerName()) == null) {
            for (ServerInfo info : Proxy.getInstance().getProxy().getServers().values()) {
                if (senderAddress.equals(info.getAddress().getAddress())) {
                    serverInfo.setServerName(info.getName());
                }
            }
        }
        serverInfos.put(serverInfo.getServerName(), serverInfo);

        System.out.println("Clients: " + broadcastServer.getClientAcceptor().getClients().size());

        // Broadcast to others
        for (ClientConnection clientConnection : broadcastServer.getClientAcceptor().getClients()) {
            clientConnection.sendPacket(new ServerInfoDataPacket(serverInfo));
        }
    }

}
