package SISv6.Server;

import SISv6.Utils.KeyValueList;
import SISv6.Utils.MQConnection;
import SISv6.Utils.MsgDecoder;
import com.rabbitmq.client.Address;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * SISServer is used for re-routing messages from some components
 * (must be allowed in their definition) to other components
 * that need those messages (according to their definition)
 */
public class SISServer {
	private final static String SIS_QUEUE_NAME = "SISMQ";
	private final static String SERVERS_STRING = PropertyResourceBundle.getBundle("Settings").getString("RMQ_Servers");
	private final static String SIS_EXCHANGE_NAME = "SISINIT";
	private static Address[] addresses;

	// internal routing table for each component
	// key is the name of a component, see ComponentInfo for details

	static int port = Integer.parseInt(PropertyResourceBundle.getBundle("Settings").getString("TCP_Listening_Port"));

	static Map<ComponentInfo, ComponentConnection> mapping = new ConcurrentHashMap<>();

	static String getTopScope() {
		return "SIS";
		// return scope.current;
	}
	public static void reRoute(String scope, KeyValueList kvList){
		SISServer.mapping.entrySet().stream()
			.filter(x -> (x.getKey().scope.equals(scope)
					&& (x.getKey().componentType == ComponentType.Monitor || x.getKey().componentType == ComponentType.Debugger)
					&& x.getValue().encoder != null))
			.forEach(x -> {
				try {
					// re-route this message to each
					// qualified
					// component
					x.getValue().encoder.sendMsg(kvList);
				} catch (Exception e) {
					System.out.println("ERROR: Fail to send " + kvList + ", abort subtask");
				}
			});
	}
	public static void main(String[] args) {
		addresses = MQConnection.ParseAddress(SERVERS_STRING);
		if(addresses.length==0){
			System.exit(1);
			return;
		}
		// thread pool for handling connections to components
		ExecutorService service = Executors.newCachedThreadPool();
		// server socket that accepts new connections
		ServerSocket serverSocket = null;

		try {
			if (args.length > 0 && args[0].equals("--legacy")) serverSocket = new ServerSocket(port);
			System.out.println("SISServer starts, waiting for new components");
			service.execute(new SISPullTask());
			// Setup MQ consumer
//			MQConnection mq = new MQConnection(MQ_HOST);
			MQConnection mq = new MQConnection(addresses);
			MsgDecoder decoder = new MsgDecoder(mq.getConnection(),SIS_QUEUE_NAME,SIS_EXCHANGE_NAME,"fanout");
			SISTaskManager taskManager = new SISTaskManager(mq);
			decoder.setMQConsumer((consumerTag, delivery)->{
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
				if (message.length() > 2){ taskManager.handleMessage(message); }
			});
			while (serverSocket != null) {
				// initialize a secondary task for each
				// new connection in the thread pool
				service.execute(new SISTask(serverSocket.accept()));
			}
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}







