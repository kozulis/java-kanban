package ru.yandex.praktikum.model;

import java.time.LocalDateTime;

public class Subtask extends Task{
    protected TaskType taskType = TaskType.SUBTASK;
    private int epicId;

    public Subtask(int id, String name, String description, TaskStatus status, long duration, LocalDateTime startTime, int epicId) {
        super(id, name, description, status, duration, startTime);
        this.epicId = epicId;
        setTaskType(TaskType.SUBTASK);
    }

    public Subtask(int epicId, String name, String description, long duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
        setTaskType(TaskType.SUBTASK);
    }

    public Subtask(int id, int epicId, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.epicId = epicId;
        setTaskType(TaskType.SUBTASK);
    }

    public Subtask(int epicId, String name, String description) {
        super(name, description);
        this.epicId = epicId;
        setTaskType(TaskType.SUBTASK);
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
        return String.format("%d,%s,%s,%s,%s,%s,%s,%d", id, taskType, name, status,
                description, duration, startTime, epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", taskType=" + taskType +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", startTime=" + (startTime == null ? "null" : startTime.format(formatter)) +
                ", endTime=" + (getEndTime() == null ? null : getEndTime().format(formatter)) +
                ", epicId=" + epicId +
                '}';
    }
}
