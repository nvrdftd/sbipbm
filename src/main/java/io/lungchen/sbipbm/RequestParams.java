package io.lungchen.sbipbm;

import java.util.HashMap;

public class RequestParams {

    private HashMap<String, String> params;

    public RequestParams() {
        params = new HashMap<String, String>();
    }

    public void put(String param, String val) {
        params.put(param, val);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        params.forEach((key, val) -> {
            stringBuilder.append(key);
            stringBuilder.append('=');
            stringBuilder.append(val);
            stringBuilder.append('&');
        });

        return stringBuilder.toString();
    }
}
