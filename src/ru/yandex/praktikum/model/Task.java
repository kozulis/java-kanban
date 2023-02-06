package ru.yandex.praktikum.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected int id;
    protected TaskType taskType = TaskType.TASK;
    protected String name;
    protected TaskStatus status;
    protected String description;
    protected long duration;
    protected LocalDateTime startTime;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm");

    public Task(int id, String name, String description, TaskStatus status, long duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String toCSVForm() {
        return String.format("%d,%s,%s,%s,%s,%s,%s", id, taskType, name, status,
                description, duration, startTime);
    }

    public LocalDateTime getEndTime() {
        return startTime != null ? startTime.plusMinutes(duration) : null;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskType=" + taskType +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", startTime=" + (startTime == null ? "null" : startTime.format(formatter)) +
                ", endTime=" + (getEndTime() == null ? "null" : getEndTime().format(formatter)) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && taskType == task.taskType && Objects.equals(name, task.name) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskType, name, description);
    }
}
