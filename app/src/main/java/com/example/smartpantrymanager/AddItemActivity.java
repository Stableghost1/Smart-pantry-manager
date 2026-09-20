package com.example.smartpantrymanager;

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
    private EditText editUnit;
    private EditText editCategory;
    private EditText editExpiryDate;

    private DatabaseHelper databaseHelper;

    private boolean editMode = false;
    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editItemName = findViewById(R.id.editItemName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editCategory = findViewById(R.id.editCategory);
        editExpiryDate = findViewById(R.id.editExpiryDate);

        Button saveButton = findViewById(R.id.buttonSaveItem);
        Button cancelButton = findViewById(R.id.buttonCancel);

        databaseHelper = new DatabaseHelper(this);

        editMode = getIntent().getBooleanExtra(
                "editMode",
                false
        );

        if (editMode) {

            itemId = getIntent().getIntExtra(
                    "itemId",
                    -1
            );

            editItemName.setText(
                    getIntent().getStringExtra("itemName")
            );

            editQuantity.setText(
                    getIntent().getStringExtra("quantity")
            );

            editUnit.setText(
                    getIntent().getStringExtra("unit")
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

        String name =
                editItemName.getText().toString().trim();

        String quantityText =
                editQuantity.getText().toString().trim();

        String unit =
                editUnit.getText().toString().trim().toLowerCase();

        String category =
                editCategory.getText().toString().trim();

        String expiryDate =
                editExpiryDate.getText().toString().trim();

        if (name.isEmpty()) {

            editItemName.setError(
                    "Enter an item name"
            );

            editItemName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {

            editQuantity.setError(
                    "Enter a quantity"
            );

            editQuantity.requestFocus();
            return;
        }

        int quantity;

        try {

            quantity = Integer.parseInt(
                    quantityText
            );

        } catch (NumberFormatException e) {

            editQuantity.setError(
                    "Enter a valid quantity"
            );

            editQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {

            editQuantity.setError(
                    "Quantity must be more than 0"
            );

            editQuantity.requestFocus();
            return;
        }

        if (unit.isEmpty()) {

            editUnit.setError(
                    "Enter a unit"
            );

            editUnit.requestFocus();
            return;
        }

        if (!validUnit(unit)) {

            editUnit.setError(
                    "Use pieces, g, kg, ml or l"
            );

            editUnit.requestFocus();
            return;
        }

        if (category.isEmpty()) {

            editCategory.setError(
                    "Enter a category"
            );

            editCategory.requestFocus();
            return;
        }

        if (expiryDate.isEmpty()) {

            editExpiryDate.setError(
                    "Enter an expiry date"
            );

            editExpiryDate.requestFocus();
            return;
        }

        if (!validDate(expiryDate)) {

            editExpiryDate.setError(
                    "Use DD/MM/YYYY"
            );

            editExpiryDate.requestFocus();
            return;
        }

        if (editMode && itemId >= 0) {

            int result =
                    databaseHelper.updatePantryItem(
                            itemId,
                            name,
                            quantity,
                            unit,
                            category,
                            expiryDate
                    );

            if (result > 0) {

                Toast.makeText(
                        this,
                        "Item updated",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Item could not be updated",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {

            long result =
                    databaseHelper.addPantryItem(
                            name,
                            quantity,
                            unit,
                            category,
                            expiryDate
                    );

            if (result != -1) {

                Toast.makeText(
                        this,
                        "Item saved",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Item could not be saved",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private boolean validUnit(String unit) {

        return unit.equals("pieces")
                || unit.equals("g")
                || unit.equals("kg")
                || unit.equals("ml")
                || unit.equals("l");
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