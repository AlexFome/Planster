package com.fome.planster;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.fome.planster.daterepresenters.DatePicker;
import com.fome.planster.models.Contact;
import com.fome.planster.models.Contacts;
import com.fome.planster.adapters.ContactsAdapter;
import com.fome.planster.models.List;
import com.fome.planster.models.Task;
import com.fome.planster.models.Time;
import com.fome.planster.models.TimePeriod;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Date;

import static android.media.CamcorderProfile.get;

public class TaskCreationActivity extends AppCompatActivity {

    private int id;
    private List list;
    private com.fome.planster.models.Place place;
    private TasksManager.RepeatType repeatType = TasksManager.RepeatType.ONCE;

    private boolean alertEnabled = true;
    private int alertBefore = -1;

    private EditText nameInput;
    private EditText notesInput;

    private SeekBar priorityBar;

    private Contacts contacts;

    private TextView finishTaskCustomizationButton;
    private LinearLayout contactsPickerLayout;
    private LinearLayout datePickerLayout;
    private LinearLayout setDateButton;
    private LinearLayout contactsSelectionButton;
    private LinearLayout placesSelectionButton;
    private LinearLayout typeSelectionBar;
    private TextView setContactsButton;

    private TextView addListButton;

    private TextView[] alertButtons;
    private TextView[] repeatButtons;

    private TextView sendEmailButton;
    private TextView shareButton;

    private TextView date_switch;
    private TextView time_switch;

    private View underline;

    DatePicker datePicker;

    LinearLayout startDate;
    LinearLayout endDate;

    float datePickerHeight;
    int datePickerWidth;

    float contactsPickerHeight;

    GridView contactsGrid;
    ContactsAdapter contactsAdapter;

    TimePeriod timePeriod;

    LinearLayout notificationBar;
    LinearLayout alertBar;

    boolean contactsPermissionGranted;
    boolean datePickerOpened;
    boolean contactsPickerOpened;
    boolean startDateShown = true;

    boolean involvedContactsPickerShown;
    boolean receiverContactsPickerShown;

    int selectedTaskType = 0;

    final int PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    final int LIST_REQUEST_CODE = 1;
    final int PLACES_REQUEST_CODE = 2;
    final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3;

    Gson gson = new Gson();

    ArrayList<Target> targets = new ArrayList<>(); // Solid refference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        timePeriod = new TimePeriod(this);

