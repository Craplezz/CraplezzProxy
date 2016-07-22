package me.mani.clproxy;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import me.mani.clproxy.util.CachedServerInfo;
import me.mani.clproxy.util.ConvertUtils;
import net.md_5.bungee.api.config.ServerInfo;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Schuckmann on 06.05.2016.
 */
public class ServerManager {

    private MongoCollection<Document> mongoCollection;

    public ServerManager(MongoClient mongoClient, String database, String collection) {
        mongoCollection = mongoClient.getDatabase(database).getCollection(collection);
    }

    public void sendServerInfos(List<CachedServerInfo> serverInfos) {
        Document document = new Document();
        for (CachedServerInfo serverInfo : serverInfos)
            document.append(serverInfo.getServerName(), ConvertUtils.toDocument(serverInfo));
        if (mongoCollection.find(new Document("type", "servers")).first() == null)
            mongoCollection.insertOne(new Document("type", "servers").append("data", document));
        else
            mongoCollection.updateOne(new Document("type", "servers"), new Document("$set", new Document("data", document)));
    }

    public void sendServerInfos() {
        List<CachedServerInfo> serverInfos = new ArrayList<>();
        for (ServerInfo serverInfo : Proxy.getInstance().getProxy().getServers().values())
            serverInfo.ping(((serverPing, throwable) -> {
                serverInfos.add(new CachedServerInfo(serverInfo.getName(), serverPing.getPlayers().getMax(), serverPing.getPlayers().getOnline(), serverPing.getDescription()));
            }));
        sendServerInfos(serverInfos);
    }

}
