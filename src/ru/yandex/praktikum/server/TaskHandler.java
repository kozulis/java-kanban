package ru.yandex.praktikum.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.praktikum.model.Task;
import ru.yandex.praktikum.service.ManagerException;
import ru.yandex.praktikum.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static ru.yandex.praktikum.server.HttpTaskServer.writeResponse;

public class TaskHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected Gson gson = GsonConverter.getGsonConverter();

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String requestMethod = httpExchange.getRequestMethod();
        Endpoints endpoint = getEndpoint(path, requestMethod);

        switch (endpoint) {
            case GET_ALL: {
                String response = gson.toJson(taskManager.getTasksList());
                writeResponse(httpExchange, response, 200);
                break;
            }
            case GET_ID: {
                String pathId = path.replaceFirst("/tasks/task/", "");
                int id = parsePathId(pathId);
                if (id != -1 && taskManager.getTaskById(id) != null) {
                    String response = gson.toJson(taskManager.getTaskById(id));
                    writeResponse(httpExchange, response, 200);
                } else {
                    writeResponse(httpExchange, "Получен некорректный идентификатор задачи", 400);
                }
                break;
            }
            case ADD: {
                Task task = getTaskFromJson(httpExchange);
                if (task == null) return;
                try {
                    taskManager.addNewTask(task);
                } catch (ManagerException exception) {
                    writeResponse(httpExchange, exception.getMessage(), 400);
                }
                writeResponse(httpExchange, gson.toJson(task), 200);
                break;
            }
            case UPDATE: {
                String pathId = path.replaceFirst("/tasks/task/", "");
                int id = parsePathId(pathId);
                if (id != -1) {
                    Task task = getTaskFromJson(httpExchange);
                    try {
                        taskManager.updateTask(task);
                    } catch (ManagerException exception) {
                        writeResponse(httpExchange, exception.getMessage(), 400);
                    }
                    writeResponse(httpExchange, gson.toJson(task), 200 );
                } else {
                    writeResponse(httpExchange, "Получен некорректный идентификатор задачи", 400);
                }
                break;
            }
            case DELETE_ALL: {
                taskManager.cleanAllTasks();
                writeResponse(httpExchange, "Все задачи удалены", 200);
                break;
            }
            case DELETE_ID: {
                String pathId = path.replaceFirst("/tasks/task/", "");
                int id = parsePathId(pathId);
                if (id != -1) {
                    try {
                        taskManager.removeTaskById(id);
                    } catch (ManagerException exception) {
                        writeResponse(httpExchange, exception.getMessage(), 400);
                    }
                    writeResponse(httpExchange, "Задача с id " + id + " удалена", 200 );
                } else {
                    writeResponse(httpExchange, "Получен некорректный идентификатор задачи", 400);
                }
                break;
            }
            default:
                writeResponse(httpExchange, "Получен некорректный метод запроса", 400);
        }
    }

    protected Endpoints getEndpoint(String path, String requestMethod) {
        switch (requestMethod) {
            case "GET":
                if (Pattern.matches("^/tasks/task$", path)) {
                    return Endpoints.GET_ALL;
                } else if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                    return Endpoints.GET_ID;
                }
                break;
            case "POST":
                if (Pattern.matches("^/tasks/task$", path)) {
                    return Endpoints.ADD;
                } else if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                    return Endpoints.UPDATE;
                }
                break;
            case "DELETE":
                if (Pattern.matches("^/tasks/task$", path)) {
                    return Endpoints.DELETE_ALL;
                } else if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                    return Endpoints.DELETE_ID;
                }
                break;
        }
        return Endpoints.UNKNOWN;
    }

    protected Task getTaskFromJson (HttpExchange httpExchange) throws IOException {
        try {
//            InputStream inputStream = httpExchange.getRequestBody();
            String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            JsonElement jsonElement = JsonParser.parseString(requestBody);
            try {
                return gson.fromJson(jsonElement, Task.class);
            } catch (JsonSyntaxException e) {
                writeResponse(httpExchange, "Некорректный JSON", 400);
            }
        } catch (Exception e) {
            writeResponse(httpExchange, e.getMessage(), 400);
        }
        return null;
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
