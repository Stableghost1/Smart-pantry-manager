package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShoppingListActivity extends AppCompatActivity {

    private EditText editShoppingItem;
    private LinearLayout shoppingListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        editShoppingItem = findViewById(R.id.editShoppingItem);
        shoppingListContainer = findViewById(R.id.shoppingListContainer);

        Button addButton = findViewById(R.id.buttonAddShoppingItem);
        Button backButton = findViewById(R.id.buttonBack);

        addButton.setOnClickListener(v -> addShoppingItem());
        backButton.setOnClickListener(v -> finish());

        displayShoppingList();
    }

    private void addShoppingItem() {

        String item = editShoppingItem.getText().toString().trim();

        if (item.isEmpty()) {
            editShoppingItem.setError("Enter an item");
            return;
        }

        SharedPreferences preferences =
                getSharedPreferences("ShoppingData", MODE_PRIVATE);

        String savedItems =
                preferences.getString("shoppingItems", "");

        if (!savedItems.isEmpty()) {
            savedItems += "\n";
        }

        savedItems += item;

        preferences.edit()
                .putString("shoppingItems", savedItems)
                .apply();

        editShoppingItem.setText("");

        Toast.makeText(
                this,
                "Added to shopping list",
                Toast.LENGTH_SHORT
        ).show();

        displayShoppingList();
    }

    private void displayShoppingList() {

        shoppingListContainer.removeAllViews();

        SharedPreferences preferences =
                getSharedPreferences("ShoppingData", MODE_PRIVATE);

        String savedItems =
                preferences.getString("shoppingItems", "");

        if (savedItems.isEmpty()) {
            showEmptyMessage();
            return;
        }

        String[] items = savedItems.split("\n");

        for (int i = 0; i < items.length; i++) {

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(20, 12, 10, 12);
            row.setBackgroundColor(Color.WHITE);

            LinearLayout.LayoutParams rowParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            rowParams.setMargins(0, 0, 0, 16);
            row.setLayoutParams(rowParams);

            TextView itemText = new TextView(this);
            itemText.setText(items[i]);
            itemText.setTextSize(17);
            itemText.setTextColor(
                    getResources().getColor(R.color.text_dark)
            );

            LinearLayout.LayoutParams textParams =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    );

            itemText.setLayoutParams(textParams);

            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");
            deleteButton.setTextColor(Color.WHITE);
            deleteButton.setTextSize(13);
            deleteButton.setBackgroundTintList(
                    getColorStateList(R.color.delete_red)
            );

            final int position = i;

            deleteButton.setOnClickListener(v ->
                    deleteShoppingItem(position)
            );

            row.addView(itemText);
            row.addView(deleteButton);

            shoppingListContainer.addView(row);
        }
    }

    private void deleteShoppingItem(int position) {

        SharedPreferences preferences =
                getSharedPreferences("ShoppingData", MODE_PRIVATE);

        String savedItems =
                preferences.getString("shoppingItems", "");

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
                .putString("shoppingItems", updatedItems.toString())
                .apply();

        Toast.makeText(
                this,
                "Item removed",
                Toast.LENGTH_SHORT
        ).show();

        displayShoppingList();
    }

    private void showEmptyMessage() {

        TextView emptyMessage = new TextView(this);

        emptyMessage.setText("Shopping list is empty");
        emptyMessage.setTextSize(17);
        emptyMessage.setTextColor(
                getResources().getColor(R.color.text_grey)
        );
        emptyMessage.setPadding(20, 30, 20, 30);

        shoppingListContainer.addView(emptyMessage);
    }
}