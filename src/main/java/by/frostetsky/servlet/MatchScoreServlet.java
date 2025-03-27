package by.frostetsky.servlet;

import by.frostetsky.exception.GameFinishedException;
import com.google.gson.Gson;
import by.frostetsky.model.CurrentMatchModel;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import by.frostetsky.dto.MatchScoreRequest;
import by.frostetsky.service.OngoingMatchService;
import by.frostetsky.util.GsonSingleton;
import by.frostetsky.util.RequestReader;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    RequestReader requestReader = RequestReader.getInstance();
    Gson gson = GsonSingleton.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
        String uuid = req.getParameter("uuid");
        try {
            CurrentMatchModel currentMatchModel = ongoingMatchService.getCurrentMatch(UUID.fromString(uuid));

            resp.setContentType("application/json");
            gson.toJson(currentMatchModel, resp.getWriter());

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody =  requestReader.readRequestBody(req);
        MatchScoreRequest matchScoreRequest = gson.fromJson(requestBody, MatchScoreRequest.class);
        try {
            CurrentMatchModel currentMatchModel = ongoingMatchService.addPointToPlayer(matchScoreRequest.getUuid(), matchScoreRequest.getPlayerId());
            resp.setContentType("application/json");
            gson.toJson(currentMatchModel, resp.getWriter());
        } catch (GameFinishedException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(e.getMessage()));
        }
    }
}
