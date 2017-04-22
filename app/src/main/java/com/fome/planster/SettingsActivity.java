package com.fome.planster;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox notificationWithSound;
    private TextView sendEmailButton;
    private TextView openPrivacyPolicyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bindView();
    }

    void bindView () {
        FontManager.setFont(this, findViewById(R.id.settings_activity), FontManager.BOLDFONT);
        notificationWithSound = (CheckBox) findViewById(R.id.notification_with_sound);
        notificationWithSound.setChecked(Settings.isNotificationSoundEnabled());
        notificationWithSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.setNotificationSound(isChecked);
            }
        });
        sendEmailButton = (TextView) findViewById(R.id.send_email_button);
        sendEmailButton.setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));
        final Context context = this;
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetConnection.sendEmail(context, "alxfome@gmail.com", "Let me tell you something about your app", "");
            }
        });
        openPrivacyPolicyButton = (TextView) findViewById(R.id.open_privacy_policy_button);
        openPrivacyPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetConnection.openLink(context, "https://docs.google.com/document/d/1fEln7GRQbU-_Cy1tMdAo5VUJRi6XuiNbTC6OJ_DbjPk/edit?usp=sharing");
            }
        });
    }
}
