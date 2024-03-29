package ru.yandex.praktikum.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.model.Subtask;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void ShouldCalculateEpicDuration() {
        taskManager.addNewEpic(epic);
        assertEquals(0, epic.getDuration(), "Продолжительность эпика не равна 0");
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(epic.getDuration(), subtask1.getDuration() + subtask2.getDuration(),
                "Продолжительность эпика не равна продолжительности подзадач");
    }

    @Test
    void ShouldCalculateEpicDurationNullWithSubtasksDurationNull() {
        taskManager.addNewEpic(epic);
        assertEquals(0, epic.getDuration(), "Продолжительность эпика не равна 0");
        taskManager.addNewSubtask(subtask3);
        assertEquals(epic.getDuration(), subtask3.getDuration(),
                "Продолжительность эпика не равна продолжительности подзадач");
        assertEquals(0, epic.getDuration(), "Продолжительность эпика не равна 0");
        assertNull(epic.getStartTime(), "Время начала эпика не равно null");
        assertNull(epic.getEndTime(), "Время окончания эпика не равно null");
    }

    @Test
    void ShouldCalculateEpicStartTimeAndEndTime() {
        taskManager.addNewEpic(epic);
        assertNull(epic.getStartTime(), "Начало эпика не равно null");
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(epic.getStartTime(), subtask1.getStartTime() ,
                "Время начала эпика не равно началу самой ранней подзадачи");
        assertEquals(epic.getEndTime(), subtask2.getEndTime() ,
                "Время окончания эпика не равно концу самой поздней подзадачи");
    }

    @Test
    void ShouldCheckTasksNotCrossInTime() {
        taskManager.addNewEpic(epic);
        taskManager.addNewTask(task);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertTrue(subtask1.getStartTime().isAfter(task.getEndTime()));
        assertTrue(subtask1.getEndTime().isBefore(subtask2.getStartTime()));
    }

    @Test
    void ShouldGetExceptionTasksCrossInTime() {
        taskManager.addNewEpic(epic);
        taskManager.addNewTask(task);
        subtask1 = new Subtask(1, "Subtask1 name", "Subtask1 description", 60 ,
                LocalDateTime.of(2022, 2, 6, 11, 0));
        subtask2 = new Subtask(1, "Subtask2 name", "Subtask2 description", 60 ,
                LocalDateTime.of(2022, 2, 6, 11, 30));
        taskManager.addNewSubtask(subtask1);
        final ManagerException exception = Assertions.assertThrows(
                ManagerException.class,
                () -> taskManager.addNewSubtask(subtask2));
        Assertions.assertEquals("задача \"Subtask2 name\" пересекается с существующей задачей \"Subtask1 name\"",
                exception.getMessage());
    }
}
