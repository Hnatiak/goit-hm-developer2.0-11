package org.example;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String timezone = req.getParameter("timezone");

        if (timezone == null || timezone.isEmpty()) {
            if (req.getCookies() != null) {
                for (Cookie cookie : req.getCookies()) {
                    if ("lastTimezone".equals(cookie.getName())) {
                        timezone = cookie.getValue();
                    }
                }
            }
        }

        if (timezone == null || timezone.isEmpty()) {
            timezone = "UTC";
        }

        ZoneId zone;
        try {
            zone = ZoneId.of(timezone.replace("UTC", "GMT"));
        } catch (Exception e) {
            zone = ZoneId.of("UTC");
        }

        String time = ZonedDateTime.now(zone)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

        Cookie cookie = new Cookie("lastTimezone", timezone);
        cookie.setMaxAge(60 * 60);
        resp.addCookie(cookie);

        WebContext context = new WebContext(req, resp, getServletContext());
        context.setVariable("time", time);

        resp.setContentType("text/html; charset=UTF-8");
        templateEngine.process("time", context, resp.getWriter());
    }
}