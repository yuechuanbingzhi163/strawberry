package org.xlet.strawberry.persistent.mongo.dao.message.client;

import org.xlet.strawberry.core.message.persistent.MessagePersistentProvider;
import org.xlet.strawberry.core.message.client.ConnectMessage;
import org.xlet.strawberry.persistent.mongo.domain.message.client.MongoConnectMessage;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Creator: JackieHan.
 * DateTime: 14-1-16 下午2:34.
 * Summary: mongoConnectMessage Dao.
 */
@Component(value = "mongoConnectMessagePersistentProvider")
public class MongoConnectMessageDao extends BasicDAO<MongoConnectMessage, ObjectId> implements MessagePersistentProvider<ConnectMessage> {

    @Autowired
    protected MongoConnectMessageDao(@Qualifier("dataStore") Datastore ds) {
        super(ds);
    }

    @Override
    public void save(ConnectMessage message) {
        MongoConnectMessage mongoMessage = new MongoConnectMessage(message);
        this.save(mongoMessage);
    }
}
