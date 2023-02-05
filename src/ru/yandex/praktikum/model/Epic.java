package ru.yandex.praktikum.model;

import java.util.ArrayList;

public class Epic extends Task{
    protected TaskType taskType = TaskType.EPIC;
    private final ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        setTaskType(TaskType.EPIC);
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        setTaskType(TaskType.EPIC);
    }

    public Epic(String name, String description) {
        super(name, description);
        setTaskType(TaskType.EPIC);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    /**
     * добавление подзадачи в список
     */
    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    /**
     * удаление всего списка подзадач
     */
    public void cleanAllSubtask() {
        subtaskIds.clear();
    }

    /**
     * удаление подзадачи из списка
     */
     public void removeSubtaskById(Integer id) {
        subtaskIds.remove(id);
    }

//    @Override
//    public String toCSVForm() {
//        return String.format("%d,%s,%s,%s,%s,", id, taskType, name, status,
//                description);
//    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", taskType=" + taskType +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
