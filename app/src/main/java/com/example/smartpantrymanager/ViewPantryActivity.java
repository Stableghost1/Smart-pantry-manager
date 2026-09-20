package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewPantryActivity extends AppCompatActivity {

    private RecyclerView pantryRecyclerView;
    private TextView textEmptyPantry;
    private ArrayList<PantryItem> pantryItems;
    private PantryAdapter pantryAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pantry);

        pantryRecyclerView = findViewById(
                R.id.pantryRecyclerView
        );

        textEmptyPantry = findViewById(
                R.id.textEmptyPantry
        );

        Button addItemButton = findViewById(
                R.id.buttonAddItem
        );

        Button backButton = findViewById(
                R.id.buttonBack
        );

        databaseHelper = new DatabaseHelper(this);
        pantryItems = new ArrayList<>();

        pantryRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        pantryAdapter = new PantryAdapter(
                pantryItems,
                new PantryAdapter.OnItemClickListener() {

                    @Override
                    public void onEditClick(PantryItem item) {

                        Intent intent = new Intent(
                                ViewPantryActivity.this,
                                AddItemActivity.class
                        );

                        intent.putExtra(
                                "editMode",
                                true
                        );

                        intent.putExtra(
                                "itemId",
                                item.getId()
                        );

                        intent.putExtra(
                                "itemName",
                                item.getName()
                        );

                        intent.putExtra(
                                "quantity",
                                String.valueOf(
                                        item.getQuantity()
                                )
                        );

                        intent.putExtra(
                                "unit",
                                item.getUnit()
                        );

                        intent.putExtra(
                                "category",
                                item.getCategory()
                        );

                        intent.putExtra(
                                "expiryDate",
                                item.getExpiryDate()
                        );

                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(PantryItem item) {

                        int result =
                                databaseHelper.deletePantryItem(
                                        item.getId()
                                );

                        if (result > 0) {

                            Toast.makeText(
                                    ViewPantryActivity.this,
                                    "Item deleted",
                                    Toast.LENGTH_SHORT
                            ).show();

                            loadPantryItems();

                        } else {

                            Toast.makeText(
                                    ViewPantryActivity.this,
                                    "Item could not be deleted",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                }
        );

        pantryRecyclerView.setAdapter(
                pantryAdapter
        );

        addItemButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ViewPantryActivity.this,
                    AddItemActivity.class
            );

            startActivity(intent);
        });

        backButton.setOnClickListener(
                v -> finish()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPantryItems();
    }

    private void loadPantryItems() {

        pantryItems.clear();

        Cursor cursor =
                databaseHelper.getAllPantryItems();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_ID
                    )
            );

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

            String category = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_CATEGORY
                    )
            );

            String expiry = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_EXPIRY
                    )
            );

            PantryItem item = new PantryItem(
                    id,
                    name,
                    quantity,
                    unit,
                    category,
                    expiry
            );

            pantryItems.add(item);
        }

        cursor.close();

        pantryAdapter.notifyDataSetChanged();

        if (pantryItems.isEmpty()) {

            pantryRecyclerView.setVisibility(View.GONE);
            textEmptyPantry.setVisibility(View.VISIBLE);

        } else {

            pantryRecyclerView.setVisibility(View.VISIBLE);
            textEmptyPantry.setVisibility(View.GONE);
        }
    }
}