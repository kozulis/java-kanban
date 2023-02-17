package ru.yandex.praktikum;

import ru.yandex.praktikum.model.*;
import ru.yandex.praktikum.server.KVServer;
import ru.yandex.praktikum.service.Managers;
import ru.yandex.praktikum.service.TaskManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
//
        new KVServer().start();
        TaskManager taskManager = Managers.getDefault();
//        new HttpTaskServer(taskManager).start();

//        HttpTaskManager taskManager = new HttpTaskManager(URI.create("http://localhost:8078"));

//        taskManager.loadFromServer();

//        TaskManager taskManager = Managers.getDefaultFBTManager();

        System.out.println("Создание новых задач");
        Task task = new Task("Утренняя зарядка", "Пробежка 3 км", 10,
                LocalDateTime.of(2022, 2, 8, 6, 50));
        taskManager.addNewTask(task);
        Task task1 = new Task("Позавтракать", "Сварить кашку", 20,
                LocalDateTime.of(2022, 2, 8, 17, 0));
        taskManager.addNewTask(task1);
        System.out.println(taskManager.getTasksList());
        System.out.println();

        System.out.println("Создание эпика с 3мя поздадачами");
        Epic epic = new Epic("Собраться на работу", "Одеться");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask(epic.getId(), "Надеть брюки","Синие брюки"/*,
                50, LocalDateTime.of(2022, 2, 6, 17, 0)*/);
        taskManager.addNewSubtask(subtask);
        Subtask subtask1 = new Subtask(epic.getId(), "Надеть рубашку","Белая рубашка",
                60, LocalDateTime.of(2022, 2, 7, 17, 0));
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask(epic.getId(), "Надеть галстук","В горошек",
                70, LocalDateTime.of(2022, 2, 9, 17, 0));
        taskManager.addNewSubtask(subtask2);
        System.out.println(epic);
        System.out.println(subtask);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println();

        System.out.println("Создание эпика без подзадач");
        Epic epic1 = new Epic("Поездка на работу", "Сесть в трамвай");
        taskManager.addNewEpic(epic1);
        System.out.println(epic1);
        System.out.println();

        System.out.println("Проверка записи в историю : \n" + taskManager.getHistory());
        System.out.println("Размер списка: " + taskManager.getHistory().size());
        System.out.println();

        System.out.println("Получение задачи по id");
        System.out.println(taskManager.getTaskById(1));
        System.out.println();

        System.out.println("Получение задачи по id");
        System.out.println(taskManager.getTaskById(2));
        System.out.println();

        System.out.println("Получение подзадачи по id");
        System.out.println(taskManager.getSubtaskById(4));
        System.out.println();

        System.out.println("Получение подзадачи по id");
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println();

        System.out.println("Получение эпика по id");
        System.out.println(taskManager.getEpicById(3));
        System.out.println();

        System.out.println("Получение эпика по id");
        System.out.println(taskManager.getEpicById(7));
        System.out.println();

        System.out.println("Получение подзадачи по id");
        System.out.println(taskManager.getSubtaskById(6));
        System.out.println();

        System.out.println("Получение подзадачи по id");
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println();

        System.out.println("Получение подзадачи по id");
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println();

        System.out.println("Проверка записи в историю: " + taskManager.getHistory());
        System.out.println("Размер списка: " + taskManager.getHistory().size());
        System.out.println();

        System.out.println("Проверка задач по приоритету: "/* + taskManager.getPrioritizedTasks()*/);
        taskManager.getPrioritizedTasks().forEach(System.out::println);
        System.out.println("Размер списка: " + taskManager.getPrioritizedTasks().size());
        System.out.println();

        System.out.println("Проверка записи в историю: " + taskManager.getHistory());
        System.out.println("Размер списка: " + taskManager.getHistory().size());
        System.out.println();

        System.out.println("Обновление подзадач эпика");
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        System.out.println("Обновили подзадачу: " + subtask.getId());
        taskManager.updateSubtask(subtask);
        System.out.println("Обновили подзадачу: " + subtask1.getId());
        taskManager.updateSubtask(subtask1);
        System.out.println("Получение списка подзадач определенного эпика");
        System.out.println(taskManager.getSubtaskListByEpic(taskManager.getEpicById(3)));
        System.out.println();

        System.out.println("Проверка задач по приоритету: " /*+ taskManager.getPrioritizedTasks()*/);
        taskManager.getPrioritizedTasks().forEach(System.out::println);
        System.out.println("Размер списка: " + taskManager.getPrioritizedTasks().size());
        System.out.println();

        System.out.println("Получение списка всех задач");
        System.out.println(taskManager.getTasksList());
        System.out.println();

        System.out.println("Получение списка всех эпиков");
        System.out.println(taskManager.getEpicsList());
        System.out.println();

        System.out.println("Получение списка всех подзадач");
        System.out.println(taskManager.getSubtasksList());
        System.out.println();


