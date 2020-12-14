package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private final String GET = "GET";
    private final String POST = "POST";
    private final String DELETE = "DELETE";
    private final String validPATH = "/1_war/api/posts";

    private PostController controller;

    @Override
    public void init() throws ServletException {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
        this.controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            if (method.equals(GET) && path.equals(validPATH)) {
                controller.all(resp);
                return;
            }

            if (method.equals(GET) && path.matches(validPATH + "/\\d+")) {
                final var id = parseId(path);
                controller.getById(id, resp);
                return;
            }

            if (method.equals(POST) && path.equals(validPATH)) {
                controller.save(req.getReader(), resp);
                return;
            }

            if (method.equals(DELETE) && path.matches(validPATH + "/\\d+")) {
                final var id = parseId(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long parseId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}


