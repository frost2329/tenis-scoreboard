package by.frostetsky.filter;

import by.frostetsky.util.ExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlerFilter extends HttpFilter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException {
        try {
            super.doFilter(req, resp, chain);
        } catch (Throwable e) {
            ExceptionHandler.handle( (HttpServletResponse) resp, e);
        }
    }
}
