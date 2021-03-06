package org.xlet.strawberry.persistent.mongo.dao.message.server;

import org.xlet.strawberry.core.message.persistent.MessagePersistentProvider;
import org.xlet.strawberry.core.message.server.ReadyMessage;
import org.xlet.strawberry.persistent.mongo.domain.message.server.MongoReadyMessage;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-19 下午2:07.
 * Summary: mongo ready message dao.
 */
@Component(value = "mongoReadyMessagePersistentProvider")
public class MongoReadyMessageDao extends BasicDAO<MongoReadyMessage, ObjectId> implements MessagePersistentProvider<ReadyMessage> {

    /**
     * constructor.
     *
     * @param ds data store.
     */
    @Autowired
    public MongoReadyMessageDao(@Qualifier("dataStore") Datastore ds) {
        super(ds);
    }

    @Override
    public void save(ReadyMessage message) {
        MongoReadyMessage mongoMessage = new MongoReadyMessage(message);
        this.save(mongoMessage);
    }
}
