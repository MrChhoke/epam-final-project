package com.epam.cash.register.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestUtil {

    public static Map<String, String> getMapFromBody(InputStream inputStream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Map<String, String> map = new HashMap<>();
            String lineInfo;
            String lineKey = null;
            while ((lineInfo = bufferedReader.readLine()) != null) {
                if (lineInfo.contains("Content-Disposition: form-data;")) {
                    lineKey = lineInfo
                            .replace("Content-Disposition: form-data; name=\"", "")
                            .replace("\"", "");
                    continue;
                }
                if (lineInfo.length() == 0 || lineInfo.contains("WebKit")) {
                    continue;
                }
                map.put(lineKey, lineInfo);
            }
            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> getSimpleParameterMap(Map<String, String[]> source) {
        return source.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()[0]));
    }
}
