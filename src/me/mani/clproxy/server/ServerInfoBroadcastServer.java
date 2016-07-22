package me.mani.clproxy.server;

import me.mani.clapi.connection.packet.Packet;
import me.mani.clapi.connection.server.ClientConnection;
import me.mani.clapi.connection.server.Server;
import me.mani.clproxy.Proxy;
import me.mani.clproxy.server.packet.ServerInfoDataPacket;
import me.mani.clproxy.server.packet.ServerInfoUpdatePacket;
import me.mani.clproxy.util.CachedServerInfo;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Map;

/**
 * @author Overload
 * @version 1.0
 */
public class ServerInfoBroadcastServer extends Server {

    public ServerInfoBroadcastServer() {
        super(2424);

        Packet.registerPacket(ServerInfoUpdatePacket.class, (byte) 0);
        Packet.registerPacket(ServerInfoDataPacket.class, (byte) 1);
    }

    @Override
    public void onPacketRecieve(ClientConnection clientConnection, Packet packet) {
        if (packet instanceof ServerInfoUpdatePacket) {
            for (Map.Entry<String, ServerInfo> entry : Proxy.getInstance().getProxy().getServers().entrySet()) {
                entry.getValue().ping(new Callback<ServerPing>() {

                    @Override
                    public void done(ServerPing serverPing, Throwable throwable) {
                        clientConnection.sendPacket(new ServerInfoDataPacket(new CachedServerInfo(
                                entry.getKey(),
                                serverPing.getPlayers().getMax(),
                                serverPing.getPlayers().getOnline(),
                                serverPing.getDescription()
                        )));
                    }

                });
            }
        }
    }

}
