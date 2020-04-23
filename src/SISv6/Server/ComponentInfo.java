package SISv6.Server;

/**
 * ComponentInfo represents a unique composite key
 * that can identify a component
 *
 * @author dexterchen
 *
 */
public class ComponentInfo {
    String scope;
    ComponentType componentType;
    String name;
    String incomingMessages;
    String outgoingMessages;

    public ComponentInfo(String s, ComponentType t, String n) {
        // TODO Auto-generated constructor stub
        scope = s;
        componentType = t;
        name = n;
    }

    public String getIncomingMessages() {
        return incomingMessages;
    }

    public void setIncomingMessages(String incomingMessages) {
        this.incomingMessages = incomingMessages;
    }

    public String getOutgoingMessages() {
        return outgoingMessages;
    }

    public void setOutgoingMessages(String outgoingMessages) {
        this.outgoingMessages = outgoingMessages;
    }
    public static ComponentType getComponentType(String role) {
        ComponentType type = null;
        if (role == null) {
            return null;
        }
        switch (role) {
            case "Basic":
                type = ComponentType.Basic;
                break;
            case "Monitor":
                type = ComponentType.Monitor;
                break;
            case "Advertiser":
                type = ComponentType.Advertiser;
                break;
            case "Controller":
                type = ComponentType.Controller;
                break;
            case "Debugger":
                type = ComponentType.Debugger;
            default:
                break;
        }
        return type;
    }
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        ComponentInfo info = (ComponentInfo) obj;
        return info.name.equals(name) && info.scope.equals(scope);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        int result = HashCodeUtil.hash(HashCodeUtil.SEED, scope);
        result = HashCodeUtil.hash(result, name);
        return result;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        StringBuilder builder = new StringBuilder();
        builder.append("\n===== Component Info Start =====\n");
        builder.append("Scope: " + scope + "\n");
        builder.append("Component Type: " + componentType + "\n");
        builder.append("Name: " + name + "\n");
        builder.append("===== Component Info End =====\n");
        return builder.toString();
    }
}