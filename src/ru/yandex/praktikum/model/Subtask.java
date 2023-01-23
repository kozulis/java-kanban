package ru.yandex.praktikum.model;

public class Subtask extends Task{
    protected TaskType taskType = TaskType.SUBTASK;
    private int epicId;
    public Subtask(int epicId, String name, String description, TaskStatus status) {
        super(name, description, status);
        this.epicId = epicId;
    }
    @Override
    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
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
