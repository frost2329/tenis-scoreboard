package servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import model.CurrentMatch;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dto.MatchScoreRequest;
import service.MatchService;
import util.GsonSingleton;
import util.RequestReader;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    MatchService matchService = MatchService.getInstance();
    RequestReader requestReader = RequestReader.getInstance();
    Gson gson = GsonSingleton.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
        String uuid = req.getParameter("uuid");
        try {
            CurrentMatch currentMatch = matchService.getCurrentMatch(UUID.fromString(uuid));

            resp.setContentType("application/json");
            gson.toJson(currentMatch, resp.getWriter());

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestBody =  requestReader.readRequestBody(req);
        MatchScoreRequest matchScoreRequest = gson.fromJson(requestBody, MatchScoreRequest.class);
    }
}
