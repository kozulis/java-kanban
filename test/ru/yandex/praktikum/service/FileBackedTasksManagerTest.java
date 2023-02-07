package ru.yandex.praktikum.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.List;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    File file = new File("resources/historyData.csv");

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        taskManager = new FileBackedTasksManager(file);
    }

    @AfterEach
    void fileEraser() throws IOException {
        try (PrintWriter pw = new PrintWriter(file)){
        } catch (IOException exception) {
            throw new ManagerSaveException("Произошла ошибка при записи в файл", exception);
        }
    }

    @Test
    void ShouldRestoreWithEmptyTaskList(){
        taskManager = FileBackedTasksManager.loadFromFile(file);
        assertTrue(taskManager.getTasksList().isEmpty(), "Список задач не пустой");
        assertTrue(taskManager.getEpicsList().isEmpty(), "Список эпиков не пустой");
        assertTrue(taskManager.getSubtasksList().isEmpty(), "Список подзадач не пустой");
        assertTrue(taskManager.getHistory().isEmpty(), "Список истории не пустой");
    }

    @Test
    void ShouldSaveAndRestoreWithEmptyHistoryList(){
        taskManager.addNewEpic(epic);
        taskManager.addNewTask(task);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager = FileBackedTasksManager.loadFromFile(file);
        assertTrue(taskManager.getHistory().isEmpty(), "Список истории не пустой");
        assertNotNull(taskManager.getTasksList(), "Список задач не пустой");
        assertNotNull(taskManager.getEpicsList(), "Список эпиков не пустой");
        assertNotNull(taskManager.getSubtasksList(), "Список подзадач не пустой");
        assertEquals(List.of(subtask1.getId(), subtask2.getId()), epic.getSubtaskIds(),
                "Id списка подзадач эпика не совпадают");
    }

    @Test
    void ShouldAndSaveRestoreWithEpicWithoutSubtask(){
        taskManager.addNewEpic(epic);
        taskManager = FileBackedTasksManager.loadFromFile(file);
        assertTrue(taskManager.getHistory().isEmpty(), "Список истории не пустой");
        assertNotNull(taskManager.getEpicsList(), "Список эпиков не пустой");
        assertEquals(0, epic.getSubtaskIds().size(), "Список подзадач эпика не пустой");
        assertEquals(1, taskManager.getEpicsList().size(), "Размер списка эпиков на совпадает");
    }

    @Test
    void ShouldSaveAndRestoreWithHistoryListAndTasksLists(){
        taskManager.addNewEpic(epic);
        taskManager.addNewTask(task);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.getEpicById(epic.getId());
        taskManager.getTaskById(task.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getEpicById(epic.getId());
        taskManager = FileBackedTasksManager.loadFromFile(file);
        assertFalse(taskManager.getHistory().isEmpty(), "Список истории пустой");
        assertNotNull(taskManager.getTasksList(), "Список задач не пустой");
        assertNotNull(taskManager.getEpicsList(), "Список эпиков не пустой");
        assertNotNull(taskManager.getSubtasksList(), "Список подзадач не пустой");
        assertEquals(List.of(task, subtask1, subtask2, epic), taskManager.getHistory(),
                "Список истории не совпадают");
    }
}
