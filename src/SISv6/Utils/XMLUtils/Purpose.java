package SISv6.Utils.XMLUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "purpose")
@XmlAccessorType(XmlAccessType.FIELD)
public class Purpose {
    @XmlElement(name = "value")
    private String value;

    public Purpose() {

    }

    public Purpose(String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}