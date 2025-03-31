package by.frostetsky.util;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestUtil {
    public static String readRequestBody(HttpServletRequest req) {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return requestBody.toString();
    }
}
