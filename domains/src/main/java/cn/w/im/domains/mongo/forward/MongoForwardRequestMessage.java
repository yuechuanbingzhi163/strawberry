package cn.w.im.domains.mongo.forward;

import cn.w.im.domains.messages.forward.ForwardRequestMessage;
import cn.w.im.domains.mongo.MongoDomain;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-27 下午9:22.
 * Summary: ForwardRequestMessage mongo defined.
 */
@Entity("forwardRequestMessage")
public class MongoForwardRequestMessage extends ForwardRequestMessage implements MongoDomain {

    @Id
    private ObjectId id;

    private long persistentDate;

    /**
     * constructor.
     */
    public MongoForwardRequestMessage() {
        this.persistentDate = new Date().getTime();
    }

    /**
     * constructor.
     *
     * @param message ForwardRequestMessage.
     */
    public MongoForwardRequestMessage(ForwardRequestMessage message) {
        this();
        this.setMessageType(message.getMessageType());
        this.setSendTime(message.getSendTime());
        this.setReceivedTime(message.getReceivedTime());
    }


    @Override
    public ObjectId getId() {
        return this.id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public long getPersistentDate() {
        return this.persistentDate;
    }

    @Override
    public void setPersistentDate(long persistentDate) {
        this.persistentDate = persistentDate;
    }
}
