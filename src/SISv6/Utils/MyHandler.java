package SISv6.Utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.impl.DefaultExceptionHandler;

import java.util.PropertyResourceBundle;

public class MyHandler extends DefaultExceptionHandler {
    private final long NetworkRecoveryInterval = Long.parseLong(PropertyResourceBundle.getBundle("Settings").getString("Network_Recovery_Interval"));

    @Override
    public void handleUnexpectedConnectionDriverException(Connection conn, Throwable exception) {
        System.out.println("===== ConnectionDriverException Detected =====");
        System.out.println("=====   Start Recovering in: " + NetworkRecoveryInterval/1000 + " seconds   =====");
    }
}
