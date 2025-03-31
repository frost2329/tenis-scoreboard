package by.frostetsky.servlet;

import by.frostetsky.dto.MatchDto;
import by.frostetsky.exception.MatchNotFoundException;
import by.frostetsky.util.ResponseUtil;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import by.frostetsky.dto.MatchScoreRequest;
import by.frostetsky.service.OngoingMatchService;
import by.frostetsky.util.GsonSingleton;
import by.frostetsky.util.RequestUtil;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    Gson gson = GsonSingleton.getInstance();
    OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uuid = req.getParameter("uuid");
        if(uuid == null) {
            ResponseUtil.sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "UUID is empty");
        }
        try {
            MatchDto matchDto = ongoingMatchService.getCurrentMatch(UUID.fromString(uuid));
            ResponseUtil.sendResponse(resp, HttpServletResponse.SC_OK, matchDto);
        } catch (MatchNotFoundException e) {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_GATEWAY, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody =  RequestUtil.readRequestBody(req);
        MatchScoreRequest matchScoreRequest = gson.fromJson(requestBody, MatchScoreRequest.class);
        try {
            MatchDto matchDto = ongoingMatchService.addPointToPlayer(matchScoreRequest.uuid(), matchScoreRequest.playerId());
            ResponseUtil.sendResponse(resp, HttpServletResponse.SC_OK, matchDto);
        } catch (Exception e) {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
