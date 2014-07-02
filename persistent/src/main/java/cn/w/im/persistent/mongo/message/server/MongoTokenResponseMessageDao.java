package cn.w.im.persistent.mongo.message.server;

import cn.w.im.domains.messages.server.TokenResponseMessage;
import cn.w.im.domains.mongo.server.MongoTokenResponseMessage;
import cn.w.im.persistent.MessageDao;
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
@Component(value = "mongoTokenResponseMessageDao")
public class MongoTokenResponseMessageDao extends BasicDAO<MongoTokenResponseMessage, ObjectId> implements MessageDao<TokenResponseMessage> {
    /**
     * constructor.
     *
     * @param ds data store.
     */
    @Autowired
    public MongoTokenResponseMessageDao(@Qualifier("dataStore")Datastore ds) {
        super(ds);
    }

    @Override
    public void save(TokenResponseMessage message) {
        MongoTokenResponseMessage mongoMessage = new MongoTokenResponseMessage(message);
        this.save(mongoMessage);
    }
}
