package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryReminders;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchExpiryReminders =
                findViewById(R.id.switchExpiryReminders);

        Button backButton =
                findViewById(R.id.buttonBack);

        preferences = getSharedPreferences(
                "AppSettings",
                MODE_PRIVATE
        );

        boolean expiryReminders =
                preferences.getBoolean(
                        "expiryReminders",
                        true
                );

        switchExpiryReminders.setChecked(
                expiryReminders
        );

        switchExpiryReminders.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    preferences.edit()
                            .putBoolean(
                                    "expiryReminders",
                                    isChecked
                            )
                            .apply();

                    if (isChecked) {

                        Toast.makeText(
                                this,
                                "Expiry reminders turned on",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                this,
                                "Expiry reminders turned off",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        backButton.setOnClickListener(
                v -> finish()
        );
    }
}