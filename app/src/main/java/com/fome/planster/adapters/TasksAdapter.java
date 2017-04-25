package com.fome.planster.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.fome.planster.DateManager;
import com.fome.planster.DayActivity;
import com.fome.planster.FontManager;
import com.fome.planster.models.Task;
import com.fome.planster.R;
import com.fome.planster.TasksManager;
import com.fome.planster.UIManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 15.03.2017.
 */
public class TasksAdapter extends BaseAdapter {

    ListView listView;
    Context context;
    Date displayedDate;
    ArrayList<Task> tasks;
    ArrayList<Target> targets = new ArrayList<>();

    ArrayList<View> extendedTasks = new ArrayList<>();

    int extensionHeight = 100;

    public TasksAdapter (Context context, ListView listView, ArrayList<Task> tasks, Date displayedDate) {

        for (int i = 0; i < tasks.size(); i++) {
            for (int t = i + 1; t < tasks.size(); t++) {
                if (tasks.get(i).getId() == tasks.get(t).getId()) {
                    tasks.remove(i);
                    i--;
                    break;
                }
            }
        }

        this.context = context;
        this.tasks = tasks;
        this.displayedDate = displayedDate;
        this.listView = listView;
        extensionHeight = UIManager.dpToPx(context, extensionHeight);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Task task = (Task) getItem(position);

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task, null);
            viewHolder = new ViewHolder();
            viewHolder.position = position;
            viewHolder.view = convertView;
            viewHolder.layout_parent = (LinearLayout) convertView.findViewById(R.id.task_layout_parent);
            viewHolder.locationText = (TextView) convertView.findViewById(R.id.location_text);
            viewHolder.taskDate = (TextView) convertView.findViewById(R.id.task_date);
            viewHolder.taskText = (TextView) convertView.findViewById(R.id.task_text);
            viewHolder.taskType = (TextView) convertView.findViewById(R.id.task_type);
            viewHolder.extraBar = (LinearLayout) convertView.findViewById(R.id.extra_bar);
            viewHolder.editTask = (TextView) convertView.findViewById(R.id.edit_task);
            viewHolder.openTaskList = (TextView) convertView.findViewById(R.id.open_task_list);
            viewHolder.removeTask = (TextView) convertView.findViewById(R.id.remove_task);
            viewHolder.alarmOff = (TextView) convertView.findViewById(R.id.alarm_off);

            viewHolder.layout_parent.setBackgroundResource(R.drawable.task);

            if (task.getList() != null && !task.getList().isEmpty()) {
                viewHolder.openTaskList.setBackgroundResource(R.drawable.empty_circle);
                viewHolder.openTaskList.setTextColor(ContextCompat.getColor(context, R.color.red));
            } else {
                viewHolder.openTaskList.setBackgroundResource(R.drawable.dark_empty_circle);
                viewHolder.openTaskList.setTextColor(ContextCompat.getColor(context, R.color.light_grey_2));
            }

