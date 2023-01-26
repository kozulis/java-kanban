package ru.yandex.praktikum.model;

public class Subtask extends Task{
    protected TaskType taskType = TaskType.SUBTASK;
    private int epicId;
    public Subtask(int epicId, String name, String description, TaskStatus status) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, int epicId, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toCSVForm() {
        return String.format("%d,%s,%s,%s,%s,%d", id, taskType, name, status,
                description, epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", taskType=" + taskType +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}
