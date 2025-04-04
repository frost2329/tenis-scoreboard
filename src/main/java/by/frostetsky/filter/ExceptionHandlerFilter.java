package by.frostetsky.filter;

import by.frostetsky.util.ExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter("/*")
public class ExceptionHandlerFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException {
        try {
            super.doFilter(req, resp, chain);
        } catch (Throwable e) {
            log.error("Exception was occurred", e);
            ExceptionHandler.handle(resp, e);
        }
    }
}
