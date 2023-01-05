package ru.yandex.praktikum.service;

import ru.yandex.praktikum.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;

    private final Map<Integer, Node> idTasksNodes = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (idTasksNodes.containsKey(id)) {
            removeNode(idTasksNodes.get(id)); //удаление ноды из списка
            idTasksNodes.remove(id); // удаление элемента из мапы
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    void linkLast(Task task) { //добавить задачу в конец списка
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        idTasksNodes.put(task.getId(), newNode);
    }

    List<Task> getTasks() { //собрать все задачи из списка в обычный ArrayList
        List<Task> historyList = new ArrayList<>();
        Node node = head;
        while (node != null) {
            historyList.add(node.task);
            node = node.next;
        }
        return historyList;
    }

    void removeNode(Node node) { //удаление ноды, замена ссылок
        if (node != null) {
            if (node.prev != null && node.next != null) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            } else if (node.prev == null) {
                head = node.next;
                head.prev = null;
            } else {
                tail = node.prev;
                tail.next = null;
            }
        }
    }

    static class Node {
        public Node prev;
        public Task task;
        public Node next;


        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;

        }
    }
}