        bindViews ();
        PermissionManager.askForPermission(Manifest.permission.READ_CONTACTS, PERMISSIONS_REQUEST_READ_CONTACTS, this, this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("task")) {
            Task task = gson.fromJson(intent.getExtras().getString("task"), Task.class);
            setTaskData (task);
        } else {
            list = new List();
            getContacts();
            selectAlertTime(0, alertButtons[0]);
            selectRepeatTime(TasksManager.RepeatType.ONCE);
        }

    }

    void bindViews () {

        FontManager.setFont(this, findViewById(R.id.task_creation_activity), FontManager.BOLDFONT);
        FontManager.setFont(this, findViewById(R.id.time_selector), FontManager.BOLDFONT);
        FontManager.setFont(this, findViewById(R.id.contacts_selection), FontManager.BOLDFONT);
        FontManager.setFont(this, findViewById(R.id.new_task_sign), FontManager.BOLDFONT);

        ((TextView) findViewById(R.id.arrow)).setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));
        ((TextView) findViewById(R.id.sun_icon)).setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));
        ((TextView) findViewById(R.id.moon_icon)).setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));

        nameInput = (EditText) findViewById(R.id.task_name_input);
        notesInput = (EditText) findViewById(R.id.task_notes_input);

        priorityBar = (SeekBar) findViewById(R.id.priority_bar);

        addListButton = (TextView) findViewById(R.id.add_list_button);
        addListButton.setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));

        repeatButtons = new TextView[] {
                (TextView) ((LinearLayout) findViewById(R.id.repeat_bar)).getChildAt(1),
                (TextView) ((LinearLayout) findViewById(R.id.repeat_bar)).getChildAt(2),
                (TextView) ((LinearLayout) findViewById(R.id.repeat_bar)).getChildAt(3)
        };
        alertButtons = new TextView [] {
                (TextView) ((LinearLayout) findViewById(R.id.before_bar)).getChildAt(1),
                (TextView) ((LinearLayout) findViewById(R.id.before_bar)).getChildAt(2),
                (TextView) ((LinearLayout) findViewById(R.id.before_bar)).getChildAt(3)
        };

        date_switch = (TextView) findViewById(R.id.date_switch);
        time_switch = (TextView) findViewById(R.id.time_switch);

        startDate = (LinearLayout) findViewById(R.id.start_date_selection);
        endDate = (LinearLayout) findViewById(R.id.end_date_selection);
        datePickerLayout = (LinearLayout) findViewById(R.id.time_selector);
        contactsPickerLayout = (LinearLayout) findViewById(R.id.contacts_selection);
        finishTaskCustomizationButton = (TextView) findViewById(R.id.finish_task_customization_button);
        setDateButton = (LinearLayout) findViewById(R.id.set_date_button);
        setDateButton = (LinearLayout) findViewById(R.id.set_date_button);
        underline = findViewById(R.id.underline);
        contactsSelectionButton = (LinearLayout) findViewById(R.id.contacts_selection_button);
        placesSelectionButton = (LinearLayout) findViewById(R.id.places_selection_button);
        typeSelectionBar = (LinearLayout) findViewById(R.id.task_type_bar);
        setContactsButton = (TextView) findViewById(R.id.finish_contacts_selection_button);
        ((TextView) placesSelectionButton.getChildAt(0)).setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));
        contactsGrid = (GridView) findViewById(R.id.contacts_grid);
        notificationBar = (LinearLayout) findViewById(R.id.notification_bar);
        alertBar = (LinearLayout) findViewById(R.id.alert_bar);

        ((CheckBox) notificationBar.getChildAt(1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView sign = (TextView) notificationBar.getChildAt(0);
                if (isChecked) {
                    sign.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_grey_3));
                } else {
                    sign.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_grey_2));
                }
            }
        });

        ((CheckBox) alertBar.getChildAt(1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView sign = (TextView) alertBar.getChildAt(0);
                if (isChecked) {
                    sign.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_grey_3));
                } else {
                    sign.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_grey_2));
                }
            }
        });

        placesSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlaceSelection();
            }
        });

        finishTaskCustomizationButton.setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));

        ((TextView) ((LinearLayout) contactsSelectionButton.getChildAt(contactsSelectionButton.getChildCount() - 1)).getChildAt(0)).setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));
        ((TextView) startDate.getChildAt(0)).setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));
        ((TextView) endDate.getChildAt(0)).setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));

        sendEmailButton = (TextView) findViewById(R.id.send_task_via_email_button);
        sendEmailButton.setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));

        shareButton = (TextView) findViewById(R.id.share_task_button);
        shareButton.setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        datePicker = new DatePicker(
                new LinearLayout[] {
                        (LinearLayout) findViewById(R.id.row_1),
                        (LinearLayout) findViewById(R.id.row_2),
                        (LinearLayout) findViewById(R.id.row_3)
                },
                this
        );
        switchToDate (timePeriod.getStartTime().getDate(), timePeriod.getStartTime());

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTask ();
            }
        });

        setContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContacts();
            }
        });

        date_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDateShown) {
                    switchToDate(timePeriod.getActualTime(), timePeriod.getStartTime());
                } else {
                    switchToDate(timePeriod.getStartTime().getDate(), timePeriod.getEndTime());
                }
            }
        });
        time_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDateShown) {
                    switchToTime(timePeriod.getActualTime(), timePeriod.getStartTime());
                } else {
                    switchToTime(timePeriod.getStartTime().getDate(), timePeriod.getEndTime());
                }
            }
        });
        finishTaskCustomizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIManager.setBackgroundShapeColor(underline.getBackground(), timePeriod.getStartTime().getColor());
                ((TextView) setDateButton.getChildAt(0)).setTextColor(timePeriod.getStartTime().getColor());
                startDateShown = true;
                if (!datePickerOpened) {
                    openDatePicker(timePeriod.getActualTime(), timePeriod.getStartTime());
                } else {
                    datePicker.deleteSelections();
                    switchToDate(timePeriod.getActualTime(), timePeriod.getStartTime());
                }
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDateShown = false;
                UIManager.setBackgroundShapeColor(underline.getBackground(), timePeriod.getEndTime().getColor());
                ((TextView) setDateButton.getChildAt(0)).setTextColor(timePeriod.getEndTime().getColor());
                if (!datePickerOpened) {
                    openDatePicker(timePeriod.getStartTime().getDate(), timePeriod.getEndTime());
                } else {
                    datePicker.deleteSelections();
                    switchToDate(timePeriod.getStartTime().getDate(), timePeriod.getEndTime());
                }
            }
        });
        contactsSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactSelection(contacts);
            }
        });
        alertButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAlertTime(0, v);
            }
        });
        alertButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAlertTime(5, v);
            }
        });
        alertButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAlertTime(15, v);
            }
        });

        repeatButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRepeatTime(TasksManager.RepeatType.ONCE);
            }
        });
        repeatButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRepeatTime(TasksManager.RepeatType.WEEKDAYS);
            }
        });
        repeatButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRepeatTime(TasksManager.RepeatType.WEEKENDS);
            }
        });

        selectTaskType (selectedTaskType);
        for (int i = 0; i < typeSelectionBar.getChildCount(); i++) {
            TextView textView = (TextView) typeSelectionBar.getChildAt(i);
            final int num = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectTaskType (num);
                }
            });
        }

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openList();
            }
        });
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail ();
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        datePickerHeight = datePickerLayout.getHeight();
        datePickerWidth = (int) (datePickerLayout.getWidth() / 2f);
        contactsPickerHeight = contactsPickerLayout.getHeight();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) datePickerLayout.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, - (int) datePickerHeight);
        datePickerLayout.setLayoutParams(layoutParams);

        layoutParams = (RelativeLayout.LayoutParams) underline.getLayoutParams();
        layoutParams.width = datePickerWidth;
        underline.setLayoutParams(layoutParams);
        underline.bringToFront();

        RelativeLayout.LayoutParams linearLayoutParams = (RelativeLayout.LayoutParams) contactsPickerLayout.getLayoutParams();
        linearLayoutParams.setMargins(linearLayoutParams.leftMargin, linearLayoutParams.topMargin, linearLayoutParams.rightMargin, - (int) contactsPickerHeight);
        contactsPickerLayout.setLayoutParams(linearLayoutParams);

        closeDatePicker();
        closeContactsPicker();

    }

    void openContactsPicker (Contacts contacts) {
        contactsPickerOpened = true;
        if (datePickerOpened) {
            closeDatePicker();
        }

        if (contactsAdapter == null) {
            contactsAdapter = new ContactsAdapter(contacts, this);
        } else {
            contactsAdapter.setContacts(contacts);
        }

        contactsGrid.setAdapter(contactsAdapter);
        contactsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contactsAdapter.selectContact(position, view);
            }
        });

        ViewPropertyObjectAnimator.animate(contactsPickerLayout).bottomMargin(0).setDuration(300).start();
    }

    void closeContactsPicker () {
        contactsPickerOpened = false;
        ViewPropertyObjectAnimator.animate(contactsPickerLayout).bottomMargin( -(int) contactsPickerHeight).setDuration(300).start();
    }

    void closeDatePicker () {
        datePickerOpened = false;
        datePicker.deleteSelections();
        ViewPropertyObjectAnimator.animate(datePickerLayout).bottomMargin( -(int) datePickerHeight).setDuration(300).start();
    }

    void openDatePicker(Date minDate, Time time) {
        datePickerOpened = true;
        if (contactsPickerOpened) {
            closeContactsPicker();
        }
        ViewPropertyObjectAnimator.animate(datePickerLayout).bottomMargin(0).setDuration(300).start();
        switchToDate(minDate, time);
    }

    void switchToTime (Date minDate, Time time) {

        datePicker.switchToTime(time);
        datePicker.setDate(minDate, time.getDate());

        ViewPropertyObjectAnimator.animate(underline).leftMargin(datePickerWidth).setDuration(300).start();
        date_switch.setTextColor(ContextCompat.getColor(this, R.color.light_grey_2));
        time_switch.setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
    }

    void switchToDate (Date minDate, Time time) {

        datePicker.switchToDate(time);
        datePicker.setDate(minDate, time.getDate());

        ViewPropertyObjectAnimator.animate(underline).leftMargin(0).setDuration(300).start();
        date_switch.setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
        time_switch.setTextColor(ContextCompat.getColor(this, R.color.light_grey_2));
    }

    void setDate () {
        closeDatePicker();
        ((TextView) startDate.getChildAt(0)).setText(String.valueOf(timePeriod.getStartTime().getHour()) + ":" + (timePeriod.getStartTime().getMinutes() >= 10 ? "":"0") + String.valueOf(timePeriod.getStartTime().getMinutes()));
        ((TextView) startDate.getChildAt(0)).setTypeface(FontManager.getTypeface(this, FontManager.BOLDFONT));
        ((TextView) startDate.getChildAt(1)).setText(String.valueOf(timePeriod.getStartTime().getDay()) + "/"+ String.valueOf(timePeriod.getStartTime().getMonth()) + "/"+ String.valueOf(timePeriod.getStartTime().getYear()));
        ((TextView) startDate.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
        ((TextView) startDate.getChildAt(1)).setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));

        ((TextView) endDate.getChildAt(0)).setText(String.valueOf(timePeriod.getEndTime().getHour()) + ":" + (timePeriod.getEndTime().getMinutes() >= 10 ? "":"0") + String.valueOf(timePeriod.getEndTime().getMinutes()));
        ((TextView) endDate.getChildAt(0)).setTypeface(FontManager.getTypeface(this, FontManager.BOLDFONT));
        ((TextView) endDate.getChildAt(1)).setText(String.valueOf(timePeriod.getEndTime().getDay()) + "/"+ String.valueOf(timePeriod.getEndTime().getMonth()) + "/"+ String.valueOf(timePeriod.getEndTime().getYear()));
        ((TextView) endDate.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
        ((TextView) endDate.getChildAt(1)).setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
    }

    void getContacts () {

        contactsPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;

        if (!contactsPermissionGranted) {
            disableContactSelection();
            return;
        }

        ArrayList<Contact> contactsArray = new ArrayList<>();

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {
                        ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI},
                null, null, null);

        if (cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                contactsArray.add(new Contact(Integer.valueOf(cursor.getString(0)), cursor.getString(1), cursor.getString(2) != null ? cursor.getString(2): (getUriToDrawable(getApplicationContext(), R.drawable.default_avatar)).toString()));
            }
        } else {
            disableContactSelection();
            return;
        }

        contacts = new Contacts(contactsArray);

        /*
        contactsArray = new ArrayList<>();
        contactsArray.add(new Contact(0, "Daryl", "http://fotohost.by/images/2017/04/19/4N9R7r-SjdUCropped.jpg"));
        contactsArray.add(new Contact(1, "Rick", "http://fotohost.by/images/2017/04/19/the-walking-dead-episode-509-rick-lincoln-600x400-3Cropped.jpg"));
        contactsArray.add(new Contact(2, "Michonne", "http://fotohost.by/images/2017/04/19/michonneCropped.jpg"));
        contactsArray.add(new Contact(3, "Glen", "http://fotohost.by/images/2017/04/19/glenn-walking-dead-surviving-11Cropped.jpg"));
        contactsArray.add(new Contact(4, "Maggy", "http://fotohost.by/images/2017/04/19/640_Walking_Dead_Lauren_CohanCropped.jpg"));
        contactsArray.add(new Contact(5, "Sasha", "http://fotohost.by/images/2017/04/19/150214-news-sonequa-martin-greenCropped.jpg"));
        contactsArray.add(new Contact(6, "Morgan", "http://fotohost.by/images/2017/04/19/The-Walking-Dead-Morgan-6x07Cropped.jpg"));
        contactsArray.add(new Contact(7, "Negan", "http://fotohost.by/images/2017/04/19/negan-problemCropped.jpg"));

        contacts = new Contacts(contactsArray);
        */
        showContacts(contacts);

        getContactsWithApp();

    }

    void showContacts (Contacts contacts) {
        for (int i = 0; i < contactsSelectionButton.getChildCount(); i++) {
            final View thumbnail = contactsSelectionButton.getChildAt(i);
            if (thumbnail instanceof ImageView) {
                if (contacts.getSize() <= i) {
                    thumbnail.setVisibility(View.GONE);
                } else {
                    Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ((ImageView)thumbnail).setImageBitmap(UIManager.getRoundedCornerBitmap(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };
                    Picasso.with(this).load(contacts.getContacts().get(i).getPhotoThumbnailUri()).into(target);
                    targets.add(target);
                }
            }
        }
    }

    void setContacts () {
        if (involvedContactsPickerShown) {
            involvedContactsPickerShown = false;

        } else if (receiverContactsPickerShown) {
            receiverContactsPickerShown = false;

        }
        closeContactsPicker();
    }

    void disableContactSelection () {
        for (int i = 0; i < contactsSelectionButton.getChildCount(); i++) {
            final View thumbnail = contactsSelectionButton.getChildAt(i);
            if (thumbnail instanceof ImageView) {
                    Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ((ImageView)thumbnail).setImageBitmap(UIManager.getRoundedCornerBitmap(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };
                    Picasso.with(this).load(R.drawable.default_avatar).into(target);
                    targets.add(target);
            }
        }
    }

    void selectRepeatTime (TasksManager.RepeatType repeatType) {
        for (int i = 0; i < repeatButtons.length; i++) {
            disableRepeatSelection(repeatButtons[i]);
        }
        this.repeatType = repeatType;
        if (repeatType == TasksManager.RepeatType.ONCE) {
            enableRepeatSelection(repeatButtons[0]);
        } else if (repeatType == TasksManager.RepeatType.WEEKDAYS) {
            enableRepeatSelection(repeatButtons[1]);
        } else if (repeatType == TasksManager.RepeatType.WEEKENDS) {
            enableRepeatSelection(repeatButtons[2]);
        }

    }

    void disableRepeatSelection (View view) {
        ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.light_grey_2));
        ((TextView) view).setTypeface(FontManager.getTypeface(this, FontManager.LIGHTFONT));
    }
    void enableRepeatSelection (View view) {
        ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
        ((TextView) view).setTypeface(FontManager.getTypeface(this, FontManager.BOLDFONT));
    }

    void selectAlertTime (int minutes, View view) {
        for (int i = 0; i < alertButtons.length; i++) {
            disableAlertSelection(alertButtons[i]);
        }
        if (alertBefore == minutes) {
            alertBefore = -5;
            alertEnabled = false;
        } else {
            alertEnabled = true;
            alertBefore = minutes;
            enableAlertSelection(view);
        }
    }

    void disableAlertSelection (View view) {
        ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.light_grey_2));
        ((TextView) view).setTypeface(FontManager.getTypeface(this, FontManager.LIGHTFONT));
    }
    void enableAlertSelection (View view) {
        ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
        ((TextView) view).setTypeface(FontManager.getTypeface(this, FontManager.BOLDFONT));
    }

    void openContactSelection (Contacts contacts) {

        if (!contactsPermissionGranted) {
            PermissionManager.askForPermission(Manifest.permission.READ_CONTACTS, PERMISSIONS_REQUEST_READ_CONTACTS, this, this);
            return;
        }

        if (datePickerOpened) {
            setDate();
        }

        receiverContactsPickerShown = false;
        involvedContactsPickerShown = true;
        openContactsPicker(contacts);

    }

    void openReceiverContactSelection (Contacts contacts) {

        if (!contactsPermissionGranted) {
            PermissionManager.askForPermission(Manifest.permission.READ_CONTACTS, PERMISSIONS_REQUEST_READ_CONTACTS, this, this);
            return;
        }

        if (datePickerOpened) {
            setDate();
        }

        involvedContactsPickerShown = false;
        receiverContactsPickerShown = true;
        openContactsPicker(contacts);

    }

    void setContacts (Contacts contacts) {
        this.contacts = contacts;
    }

    void setPlace (com.fome.planster.models.Place place) {
        this.place = place;
        if (place != null) {
            ((TextView) placesSelectionButton.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
            ((TextView) placesSelectionButton.getChildAt(1)).setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
            ((TextView) placesSelectionButton.getChildAt(1)).setText("  " + place.getName());
            ((TextView) placesSelectionButton.getChildAt(1)).setTypeface(FontManager.getTypeface(this, FontManager.BOLDFONT));
        }
    }

    void setTaskData (Task task) {
        id = task.getId ();
        nameInput.setText(task.getName());
        notesInput.setText(task.getNotes());
        list = task.getList();
        if (list != null && !list.isEmpty()) {
            addListButton.setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
        } else {
            addListButton.setTextColor(ContextCompat.getColor(this, R.color.light_grey_2));
        }
        priorityBar.setProgress((int) task.getPriority());
        timePeriod = new TimePeriod(this);
        timePeriod.setStartTime(new Time(task.getStartDate(), timePeriod, ContextCompat.getColor(this, R.color.red)));
        timePeriod.setEndTime(new Time(task.getEndDate(), timePeriod, ContextCompat.getColor(this, R.color.blue)));
        setDate();

        ((CheckBox) notificationBar.getChildAt(1)).setChecked(task.isNotificationEnabled());
        ((CheckBox) alertBar.getChildAt(1)).setChecked(task.isAlertEnabled());

        switch (task.getNotificateBefore()) {
            case 0:
                selectAlertTime(0, alertButtons[0]);
                break;
            case 5:
                selectAlertTime(0, alertButtons[1]);
                break;
            case 15:
                selectAlertTime(0, alertButtons[2]);
                break;
        }

        contactsPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;

        Contacts contacts = task.getContacts();
        if (contacts == null || contacts.getSize() == 0) {
            getContacts();
        } else {
            setContacts(contacts);
            showContacts(contacts);
        }
        if (contactsPermissionGranted) {
            getContactsWithApp();
        }
        selectRepeatTime(task.getRepeatType());
        setPlace(task.getPlace());
    }

    void saveTask () {
        if (datePickerOpened || contactsPickerOpened || !taskIsValid()) return;
        list.format();
        Task task = composeTask();
        if (task.isAlertEnabled() || task.isNotificationEnabled()) {
            AlarmManager.setAlarm(task);
        }
        TasksManager.saveTask(task);
        FirebaseAnalyticsManager.logEvent(String.valueOf(task.getId()), "Logic checkpoint", "new task is created");
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    Task composeTask () {
        int id = this.id == 0 ? TasksManager.getNewId() : this.id;
        this.id = id;
        Task task = new Task(
                id,
                timePeriod.getStartTime().getDate(),
                timePeriod.getEndTime().getDate(),
                nameInput.getText().toString(),
                notesInput.getText().toString(),
                list,
                TasksManager.TaskType.values()[selectedTaskType],
                priorityBar.getProgress(),
                place,
                ((CheckBox) notificationBar.getChildAt(1)).isChecked(),
                ((CheckBox) alertBar.getChildAt(1)).isChecked(),
                alertBefore,
                contacts,
                repeatType
        );
        return task;
    }

    boolean taskIsValid () {
        if (nameInput.getText().toString().equals("")) {
            Toast.makeText(this, getResources().getString(R.string.type_task_text), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!PermissionManager.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionManager.askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, this, this);
            Toast.makeText(this, getResources().getString(R.string.read_storage), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {

                PermissionManager.askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, this, this);

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {
                    disableContactSelection();
                }
                break;
            }

        }
    }

    void openList () {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("list", gson.toJson(list));
        startActivityForResult(intent, LIST_REQUEST_CODE);
    }

    void selectTaskType (int num) {
        selectedTaskType = num;
        for (int i = 0; i < typeSelectionBar.getChildCount(); i++) {
            TextView textView = (TextView) typeSelectionBar.getChildAt(i);
            switch (i) {
                case 0:
                    textView.setText(getResources().getString(R.string.general));
                    if (num == 0) {
                        textView.setTextColor(ContextCompat.getColor(this, R.color.general_type_color));
                    } else {
                        textView.setTextColor(ContextCompat.getColor(this, R.color.light_grey_2));
                    }
                    break;
                case 1:
                    textView.setText(getResources().getString(R.string.job));
                    if (num == 1) {
                        textView.setTextColor(ContextCompat.getColor(this, R.color.job_type_color));
                    } else {
                        textView.setTextColor(ContextCompat.getColor(this, R.color.light_grey_2));
                    }
                    break;
                case 2:
                    textView.setText(getResources().getString(R.string.personal));
                    if (num == 2) {
                        textView.setTextColor(ContextCompat.getColor(this, R.color.personal_type_color));
                    } else {
                        textView.setTextColor(ContextCompat.getColor(this, R.color.light_grey_2));
                    }
                    break;
            }
        }
    }

    void openPlaceSelection() {
        if (!InternetConnection.isOnline(this)) {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACES_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(this, getResources().getString(R.string.google_places_are_not_available), Toast.LENGTH_LONG).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, getResources().getString(R.string.google_places_are_not_available), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LIST_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    list = gson.fromJson(data.getExtras().getString("list"), List.class);
                    if (!list.isEmpty()) {
                        addListButton.setTextColor(ContextCompat.getColor(this, R.color.light_grey_3));
                    } else {
                        addListButton.setTextColor(ContextCompat.getColor(this, R.color.light_grey_2));
                    }
                }
                break;
            case PLACES_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (resultCode == RESULT_OK) {
                        // retrieve the data by using getPlace() method.
                        Place place = PlaceAutocomplete.getPlace(this, data);
                        setPlace(new com.fome.planster.models.Place (place.getId(), place.getName().toString(), place.getLatLng(), place.getAddress().toString()));

                    } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                        Status status = PlaceAutocomplete.getStatus(this, data);
                        // TODO: Handle the error.
                        Log.e("Tag", status.getStatusMessage());

                    } else if (resultCode == RESULT_CANCELED) {
                        // The user canceled the operation.
                    }
                }
                break;
        }
    }

    void getContactsWithApp () {
        if (InternetConnection.isOnline(this)) {

        } else {

        }
    }

    void sendEmail () {
        if (InternetConnection.isOnline(this) && !datePickerOpened && !contactsPickerOpened && taskIsValid()) {
            Task task = composeTask();
            String title = "";
            switch (task.getTaskType()) {
                case GENERAL:
                    title = title + " " + getResources().getString(R.string.general_task);
                    break;
                case JOB:
                    title = title + " " + getResources().getString(R.string.job_task);
                    break;
                case PERSONAL:
                    title = title + " " + getResources().getString(R.string.personal_task);
                    break;
            }
            String body = "";
            body = body + task.getName() + " " + task.getNotes();
            if (task.getContacts().getSelectedContacts() != null && task.getContacts().getSelectedContacts().size() > 0) {
                body = body + getResources().getString(R.string.with);
                for (int i = 0; i < task.getContacts().getSelectedContacts().size(); i++) {
                    body = body + " " + task.getContacts().getSelectedContacts().get(i).getName();
                }
            }
            body = body + ".";
            Date startDate = task.getStartDate();
            body = body + " " + DateManager.getDay (startDate) + "/" + DateManager.getMonthNum(startDate) + "/" + DateManager.getYear(startDate);
            body = body + " " + (DateManager.getHours(startDate) < 10 ? "0" + DateManager.getHours(task.getStartDate()): DateManager.getHours(task.getStartDate()) + "") + ":" + (DateManager.getMinutes(startDate) < 10 ? "0" + DateManager.getMinutes(task.getStartDate()): DateManager.getMinutes(task.getStartDate()) + "");
            if (task.getPlace () != null) {
                body = body + " " + task.getPlace().getName();
            }

            InternetConnection.sendEmail(
                    this,
                    "",
                    title,
                    body);
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    void shareTask () {
        if (!taskIsValid() || datePickerOpened || contactsPickerOpened) return;
        Task task = composeTask();
        String body = "";
        switch (task.getTaskType()) {
            case GENERAL:
                body = body + " " + getResources().getString(R.string.general_task_);
                break;
            case JOB:
                body = body + " " + getResources().getString(R.string.job_task_);
                break;
            case PERSONAL:
                body = body + " " + getResources().getString(R.string.personal_task_);
                break;
        }
        body = body + task.getName() + " " + task.getNotes();
        if (task.getContacts().getSelectedContacts() != null && task.getContacts().getSelectedContacts().size() > 0) {
            body = body + getResources().getString(R.string.with);
            for (int i = 0; i < task.getContacts().getSelectedContacts().size(); i++) {
                body = body + " " + task.getContacts().getSelectedContacts().get(i).getName();
            }
        }
        body = body + ".";
        Date startDate = task.getStartDate();
        body = body + " " + DateManager.getDay (startDate) + "/" + DateManager.getMonthNum(startDate) + "/" + DateManager.getYear(startDate);
        body = body + " " + (DateManager.getHours(startDate) < 10 ? "0" + DateManager.getHours(task.getStartDate()): DateManager.getHours(task.getStartDate()) + "") + ":" + (DateManager.getMinutes(startDate) < 10 ? "0" + DateManager.getMinutes(task.getStartDate()): DateManager.getMinutes(task.getStartDate()) + "");
        if (task.getPlace () != null) {
            body = body + " " + task.getPlace().getName();
        }

        InternetConnection.shareText(this, getResources().getString(R.string.Task), body);
    }

    public final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }

    @Override
    public void onBackPressed() {
        if (datePickerOpened) {
            closeDatePicker();
        } else if (contactsPickerOpened) {
            closeContactsPicker();
        } else {
            finish ();
        }
    }
    
}
