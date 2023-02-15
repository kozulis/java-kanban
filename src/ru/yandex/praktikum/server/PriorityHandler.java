package ru.yandex.praktikum.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.praktikum.service.TaskManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static ru.yandex.praktikum.server.HttpTaskServer.writeResponse;

public class PriorityHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected Gson gson = GsonConverter.getGsonConverter();

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public PriorityHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();

            if (requestMethod.equals("GET") && Pattern.matches("^/tasks$", path)) {
                String response = gson.toJson(taskManager.getPrioritizedTasks());
                writeResponse(httpExchange, response, 200);
            } else {
                writeResponse(httpExchange, "Некорректный запрос списка по приоритету", 400);
            }
        } catch (Exception e) {
            writeResponse(httpExchange, e.getMessage(), 400);
            e.printStackTrace();
        }
    }
}
