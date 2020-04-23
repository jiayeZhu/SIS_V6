package SISv6.Utils;

import com.rabbitmq.client.Channel;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeoutException;

public class MsgEncoder {

    // used for writing Strings
    private PrintStream writer;

    private Channel channel;
    private String QUEUE_NAME;
    private String EXCHANGE_NAME;
    /*
     * Constructor
     */
    public MsgEncoder(OutputStream out) throws IOException {
        writer = new PrintStream(out);
    }
    public MsgEncoder(Channel channel, String QUEUE_NAME) throws IOException {
        this.channel = channel;
        this.QUEUE_NAME = QUEUE_NAME;
        boolean durable = true;
        this.channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
    }
    public MsgEncoder(Channel channel, String EXCHANGE_NAME, String exchangeType) throws IOException {
        this.channel = channel;
        this.EXCHANGE_NAME = EXCHANGE_NAME;
        boolean durable = true;
        this.channel.exchangeDeclare(this.EXCHANGE_NAME,exchangeType,true);
    }

    /*
     * encode the KeyValueList that represents a message into a String and send
     */
    public void sendMsg(KeyValueList kvList) throws IOException {
        if (kvList == null || kvList.size() < 1) { return; }
        if (this.writer == null) { sendMsgToMQ(kvList); return;}
        this.writer.print(kvList.encodedString() + "\n");
        this.writer.flush();
    }
    public void sendMsgToMQ(KeyValueList kv) throws IOException {
        if (kv == null || kv.size() < 1) { return; }
        this.channel.basicPublish("", this.QUEUE_NAME, null, kv.encodedString().getBytes());
    }
    public void sendMsgToEC(KeyValueList kv) throws IOException {
        if (kv == null || kv.size() < 1) { return; }
        this.channel.basicPublish(this.EXCHANGE_NAME, "", null, kv.encodedString().getBytes());
    }
    public void close() throws IOException, TimeoutException { this.channel.close(); }
}