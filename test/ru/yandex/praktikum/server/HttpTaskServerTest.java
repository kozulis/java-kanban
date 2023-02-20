package ru.yandex.praktikum.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.model.Epic;
import ru.yandex.praktikum.model.Subtask;
import ru.yandex.praktikum.model.Task;
import ru.yandex.praktikum.service.Managers;
import ru.yandex.praktikum.service.TaskManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    KVServer kvServer;
    TaskManager httpTaskManager;
    HttpTaskServer httpTaskServer;
    Gson gson;
    HttpClient client;

    Epic epic = new Epic("Epic name", "Epic description"); // id 1
    Epic epic1 = new Epic("Epic1 name", "Epic1 description");  // id 2
    Task task = new Task("Task name", "Task description", 30 ,
            LocalDateTime.of(2022, 2, 5, 10, 0));  // id 3
    Task task1 = new Task("Task1 name", "Task description", 30 ,
            LocalDateTime.of(2022, 2, 5, 11, 0));  // id 4

    Subtask subtask1 = new Subtask(1, "Subtask1 name", "Subtask1 description", 40 ,
            LocalDateTime.of(2022, 2, 6, 11, 0));  // id 5
    Subtask subtask2 = new Subtask(1, "Subtask2 name", "Subtask2 description", 50 ,
            LocalDateTime.of(2022, 2, 7, 12, 0));  // id 6
    Subtask subtask3 = new Subtask(1, "Subtask2 name", "Subtask2 description");  // id 7

    @BeforeEach
    void StartAndCreate() throws IOException {
        gson = GsonConverter.getGsonConverter();
        kvServer = new KVServer();
        kvServer.start();
        httpTaskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(httpTaskManager);
        httpTaskServer.start();
        client = HttpClient.newHttpClient();


        httpTaskManager.addNewEpic(epic);
        httpTaskManager.addNewEpic(epic1);
        httpTaskManager.addNewTask(task);
        httpTaskManager.addNewTask(task1);
        httpTaskManager.addNewSubtask(subtask1);
        httpTaskManager.addNewSubtask(subtask2);
        httpTaskManager.addNewSubtask(subtask3);

        httpTaskManager.getEpicById(epic1.getId());
        httpTaskManager.getTaskById(task.getId());
        httpTaskManager.getTaskById(task1.getId());
        httpTaskManager.getSubtaskById(subtask1.getId());
        httpTaskManager.getSubtaskById(subtask3.getId());
        httpTaskManager.getSubtaskById(subtask2.getId());
        httpTaskManager.getEpicById(epic.getId());
    }

    @AfterEach
    void stopServer() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void shouldGetAllTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasksFromJson = gson.fromJson(response.body(), listType);
        assertEquals(httpTaskManager.getTasksList().size(), tasksFromJson.size(),
                "Размер списков задач не совпадает");
        for (int i = 0; i < httpTaskManager.getTasksList().size(); i++) {
            assertEquals(httpTaskManager.getTasksList().get(i), tasksFromJson.get(i));
        }
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/3");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task2 = gson.fromJson(response.body(), Task.class);
        assertEquals(task2, httpTaskManager.getTaskById(3), "Задачи не совпадают");
    }

    @Test
    void shouldAddAndGetTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        gson = GsonConverter.getGsonConverter();
        Task task2 = new Task("Task2 name check add", "Task2 description check add");
        String json = gson.toJson(task2);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        URI url1 = URI.create("http://localhost:8080/tasks/task/8");
        HttpRequest request1 = HttpRequest.newBuilder().GET().uri(url1).build();
        HttpResponse<String> newResponse = client.send(request1, HttpResponse.BodyHandlers.ofString());
        Task task3 = gson.fromJson(newResponse.body(), Task.class);
        assertEquals(task3, httpTaskManager.getTaskById(8), "Задачи не совпадают");
    }

    @Test
    void shouldDeleteTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        gson = GsonConverter.getGsonConverter();
        Task task2 = new Task("Task2 name check add", "Task2 description check add");
        String json = gson.toJson(task2);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        URI url1 = URI.create("http://localhost:8080/tasks/task/8");
        HttpRequest request1 = HttpRequest.newBuilder().DELETE().uri(url1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertNull(httpTaskManager.getTaskById(8), "Задача не удалена");
    }

    @Test
    void shouldDeleteAllTasks() throws IOException, InterruptedException {
        URI url1 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request1 = HttpRequest.newBuilder().DELETE().uri(url1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertTrue(httpTaskManager.getTasksList().isEmpty(), "Задачи не удалена");
    }

    @Test
    void shouldGetAllEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type listType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Task> tasksFromJson = gson.fromJson(response.body(), listType);
        assertEquals(httpTaskManager.getTasksList().size(), tasksFromJson.size(),
                "Размер списков эпиков не совпадает");
        for (int i = 0; i < httpTaskManager.getTasksList().size(); i++) {
            assertEquals(httpTaskManager.getEpicsList().get(i), tasksFromJson.get(i));
        }
    }

    @Test
    void shouldGetEpicById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic2 = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic2, httpTaskManager.getEpicById(1), "Эпики не совпадают");
    }

    @Test
    void shouldAddAndGetEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        gson = GsonConverter.getGsonConverter();
        Epic epic2 = new Epic("Epic2 name check add", "Epic2 description check add");
        String json = gson.toJson(epic2);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        URI url1 = URI.create("http://localhost:8080/tasks/epic/8");
        HttpRequest request1 = HttpRequest.newBuilder().GET().uri(url1).build();
        HttpResponse<String> newResponse = client.send(request1, HttpResponse.BodyHandlers.ofString());
        Epic epic3 = gson.fromJson(newResponse.body(), Epic.class);
        assertEquals(epic3, httpTaskManager.getEpicById(8), "Эпики не совпадают");
    }

    @Test
    void shouldDeleteEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        gson = GsonConverter.getGsonConverter();
        Epic epic2 = new Epic("Epic2 name check add", "Epic2 description check add");
        String json = gson.toJson(epic2);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        URI url1 = URI.create("http://localhost:8080/tasks/epic/8");
        HttpRequest request1 = HttpRequest.newBuilder().DELETE().uri(url1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertNull(httpTaskManager.getEpicById(8), "Эпик не удален");
    }

    @Test
    void shouldDeleteAllEpics() throws IOException, InterruptedException {
        URI url1 = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request1 = HttpRequest.newBuilder().DELETE().uri(url1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertTrue(httpTaskManager.getEpicsList().isEmpty(), "Эпики не удалены");
    }

    @Test
    void shouldGetAllSubtasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type listType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> tasksFromJson = gson.fromJson(response.body(), listType);
        assertEquals(httpTaskManager.getSubtasksList().size(), tasksFromJson.size(),
                "Размер списков подзадач не совпадает");
        for (int i = 0; i < httpTaskManager.getSubtasksList().size(); i++) {
            assertEquals(httpTaskManager.getSubtasksList().get(i), tasksFromJson.get(i));
        }
    }

    @Test
    void shouldGetSubtaskById() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/5");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask2 = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask2, httpTaskManager.getSubtaskById(5), "Подзадачи не совпадают");
    }

    @Test
    void shouldAddAndGetSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        gson = GsonConverter.getGsonConverter();
        Subtask Subtask2 = new Subtask(1, "Subtask2 name check add", "Subtask2 description check add");
        String json = gson.toJson(Subtask2);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        URI url1 = URI.create("http://localhost:8080/tasks/subtask/8");
        HttpRequest request1 = HttpRequest.newBuilder().GET().uri(url1).build();
        HttpResponse<String> newResponse = client.send(request1, HttpResponse.BodyHandlers.ofString());
        Subtask subtask3 = gson.fromJson(newResponse.body(), Subtask.class);
        assertEquals(subtask3, httpTaskManager.getSubtaskById(8), "Подзадачи не совпадают");
    }

    @Test
    void shouldDeleteSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        gson = GsonConverter.getGsonConverter();
        Subtask subtask2 = new Subtask(1, "Subtask2 name check add", "Subtask2 description check add");
        String json = gson.toJson(subtask2);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        URI url1 = URI.create("http://localhost:8080/tasks/subtask/8");
        HttpRequest request1 = HttpRequest.newBuilder().DELETE().uri(url1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertNull(httpTaskManager.getSubtaskById(8), "Подзадача не удалена");
    }

    @Test
    void shouldDeleteAllSubtasks() throws IOException, InterruptedException {
        URI url1 = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request1 = HttpRequest.newBuilder().DELETE().uri(url1).build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertTrue(httpTaskManager.getSubtasksList().isEmpty(), "Подзадачи не удалены");
    }

    @Test
    void shouldGetSubtaskByEpicId() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), gson.toJson(httpTaskManager.getSubtaskListByEpic(epic)));
    }

    @Test
    void shouldGetHistoryList() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), gson.toJson(httpTaskManager.getHistory().
                stream().map(Task::getId).collect(Collectors.toList())));
    }

    @Test
    void shouldGetPriorityList() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), gson.toJson(httpTaskManager.getPrioritizedTasks()));
    }
}