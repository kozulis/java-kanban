package ru.yandex.praktikum.model;

import java.util.ArrayList;

public class Epic extends Task{
    public ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
    }

    public void addSubtaskId(int id) {
        subTaskIds.add(id);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void cleanAllSubtask() {  /* на случай, если нужно будет удалить все подзадачи определенного эпика */
        subTaskIds.clear();
    }

     public void removeSubtaskById(int id) {  /* на случай, если нужно будет удалить все подзадачи определенного эпика*/
        subTaskIds.remove(Integer.valueOf(id));
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", subTaskIds=" + subTaskIds +
                ", status='" + status + '\'' +
                '}';
    }
}
