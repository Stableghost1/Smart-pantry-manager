package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textRecipeName;
    private TextView textIngredients;
    private TextView textMethod;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        textRecipeName = findViewById(R.id.textRecipeName);
        textIngredients = findViewById(R.id.textIngredients);
        textMethod = findViewById(R.id.textMethod);

        Button backButton = findViewById(R.id.buttonBack);

        databaseHelper = new DatabaseHelper(this);

        backButton.setOnClickListener(v -> finish());

        int recipeId = getIntent().getIntExtra(
                "recipeId",
                -1
        );

        String recipeName =
                getIntent().getStringExtra("recipeName");

        String recipeMethod =
                getIntent().getStringExtra("recipeMethod");

        textRecipeName.setText(recipeName);
        textMethod.setText(recipeMethod);

        loadIngredients(recipeId);
    }

    private void loadIngredients(int recipeId) {

        Cursor cursor =
                databaseHelper.getRecipeIngredients(recipeId);

        StringBuilder ingredients =
                new StringBuilder();

        while (cursor.moveToNext()) {

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.INGREDIENT_NAME
                    )
            );

            int quantity = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.INGREDIENT_QUANTITY
                    )
            );

            String unit = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.INGREDIENT_UNIT
                    )
            );

            ingredients.append("• ")
                    .append(name)
                    .append(" - ")
                    .append(quantity)
                    .append(" ")
                    .append(unit)
                    .append("\n");
        }

        cursor.close();

        textIngredients.setText(
                ingredients.toString().trim()
        );
    }
}