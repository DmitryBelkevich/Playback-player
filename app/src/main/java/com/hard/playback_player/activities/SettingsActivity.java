package com.hard.playback_player.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.hard.playback_player.R;
import com.hard.playback_player.models.Settings;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // init settings >
        if (Settings.isKeepScreenOn())
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // init settings <

        // Toolbar >
        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");

        actionBar.setDisplayHomeAsUpEnabled(true);
        // Toolbar <

        // Components >
        CheckBox modeNight_checkBox = findViewById(R.id.modeNight_checkBox);
        CheckBox keepScreenOn_checkBox = findViewById(R.id.keepScreenOn_checkBox);

        modeNight_checkBox.setChecked(Settings.isModeNight());
        keepScreenOn_checkBox.setChecked(Settings.isKeepScreenOn());

        modeNight_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.setModeNight(!Settings.isModeNight());

                if (!Settings.isModeNight())
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        keepScreenOn_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.setKeepScreenOn(!Settings.isKeepScreenOn());


            }
        });
        // Components <
    }

    /**
     * Menu
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}