package SISv6.Utils.XMLUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transition")
@XmlAccessorType(XmlAccessType.FIELD)
public class Transition {

    @XmlElement(name = "name")
    private Name name;
    @XmlElement(name = "code")
    private Code code;
    @XmlElement(name = "source")
    private Source source;
    @XmlElement(name = "purpose")
    private Purpose purpose;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

}