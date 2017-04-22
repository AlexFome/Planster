package com.fome.planster.models;

import com.fome.planster.TasksManager;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Alex on 14.03.2017.
 */
public class Task implements Serializable {

    private int id;
    private Date startDate;
    private Date endDate;
    private String name;
    private float priority;
    private String notes;
    private com.fome.planster.models.Place place;
    private boolean notificationEnabled;
    private boolean alertEnabled;
    private int notificateBefore;
    private List list;
    private TasksManager.TaskType taskType = TasksManager.TaskType.GENERAL;
    private TasksManager.RepeatType repeatType = TasksManager.RepeatType.ONCE;
    private Contacts contacts;

    public Task () {}

    public Task (int id, Date startDate, Date endDate, String name, String notes, List list, TasksManager.TaskType taskType, float priority, com.fome.planster.models.Place place, boolean notificationEnabled, boolean alertEnabled, int notificateBefore, Contacts contacts, TasksManager.RepeatType repeatType) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.notes = notes;
        this.list = list;
        this.taskType = taskType;
        this.place = place;
        this.priority = priority;
        this.notificationEnabled = notificationEnabled;
        this.alertEnabled = alertEnabled;
        this.notificateBefore = notificateBefore;
        this.contacts = contacts;
        this.repeatType = repeatType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getId () {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPriority() {
        return priority;
    }

    public void setPriority(float priority) {
        this.priority = priority;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public com.fome.planster.models.Place getPlace() {
        return place;
    }

    public void setPlace(com.fome.planster.models.Place place) {
        this.place = place;
    }

    public int getNotificateBefore() {
        return notificateBefore;
    }

    public void setNotificateBefore(int notificateBefore) {
        this.notificateBefore = notificateBefore;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public boolean isAlertEnabled() {
        return alertEnabled;
    }

    public TasksManager.TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TasksManager.TaskType taskType) {
        this.taskType = taskType;
    }

    public TasksManager.RepeatType getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(TasksManager.RepeatType repeatType) {
        this.repeatType = repeatType;
    }
}