/**
 * хранилище методов, чтобы не писать их :)
 */

//
//        System.out.println("Удаление списка всех тасков");
//        taskManager.cleanAllTasks();
//        System.out.println(taskManager.getTasksList());
//        System.out.println();
//
//        System.out.println("Удаление эпика по id");
//        taskManager.removeEpicById(3);
//        System.out.println();
//
//        System.out.println("удаление задачи 2");
//        taskManager.removeTaskById(2);
//        System.out.println(taskManager.getTasksList());
//        System.out.println();
//
//        System.out.println("удаление эпика без подзадачи");
//        taskManager.removeEpicById(7);

//        taskManager.removeTaskById(1);
//        taskManager.removeTaskById(2);

//        System.out.println("удаление эпика без подзадач");
//        taskManager.removeEpicById(7);

//        System.out.println("Обновление задач");
//        task.setStatus(TaskStatus.DONE);
//        task1.setStatus(TaskStatus.DONE);
//        System.out.println("Обновили задачу: " + task.getId());
//        taskManager.updateTask(task);
//        System.out.println("Обновили задачу: " + task1.getId());
//        taskManager.updateTask(task1);
//        System.out.println();
//
//        System.out.println("Обновление подзадач эпика");
//        subtask.setStatus(TaskStatus.IN_PROGRESS);
//        subtask1.setStatus(TaskStatus.DONE);
//        subtask2.setStatus(TaskStatus.DONE);
//        System.out.println("Обновили подзадачу: " + subtask.getId());
//        taskManager.updateSubtask(subtask);
//        System.out.println("Обновили подзадачу: " + subtask1.getId());
//        taskManager.updateSubtask(subtask1);
//        System.out.println("Получение списка подзадач определенного эпика");
//        System.out.println(taskManager.getSubtaskListByEpic(taskManager.getEpicById(3)));
//        System.out.println();

//        Subtask subtask3 = new Subtask(epic.getId(), "Надеть галстук","В горошек");
//        taskManager.addNewSubtask(subtask2);
//
//
//        System.out.println("Получение списка подзадач определенного эпика");
//        System.out.println(taskManager.getSubtaskListByEpic(taskManager.getEpicById(3)));
//        System.out.println();
//
//        System.out.println("Удаление задачи по id");
//        taskManager.removeTaskById(2);
//
//        System.out.println("Удаление списка всех задач");
//        taskManager.cleanAllTasks();
//        System.out.println(taskManager.getTasksList());
//        System.out.println();
//
//        System.out.println("Удаление задач");
//        taskManager.cleanAllTasks();
//        System.out.println();
//
//        System.out.println("удаление подзадачи");
//        taskManager.removeSubtaskById(4);
//
//        System.out.println("Удаление всех подзадач");
//        taskManager.cleanAllSubtasks();
//        System.out.println();
//
//        System.out.println("Удаление эпика по id и его подзадач в из мапы");
//        taskManager.removeEpicById(3);
//        System.out.println(taskManager.getEpicsList());
//
//        System.out.println("Удаление всех эпиков и проверка списка эпиков");
//        taskManager.cleanAllEpics();
//        System.out.println(taskManager.getEpicsList());
//
//        System.out.println("Проверка изменения статуса эпика в зависимости от статусов подзадач");
//        System.out.println(taskManager.getEpicById(3));
//        System.out.println();

//        System.out.println("Получение списка подзадач определенного эпика");
//        System.out.println(taskManager.getSubtaskListByEpic(taskManager.getEpicById(3)));
//        System.out.println();
//
//        System.out.println("Очистка списка подзадач определенного эпика");
//        taskManager.cleanAllSubtasksByEpic(taskManager.getEpicById(3));
//        System.out.println();
//
//        System.out.println("Получение списка по id");
//        System.out.println(taskManager.getTaskById(2));


    }
}