            viewHolder.openTaskList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DayActivity) context).openTasksList(task);
                }
            });
            viewHolder.editTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DayActivity) context).openTask(task);
                }
            });
            viewHolder.removeTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TasksManager.removeTask(task);
                    tasks.remove(task);
                    ((DayActivity) context).refreshTasksNumBar();
                    closeAllOpenedTasks();
                    notifyDataSetChanged();
                }
            });
            viewHolder.alarmOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmOff();
                }
            });

            viewHolder.notesBar = (LinearLayout) convertView.findViewById(R.id.task_notes);
            viewHolder.notesText = (TextView) viewHolder.notesBar.getChildAt(0);
            convertView.setTag(viewHolder);
            FontManager.setFont(context, convertView, FontManager.BOLDFONT);
            FontManager.setFont(context, viewHolder.extraBar,FontManager.ICONFONT);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.alarmOff.setVisibility(View.GONE);
        viewHolder.removeTask.setVisibility(View.VISIBLE);
        viewHolder.editTask.setVisibility(View.VISIBLE);
        viewHolder.openTaskList.setVisibility(View.VISIBLE);

        LinearLayout contacts = (LinearLayout) convertView.findViewById(R.id.contacts);
        RelativeLayout location = (RelativeLayout) convertView.findViewById(R.id.places);

        TextView taskDate = (TextView) convertView.findViewById(R.id.task_date);

        Date startDate = task.getStartDate();
        Date endDate = task.getEndDate();

        if (task.getRepeatType() == TasksManager.RepeatType.WEEKDAYS) {
            String date = context.getResources().getString(R.string.every_weekday) + " ";
            String hours = DateManager.getHours(task.getStartDate()) < 10 ? "0" + DateManager.getHours(task.getStartDate()): DateManager.getHours(task.getStartDate()) + "";
            String munites = DateManager.getMinutes(task.getStartDate()) < 10 ? "0" + DateManager.getMinutes(task.getStartDate()): DateManager.getMinutes(task.getStartDate()) + "";
            date = date + hours + ":" + munites;
            taskDate.setText(date);
        } else if (task.getRepeatType() == TasksManager.RepeatType.WEEKENDS) {
            String date = context.getResources().getString(R.string.every_weekend) + " ";
            String hours = DateManager.getHours(task.getStartDate()) < 10 ? "0" + DateManager.getHours(task.getStartDate()): DateManager.getHours(task.getStartDate()) + "";
            String munites = DateManager.getMinutes(task.getStartDate()) < 10 ? "0" + DateManager.getMinutes(task.getStartDate()): DateManager.getMinutes(task.getStartDate()) + "";
            date = date + hours + ":" + munites;
            taskDate.setText(date);
        } else {
            String startDateString;
            if (DateManager.compareDays(startDate, displayedDate) == 0) {
                String startHours = String.valueOf(DateManager.getHours(startDate));
                String startMinutes = String.valueOf(DateManager.getMinutes(startDate));
                if (startHours.length() == 1) {
                    startHours = "0" + startHours;
                }
                if (startMinutes.length() == 1) {
                    startMinutes = "0" + startMinutes;
                }
                startDateString = startHours + ":" + startMinutes;
            } else {
                startDateString = String.valueOf(DateManager.getDay(startDate) + context.getResources().getString(R.string.th_of) + " " + DateManager.getMonthName(startDate));
            }

            String endDateString;
            if (DateManager.compareDays(endDate, displayedDate) == 0) {
                String endHours = String.valueOf(DateManager.getHours(endDate));
                String endMinutes = String.valueOf(DateManager.getMinutes(endDate));
                if (endHours.length() == 1) {
                    endHours = "0" + endHours;
                }
                if (endMinutes.length() == 1) {
                    endMinutes = "0" + endMinutes;
                }
                endDateString = endHours + ":" + endMinutes;
            } else {
                endDateString = String.valueOf(DateManager.getDay(endDate) + context.getResources().getString(R.string.th_of) + " " + DateManager.getMonthName(endDate));
            }

            String date = startDateString + " - " + endDateString;
            taskDate.setText(date);
        }

        if (taskDate.length() >= 19) {
            taskDate.setTextSize((int) (context.getResources().getDimension(R.dimen.middle_text_size) / context.getResources().getDisplayMetrics().density));
        } else {
            taskDate.setTextSize((int) (context.getResources().getDimension(R.dimen.big_text_size) / context.getResources().getDisplayMetrics().density));
        }

        TextView taskText = (TextView) convertView.findViewById(R.id.task_text);
        taskText.setText(task.getName());
        TextView taskType = (TextView) convertView.findViewById(R.id.task_type);
        taskType.setText(String.valueOf(task.getTaskType()));
        switch (task.getTaskType()) {
            case JOB:
                taskType.setText(context.getResources().getString(R.string.job));
                taskType.setTextColor(ContextCompat.getColor(context, R.color.job_type_color));
                break;
            case GENERAL:
                taskType.setText(context.getResources().getString(R.string.general));
                taskType.setTextColor(ContextCompat.getColor(context, R.color.general_type_color));
                break;
            case PERSONAL:
                taskType.setText(context.getResources().getString(R.string.personal));
                taskType.setTextColor(ContextCompat.getColor(context, R.color.personal_type_color));
                break;
        }

        String notes;
        if (task.getNotes().equals("")) {
            notes = context.getResources().getString(R.string.no_notes);
        } else {
            notes = task.getNotes();
        }
        ((TextView) viewHolder.notesBar.getChildAt(0)).setText(notes);

        int selectedContactsNum;
        if (task.getContacts() == null) {
            selectedContactsNum = 0;
        } else {
            selectedContactsNum = task.getContacts().getSelectedContacts().size();
        }
        
        for (int i = 0; i < 4; i++) {
            if (i < 3) {
                final ImageView thumbnail = (ImageView) contacts.getChildAt(i);
                if (selectedContactsNum > i) {
                    thumbnail.setVisibility(View.VISIBLE);
                    Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            thumbnail.setImageBitmap(UIManager.getRoundedCornerBitmap(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {}

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {}
                    };
                    Picasso.with(context).load(task.getContacts().getSelectedContacts().get(i).getPhotoThumbnailUri()).into(target);
                    targets.add(target);
                } else {
                    thumbnail.setVisibility(View.GONE);
                }
            } else {
                if (selectedContactsNum > i) {
                    contacts.getChildAt(3).setVisibility(View.VISIBLE);
                    ((TextView)((LinearLayout) contacts.getChildAt(3)).getChildAt(0)).setText("+" + (selectedContactsNum - 3));
                } else {
                    contacts.getChildAt(3).setVisibility(View.GONE);
                }
            }
        }

        if (task.getPlace() != null) {
            location.setVisibility(View.VISIBLE);
            ((TextView) (((LinearLayout) location.getChildAt(0)).getChildAt(0))).setTypeface(FontManager.getTypeface(context, FontManager.ICONFONT));
            ((TextView) (((LinearLayout) location.getChildAt(0)).getChildAt(1))).setText("  " + task.getPlace().getName());
        } else {
            location.setVisibility(View.GONE);
        }

        viewHolder.layout_parent.setBackgroundResource(R.drawable.task);
        viewHolder.taskText.setTextColor(ContextCompat.getColor(context, R.color.light_grey_3));
        viewHolder.taskDate.setTextColor(ContextCompat.getColor(context, R.color.light_grey_2));
        viewHolder.notesText.setTextColor(ContextCompat.getColor(context, R.color.light_grey_2));
        int color = ContextCompat.getColor(context, R.color.light_grey_2);
        viewHolder.locationText.setTextColor(color);
        ((TextView)((LinearLayout) viewHolder.locationText.getParent()).getChildAt(0)).setTextColor(color);

        return convertView;
    }

    public void extandView (View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (!viewHolder.extanded) {
            ViewPropertyObjectAnimator.animate(viewHolder.extraBar).height(extensionHeight).setDuration(300).start();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            viewHolder.notesBar.setLayoutParams(layoutParams);
            viewHolder.extanded = true;
            extendedTasks.add(view);
        } else {
            ViewPropertyObjectAnimator.animate(viewHolder.extraBar).height(0).setDuration(300).start();
            ViewPropertyObjectAnimator.animate(viewHolder.notesBar).height(0).setDuration(300).start();
            viewHolder.extanded = false;
            extendedTasks.remove(view);
        }

    }

    public void alertTask(Task task) {
        int position = -1;
        for (int i = 0; i < tasks.size(); i++) {
            if (task.getId() == tasks.get(i).getId()) {
                position = i;
                break;
            }
        }
        if (position == -1) return;
        View view = listView.getChildAt(position);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        extandView(view);
        viewHolder.layout_parent.setBackgroundResource(R.drawable.alerted_task);
        viewHolder.taskText.setTextColor(ContextCompat.getColor(context, R.color.white));
        viewHolder.taskDate.setTextColor(ContextCompat.getColor(context, R.color.white));
        viewHolder.notesText.setTextColor(ContextCompat.getColor(context, R.color.white));
        viewHolder.taskType.setTextColor(ContextCompat.getColor(context, R.color.white));
        viewHolder.locationText.setTextColor(ContextCompat.getColor(context, R.color.white));
        ((TextView)((LinearLayout) viewHolder.locationText.getParent()).getChildAt(0)).setTextColor(ContextCompat.getColor(context, R.color.white));

        viewHolder.alarmOff.setVisibility(View.VISIBLE);
        viewHolder.removeTask.setVisibility(View.GONE);
        viewHolder.editTask.setVisibility(View.GONE);
        viewHolder.openTaskList.setVisibility(View.GONE);
    }

    void alarmOff () {
        ((DayActivity) context).alarmOff();
        notifyDataSetChanged();
    }

    public void closeAllOpenedTasks () {
        targets.clear();
        for (int i = 0; i < extendedTasks.size(); i++) {
            extandView (extendedTasks.get(i));
        }
    }

    public class ViewHolder {
        View view;
        int position;
        TextView taskDate;
        TextView taskType;
        TextView taskText;
        TextView locationText;
        TextView notesText;
        TextView editTask;
        TextView openTaskList;
        TextView removeTask;
        TextView alarmOff;
        LinearLayout extraBar;
        LinearLayout notesBar;
        LinearLayout layout_parent;
        boolean extanded;
    }

}
