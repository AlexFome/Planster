package com.fome.planster;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fome.planster.models.Contact;
import com.fome.planster.models.Contacts;
import com.fome.planster.models.Task;
import com.fome.planster.models.Tasks;
import com.fome.planster.adapters.TasksAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class DayActivity extends AppCompatActivity {

    private Date date;

    private LinearLayout sortingBar;
    private TextView openCalendarButton;
    private TextView openSettingsButton;

    private ListView tasksGrid;
    private TasksAdapter tasksAdapter;
    private Tasks tasks;
    private TextView tasksNum;

    private LinearLayout newTaskButton;

    boolean sortByDate = true;

    private final int OPEN_CALENDAR_REQUEST_CODE = 0;
    private final int CREATE_NEW_TASK_REQUEST_CODE = 1;
    private final int OPEN_LIST_REQUEST_CODE = 2;
    private final int OPEN_SETTINGS_REQUEST_CODE = 3;

    private Gson gson = new Gson ();

    private Task alertedTask;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        /*We're adding this group of flags to let the app start in case of alert*/
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        /*We're passing context to all static classes*/
        DateManager.init(getApplicationContext());
        TasksManager.init(getApplicationContext());
        SharedPreferencesManager.init(getApplicationContext());
        FirebaseAnalyticsManager.init(getApplicationContext());
        AlarmManager.init(getApplicationContext());

        if (SharedPreferencesManager.isFirstLaunch()) { // Creates intro task in case of first app launch
            Task firstTask = new Task(
                    TasksManager.getNewId(),
                    new Date(),
                    DateManager.getDateAfterMinutes(new Date(), 15),
                    getResources().getString(R.string.create_your_first_task),
                    getResources().getString(R.string.press_red_button_in_the_right_botton_corner_to_create_your_first_task),
                    new com.fome.planster.models.List(),
                    TasksManager.TaskType.GENERAL,
                    50,
                    null,
                    false,
                    false,
                    0,
                    new Contacts(new ArrayList<Contact>()),
                    TasksManager.RepeatType.ONCE
            );
            TasksManager.saveTask(firstTask);
        }

        bindViews();

        /*Opening today's task list*/
        Date currentDate = new Date ();
        openDay(currentDate);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("alert")) { // If app is started by alert manager
            alertedTask = gson.fromJson(intent.getExtras().getString("alert"), Task.class);
            mediaPlayer = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

    }

    void bindViews () {

        FontManager.setFont(this, findViewById(R.id.activity_day), FontManager.BOLDFONT);
        FontManager.setFont(this, findViewById(R.id.top_bar), FontManager.ICONFONT);

        sortingBar = (LinearLayout) findViewById(R.id.sorting_bar);
        sortingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSorting();
            }
        });

        tasksGrid = (ListView) findViewById(R.id.tasks_grid);
        newTaskButton = (LinearLayout) findViewById(R.id.new_task_button);
        tasksNum = (TextView) findViewById(R.id.tasks_num);
        openCalendarButton = (TextView) findViewById (R.id.open_calendar_button);
        openSettingsButton = (TextView) findViewById (R.id.open_settings_button);

        openCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });
        openSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings ();
            }
        });
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTaskCreationActivity();
            }
        });

        TextView textSign = (TextView) findViewById(R.id.tasks_sign);
        if (textSign.length() > 5) {
            textSign.setTextSize((getResources().getDimension((R.dimen.capital_text_size))/ getResources().getDisplayMetrics().density) * 0.7f);
        } else {
            textSign.setTextSize(getResources().getDimension(R.dimen.capital_text_size) / getResources().getDisplayMetrics().density);
        }

    }

    void toggleSorting () {
        alarmOff();
        tasksAdapter.closeAllOpenedTasks();
        if (sortByDate) {
            sortByPriority();
        } else {
            sortByDate ();
        }
    }

    void sortByDate () {
        sortByDate = true;
        ((TextView) sortingBar.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
        ((TextView) sortingBar.getChildAt(1)).setTextColor(ContextCompat.getColor(this, R.color.light_grey_2));
        tasks.sortByDate();
        tasksAdapter.notifyDataSetChanged();
    }

    void sortByPriority () {
        sortByDate = false;
        ((TextView) sortingBar.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.light_grey_2));
        ((TextView) sortingBar.getChildAt(1)).setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
        tasks.sortByPriority();
        tasksAdapter.notifyDataSetChanged();
    }

    void openDay (Date date) {
        this.date = date;
        tasks = TasksManager.getTasksForDate(date);
        if (tasks == null || tasks.getTasks() == null) {
            tasks = new Tasks();
        }
        showTasks(tasks.getTasks());
    }

    public void refreshTasksNumBar () {
        String one = getResources().getString(R.string.you_have);
        String two = "<font color='" + ContextCompat.getColor(this, R.color.light_grey_3) + "'>" + " " +tasks.getTasksNum() + "</font>";
        String three = " " + (tasks.getTasksNum() == 1 ? getResources().getString(R.string.task):getResources().getString(R.string.ts)) + " ";
        String four = DateManager.compareDays(new Date(), date) == 0 ? getResources().getString(R.string.today):getResources().getString(R.string.for_) + " " + DateManager.getDay(date) + getResources().getString(R.string.th_of) + " " + DateManager.getMonthName(date);
        tasksNum.setText(Html.fromHtml(one + two + three + four));
    }

    void showTasks (ArrayList<Task> tasks) {

        tasksAdapter = new TasksAdapter(this, tasksGrid, tasks, date);
        tasksGrid.setAdapter(tasksAdapter);
        tasksGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tasksAdapter.extandView(view);

            }
        });
        sortByDate ();
        refreshTasksNumBar();

    }

    void openTaskCreationActivity() {
        if (alertedTask != null) {
            alertedTask = null;
            tasksAdapter.notifyDataSetChanged();
            alarmOff();
        }
        Intent intent = new Intent(this, TaskCreationActivity.class);
        startActivityForResult(intent, CREATE_NEW_TASK_REQUEST_CODE);
    }

    public void openTask (Task task) {
        Intent intent = new Intent(this, TaskCreationActivity.class);
        intent.putExtra("task", gson.toJson(task));
        startActivityForResult(intent, CREATE_NEW_TASK_REQUEST_CODE);
    }

    public void openTasksList (Task task) {
        if (task.getList() == null || task.getList().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.list_is_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("task", gson.toJson(task));
        startActivityForResult(intent, OPEN_LIST_REQUEST_CODE);
    }

    void openSettings () {
        if (alertedTask != null) {
            alertedTask = null;
            tasksAdapter.notifyDataSetChanged();
            alarmOff();
        }
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, OPEN_SETTINGS_REQUEST_CODE);
    }

    void openCalendar () {
        if (alertedTask != null) {
            alertedTask = null;
            tasksAdapter.notifyDataSetChanged();
            alarmOff();
        }
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivityForResult(intent, OPEN_CALENDAR_REQUEST_CODE);
    }

    public void alarmOff () {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
        mediaPlayer.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CREATE_NEW_TASK_REQUEST_CODE:
                    openDay(new Date());
                    break;
                case OPEN_CALENDAR_REQUEST_CODE:
                    Date date = gson.fromJson(data.getExtras().getString("date"), Date.class);
                    openDay(date);
                    break;
                case OPEN_LIST_REQUEST_CODE:
                    Task task = gson.fromJson(data.getExtras().getString("task"), Task.class);
                    TasksManager.saveTask(task);
                    openDay(new Date());
                    break;
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (alertedTask == null) return; // If app is started by alert manager
        tasksAdapter.alertTask(alertedTask); // alerting a task
        alertedTask = null;
    }

}
