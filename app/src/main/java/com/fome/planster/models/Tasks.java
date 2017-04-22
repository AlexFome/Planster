package com.fome.planster.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Alex on 14.03.2017.
 */
public class Tasks {

    private ArrayList<Task> tasks;

    private DateComparator dateComparator = new DateComparator();
    private PriorityComparator priorityComparator = new PriorityComparator();

    public Tasks () {
        tasks = new ArrayList<>();
    }

    public ArrayList<Task> getTasks () {
        if (tasks == null) tasks = new ArrayList<>();
        return tasks;
    }
    public void addTask (Task task) {
        if (tasks == null) tasks = new ArrayList<>();
        tasks.add(task);
    }
    public void removeTask (Task task) {
        int taskId = task.getId();
        task = null;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == taskId) {
                task = tasks.get(i);
            }
        }
        if (tasks == null || task == null) return;
        tasks.remove(task);
    }

    public int getTasksNum () {
        if (tasks == null) {
            tasks = new ArrayList<>();
            return 0;
        }

        return tasks.size();

    }

    public void sortByDate () {
        Collections.sort(tasks, dateComparator);
    }

    public void sortByPriority () {
        Collections.sort(tasks, priorityComparator);
    }

    public Task find (Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                return tasks.get(i);
            }
        }
        return null;
    }

    public void addTasks (Tasks tasks) {
        this.tasks.addAll(tasks.getTasks());
    }

    class DateComparator implements Comparator<Task> {
        public int compare(Task task1, Task task2) {
            return task1.getStartDate().compareTo(task2.getStartDate());
        }
    }

    class PriorityComparator implements Comparator<Task> {
        public int compare(Task task1, Task task2) {
            return Float.compare(task2.getPriority(),task1.getPriority());
        }
    }

}
