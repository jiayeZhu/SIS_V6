package SISv6.Components.Gesture;

import SISv6.Utils.MQConnection;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.concurrent.TimeoutException;

public class CreateGesture {
    // scope of this component
    private static final String SCOPE = "SIS.Scope1";
    // name of this component
    private static final String NAME = "Gesture";
    // messages types that can be handled by this component
    private static List<String> TYPES = new ArrayList<>(Arrays.asList("Setting", "Confirm"));

    private final static String SERVERS_STRING = PropertyResourceBundle.getBundle("Settings").getString("RMQ_Servers");
    private static Address[] addresses;
    private static final String COMPONENT_QUEUE_NAME = SCOPE+"."+NAME;
    private static final String SIS_QUEUE_NAME = "SISMQ";

    public static void main(String[] args) {
        addresses = MQConnection.ParseAddress(SERVERS_STRING);
        if(addresses.length==0){
            System.exit(1);
            return;
        }
        try {
            GestureComponent gestureComponent = new GestureComponent(addresses, COMPONENT_QUEUE_NAME, SIS_QUEUE_NAME, SCOPE, NAME, TYPES);
            DeliverCallback cb = (consumerTag, delivery)->{
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                if (message.length() > 2){ gestureComponent.handleMessage(message); }
            };
            gestureComponent.getDecoder().setMQConsumer(cb);
            gestureComponent.connect();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
