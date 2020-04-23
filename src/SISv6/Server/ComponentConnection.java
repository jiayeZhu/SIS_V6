package SISv6.Server;

import SISv6.Utils.MQConnection;
import SISv6.Utils.MsgDecoder;
import SISv6.Utils.MsgEncoder;

/**
 *
 * ComponentConnection represents connections
 * that are related to a component
 * see MsgEncoder, MsgDecoder, KeyValueList for details
 *
 * @author dexterchen
 *
 */
public class ComponentConnection {
    // message writer for a component
    MsgEncoder encoder;
    // message reader for a component
    MsgDecoder decoder;

    private MQConnection mq;

    public MQConnection getMq() {
        return mq;
    }

    public ComponentConnection() {
        // TODO Auto-generated constructor stub
    }
    public ComponentConnection(MQConnection mq){
        this.mq = mq;
    }

    public ComponentConnection(MsgEncoder e, MsgDecoder d) {
        // TODO Auto-generated constructor stub
        encoder = e;
        decoder = d;
    }
}