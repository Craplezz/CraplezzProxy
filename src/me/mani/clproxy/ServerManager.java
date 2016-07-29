package me.mani.clproxy;

import me.mani.clapi.connection.server.ClientConnection;
import me.mani.clproxy.server.ServerInfoBroadcastServer;
import me.mani.clproxy.server.packet.ServerInfoDataPacket;
import me.mani.clproxy.util.CachedServerInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class ServerManager {

    private ServerInfoBroadcastServer broadcastServer;
    private Map<ClientConnection, String> clientServerNames = new HashMap<>();
    private Map<String, CachedServerInfo> serverInfos = new HashMap<>();

    public ServerManager() {
        broadcastServer = new ServerInfoBroadcastServer();
    }

    public void updateServerInfo(ClientConnection clientConnection, CachedServerInfo serverInfo) {
        // Check for first update
        if (!clientServerNames.containsKey(clientConnection)) {
            clientServerNames.put(clientConnection, serverInfo.getServerName());
        }
        serverInfos.put(serverInfo.getServerName(), serverInfo);

        System.out.println("Clients: " + broadcastServer.getClientAcceptor().getClients().size());

        // Broadcast to others
        broadcastServerInfo(serverInfo);
    }

    private void broadcastServerInfo(CachedServerInfo serverInfo) {
        for (ClientConnection connectedClientConnection : broadcastServer.getClientAcceptor().getClients()) {
            connectedClientConnection.sendPacket(new ServerInfoDataPacket(serverInfo));
        }
    }

    public void sendExistingServerInfos(ClientConnection clientConnection) {
        for (CachedServerInfo serverInfo : serverInfos.values()) {
            clientConnection.sendPacket(new ServerInfoDataPacket(serverInfo));
        }
    }

    public void removeClient(ClientConnection clientConnection) {
        if (clientServerNames.containsKey(clientConnection)) {
            if (serverInfos.containsKey(clientServerNames.get(clientConnection))) {
                CachedServerInfo serverInfo;
                (serverInfo = serverInfos.get(clientServerNames.get(clientConnection))).setOffline(true);
                broadcastServerInfo(serverInfo);
            }
        }
        clientServerNames.remove(clientConnection);
    }

}
