package com.example.smartpantrymanager;

import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiry_tracker);

        expiryItemsContainer = findViewById(R.id.expiryItemsContainer);

        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayExpiryItems();
    }

    private void displayExpiryItems() {

        expiryItemsContainer.removeAllViews();

        SharedPreferences preferences =
                getSharedPreferences("PantryData", MODE_PRIVATE);

        String savedItems =
                preferences.getString("pantryItems", "");

        if (savedItems.isEmpty()) {
            showEmptyMessage();
            return;
        }

        String[] items = savedItems.split("\n");

        for (String item : items) {

            String[] details = item.split("\\|");

            if (details.length == 4) {

                String name = details[0];
                String quantity = details[1];
                String expiryDate = details[3];

                String status = getExpiryStatus(expiryDate);

                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(30, 25, 30, 25);
                card.setBackgroundColor(Color.WHITE);

                LinearLayout.LayoutParams cardParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                cardParams.setMargins(0, 0, 0, 20);
                card.setLayoutParams(cardParams);

                TextView itemText = new TextView(this);
                itemText.setText(
                        name +
                                "\nQuantity: " + quantity +
                                "\nExpiry: " + expiryDate
                );

                itemText.setTextSize(17);
                itemText.setTextColor(
                        getResources().getColor(R.color.text_dark)
                );

                TextView statusText = new TextView(this);
                statusText.setText(status);
                statusText.setTextSize(15);
                statusText.setPadding(0, 15, 0, 0);

                if (status.equals("Expired")) {

                    statusText.setTextColor(
                            getResources().getColor(R.color.delete_red)
                    );

                } else if (status.equals("Expires today")
                        || status.startsWith("Expiring soon")) {

                    statusText.setTextColor(
                            getResources().getColor(R.color.expiry_warning)
                    );

                } else {

                    statusText.setTextColor(
                            getResources().getColor(R.color.expiry_safe)
                    );
                }

                card.addView(itemText);
                card.addView(statusText);

                expiryItemsContainer.addView(card);
            }
        }
    }

    private String getExpiryStatus(String expiryDate) {

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                );

        format.setLenient(false);

        try {

            Date expiry = format.parse(expiryDate);
            Date today = format.parse(
                    format.format(new Date())
            );

            if (expiry == null || today == null) {
                return "Invalid date";
            }

            long difference =
                    expiry.getTime() - today.getTime();

            long days =
                    TimeUnit.MILLISECONDS.toDays(difference);

            if (days < 0) {
                return "Expired";
            }

            if (days == 0) {
                return "Expires today";
            }

            if (days <= 7) {
                return "Expiring soon - " + days + " days left";
            }

            return days + " days left";

        } catch (ParseException e) {

            return "Invalid date";
        }
    }

    private void showEmptyMessage() {

        TextView emptyMessage = new TextView(this);

        emptyMessage.setText("No items to track");
        emptyMessage.setTextSize(17);
        emptyMessage.setTextColor(
                getResources().getColor(R.color.text_grey)
        );
        emptyMessage.setPadding(20, 30, 20, 30);

        expiryItemsContainer.addView(emptyMessage);
    }
}