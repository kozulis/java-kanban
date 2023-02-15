package ru.yandex.praktikum.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.praktikum.model.Epic;
import ru.yandex.praktikum.model.Subtask;
import ru.yandex.praktikum.model.Task;
import ru.yandex.praktikum.service.ManagerException;
import ru.yandex.praktikum.service.TaskManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static ru.yandex.praktikum.server.HttpTaskServer.writeResponse;

public class EpicHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected Gson gson = GsonConverter.getGsonConverter();

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public EpicHandler(TaskManager taskManager) {
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
                    String response = gson.toJson(taskManager.getEpicsList());
                    writeResponse(httpExchange, response, 200);
                    break;
                }
                case GET_ID: {
                    String pathId = path.replaceFirst("/tasks/epic/", "");
                    int id = parsePathId(pathId);
                    if (id != -1 && taskManager.getEpicById(id) != null) {
                        String response = gson.toJson(taskManager.getEpicById(id));
                        writeResponse(httpExchange, response, 200);
                    } else {
                        writeResponse(httpExchange, "Получен некорректный идентификатор эпика", 400);
                    }
                    break;
                }
                case ADD: {
                    Epic epic = getEpicFromJson(httpExchange);
                    if (epic == null) return;
                    try {
                        taskManager.addNewEpic(epic);
                    } catch (ManagerException exception) {
                        writeResponse(httpExchange, exception.getMessage(), 400);
                    }
                    writeResponse(httpExchange, gson.toJson(epic), 200);
                    break;
                }
                case UPDATE: {
                    String pathId = path.replaceFirst("/tasks/epic/", "");
                    int id = parsePathId(pathId);
                    if (id != -1) {
                        Epic epic = getEpicFromJson(httpExchange);
                        try {
                            taskManager.updateEpic(epic);
                        } catch (ManagerException exception) {
                            writeResponse(httpExchange, exception.getMessage(), 400);
                        }
                        writeResponse(httpExchange, gson.toJson(epic), 200);
                    } else {
                        writeResponse(httpExchange, "Получен некорректный идентификатор эпика", 400);
                    }
                    break;
                }
                case DELETE_ALL: {
                    taskManager.cleanAllEpics();
                    writeResponse(httpExchange, "Все эпики удалены", 200);
                    break;
                }
                case DELETE_ID: {
                    String pathId = path.replaceFirst("/tasks/epic/", "");
                    int id = parsePathId(pathId);
                    if (id != -1) {
                        try {
                            taskManager.removeEpicById(id);
                        } catch (ManagerException exception) {
                            writeResponse(httpExchange, exception.getMessage(), 400);
                        }
                        writeResponse(httpExchange, "Эпик с id " + id + " удален", 200 );
                    } else {
                        writeResponse(httpExchange, "Получен некорректный идентификатор эпика", 400);
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
                if (Pattern.matches("^/tasks/epic$", path)) {
                    return Endpoints.GET_ALL;
                } else if (Pattern.matches("^/tasks/epic/\\d+$", path)) {
                    return Endpoints.GET_ID;
                }
                break;
            case "POST":
                if (Pattern.matches("^/tasks/epic$", path)) {
                    return Endpoints.ADD;
                } else if (Pattern.matches("^/tasks/epic/\\d+$", path)) {
                    return Endpoints.UPDATE;
                }
                break;
            case "DELETE":
                if (Pattern.matches("^/tasks/epic$", path)) {
                    return Endpoints.DELETE_ALL;
                } else if (Pattern.matches("^/tasks/epic/\\d+$", path)) {
                    return Endpoints.DELETE_ID;
                }
                break;
        }
        return Endpoints.UNKNOWN;
    }

    protected Epic getEpicFromJson (HttpExchange httpExchange) throws IOException {
        try {
//            InputStream inputStream = httpExchange.getRequestBody();
            String requestBody = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            JsonElement jsonElement = JsonParser.parseString(requestBody);
            try {
                return gson.fromJson(jsonElement, Epic.class);
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
