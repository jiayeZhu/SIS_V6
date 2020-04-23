package SISv6.Utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class MsgDecoder {
    // used for reading Strings
    private BufferedReader reader;

    private Channel channel;
    private Channel initChannel; //channel for receiving initializer message
    private String QUEUE_NAME;
    private String EXCHANGE_NAME;
    private String ECQUEUE_NAME;
    /*
     * Constructor
     */
    public MsgDecoder(InputStream in) throws IOException {
        reader = new BufferedReader(new InputStreamReader(in));
    }
    public MsgDecoder(Channel channel, String QUEUE_NAME) throws IOException {
        this.channel = channel;
        this.QUEUE_NAME = QUEUE_NAME;
        boolean durable = true;
        this.channel.queueDeclare(this.QUEUE_NAME, durable, false, false, null);
    }
    public MsgDecoder(Connection connection, String QUEUE_NAME, String EXCHANGE_NAME, String exchangeType) throws IOException {
        this.channel = connection.createChannel();
        this.initChannel = connection.createChannel();
        this.QUEUE_NAME = QUEUE_NAME;
        this.EXCHANGE_NAME = EXCHANGE_NAME;
        boolean durable = true;
        this.channel.queueDeclare(this.QUEUE_NAME, durable, false, false, null);
        this.initChannel.exchangeDeclare(this.EXCHANGE_NAME,exchangeType, true);
        this.ECQUEUE_NAME = this.initChannel.queueDeclare().getQueue();
        this.initChannel.queueBind(this.ECQUEUE_NAME, this.EXCHANGE_NAME, "");
    }
    /*
     * read and decode the message into KeyValueList
     */
    public KeyValueList getMsg() throws Exception {
        KeyValueList kvList = new KeyValueList();
        StringBuilder builder = new StringBuilder();

        String message = reader.readLine();

        if (message != null && message.length() > 2) {

            builder.append(message);

            while (message != null && !message.endsWith(")")) {
                message = reader.readLine();
                builder.append("\n" + message);
            }

            kvList = KeyValueList
                    .decodedKV(builder.substring(1, builder.length() - 1));
        }
        return kvList;
    }

    public void setMQConsumer(DeliverCallback cb) throws IOException {
        this.channel.basicConsume(this.QUEUE_NAME, true, cb, consumerTag -> { });
        if (this.initChannel != null && this.ECQUEUE_NAME != null) {
            this.initChannel.basicConsume(this.ECQUEUE_NAME, true, cb, consumerTag -> { });
        }
    }
    public void close() throws IOException, TimeoutException { this.channel.close(); }
}