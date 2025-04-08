package by.frostetsky.servlet;

import by.frostetsky.dto.FinishedMatchesResponse;
import by.frostetsky.service.FinishedMatchService;
import by.frostetsky.util.ValidateUtil;
import by.frostetsky.util.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet("/matches-data")
public class FinishedMatchesDataServlet extends HttpServlet {
    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    FinishedMatchService finishedMatchService = new FinishedMatchService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer page = ValidateUtil.validatePageParam(req.getParameter("page"), DEFAULT_PAGE_NUMBER);
        Integer size = ValidateUtil.validatePageParam(req.getParameter("size"), DEFAULT_PAGE_SIZE);
        String filter = ValidateUtil.validateNameFilter(req.getParameter("filter"));
        log.info("Get parameters, page: {}, size: {} , playerName: {} ", page, size, filter);
        FinishedMatchesResponse matches = finishedMatchService.findAllMatches(page, size, filter);
        log.info("Successfully got finished matches list");
        ResponseUtil.sendResponse(resp, HttpServletResponse.SC_OK, matches);
    }
}
