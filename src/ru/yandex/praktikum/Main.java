package ru.yandex.praktikum;

import ru.yandex.praktikum.model.Epic;
import ru.yandex.praktikum.model.Subtask;
import ru.yandex.praktikum.model.Task;
import ru.yandex.praktikum.service.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        System.out.println("Создание новых задач");
        Task task = new Task(manager.generateId, "Утренняя зарядка", "Пробежка 3 км", "NEW");
        System.out.println("Вернули id задачи: " + manager.addNewTask(task));
        Task task1 = new Task(manager.generateId, "Позавтракать", "Сварить кашку", "NEW");
        System.out.println("Вернули id задачи: " + manager.addNewTask(task1));
        System.out.println(manager.getTasksList());
        System.out.println();

        System.out.println("Создание эпика с 2мя поздадачами");
        Epic epic = new Epic(manager.generateId, "Собраться на работу", "Одеться", "NEW");
        System.out.println("Вернули id эпика: " + manager.addNewEpic(epic));
        Subtask subtask = new Subtask(epic.getId(), manager.generateId, "Надеть брюки", "Синие брюки",
                "NEW");
        System.out.println("Вернули id подзадачи: " + manager.addNewSubtask(subtask));
        Subtask subtask1 = new Subtask(epic.getId(), manager.generateId, "Надеть рубашку",
                "Белая рубашка", "NEW");
        System.out.println("Вернули id подзадачи: " + manager.addNewSubtask(subtask1));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getSubtaskById(4));
        System.out.println(manager.getSubtaskById(5));
        System.out.println();

        System.out.println("Создание эпика с 1 подзадачей");
        Epic epic1 = new Epic(manager.generateId, "Сесть в трамвай", "Поездка на работу", "NEW");
        System.out.println("Вернули id эпика: " + manager.addNewEpic(epic1));
        Subtask subtask2 = new Subtask(epic1.getId(), manager.generateId, "Оплатить проезд",
                "Купить билет", "NEW");
        System.out.println("Вернули id подзадачи: " + manager.addNewSubtask(subtask2));
        System.out.println(manager.getEpicById(6));
        System.out.println(manager.getSubtaskById(7));
        System.out.println();

//        System.out.println("Получение списка всех задач");
//        System.out.println(manager.getTasksList());
//        System.out.println();
//
//        System.out.println("Получение задачи по id");
//        System.out.println(manager.getTaskById(1));
//        System.out.println();

//        System.out.println("Обновление задачи");
//        Task task3 = new Task(1, "Утренняя зарядка", "Пробежка 3 км", "DONE");
//        Task task4 = new Task(2, "Позавтракать", "Сварить кашку", "DONE");
//        System.out.println("Вернули id задачи: " + manager.updateTask(task3));
//        System.out.println("Вернули id задачи: " + manager.updateTask(task4));
//        System.out.println(manager.getTasksList());
//        System.out.println();

//        System.out.println("Удаление задачи по id");
//        manager.removeTaskById(2);
//        System.out.println(manager.getTasksList());
//        System.out.println();
//
//        System.out.println("Удаление списка всех задач");
//        manager.cleanAllTasks();
//        System.out.println(manager.getTasksList());
//        System.out.println();

        System.out.println("Получение списка подзадач определенного эпика");
        System.out.println(manager.getSubtaskListByEpic(manager.getEpicById(3)));
        System.out.println();

        System.out.println("Обновление подзадач эпика");
        Subtask subtask3 = new Subtask(epic.getId(), 4, "Надеть брюки",
                "Синие брюки", "DONE");
        Subtask subtask4 = new Subtask(epic.getId(), 5, "Надеть рубашку",
                "Белая рубашка", "NEW");
        System.out.println("Вернули id подзадачи: " + manager.updateSubtask(subtask3));
        System.out.println("Вернули id подзадачи: " + manager.updateSubtask(subtask4));
        System.out.println(manager.getSubtaskListByEpic(manager.getEpicById(3)));
        System.out.println();

        System.out.println("Получение списка всех эпиков");
        System.out.println(manager.getEpicsList());
        System.out.println();

//        System.out.println("Получение списка всех подзадач");
//        System.out.println(manager.getSubtasksList());
//        System.out.println();

//        System.out.println("Проверка изменения статуса эпика в зависимости от статусов подзадач");
//        System.out.println(manager.getEpicById(3));
//        System.out.println();

//        System.out.println("Удаление всех эпиков и проверка списка эпиков");
//        manager.cleanAllEpics();
//        System.out.println(manager.getEpicsList());

//        System.out.println("Удаление эпика по id и его подзадач в из мапы");
//        manager.removeEpicById(3);
//        System.out.println(manager.getEpicsList());

        System.out.println("Получение списка подзадач определенного эпика");
        System.out.println(manager.getSubtaskListByEpic(manager.getEpicById(3)));
        System.out.println();

        System.out.println("Получение списка всех подзадач");
        System.out.println(manager.getSubtasksList());
        System.out.println();

        System.out.println("Очистка всех подзадач ");
        manager.cleanAllSubtasks();
        System.out.println();

        System.out.println("Получение списка всех подзадач");
        System.out.println(manager.getSubtasksList());
        System.out.println();

//        System.out.println("Очистка списка подзадач определенного эпика");
//        manager.cleanAllSubtasksByEpic(manager.getEpicById(3));
//        System.out.println();

        System.out.println("Получение списка подзадач определенного эпика");
        System.out.println(manager.getSubtaskListByEpic(manager.getEpicById(3)));
        System.out.println();

        System.out.println("Получение списка всех подзадач");
        System.out.println(manager.getSubtasksList());
        System.out.println();

//        System.out.println("удаление подзадачи");
//        manager.removeSubtaskById(5);
//
//        System.out.println("Получение списка всех эпиков");
//        System.out.println(manager.getEpicsList());
//        System.out.println();






    }
}
