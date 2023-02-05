import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.model.TaskStatus;
import ru.yandex.praktikum.service.InMemoryTaskManager;

import static ru.yandex.praktikum.model.TaskStatus.NEW;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
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
}
