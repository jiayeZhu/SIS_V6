package SISv6.Utils;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ExceptionHandler;

import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.concurrent.TimeoutException;

public class MQConnection {
    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private MyHandler exceptionHandler = new MyHandler();
    static long NetworkRecoveryInterval = Long.parseLong(PropertyResourceBundle.getBundle("Settings").getString("Network_Recovery_Interval"));
    public MQConnection(String MQ_HOST) throws IOException, TimeoutException {
        this.factory.setHost(MQ_HOST);
        this.factory.setNetworkRecoveryInterval(NetworkRecoveryInterval);
        this.factory.setExceptionHandler(exceptionHandler);
        this.connection = this.factory.newConnection();
    }
    public MQConnection(Address[] addresses) throws IOException, TimeoutException {
        this.factory.setNetworkRecoveryInterval(NetworkRecoveryInterval);
        this.factory.setExceptionHandler(exceptionHandler);
        this.connection = this.factory.newConnection(addresses);
    }
    public Connection getConnection() { return connection; }
    public ConnectionFactory getFactory() { return factory; }
    public void close() throws IOException { this.connection.close(); }
    public static Address[] ParseAddress(String AddressString){
        String[] Servers = AddressString.split(",");
        if(Servers.length == 0 || !AddressString.contains(":")){
            System.err.println("RMQ_Servers are not set properly in the Settings.properties!!");
            return new Address[0];
        }
        Address[] addresses = new Address[Servers.length];
        for (int i = 0; i < Servers.length; i++) {
            String ip = Servers[i].split(":")[0];
            int port = Integer.parseInt(Servers[i].split(":")[1]);
            addresses[i] = new Address(ip,port);
        }
        return addresses;
    }
}

