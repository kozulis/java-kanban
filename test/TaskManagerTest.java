import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.praktikum.model.TaskStatus.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.model.Epic;
import ru.yandex.praktikum.model.Subtask;
import ru.yandex.praktikum.model.Task;
import ru.yandex.praktikum.model.TaskStatus;
import ru.yandex.praktikum.service.ManagerException;
import ru.yandex.praktikum.service.TaskManager;

import java.time.LocalDateTime;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    Task task;
    Epic epic;
    Epic epic1;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("Epic name", "Epic description");
        epic1 = new Epic("Epic1 name", "Epic1 description");
        task = new Task("Task name", "Task description");
        subtask1 = new Subtask(1, "Subtask1 name", "Subtask1 description");
        subtask2 = new Subtask(1, "Subtask2 name", "Subtask2 description");
        subtask3 = new Subtask(2, "Subtask2 name", "Subtask2 description");
    }

    //  тесты для Task
    @Test
    void shouldGetHistory() {
        taskManager.addNewEpic(epic);
        taskManager.addNewTask(task);
        taskManager.addNewSubtask(subtask1);

        List<Task> historyList = List.of(taskManager.getTaskById(task.getId()),
                taskManager.getEpicById(epic.getId()),
                taskManager.getSubtaskById(subtask1.getId()));

        Assertions.assertEquals(3, taskManager.getHistory().size(),
                "Размер списка истории не совпадает");
        Assertions.assertEquals(historyList, taskManager.getHistory(), "Содержимое списка не совпадает");
    }

    @Test
    void ShouldGetTasksList() {
        Assertions.assertTrue(taskManager.getTasksList().isEmpty(), "Список не пустой");
        taskManager.addNewTask(task);
        Assertions.assertEquals(1, taskManager.getTasksList().size(), "Размер списка не совпадает");
    }

    @Test
    void ShouldGetEmptyTasksList() {
        List<Task> taskList = taskManager.getTasksList() ;
        Assertions.assertTrue(taskList.isEmpty(), "Список не пустой");
    }

    @Test
    void ShouldCleanAllTasks() {
        taskManager.addNewTask(task);
        Assertions.assertFalse(taskManager.getTasksList().isEmpty(), "Список пустой");
        taskManager.cleanAllTasks();
        Assertions.assertTrue(taskManager.getTasksList().isEmpty(), "Список не пустой");
    }

    @Test
    void ShouldCleanAllTasksWhenTasksIsEmpty() {
        Assertions.assertTrue(taskManager.getTasksList().isEmpty(), "Список не пустой");
        taskManager.cleanAllTasks();
    }

    @Test
    void ShouldGetTaskById() {
        taskManager.addNewTask(task);
        Assertions.assertNotNull(taskManager.getTaskById(1), "Задача не найдена или неверный id");
        Assertions.assertEquals(task, taskManager.getTaskById(1), "Задачи не совпадают");
    }

    @Test
    void ShouldAddNewTask() {
        taskManager.addNewTask(task);
        Assertions.assertEquals(task, taskManager.getTaskById(1), "Задачи не совпадают");
        Assertions.assertNotNull(taskManager.getTaskById(1), "Задача не найдена");
        Assertions.assertEquals(1, taskManager.getTasksList().size(), "Размер списка не совпадает");
        Assertions.assertEquals(NEW, task.getStatus(), "Статус не NEW");
    }

    @Test
    void ShouldUpdateTask() {
        taskManager.addNewTask(task);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDescription("Task description update");
        taskManager.updateTask(task);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, task.getStatus(), "Статус не обновился");
        Assertions.assertEquals("Task description update", task.getDescription(),
                "Описание не обновилось");
    }

    @Test
    void ShouldThrowExceptionWhenUpdateWrongTask() {
        final ManagerException exception = Assertions.assertThrows(
                ManagerException.class,
                () -> taskManager.updateTask(task));
        Assertions.assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void ShouldRemoveTaskById() {
        taskManager.addNewTask(task);
        Assertions.assertEquals(1, taskManager.getTasksList().size(), "Размер списка не совпадает");
        taskManager.removeTaskById(1);
        Assertions.assertNull(taskManager.getTaskById(1), "Задача не удалена");
        Assertions.assertTrue(taskManager.getEpicsList().isEmpty(), "Список не пустой");
    }

    @Test
    void ShouldThrowExceptionRemoveWithWrongTaskId() {
        final ManagerException exception = Assertions.assertThrows(
                ManagerException.class,
                () -> taskManager.removeTaskById(10));
        Assertions.assertEquals("Id задачи указан не верно", exception.getMessage());
    }

    //  тесты для Task

    @Test
    void ShouldGetSubtasksList() {
        Assertions.assertTrue(taskManager.getSubtasksList().isEmpty(), "Список не пустой");
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        Assertions.assertEquals(2, taskManager.getSubtasksList().size(), "Размер списка не совпадает");
    }

    @Test
    void ShouldGetEmptySubtasksList() {
        List<Subtask> taskList = taskManager.getSubtasksList() ;
        Assertions.assertTrue(taskList.isEmpty(), "Список не пустой");
    }

    @Test
    void ShouldCleanAllSubtasks() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        Assertions.assertFalse(taskManager.getSubtasksList().isEmpty(), "Список пустой");
        taskManager.cleanAllSubtasks();
        Assertions.assertTrue(taskManager.getSubtasksList().isEmpty(), "Список не пустой");
    }

    @Test
    void ShouldCleanAllSubtasksWhenSubtasksIsEmpty() {
        taskManager.cleanAllSubtasks();
        Assertions.assertTrue(taskManager.getSubtasksList().isEmpty(), "Список не пустой");
    }

    @Test
    void ShouldGetSubtaskById() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        Assertions.assertNotNull(taskManager.getSubtaskById(2), "Подзадача не найдена или неверный id");
        Assertions.assertEquals(subtask1, taskManager.getSubtaskById(2), "Подзадачи не совпадают");
    }

    @Test
    void ShouldAddNewSubtask() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        Assertions.assertEquals(subtask1, taskManager.getSubtaskById(2), "Подзадачи не совпадают");
        Assertions.assertNotNull(taskManager.getSubtaskById(2), "Подзадача не найдена");
        Assertions.assertEquals(2, taskManager.getSubtasksList().size(), "Размер списка не совпадает");
        Assertions.assertEquals(NEW, subtask1.getStatus(), "Статус не NEW");
    }

    @Test
    void ShouldThrowExceptionWhenAddNewSubtaskWithoutEpic() {
        final ManagerException exception = Assertions.assertThrows(
                ManagerException.class,
                () -> taskManager.addNewSubtask(subtask1));
        Assertions.assertEquals("Эпик не найден", exception.getMessage());
    }

    @Test
    void ShouldUpdateSubtask() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setDescription("Subtask1 description update");
        taskManager.updateSubtask(subtask1);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, subtask1.getStatus(), "Статус не обновился");
        Assertions.assertEquals("Subtask1 description update", subtask1.getDescription(),
                "Описание не обновилось");
    }

    @Test
    void ShouldThrowExceptionWhenUpdateWrongSubtask() {
        final ManagerException exception = Assertions.assertThrows(
                ManagerException.class,
                () -> taskManager.updateSubtask(subtask1));
        Assertions.assertEquals("Подзадача не найдена", exception.getMessage());
    }

    @Test
    void ShouldRemoveSubtaskById() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        Assertions.assertEquals(1, taskManager.getSubtasksList().size(), "Размер списка не совпадает");
        Assertions.assertEquals(1, epic.getSubtaskIds().size(), "Список подзадач эпика пустой");
        taskManager.removeSubtaskById(2);
        Assertions.assertNull(taskManager.getSubtaskById(2), "Задача не удалена");
        Assertions.assertTrue(taskManager.getSubtasksList().isEmpty(), "Список не пустой");
        Assertions.assertTrue(epic.getSubtaskIds().isEmpty(), "Список подзадач эпика не пустой");
    }

    @Test
    void ShouldThrowExceptionRemoveWithWrongSubTaskId() {
        final ManagerException exception = Assertions.assertThrows(
                ManagerException.class,
                () -> taskManager.removeSubtaskById(10));
        Assertions.assertEquals("Id подзадачи указан не верно", exception.getMessage());
    }

    @Test
    void ShouldCleanAllSubtasksByEpic() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        Assertions.assertTrue(epic.getSubtaskIds().contains(subtask1.getId()), "Подзадача не найдена");
        Assertions.assertEquals(subtask1.getId(), epic.getSubtaskIds().get(0), "Подзадачи не равны");
        Assertions.assertEquals(subtask2.getId(), epic.getSubtaskIds().get(1), "Подзадачи не равны");
        Assertions.assertEquals(2, epic.getSubtaskIds().size(), "Списки на равны");
        taskManager.cleanAllSubtasksByEpic(epic);
        Assertions.assertFalse(taskManager.getSubtasksList().contains(subtask1),
                "Подзадача не удалилась из списка подзадач");
        Assertions.assertTrue(epic.getSubtaskIds().isEmpty(), "Список подзадач эпика не пуст");
    }

    @Test
    void ShouldThrowExceptionWithEmptyEpicWhenCleanAllSubtasksByEpic() {
        final ManagerException exception = Assertions.assertThrows(
                ManagerException.class,
                () -> taskManager.cleanAllSubtasksByEpic(epic));
        Assertions.assertEquals("Эпик на найден", exception.getMessage());
    }

    @Test
    void ShouldGetSubtaskListByEpic() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        Assertions.assertEquals(2, epic.getSubtaskIds().size(), "Списки на равны");
        List<Subtask> list = List.of(subtask1, subtask2);
        Assertions.assertEquals(list, taskManager.getSubtaskListByEpic(epic), "Списки не равны");
    }

    @Test
    void ShouldGetNullWhenSubtaskListByEpicIsEmpty() {
        Assertions.assertNull(taskManager.getSubtaskListByEpic(epic), "Список не пустой");
    }

    @Test
    void ShouldNotGetSubtaskListByEpicWithWrongEpic() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        Assertions.assertNotEquals(taskManager.getSubtaskListByEpic(epic1), taskManager.getSubtaskListByEpic(epic),
                "Списки должны быть не равны");
    }

    @Test
    void ShouldGetEpicsList() {
        Assertions.assertTrue(taskManager.getEpicsList().isEmpty(), "Список не пустой");
        taskManager.addNewEpic(epic);
        Assertions.assertEquals(1, taskManager.getEpicsList().size(), "Размер списка не совпадает");
    }

    @Test
    void ShouldGetEmptyEpicsList() {
        List<Epic> taskList = taskManager.getEpicsList();
        Assertions.assertTrue(taskList.isEmpty(), "Список не пустой");
    }

    @Test
    void ShouldCleanAllEpics() {
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        Assertions.assertFalse(taskManager.getEpicsList().isEmpty(), "Список пустой");
        taskManager.cleanAllEpics();
        Assertions.assertTrue(taskManager.getEpicsList().isEmpty(), "Список не пустой");
        Assertions.assertTrue(taskManager.getSubtasksList().isEmpty(), "Список подзадач не пустой");
    }

    @Test
    void ShouldCleanAllEpicsWhenEpicsEmpty() {
        taskManager.cleanAllEpics();
        Assertions.assertTrue(taskManager.getEpicsList().isEmpty(), "Список не пустой");
    }

    @Test
    void ShouldGetEpicById() {
        taskManager.addNewEpic(epic);
        Assertions.assertNotNull(taskManager.getEpicById(1), "Эпик не найден или неверный id");
        Assertions.assertEquals(epic, taskManager.getEpicById(1), "Эпики не совпадают");
    }

    @Test
    void addNewEpic() {
        taskManager.addNewEpic(epic);
        Assertions.assertEquals(epic, taskManager.getEpicById(1), "Задачи не совпадают");
        Assertions.assertNotNull(taskManager.getEpicById(1), "Задача не найдена");
        Assertions.assertEquals(1, taskManager.getEpicsList().size(), "Размер списка не совпадает");
        Assertions.assertEquals(NEW, epic.getStatus(), "Статус не NEW");
    }

    @Test
    void ShouldUpdateEpic() {
        taskManager.addNewEpic(epic);
        epic.setStatus(TaskStatus.DONE);
        epic.setDescription("Epic description update");
        taskManager.updateEpic(epic);
        Assertions.assertEquals(TaskStatus.DONE, epic.getStatus(), "Статус не обновился");
        Assertions.assertEquals("Epic description update", epic.getDescription(),
                "Описание не обновилось");
    }

    @Test
    void ShouldThrowExceptionWhenUpdateWrongEpic() {
        final ManagerException exception = Assertions.assertThrows(
                ManagerException.class,
                () -> taskManager.updateEpic(epic));
        Assertions.assertEquals("Эпик не найден", exception.getMessage());
    }

    @Test
    void ShouldRemoveEpicById() {
        taskManager.addNewEpic(epic);
        Assertions.assertEquals(1, taskManager.getEpicsList().size(), "Размер списка не совпадает");
        taskManager.removeEpicById(1);
        Assertions.assertNull(taskManager.getEpicById(1), "Задача не удалена");
        Assertions.assertTrue(taskManager.getEpicsList().isEmpty(), "Список не пустой");
    }

    @Test
    void ShouldThrowExceptionRemoveWithWrongEpicId() {
        final ManagerException exception = Assertions.assertThrows(
                ManagerException.class,
                () -> taskManager.removeEpicById(10));
        Assertions.assertEquals("Id эпика указан не верно", exception.getMessage());
    }

    //  Дополнительные тесты для эпика

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