package dhu.cst.zjm.encrypt.action;

import java.util.HashMap;

/**
 * Created by zjm on 2016/11/3.
 */

public class Action {
    private final String type;
    private final HashMap<String, Object> data;

    Action(String type, HashMap<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    public static Builder type(String type) {
        return new Builder().create(type);
    }

    public String getType() {
        return type;
    }

    public HashMap getData() {
        return data;
    }

    public static class Builder {

        private String type;
        private HashMap<String, Object> data;

        Builder create(String type) {
            if (type == null) {
                throw new IllegalArgumentException("Type may not be null.");
            }
            this.type = type;
            this.data = new HashMap<>();
            return this;
        }

        public Builder bundle(String key, Object value) {
            if (key == null) {
                throw new IllegalArgumentException("Key may not be null.");
            }

            if (value == null) {
                throw new IllegalArgumentException("Value may not be null.");
            }
            data.put(key, value);
            return this;
        }

        public Action build() {
            if (type == null || type.isEmpty()) {
                throw new IllegalArgumentException("At least one key is required.");
            }
            return new Action(type, data);
        }
    }
}
