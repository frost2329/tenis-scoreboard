package by.frostetsky.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ExceptionHandler {
    public static void handle(HttpServletResponse resp, Throwable e) throws IOException {
        int statusCode = getStatusCode(e);
        ResponseUtil.sendErrorResponse(resp, statusCode, e.getMessage());
    }

    private static int getStatusCode(Throwable e) {
        return switch (e.getClass().getSimpleName()) {
            case "BadRequestException" -> HttpServletResponse.SC_BAD_REQUEST;
            case "MatchNotFoundException" -> HttpServletResponse.SC_NOT_FOUND;
            default -> HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        };
    }
}
