package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String matchId = req.getParameter("uuid");

        // Создаем объект для JSON
        Map<String, String> responseData = new HashMap<>();
        responseData.put("matchId", matchId);
        responseData.put("score", "2-1"); // Пример данных

        // Устанавливаем тип содержимого ответа
        resp.setContentType("application/json");

        // Преобразуем объект в JSON и отправляем в ответ
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(resp.getWriter(), responseData);
    }

}
