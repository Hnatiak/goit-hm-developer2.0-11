package org.example;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String timezone = req.getParameter("timezone");

        if (timezone != null && !timezone.isEmpty()) {
            try {
                timezone = timezone.replace("UTC", "GMT");
                ZoneId.of(timezone);
            } catch (Exception e) {
                resp.setStatus(400);
                resp.setContentType("text/html; charset=UTF-8");
                resp.getWriter().write("Invalid timezone");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}