package ru.yandex.praktikum.model;

import java.util.ArrayList;

public class Epic extends Task{
    public ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
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

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", subTaskIds=" + subtaskIds +
                ", status='" + status + '\'' +
                '}';
    }
}
