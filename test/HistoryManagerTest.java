import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.model.*;
import ru.yandex.praktikum.service.HistoryManager;
import ru.yandex.praktikum.service.InMemoryHistoryManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.praktikum.model.TaskStatus.*;

class HistoryManagerTest {

    Epic epic = new Epic(1, "Epic name", "Epic description", NEW);
    Epic epic1 = new Epic(2, "Epic1 name", "Epic1 description", NEW);
    Task task = new Task(3, "Task name", "Task description", NEW);
    Subtask subtask1 = new Subtask(4, 1, "Subtask1 name", "Subtask1 description", NEW);
    Subtask subtask2 = new Subtask(5, 1, "Subtask2 name", "Subtask2 description" , NEW);
    Subtask subtask3 = new Subtask(6, 2, "Subtask2 name", "Subtask2 description", NEW);

    HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void ShouldReturnEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty(), "История не пустая");
        assertEquals(0, historyManager.getHistory().size(), "Размер списка не совпадает");
    }

    @Test
    void ShouldAddTasksInHistory() {
        historyManager.add(epic);
        historyManager.add(epic1);
        assertFalse(historyManager.getHistory().isEmpty(), "История пустая");
        assertEquals(2, historyManager.getHistory().size(), "Размер списка не совпадает");
    }

    @Test
    void ShouldNotAddSameTasksInHistory() {
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.add(epic);
        assertEquals(2, historyManager.getHistory().size(), "Размер списка не совпадает");
        List<Task> list = List.of(subtask1, epic);
        assertEquals(list, historyManager.getHistory(), "Списки не равны");
    }

    @Test
    void ShouldRemoveFirstTaskFromHistory() {
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subtask1);
        List<Task> list = List.of(epic, task, subtask1);
        assertEquals(3, historyManager.getHistory().size(), "Размер списка не совпадает");
        assertEquals(list, historyManager.getHistory(), "Списки не равны");
        historyManager.remove(epic.getId());
        list = List.of(task, subtask1);
        assertEquals(2, historyManager.getHistory().size(), "Размер списка не совпадает");
        assertEquals(list, historyManager.getHistory(), "Списки не равны");
    }

    @Test
    void ShouldRemoveMiddleTaskFromHistory() {
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subtask1);
        historyManager.remove(task.getId());
        List<Task> list = List.of(epic, subtask1);
        assertEquals(2, historyManager.getHistory().size(), "Размер списка не совпадает");
        assertEquals(list, historyManager.getHistory(), "Списки не равны");
    }

    @Test
    void ShouldRemoveLastTaskFromHistory() {
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subtask1);
        historyManager.remove(subtask1.getId());
        List<Task> list = List.of(epic, task);
        assertEquals(2, historyManager.getHistory().size(), "Размер списка не совпадает");
        assertEquals(list, historyManager.getHistory(), "Списки не равны");
    }

    @Test
    void ShouldNotRemoveWrongTaskFromHistory() {
        historyManager.add(epic);
        historyManager.add(task);
        historyManager.remove(7);
        List<Task> list = List.of(epic, task);
        assertEquals(2, historyManager.getHistory().size(), "Размер списка не совпадает");
        assertEquals(list, historyManager.getHistory(), "Списки не равны");
    }
}