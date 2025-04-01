package by.frostetsky.servlet;

import by.frostetsky.dto.FinishedMatchesResponse;
import by.frostetsky.service.FinishedMatchService;
import by.frostetsky.util.GsonSingleton;
import by.frostetsky.util.ResponseUtil;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet("/matches-data")
public class FinishedMatchesDataServlet extends HttpServlet {
    FinishedMatchService finishedMatchService = FinishedMatchService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer page = Integer.valueOf(req.getParameter("page"));
        Integer size = Integer.valueOf(req.getParameter("size"));
        String filter = req.getParameter("filter");
        log.info("Get parameters, page: {}, size: {} , playerName: {} ", page, size, filter);
        try {
            FinishedMatchesResponse matches = finishedMatchService.findAllMatches(page, size, filter);
            log.info("Successfully got finished matches list");
            ResponseUtil.sendResponse(resp, HttpServletResponse.SC_OK, matches);
        } catch (Exception e) {
            log.error("Error occurred while getting finished matches list", e);
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

}
