package com.fome.planster;

import android.content.Context;

import com.fome.planster.models.Task;
import com.fome.planster.models.Tasks;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 04.04.2017.
 */
public class TasksManager {

    static Context context;
    static String all_tasks_key = "all_tasks";
    static String weekdays_tasks_key = "weekdays_tasks";
    static String weekends_tasks_key = "weekends_tasks";

    public enum TaskType {
        GENERAL, JOB, PERSONAL
    }

    public enum RepeatType {
        ONCE, WEEKDAYS, WEEKENDS
    }

    public static void init (Context c) {
        context = c;
    }

    public static Tasks getTasksForDate (Date date) {
        date = DateManager.parseDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date));
        Tasks tasks = (Tasks) SharedPreferencesManager.getObject(date.toString(), Tasks.class);
        if (tasks == null) tasks = new Tasks();
        if (DateManager.isWeekend(date)) {
            Tasks weekendsTasks = getWeekendsTasks();
            if (weekendsTasks != null && weekendsTasks.getTasks() != null)
                tasks.addTasks(weekendsTasks);
        } else {
            Tasks weekdaysTasks = getWeekdaysTasks();
            if (weekdaysTasks != null && weekdaysTasks.getTasks() != null)
                tasks.addTasks(weekdaysTasks);
        }
        return tasks;
    }

    public static void saveTask (Task task) {
        Task t = findTask(task);
        if (t != null) {
            removeTask(t);
        }

        if (task.getRepeatType() == RepeatType.WEEKDAYS) {
            Tasks weekdaysTasks = getWeekdaysTasks();
            if (weekdaysTasks == null) weekdaysTasks = new Tasks();
            weekdaysTasks.addTask(task);
            SharedPreferencesManager.saveObject(weekdays_tasks_key, weekdaysTasks);
            return;
        } else if (task.getRepeatType() == RepeatType.WEEKENDS) {
            Tasks weekendsTasks = getWeekendsTasks();
            if (weekendsTasks == null) weekendsTasks = new Tasks();
            weekendsTasks.addTask(task);
            SharedPreferencesManager.saveObject(weekends_tasks_key, weekendsTasks);
            return;
        }

        Date startDate = task.getStartDate();
        Date endDate = task.getEndDate();
        Date startDateDay = DateManager.parseDate(DateManager.getYear(startDate), DateManager.getMonthNum(startDate), DateManager.getDay(startDate));
        Date endDateDay = DateManager.parseDate(DateManager.getYear(endDate), DateManager.getMonthNum(endDate), DateManager.getDay(endDate));
        ArrayList<Date> involvedDated = DateManager.getDaysBetweenDates(startDateDay, endDateDay);

        for (int i = 0; i < involvedDated.size(); i++) {
            Date date = involvedDated.get(i);
            Tasks tasksForDay = getTasksForDate(date);
            if (tasksForDay == null) tasksForDay = new Tasks();
            tasksForDay.addTask(task);
            SharedPreferencesManager.saveObject(date.toString(), tasksForDay);
        }

        Tasks allTasks = (Tasks) SharedPreferencesManager.getObject(all_tasks_key, Tasks.class);
        allTasks.addTask(task);
        SharedPreferencesManager.saveObject(all_tasks_key, allTasks);

    }

    public static Tasks getWeekdaysTasks () {
        Tasks tasks = (Tasks) SharedPreferencesManager.getObject(weekdays_tasks_key, Tasks.class);
        if (tasks == null) {
            tasks = new Tasks();
        }
        return tasks;
    }

    public static Tasks getWeekendsTasks () {
        Tasks tasks = (Tasks) SharedPreferencesManager.getObject(weekends_tasks_key, Tasks.class);
        if (tasks == null) {
            tasks = new Tasks();
        }
        return tasks;
    }

    public static void removeTask (Task task) {

        if (task.getRepeatType() == RepeatType.WEEKDAYS) {
            Tasks weekdaysTasks = getWeekdaysTasks();
            if (weekdaysTasks == null) return;
            weekdaysTasks.removeTask(task);
            SharedPreferencesManager.saveObject(weekdays_tasks_key, weekdaysTasks);
            return;
        } else if (task.getRepeatType() == RepeatType.WEEKENDS) {
            Tasks weekendsTasks = getWeekendsTasks();
            if (weekendsTasks == null) return;
            weekendsTasks.removeTask(task);
            SharedPreferencesManager.saveObject(weekends_tasks_key, weekendsTasks);
            return;
        }

        Date startDate = task.getStartDate();
        Date endDate = task.getEndDate();
        Date startDateDay = DateManager.parseDate(DateManager.getYear(startDate), DateManager.getMonthNum(startDate), DateManager.getDay(startDate));
        Date endDateDay = DateManager.parseDate(DateManager.getYear(endDate), DateManager.getMonthNum(endDate), DateManager.getDay(endDate));
        ArrayList<Date> involvedDated = DateManager.getDaysBetweenDates(startDateDay, endDateDay);

        for (int i = 0; i < involvedDated.size(); i++) {
            Date date = involvedDated.get(i);
            Tasks tasksForDay = getTasksForDate(date);
            tasksForDay.removeTask(task);
            SharedPreferencesManager.saveObject(date.toString(), tasksForDay);
        }

        Tasks allTasks = (Tasks) SharedPreferencesManager.getObject(all_tasks_key, Tasks.class);
        allTasks.removeTask(task);

        if (task.isNotificationEnabled()) {
            NotificationManager.removeNotification(context, task.getId());
        }
        if (task.isAlertEnabled()) {
            AlarmManager.removeAlarm(task);
        }

        SharedPreferencesManager.saveObject(all_tasks_key, allTasks);

    }

    public static Task findTask (Task task) {
        Tasks allTasks = (Tasks) SharedPreferencesManager.getObject(all_tasks_key, Tasks.class);
        if (allTasks == null) {
            allTasks = new Tasks();
            SharedPreferencesManager.saveObject(all_tasks_key, allTasks);
        }
        Tasks weekdaysTasks = getWeekdaysTasks();
        Tasks weekendTasks = getWeekendsTasks();
        if (weekdaysTasks != null) {
            allTasks.addTasks(getWeekdaysTasks());
        }
        if (weekendTasks != null) {
            allTasks.addTasks(getWeekendsTasks());
        }
        Task t = allTasks.find (task);
        if (t != null) {
            return t;
        } else {
            return null;
        }
    }

    public static int getNewId () {
        int id = SharedPreferencesManager.getInt("lastId", 1);
        id++;
        SharedPreferencesManager.saveInt("lastId", id);
        return id;
    }

    public static void refreshTasksAlarms () {
        Date currentDate = new Date();
        Tasks allTasks = (Tasks) SharedPreferencesManager.getObject(all_tasks_key, Tasks.class);
        ArrayList<Task> tasks = allTasks.getTasks();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getStartDate().after(currentDate)) {
                if (task.isNotificationEnabled() || task.isAlertEnabled()) {
                    AlarmManager.setAlarm(task);
                }
            }
        }
        Tasks weekdaysTasks = getWeekdaysTasks();
        Tasks weekendsTasks = getWeekendsTasks();
        ArrayList<Date> days = new ArrayList<>();
        days.add(currentDate);
        for (int i = 1; i < 4; i++) {
            Date date = DateManager.getDateAfterDays(currentDate, i);
            DateManager.parseDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date));
            days.add(date);
        }

        for (int i = 0; i < 4; i++) {
            Date date = days.get(i);
            boolean weekday = !DateManager.isWeekend(date);
            if (weekday) {
                for (int t = 0; t < weekdaysTasks.getTasksNum(); t++) {
                    Task task = weekdaysTasks.getTasks().get(t);
                    if (DateManager.compareDays(task.getStartDate(), date) == 0 && task.getStartDate().after(date)) {
                        Date startDate = task.getStartDate();
                        task.setStartDate(DateManager.parseFullDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date), DateManager.getHours(startDate), DateManager.getMinutes(startDate), 0));
                        if (task.isNotificationEnabled() || task.isAlertEnabled())
                            AlarmManager.setAlarm(task);
                    }
                }
            } else {
                for (int t = 0; t < weekendsTasks.getTasksNum(); t++) {
                    Task task = weekendsTasks.getTasks().get(t);
                    if (DateManager.compareDays(task.getStartDate(), date) == 0 && task.getStartDate().after(date)) {
                        Date startDate = task.getStartDate();
                        task.setStartDate(DateManager.parseFullDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date), DateManager.getHours(startDate), DateManager.getMinutes(startDate), 0));
                        if (task.isNotificationEnabled() || task.isAlertEnabled())
                            AlarmManager.setAlarm(task);
                    }
                }
            }
        }

    }

}
