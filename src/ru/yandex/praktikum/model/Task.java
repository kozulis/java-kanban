package ru.yandex.praktikum.model;

import java.util.Objects;

public class Task {
    protected int id;
    protected TaskType taskType = TaskType.TASK;
    protected String name;
    protected TaskStatus status;
    protected String description;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
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
        return String.format("%d,%s,%s,%s,%s,", id, taskType, name, status,
                description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskType=" + taskType +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
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
