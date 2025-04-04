package by.frostetsky.servlet;

import by.frostetsky.dto.MatchDto;
import by.frostetsky.service.MatchScoreCalculatorService;
import by.frostetsky.service.ValidatorService;
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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private final Gson gson = GsonSingleton.getInstance();
    private final OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    private final MatchScoreCalculatorService matchScoreCalculatorService = MatchScoreCalculatorService.getInstance();
    private final ValidatorService validatorService = new ValidatorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uuid = req.getParameter("uuid");
        validatorService.validateUUID(uuid);
        MatchDto matchDto = ongoingMatchService.getCurrentMatch(UUID.fromString(uuid));
        log.info("Successfully got current match {}", matchDto);
        ResponseUtil.sendResponse(resp, HttpServletResponse.SC_OK, matchDto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody =  RequestUtil.readRequestBody(req);
        MatchScoreRequest matchScoreRequest = gson.fromJson(requestBody, MatchScoreRequest.class);
        log.info("POST request was received requestBody {}", matchScoreRequest);
        validatorService.validateUUID(matchScoreRequest.uuid());
        MatchDto matchDto = matchScoreCalculatorService.updateScore(
                UUID.fromString(matchScoreRequest.uuid()),
                matchScoreRequest.playerId());
        log.info("Match score was updated {}", matchDto);
        ResponseUtil.sendResponse(resp, HttpServletResponse.SC_OK, matchDto);
    }
}
