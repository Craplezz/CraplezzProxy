package me.mani.clproxy.server;

import me.mani.clapi.connection.packet.Packet;
import me.mani.clapi.connection.server.ClientConnection;
import me.mani.clapi.connection.server.Server;
import me.mani.clproxy.Proxy;
import me.mani.clproxy.server.packet.ServerInfoDataPacket;

/**
 * @author Overload
 * @version 1.0
 */
public class ServerInfoBroadcastServer extends Server {

    public ServerInfoBroadcastServer() {
        super(2424);

        Packet.registerPacket(ServerInfoDataPacket.class, (byte) 0);
    }

    @Override
    public void onClientConnect(ClientConnection clientConnection) {
        System.out.println("[SINFO] Client connected!");
    }

    @Override
    public void onClientDisconnect(ClientConnection clientConnection) {
        System.out.println("[SINFO] Client disconnected!");
    }

    @Override
    public void onPacketRecieve(ClientConnection clientConnection, Packet packet) {
        System.out.println("[SINFO] Packet recieved!");
        System.out.println(packet);
        if (packet instanceof ServerInfoDataPacket) {
            Proxy.getServerManager().updateServerInfo(clientConnection.getSocket().getInetAddress(), ((ServerInfoDataPacket) packet).getServerInfo());
        }
    }

}
