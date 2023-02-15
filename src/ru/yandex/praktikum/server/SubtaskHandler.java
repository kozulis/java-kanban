package ru.yandex.praktikum.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.praktikum.model.Subtask;
import ru.yandex.praktikum.model.Task;
import ru.yandex.praktikum.service.ManagerException;
import ru.yandex.praktikum.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static ru.yandex.praktikum.server.HttpTaskServer.writeResponse;

public class SubtaskHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected Gson gson = GsonConverter.getGsonConverter();

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            Endpoints endpoint = getEndpoint(path, requestMethod);

            switch (endpoint) {
                case GET_ALL: {
                    String response = gson.toJson(taskManager.getSubtasksList());
                    writeResponse(httpExchange, response, 200);
                    break;
                }
                case GET_ID: {
                    String pathId = path.replaceFirst("/tasks/subtask/", "");
                    int id = parsePathId(pathId);
                    if (id != -1 && taskManager.getSubtaskById(id) != null) {
                        String response = gson.toJson(taskManager.getSubtaskById(id));
                        writeResponse(httpExchange, response, 200);
                    } else {
                        writeResponse(httpExchange, "Получен некорректный идентификатор подзадачи", 400);
                    }
                    break;
                }
                case GET_EPIC_SUBTASK: {
                    String pathId = path.replaceFirst("/tasks/subtask/epic/", "");
                    int id = parsePathId(pathId);
                    if (id != -1 && taskManager.getEpicById(id) != null) {
                        String response = gson.toJson(taskManager.getSubtaskListByEpic(taskManager.getEpicById(id)));
                        writeResponse(httpExchange, response, 200);
                    } else {
                        writeResponse(httpExchange, "Получен некорректный идентификатор подзадачи", 400);
                    }
                    break;
                }
                case ADD: {
                    Subtask subtask = getSubtaskFromJson(httpExchange);
                    if (subtask == null) return;
                    try {
                        taskManager.addNewSubtask(subtask);
                    } catch (ManagerException exception) {
                        writeResponse(httpExchange, exception.getMessage(), 400);
                    }
                    writeResponse(httpExchange, gson.toJson(subtask), 200);
                    break;
                }
                case UPDATE: {
                    String pathId = path.replaceFirst("/tasks/subtask/", "");
                    int id = parsePathId(pathId);
                    if (id != -1) {
                        Subtask subtask = getSubtaskFromJson(httpExchange);
                        try {
                            taskManager.updateSubtask(subtask);
                        } catch (ManagerException exception) {
                            writeResponse(httpExchange, exception.getMessage(), 400);
                        }
                        writeResponse(httpExchange, gson.toJson(subtask), 200 );
                    } else {
                        writeResponse(httpExchange, "Получен некорректный идентификатор подзадачи", 400);
                    }
                    break;
                }
                case DELETE_ALL: {
                    taskManager.cleanAllSubtasks();
                    writeResponse(httpExchange, "Все подзадачи удалены", 200);
                    break;
                }
                case DELETE_ID: {
                    String pathId = path.replaceFirst("/tasks/subtask/", "");
                    int id = parsePathId(pathId);
                    if (id != -1) {
                        try {
                            taskManager.removeSubtaskById(id);
                        } catch (ManagerException exception) {
                            writeResponse(httpExchange, exception.getMessage(), 400);
                        }
                        writeResponse(httpExchange, "Подзадача с id " + id + " удалена", 200 );
                    } else {
                        writeResponse(httpExchange, "Получен некорректный идентификатор подзадачи", 400);
                    }
                    break;
                }
                default:
                    writeResponse(httpExchange, "Получен некорректный метод запроса", 400);
            }
        } catch (Exception e) {
            writeResponse(httpExchange, e.getMessage(), 400);
            e.printStackTrace();
        }
    }

    protected Endpoints getEndpoint(String path, String requestMethod) {
        switch (requestMethod) {
            case "GET":
                if (Pattern.matches("^/tasks/subtask$", path)) {
                    return Endpoints.GET_ALL;
                } else if (Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                    return Endpoints.GET_ID;
                } else if (Pattern.matches("^/tasks/subtask/epic/\\d+$", path)) {
                    return Endpoints.GET_EPIC_SUBTASK;
                }
                break;
            case "POST":
                if (Pattern.matches("^/tasks/subtask$", path)) {
                    return Endpoints.ADD;
                } else if (Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                    return Endpoints.UPDATE;
                }
                break;
            case "DELETE":
                if (Pattern.matches("^/tasks/subtask$", path)) {
                    return Endpoints.DELETE_ALL;
                } else if (Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                    return Endpoints.DELETE_ID;
                }
                break;
        }
        return Endpoints.UNKNOWN;
    }

    protected Subtask getSubtaskFromJson (HttpExchange httpExchange) throws IOException {
        try {
//            InputStream inputStream = httpExchange.getRequestBody();
            String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            JsonElement jsonElement = JsonParser.parseString(requestBody);
            try {
                return gson.fromJson(jsonElement, Subtask.class);
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
