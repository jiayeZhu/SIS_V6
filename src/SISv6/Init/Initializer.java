package SISv6.Init;

import SISv6.Utils.KeyValueList;
import SISv6.Utils.MQConnection;
import SISv6.Utils.MsgEncoder;
import SISv6.Utils.XMLUtils.Msg;
import SISv6.Utils.XMLUtils.XMLUtil;
import com.rabbitmq.client.Address;
import com.sun.nio.zipfs.JarFileSystemProvider;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.PropertyResourceBundle;
import java.util.concurrent.TimeoutException;
import java.util.jar.JarFile;

public class Initializer {
//    private final static String SIS_QUEUE_NAME = "SISMQ";
    private final static String SIS_EXCHANGE_NAME = "SISINIT";
    private final static String SERVERS_STRING = PropertyResourceBundle.getBundle("Settings").getString("RMQ_Servers");
    private static Address[] addresses;
    private final static int MAX_RETRY = 3;
    static KeyValueList parseFile(File file){
        try {
            JAXBContext context = JAXBContext.newInstance(Msg.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Msg msg = (Msg) unmarshaller.unmarshal(file);

            return XMLUtil.generateKV(msg);
        } catch (JAXBException e) {
            System.out.println("Parsing Init XML Fail: " + file.getName());
            return null;
        }
    }

    public static void main(String[] args) {
        addresses = MQConnection.ParseAddress(SERVERS_STRING);
        if(addresses.length==0){
            System.exit(1);
            return;
        }
        Path path = FileSystems.getDefault().getPath("InitXML");

        for (int retryAttempt = 0; retryAttempt <= MAX_RETRY; retryAttempt++) {
            try {
                MQConnection mq = new MQConnection(addresses);
                MsgEncoder encoder = new MsgEncoder(mq.getConnection().createChannel(),SIS_EXCHANGE_NAME,"fanout");
                Files.list(path).forEach(x -> {
                    KeyValueList kv = parseFile(x.toFile());
                    if (kv != null){
                        System.out.print("Try to init: ");
                        System.out.println(x);
                        try {
                            encoder.sendMsgToEC(kv);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                encoder.close();
                mq.close();
                break;
            } catch (TimeoutException | IOException e) {
                System.err.println("Failed to connect to the MQ with error: "+e.getMessage());
                if (retryAttempt < MAX_RETRY) System.out.println("Going to retry: "+(retryAttempt+1)+"/"+MAX_RETRY+" attempt.");

            }
        }
    }
}
