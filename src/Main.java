import model.Epic;
import model.Subtask;
import model.Task;
import service.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        System.out.println("Создание новых задач");
        Task task = new Task(manager.generateId, "Утренняя зарядка", "Пробежка 3 км", "NEW");
        manager.addNewTask(task);
        Task task1 = new Task(manager.generateId, "Позавтракать", "Сварить кашку", "NEW");
        manager.addNewTask(task1);
        System.out.println(manager.getTasksList());
        System.out.println();

        System.out.println("Создание эпика с 2мя поздадачами");
        Epic epic = new Epic(manager.generateId, "Собраться на работу", "Одеться", "NEW");
        manager.addNewEpic(epic);
        Subtask subtask = new Subtask(epic.getId(), manager.generateId, "Надеть брюки", "Синие брюки",
                "NEW");
        manager.addNewSubtask(subtask, epic);
        Subtask subtask1 = new Subtask(epic.getId(), manager.generateId, "Надеть рубашку",
                "Белая рубашка", "NEW");
        manager.addNewSubtask(subtask1, epic);
        System.out.println(manager.getEpicById(3)/*epic*/);
        System.out.println(manager.getSubtaskById(4));
        System.out.println(manager.getSubtaskById(5));
        System.out.println();

        System.out.println("Создание эпика с 1 подзадачей");
        Epic epic1 = new Epic(manager.generateId, "Сесть в трамвай", "Поездка на работу", "NEW");
        manager.addNewEpic(epic1);
        Subtask subtask2 = new Subtask(epic1.getId(), manager.generateId, "Оплатить проезд",
                "Купить билет", "NEW");
        manager.addNewSubtask(subtask2, epic1);
        System.out.println(manager.getEpicById(6));
        System.out.println(manager.getSubtaskById(7));
        System.out.println();

        System.out.println("Получение списка всех задач");
        System.out.println(manager.getTasksList());
        System.out.println();

        System.out.println("Получение списка по id");
        System.out.println(manager.getTaskById(1));
        System.out.println();

        System.out.println("Обновление задачи");
        Task task3 = new Task(1, "Утренняя зарядка", "Пробежка 3 км", "DONE");
        Task task4 = new Task(2, "Позавтракать", "Сварить кашку", "DONE");
        manager.updateTask(task3);
        manager.updateTask(task4);
        System.out.println(manager.getTasksList());
        System.out.println();

        System.out.println("Удаление задачи по id");
        manager.removeTaskById(2);
        System.out.println(manager.getTasksList());
        System.out.println();

        System.out.println("Удаление списка всех задач");
        manager.cleanAllTasks();
        System.out.println(manager.getTasksList());
        System.out.println();

        System.out.println("Получение списка подзадач определенного эпика"); /* тут не понял, какой список нужно */
        System.out.println(manager.getSubtaskListByEpic(epic));              /* выводить. Поэтому для проверки вывел */
        for (Integer id : epic.getSubTaskIds()) {                             /* и список с id подзадач, и сам список */
            manager.getSubtaskById(id);                                      /* подзадач ``\ (`_`) /`` */
            System.out.println(manager.subtasks.get(id));
        }
        System.out.println();

        System.out.println("Обновление подзадач эпика");
        Subtask subtask3 = new Subtask(epic.getId(), 4/*manager.generateId*/, "Надеть брюки",
                "Синие брюки", "DONE");
        Subtask subtask4 = new Subtask(epic.getId(), 5/*manager.generateId*/, "Надеть рубашку",
                "Белая рубашка", "DONE");
        manager.updateSubtask(subtask3);
        manager.updateSubtask(subtask4);
        System.out.println(manager.getSubtaskById(4));
        System.out.println(manager.getSubtaskById(5));
        System.out.println();

        System.out.println("Изменение статуса эпика в зависимости от статусов подзадач");
        manager.changeEpicStatus(epic);
        System.out.println(manager.getEpicById(3));
        System.out.println();

        System.out.println("Удаление всех эпиков");
        manager.cleanAllEpics();
        System.out.println(manager.getListEpics());
    }
}
