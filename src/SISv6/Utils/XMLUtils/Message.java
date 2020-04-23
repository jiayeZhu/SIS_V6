package SISv6.Utils.XMLUtils;

import SISv6.Utils.XMLUtils.Msg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Messages")
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {

    @XmlElement(name = "Msg")
    private List<Msg> msgs;

    public List<Msg> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<Msg> msgs) {
        this.msgs = msgs;
    }

}