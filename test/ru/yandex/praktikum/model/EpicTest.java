package ru.yandex.praktikum.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.service.InMemoryTaskManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.praktikum.model.TaskStatus.*;
import static ru.yandex.praktikum.model.TaskStatus.IN_PROGRESS;

class EpicTest {
    InMemoryTaskManager taskManager;

    Epic epic = new Epic("Epic name", "Epic description");
    Subtask subtask1 = new Subtask(1, "Subtask1 name", "Subtask1 description", 40 ,
                           LocalDateTime.of(2022, 2, 6, 11, 0));
    Subtask subtask2 = new Subtask(1, "Subtask2 name", "Subtask2 description", 50 ,
                           LocalDateTime.of(2022, 2, 7, 12, 0));

    @BeforeEach
    void createTaskManager() {
        taskManager = new InMemoryTaskManager();
    }


    @Test
    void ShouldChangeEpicStatus() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        Assertions.assertEquals(NEW, epic.getStatus(), "Статусы не совпадают");
        Assertions.assertEquals(NEW, subtask1.getStatus(), "Статусы не совпадают");
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статусы не совпадают");
        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        Assertions.assertEquals(TaskStatus.DONE, epic.getStatus(), "Статусы не совпадают");
    }

    @Test
    void ShouldGetEpicStatusNewWithEmptySubTaskList() {
        taskManager.addNewEpic(epic);
        assertEquals(NEW, epic.getStatus(), "Статусы не совпадают");
    }

    @Test
    void ShouldGetEpicStatusNewWithSubTasksNew() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(NEW, epic.getStatus(), "Статусы не совпадают");
    }

    @Test
    void ShouldGetEpicStatusDoneWithSubTasksDone() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        subtask1 = new Subtask(subtask1.getId(),epic.getId(), "Subtask1 name",
                "Subtask1 description", DONE);
        subtask2 = new Subtask(subtask2.getId(),epic.getId(), "Subtask2 name",
                "Subtask2 description", DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(DONE, epic.getStatus(), "Статусы не совпадают");
    }

    @Test
    void ShouldGetEpicStatusInProgressWithSubTasksDoneAndNew() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        subtask1 = new Subtask(subtask1.getId(),epic.getId(), "Subtask1 name",
                "Subtask1 description",DONE);
        subtask2 = new Subtask(subtask2.getId(),epic.getId(), "Subtask2 name",
                "Subtask2 description",NEW);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Статусы не совпадают");
    }

    @Test
    void ShouldGetEpicStatusInProgressWithSubTasksInProgress(){
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        subtask1 = new Subtask(subtask1.getId(),epic.getId(), "Subtask1 name",
                "Subtask1 description",IN_PROGRESS);
        subtask2 = new Subtask(subtask2.getId(),epic.getId(), "Subtask2 name",
                "Subtask2 description",IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Статусы не совпадают");
    }


}