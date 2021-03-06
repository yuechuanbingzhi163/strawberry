package org.xlet.strawberry.persistent.mongo.dao.message.server;

import org.xlet.strawberry.core.message.persistent.MessagePersistentProvider;
import org.xlet.strawberry.core.message.server.TokenResponseMessage;
import org.xlet.strawberry.persistent.mongo.domain.message.server.MongoTokenResponseMessage;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-29 下午6:12.
 * Summary: MongoTokenResponseMessage dao.
 */
@Component(value = "mongoTokenResponseMessagePersistentProvider")
public class MongoTokenResponseMessageDao extends BasicDAO<MongoTokenResponseMessage, ObjectId>
        implements MessagePersistentProvider<TokenResponseMessage> {
    /**
     * constructor.
     *
     * @param ds data store.
     */
    @Autowired
    public MongoTokenResponseMessageDao(@Qualifier("dataStore") Datastore ds) {
        super(ds);
    }

    @Override
    public void save(TokenResponseMessage message) {
        MongoTokenResponseMessage mongoMessage = new MongoTokenResponseMessage(message);
        this.save(mongoMessage);
    }
}
