package by.frostetsky.servlet;

import by.frostetsky.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import by.frostetsky.service.OngoingMatchService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    OngoingMatchService ongoingMatchService = OngoingMatchService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("new-match.html").forward(req, resp);
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String player1 = req.getParameter("playerName1");
        String player2 = req.getParameter("playerName2");
        log.info("POST request was received params playerName1 {}, playerName2 {}", player1, player2);
        try {
            UUID uuid = ongoingMatchService.createMatch(player1, player2);
            log.info("Current match successfully was created UUID {}", uuid );
            resp.sendRedirect("/match-table?uuid=" + uuid);
        } catch (Exception e) {
            log.error("Error occurred while crating new match", e);
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

    }
}
