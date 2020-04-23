package SISv6.Utils.XMLUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "place")
@XmlAccessorType(XmlAccessType.FIELD)
public class Place {

    @XmlElement(name = "name")
    private Name name;
    @XmlElement(name = "initialCode")
    private InitialCode initialCode;
    @XmlElement(name = "scope")
    private Scope scope;
    @XmlElement(name = "helperCode")
    private HelperCode helperCode;
    @XmlElement(name = "helperClassCode")
    private HelperClassCode helperClassCode;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public InitialCode getInitialCode() {
        return initialCode;
    }

    public void setInitialCode(InitialCode initialCode) {
        this.initialCode = initialCode;
    }

    public void setHelperCode(HelperCode helperCode) {
        this.helperCode = helperCode;
    }

    public void setHelperClassCode(HelperClassCode helperClassCode) {
        this.helperClassCode = helperClassCode;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public HelperCode getHelperCode() {
        return helperCode;
    }

    public HelperClassCode getHelperClassCode() {
        return helperClassCode;
    }

}