package ru.yandex.praktikum;

import ru.yandex.praktikum.model.*;
import ru.yandex.praktikum.service.Managers;
import ru.yandex.praktikum.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        System.out.println("Создание новых задач");
        Task task = new Task("Утренняя зарядка", "Пробежка 3 км", TaskStatus.NEW);
        taskManager.addNewTask(task);
        Task task1 = new Task("Позавтракать", "Сварить кашку",TaskStatus.NEW);
        taskManager.addNewTask(task1);
        System.out.println(taskManager.getTasksList());
        System.out.println();

        System.out.println("Создание эпика с 3мя поздадачами");
        Epic epic = new Epic("Собраться на работу", "Одеться",TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask(epic.getId(), "Надеть брюки","Синие брюки", TaskStatus.NEW);
        taskManager.addNewSubtask(subtask);
        Subtask subtask1 = new Subtask(epic.getId(), "Надеть рубашку","Белая рубашка", TaskStatus.NEW);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask(epic.getId(), "Надеть галстук","В горошек", TaskStatus.NEW);
        taskManager.addNewSubtask(subtask2);
        System.out.println(epic);
        System.out.println(subtask);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println();

        System.out.println("Создание эпика без подзадач");
        Epic epic1 = new Epic("Поездка на работу", "Сесть в трамвай", TaskStatus.NEW);
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

        System.out.println("Проверка записи в историю : \n" + taskManager.getHistory());
        System.out.println("Размер списка: " + taskManager.getHistory().size());
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
 * тут хранилище методов, чтобы не писать их заново )
 */
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
//        subtask.setStatus(TaskStatus.DONE);
//        subtask1.setStatus(TaskStatus.DONE);
//        System.out.println("Обновили подзадачу: " + subtask.getId());
//        taskManager.updateSubtask(subtask);
//        System.out.println("Обновили подзадачу: " + subtask1.getId());
//        taskManager.updateSubtask(subtask1);
//        System.out.println("Получение списка подзадач определенного эпика");
//        System.out.println(taskManager.getSubtaskListByEpic(taskManager.getEpicById(3)));
//        System.out.println();
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
