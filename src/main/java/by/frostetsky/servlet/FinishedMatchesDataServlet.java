package by.frostetsky.servlet;

import by.frostetsky.dto.FinishedMatchDto;
import by.frostetsky.dto.FinishedMatchesResponse;
import by.frostetsky.service.FinishedMatchService;
import by.frostetsky.util.GsonSingleton;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/matches-data")
public class FinishedMatchesDataServlet extends HttpServlet {
    FinishedMatchService finishedMatchService = FinishedMatchService.getInstance();
    Gson gson = GsonSingleton.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer page = Integer.valueOf(req.getParameter("page"));
        Integer size = Integer.valueOf(req.getParameter("size"));
        String filter = req.getParameter("filter");
        FinishedMatchesResponse matches = finishedMatchService.findAllMatches(page, size, filter);
        resp.setContentType("application/json");
        gson.toJson(matches, resp.getWriter());
    }

}
