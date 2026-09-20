package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ExpiryTrackerActivity extends AppCompatActivity {

    private LinearLayout expiryItemsContainer;
    private DatabaseHelper databaseHelper;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiry_tracker);

        expiryItemsContainer =
                findViewById(R.id.expiryItemsContainer);

        Button backButton =
                findViewById(R.id.buttonBack);

        databaseHelper =
                new DatabaseHelper(this);

        preferences = getSharedPreferences(
                "AppSettings",
                MODE_PRIVATE
        );

        backButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpiryItems();
    }

    private void loadExpiryItems() {

        expiryItemsContainer.removeAllViews();

        boolean remindersEnabled =
                preferences.getBoolean(
                        "expiryReminders",
                        true
                );

        if (!remindersEnabled) {
            showMessage(
                    "Expiry reminders are turned off in Settings."
            );
            return;
        }

        Cursor cursor =
                databaseHelper.getAllPantryItems();

        if (cursor.getCount() == 0) {
            cursor.close();

            showMessage(
                    "No pantry items available."
            );

            return;
        }

        while (cursor.moveToNext()) {

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_NAME
                    )
            );

            int quantity = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_QUANTITY
                    )
            );

            String unit = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_UNIT
                    )
            );

            String expiry = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_EXPIRY
                    )
            );

            displayExpiryItem(
                    name,
                    quantity,
                    unit,
                    expiry
            );
        }

        cursor.close();
    }

    private void displayExpiryItem(
            String name,
            int quantity,
            String unit,
            String expiry) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                30,
                25,
                30,
                25
        );

        card.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                0,
                0,
                0,
                20
        );

        card.setLayoutParams(cardParams);

        TextView nameText =
                new TextView(this);

        nameText.setText(name);

        nameText.setTextSize(19);

        nameText.setTextColor(
                getResources().getColor(
                        R.color.text_dark
                )
        );


        TextView quantityText =
                new TextView(this);

        quantityText.setText(
                "Quantity: " +
                        quantity +
                        " " +
                        unit
        );

        quantityText.setTextSize(14);

        quantityText.setTextColor(
                getResources().getColor(
                        R.color.text_grey
                )
        );

        quantityText.setPadding(
                0,
                8,
                0,
                5
        );


        TextView expiryText =
                new TextView(this);

        expiryText.setText(
                "Expiry: " + expiry
        );

        expiryText.setTextSize(14);

        expiryText.setTextColor(
                getResources().getColor(
                        R.color.text_grey
                )
        );


        TextView statusText =
                new TextView(this);

        statusText.setText(
                getExpiryStatus(expiry)
        );

        statusText.setTextSize(15);

        statusText.setPadding(
                0,
                8,
                0,
                0
        );

        setStatusColour(
                statusText,
                expiry
        );


        card.addView(nameText);
        card.addView(quantityText);
        card.addView(expiryText);
        card.addView(statusText);

        expiryItemsContainer.addView(card);
    }

    private String getExpiryStatus(String expiry) {

        long days =
                getDaysUntilExpiry(expiry);

        if (days == Long.MIN_VALUE) {
            return "Invalid expiry date";
        }

        if (days < 0) {
            return "Expired";
        }

        if (days == 0) {
            return "Expires today";
        }

        if (days <= 7) {
            return "Expires in " +
                    days +
                    " day" +
                    (days == 1 ? "" : "s");
        }

        return "Safe";
    }

    private void setStatusColour(
            TextView statusText,
            String expiry) {

        long days =
                getDaysUntilExpiry(expiry);

        if (days == Long.MIN_VALUE) {

            statusText.setTextColor(
                    getResources().getColor(
                            R.color.text_grey
                    )
            );

        } else if (days < 0) {

            statusText.setTextColor(
                    getResources().getColor(
                            R.color.delete_red
                    )
            );

        } else if (days <= 7) {

            statusText.setTextColor(
                    getResources().getColor(
                            R.color.expiry_warning
                    )
            );

        } else {

            statusText.setTextColor(
                    getResources().getColor(
                            R.color.expiry_safe
                    )
            );
        }
    }

    private long getDaysUntilExpiry(
            String expiry) {

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                );

        format.setLenient(false);

        try {

            Date expiryDate =
                    format.parse(expiry);

            String todayText =
                    format.format(new Date());

            Date today =
                    format.parse(todayText);

            if (expiryDate == null ||
                    today == null) {

                return Long.MIN_VALUE;
            }

            long difference =
                    expiryDate.getTime() -
                            today.getTime();

            return TimeUnit.MILLISECONDS
                    .toDays(difference);

        } catch (ParseException e) {

            return Long.MIN_VALUE;
        }
    }

    private void showMessage(
            String messageText) {

        TextView message =
                new TextView(this);

        message.setText(messageText);

        message.setTextSize(17);

        message.setTextColor(
                getResources().getColor(
                        R.color.text_grey
                )
        );

        message.setPadding(
                20,
                40,
                20,
                40
        );

        expiryItemsContainer.addView(message);
    }
}