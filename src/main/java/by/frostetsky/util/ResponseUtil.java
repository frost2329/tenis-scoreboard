package by.frostetsky.util;

import by.frostetsky.dto.ErrorMessage;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtil {
    private static Gson gson = new Gson();
    public static void sendResponse(HttpServletResponse resp, int httpStatus, Object responseBody) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(httpStatus);
        String json = gson.toJson(responseBody);
        resp.getWriter().write(json);
    }

    public static void sendErrorResponse(HttpServletResponse resp, int httpStatus, String message) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(httpStatus);
        String json = gson.toJson(new ErrorMessage(message));
        resp.getWriter().write(json);
    }
}
