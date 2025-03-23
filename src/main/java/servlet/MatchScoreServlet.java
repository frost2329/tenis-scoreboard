package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.CurrentMatch;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MatchService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    MatchService matchService = MatchService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");

        // Создаем объект для JSON
        Map<String, String> responseData = new HashMap<>();
        responseData.put("matchId", "matchId");
        try {
            CurrentMatch currentMatch = matchService.getCurrentMatch(UUID.fromString(uuid));
            // Устанавливаем тип содержимого ответа
            resp.setContentType("application/json");

            // Преобразуем объект в JSON и отправляем в ответ
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(resp.getWriter(), currentMatch);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
