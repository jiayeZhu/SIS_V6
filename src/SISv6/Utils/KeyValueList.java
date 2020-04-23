package SISv6.Utils;

import java.util.HashMap;
import java.util.Map;

public class KeyValueList {
    // interal map for the message <property name, property value>, key and
    // value are both in String format
    private Map<String, String> map;

    // delimiter for encoding the message
    static final String delim = "$$$";

    // regex pattern for decoding the message
    static final String pattern = "\\$+";

    /*
     * Constructor
     */
    public KeyValueList() {
        map = new HashMap<>();
    }

    /*
     * Add one property to the map
     */
    public boolean putPair(String key, String value) {
        key = key.trim();
        value = value.trim();
        if (key == null || key.length() == 0 || value == null
                || value.length() == 0) {
            return false;
        }
        map.put(key, value);
        return true;
    }

    /*
     * encode the KeyValueList into a String
     */
    public String encodedString() {

        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.append(entry.getKey() + delim + entry.getValue() + delim);
        }
        // X$$$Y$$$, minimum
        builder.append(")");
        return builder.toString();
    }

    /*
     * decode a message in String format into a corresponding KeyValueList
     */
    public static KeyValueList decodedKV(String message) {
        KeyValueList kvList = new KeyValueList();

        String[] parts = message.split(pattern);
        int validLen = parts.length;
        if (validLen % 2 != 0) {
            --validLen;
        }
        if (validLen < 1) {
            return kvList;
        }

        for (int i = 0; i < validLen; i += 2) {
            kvList.putPair(parts[i], parts[i + 1]);
        }
        return kvList;
    }

    /*
     * get the property value based on property name
     */
    public String getValue(String key) {
        return map.get(key);
    }

    /*
     * get the number of properties
     */
    public int size() {
        return map.size();
    }

    /*
     * toString for printing
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.append(entry.getKey() + " : " + entry.getValue() + "\n");
        }
        return builder.toString();
    }
}