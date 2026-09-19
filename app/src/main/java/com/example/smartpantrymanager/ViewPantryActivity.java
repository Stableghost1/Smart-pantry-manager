package com.example.smartpantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewPantryActivity extends AppCompatActivity {

    private LinearLayout pantryItemsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pantry);

        pantryItemsContainer = findViewById(R.id.pantryItemsContainer);

        Button backButton = findViewById(R.id.buttonBack);
        Button addItemButton = findViewById(R.id.buttonAddItem);

        backButton.setOnClickListener(v -> finish());

        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddItemActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayPantryItems();
    }

    private void displayPantryItems() {

        pantryItemsContainer.removeAllViews();

        SharedPreferences preferences =
                getSharedPreferences("PantryData", MODE_PRIVATE);

        String savedItems = preferences.getString("pantryItems", "");

        if (savedItems.isEmpty()) {
            showEmptyMessage();
            return;
        }

        String[] items = savedItems.split("\n");

        for (int i = 0; i < items.length; i++) {

            String[] details = items[i].split("\\|");

            if (details.length == 4) {

                String name = details[0];
                String quantity = details[1];
                String category = details[2];
                String expiry = details[3];

                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(30, 25, 30, 25);
                card.setBackgroundColor(Color.WHITE);

                LinearLayout.LayoutParams cardParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                cardParams.setMargins(0, 0, 0, 25);
                card.setLayoutParams(cardParams);

                TextView itemText = new TextView(this);

                itemText.setText(
                        name +
                                "\nQuantity: " + quantity +
                                "\nCategory: " + category +
                                "\nExpiry: " + expiry
                );

                itemText.setTextSize(17);
                itemText.setTextColor(
                        getResources().getColor(R.color.text_dark)
                );

                itemText.setPadding(5, 5, 5, 20);

                LinearLayout buttonRow = new LinearLayout(this);
                buttonRow.setOrientation(LinearLayout.HORIZONTAL);

                Button editButton = new Button(this);
                editButton.setText("Edit");
                editButton.setTextColor(Color.WHITE);
                editButton.setBackgroundTintList(
                        getColorStateList(R.color.primary_green)
                );

                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                deleteButton.setTextColor(Color.WHITE);
                deleteButton.setBackgroundTintList(
                        getColorStateList(R.color.delete_red)
                );

                LinearLayout.LayoutParams buttonParams =
                        new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1
                        );

                buttonParams.setMargins(5, 0, 5, 0);

                editButton.setLayoutParams(buttonParams);
                deleteButton.setLayoutParams(buttonParams);

                final int position = i;

                editButton.setOnClickListener(v -> {

                    Intent intent =
                            new Intent(this, AddItemActivity.class);

                    intent.putExtra("editMode", true);
                    intent.putExtra("itemPosition", position);
                    intent.putExtra("itemName", name);
                    intent.putExtra("quantity", quantity);
                    intent.putExtra("category", category);
                    intent.putExtra("expiryDate", expiry);

                    startActivity(intent);
                });

                deleteButton.setOnClickListener(v ->
                        deleteItem(position)
                );

                buttonRow.addView(editButton);
                buttonRow.addView(deleteButton);

                card.addView(itemText);
                card.addView(buttonRow);

                pantryItemsContainer.addView(card);
            }
        }
    }

    private void deleteItem(int position) {

        SharedPreferences preferences =
                getSharedPreferences("PantryData", MODE_PRIVATE);

        String savedItems =
                preferences.getString("pantryItems", "");

        if (savedItems.isEmpty()) {
            return;
        }

        String[] items = savedItems.split("\n");
        StringBuilder updatedItems = new StringBuilder();

        for (int i = 0; i < items.length; i++) {

            if (i != position) {

                if (updatedItems.length() > 0) {
                    updatedItems.append("\n");
                }

                updatedItems.append(items[i]);
            }
        }

        preferences.edit()
                .putString("pantryItems", updatedItems.toString())
                .apply();

        Toast.makeText(
                this,
                "Item deleted",
                Toast.LENGTH_SHORT
        ).show();

        displayPantryItems();
    }

    private void showEmptyMessage() {

        TextView emptyMessage = new TextView(this);

        emptyMessage.setText("Your pantry is empty");
        emptyMessage.setTextSize(17);
        emptyMessage.setTextColor(
                getResources().getColor(R.color.text_grey)
        );
        emptyMessage.setGravity(Gravity.CENTER);
        emptyMessage.setPadding(20, 50, 20, 50);

        pantryItemsContainer.addView(emptyMessage);
    }
}