package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    private EditText editItemName;
    private EditText editQuantity;
    private EditText editCategory;
    private EditText editExpiryDate;

    private boolean editMode = false;
    private int itemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editItemName = findViewById(R.id.editItemName);
        editQuantity = findViewById(R.id.editQuantity);
        editCategory = findViewById(R.id.editCategory);
        editExpiryDate = findViewById(R.id.editExpiryDate);

        Button saveButton = findViewById(R.id.buttonSaveItem);
        Button cancelButton = findViewById(R.id.buttonCancel);

        editMode = getIntent().getBooleanExtra("editMode", false);

        if (editMode) {

            itemPosition = getIntent().getIntExtra("itemPosition", -1);

            editItemName.setText(
                    getIntent().getStringExtra("itemName")
            );

            editQuantity.setText(
                    getIntent().getStringExtra("quantity")
            );

            editCategory.setText(
                    getIntent().getStringExtra("category")
            );

            editExpiryDate.setText(
                    getIntent().getStringExtra("expiryDate")
            );

            saveButton.setText("Update Item");
        }

        saveButton.setOnClickListener(v -> saveItem());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void saveItem() {

        String name = editItemName.getText().toString().trim();
        String quantity = editQuantity.getText().toString().trim();
        String category = editCategory.getText().toString().trim();
        String expiryDate = editExpiryDate.getText().toString().trim();

        if (name.isEmpty()) {
            editItemName.setError("Enter an item name");
            editItemName.requestFocus();
            return;
        }

        if (quantity.isEmpty()) {
            editQuantity.setError("Enter a quantity");
            editQuantity.requestFocus();
            return;
        }

        if (category.isEmpty()) {
            editCategory.setError("Enter a category");
            editCategory.requestFocus();
            return;
        }

        if (expiryDate.isEmpty()) {
            editExpiryDate.setError("Enter an expiry date");
            editExpiryDate.requestFocus();
            return;
        }

        if (!validDate(expiryDate)) {
            editExpiryDate.setError("Use DD/MM/YYYY");
            editExpiryDate.requestFocus();
            return;
        }

        String newItem =
                name + "|" +
                        quantity + "|" +
                        category + "|" +
                        expiryDate;

        SharedPreferences preferences =
                getSharedPreferences("PantryData", MODE_PRIVATE);

        String savedItems =
                preferences.getString("pantryItems", "");

        if (editMode && itemPosition >= 0) {

            String[] items = savedItems.split("\n");

            if (itemPosition < items.length) {

                items[itemPosition] = newItem;

                StringBuilder updatedItems = new StringBuilder();

                for (String item : items) {

                    if (updatedItems.length() > 0) {
                        updatedItems.append("\n");
                    }

                    updatedItems.append(item);
                }

                preferences.edit()
                        .putString("pantryItems", updatedItems.toString())
                        .apply();

                Toast.makeText(
                        this,
                        "Item updated",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {

            if (!savedItems.isEmpty()) {
                savedItems += "\n";
            }

            savedItems += newItem;

            preferences.edit()
                    .putString("pantryItems", savedItems)
                    .apply();

            Toast.makeText(
                    this,
                    "Item saved",
                    Toast.LENGTH_SHORT
            ).show();
        }

        finish();
    }

    private boolean validDate(String date) {

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                );

        format.setLenient(false);

        try {
            format.parse(date);
            return true;

        } catch (ParseException e) {
            return false;
        }
    }
}