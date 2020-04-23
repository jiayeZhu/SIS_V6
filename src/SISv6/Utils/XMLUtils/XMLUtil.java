package SISv6.Utils.XMLUtils;

import SISv6.Utils.KeyValueList;
import SISv6.Utils.XMLUtils.Item;
import SISv6.Utils.XMLUtils.Message;
import SISv6.Utils.XMLUtils.Msg;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XMLUtil {

	public static List<KeyValueList> extractToKV(String url) throws Exception {
		List<KeyValueList> kvLists = new ArrayList<KeyValueList>();
		try {
			JAXBContext context = JAXBContext.newInstance(Message.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Message message = (Message) unmarshaller.unmarshal(new URL(url));
			List<Msg> msgs = message.getMsgs();
			if (msgs != null) {
				for (Msg msg : msgs) {
					kvLists.add(generateKV(msg));
				}
			}

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kvLists;
	}

	public static KeyValueList generateKV(Msg msg) {
		KeyValueList kvList = new KeyValueList();

		List<Item> items = msg.getItems();
		for (Item i : items) {
			kvList.putPair(i.getKey(), i.getValue());
		}
		return kvList;
	}
}













