package ru.yandex.praktikum.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.praktikum.service.Managers;
import ru.yandex.praktikum.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {

        this.taskManager = taskManager; //todo изменить на httpTaskManager
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler(taskManager)); //задачи
        httpServer.createContext("/tasks/subtask", new SubtaskHandler(taskManager)); //подзадачи
        httpServer.createContext("/tasks/subtask/epic", new SubtaskHandler(taskManager)); //подзадачи эпика
        httpServer.createContext("/tasks/epic", new EpicHandler(taskManager)); //эпики
        httpServer.createContext("/tasks", new PriorityHandler(taskManager)); // приорити таскс
        httpServer.createContext("/tasks/history", new HistoryHandler(taskManager)); //история задач
//        httpServer.start();
//        System.out.println("HTTP-сервер запущен на " + PORT + " порту.");
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту.");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен на " + PORT + " порту.");
    }

    protected static void writeResponse(HttpExchange exchange, String response, int responseCode) throws IOException {
        byte[] bytes = response.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(responseCode, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        new HttpTaskServer(taskManager).start();
    }
}
