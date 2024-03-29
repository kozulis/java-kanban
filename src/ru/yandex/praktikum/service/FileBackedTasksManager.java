package ru.yandex.praktikum.service;

import ru.yandex.praktikum.model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        TaskManager taskManager = new FileBackedTasksManager(new File("resources/historyData.csv"));

        System.out.println("Создание новых задач");
        Task task = new Task("Утренняя зарядка", "Пробежка 3 км", 10,
                LocalDateTime.of(2022, 2, 6, 17, 0));
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


        System.out.println("Проверка задач по приоритету : \n"/* + taskManager.getPrioritizedTasks()*/);
        taskManager.getPrioritizedTasks().forEach(System.out::println);
        System.out.println("Размер списка: " + taskManager.getPrioritizedTasks().size());
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


        System.out.println("Проверка записи в историю :" + taskManager.getHistory());
        System.out.println("Размер списка: " + taskManager.getHistory().size());
        System.out.println();

        System.out.println("Проверка задач по приоритету :"/* + taskManager.getPrioritizedTasks()*/);
        taskManager.getPrioritizedTasks().forEach(System.out::println);
        System.out.println("Размер списка: " + taskManager.getPrioritizedTasks().size());
        System.out.println();



        System.out.println("Проверка восстановления менеджера из файла\n");
        FileBackedTasksManager loadTasksManager =
                FileBackedTasksManager.loadFromFile(new File("resources/historyData.csv"));

        System.out.println("Получение списка всех задач");
        System.out.println(loadTasksManager.getTasksList());
        System.out.println();

        System.out.println("Получение списка всех эпиков");
        System.out.println(loadTasksManager.getEpicsList());
        System.out.println();

        System.out.println("Получение списка всех подзадач");
        System.out.println(loadTasksManager.getSubtasksList());
        System.out.println();

        System.out.println("Проверка записи в историю :" + loadTasksManager.getHistory());
        System.out.println("Размер списка: " + loadTasksManager.getHistory().size());
        System.out.println();


        System.out.println("Проверка задач по приоритету :"/* + taskManager.getPrioritizedTasks()*/);
        loadTasksManager.getPrioritizedTasks().forEach(System.out::println);
        System.out.println("Размер списка: " + loadTasksManager.getPrioritizedTasks().size());
        System.out.println();

    }

    /**
     * сохранение менеджера в файл
     */
    void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write("id,type,name,status,description,duration,startTime,epic\n");
            for (Task task : tasks.values()){
                bufferedWriter.write(taskToString(task) + "\n");
            }
            for (Epic epic : epics.values()){
                bufferedWriter.write(taskToString(epic) + "\n");
            }
            for (Subtask subtask : subtasks.values()){
                bufferedWriter.write(taskToString(subtask) + "\n");
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(historyToString(historyManager));
        } catch (IOException exception) {
            throw new ManagerSaveException("Произошла ошибка при записи в файл", exception);
        }
    }

    /**
     * сохранение задачи в строку
     * @param task задача
     * @return строка
     */
    public String taskToString(Task task) {
            return task.toCSVForm();
    }

    /**
     * создание задачи из строки
     * @param value строка
     * @return задача
     */
    public Task taskFromString(String value) {
        if (!value.isEmpty()) {
            String[] split = value.split(",");
            int id = Integer.parseInt(split[0]);
            TaskType type = TaskType.valueOf(split[1]);
            String name = split[2];
            TaskStatus status = TaskStatus.valueOf(split[3]);
            String description = split[4];
            long duration = Long.parseLong(split[5]);
            LocalDateTime startTime = null;
            
            if (!split[6].equals("null")) {
                startTime = LocalDateTime.parse(split[6]);
            }

            switch (type) {
                case TASK: {
                    Task task = new Task(id, name, description, status, duration, startTime);
                    tasks.put(id,task); //// положил задачу в мапу
                    prioritizedTasks.add(task);
                    return task;
                }
                case EPIC: {
                    Epic epic = new Epic(id, name, description, status, duration, startTime);
                    epics.put(id, epic); //// положил эпик в мапу
                    return epic;
                }
                case SUBTASK: {
                    int epicId = Integer.parseInt(split[7]);
                    Subtask subtask = new Subtask(id, name, description, status, duration, startTime, epicId);
                    subtasks.put(id, subtask); //// положил сабкаску в мапу
                    prioritizedTasks.add(subtask);
                    epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId()); /// добавил id сабтаски в список к эпику
                    calculateEpicDuration(epics.get(subtask.getEpicId()));
                    return subtask;
                }
            }
        }
        return null;
    }

    /**
     * сохранение менеджера в строку
     */
    static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        if (manager.getHistory().isEmpty()) {
            return "";
        } else {
            sb.append(manager.getHistory().get(0).getId());
            for (int i = 1; i < manager.getHistory().size(); i++) {
                sb.append(",");
                sb.append(manager.getHistory().get(i).getId());
            }
            return sb.toString();
        }
    }

    /**
     * восстановление менеджера из строки
     */
    static List<Integer> historyFromString(String value) {
        List<Integer> historyList = new ArrayList<>();
        if (!value.isEmpty()) {
            String[] split = value.split(",");
            for (String id : split) {
                historyList.add(Integer.parseInt(id));
            }
        }
        return historyList;
    }

    /**
     * восстанавливает данные менеджера из файла при запуске программы
     */
    static public FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fbtManager = new FileBackedTasksManager(file);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                int maxId = 0;
                Task task;
                String line = reader.readLine(); // считываем таски
                if (line != null) {
                    while (!(line = reader.readLine()).isBlank()) {
                        task = fbtManager.taskFromString(line);

                        if (task.getId() > maxId) {
                            maxId = task.getId();
                        }
                    }

                    String lineHistory = reader.readLine(); // считываем историю
                    if (lineHistory != null) {
                        List<Integer> historyIds = historyFromString(lineHistory);
                        for (Integer id : historyIds) {
                            if (fbtManager.tasks.containsKey(id)) {
                                fbtManager.historyManager.add(fbtManager.tasks.get(id));
                            } else if (fbtManager.epics.containsKey(id)) {
                                fbtManager.historyManager.add(fbtManager.epics.get(id));
                            } else {
                                fbtManager.historyManager.add(fbtManager.subtasks.get(id));
                            }
                        }
                    }
                    fbtManager.generateId = maxId + 1;
                }
            } catch (IOException exception) {
                throw new ManagerSaveException("Произошла ошибка при чтении из файла", exception);
            }
        }
        return fbtManager;
    }

    @Override
    public void cleanAllTasks() {
        super.cleanAllTasks();
        save();
    }

    @Override
    public Task getTaskById (int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public int addNewTask(Task task) {
        int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void cleanAllSubtasks() {
        super.cleanAllSubtasks();
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        int id = super.addNewSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void cleanAllSubtasksByEpic(Epic epic) {
        super.cleanAllSubtasksByEpic(epic);
        save();
    }

    @Override
    public void cleanAllEpics() {
        super.cleanAllEpics();
        save();
    }

    @Override
    public Epic getEpicById (int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }
}


