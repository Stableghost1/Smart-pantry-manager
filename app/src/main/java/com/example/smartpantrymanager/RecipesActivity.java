package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {

    private LinearLayout recipesContainer;
    private DatabaseHelper databaseHelper;
    private ArrayList<PantryItem> pantryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        recipesContainer = findViewById(R.id.recipesContainer);
        databaseHelper = new DatabaseHelper(this);

        Button backButton = findViewById(R.id.buttonBack);

        backButton.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPantryItems();
        showSuggestedRecipes();
    }

    private void loadPantryItems() {

        pantryItems = new ArrayList<>();

        Cursor cursor = databaseHelper.getAllPantryItems();

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
    }

    private void showSuggestedRecipes() {

        recipesContainer.removeAllViews();

        Cursor recipeCursor =
                databaseHelper.getAllRecipes();

        int matches = 0;

        while (recipeCursor.moveToNext()) {

            int recipeId = recipeCursor.getInt(
                    recipeCursor.getColumnIndexOrThrow(
                            DatabaseHelper.RECIPE_ID
                    )
            );

            String recipeName = recipeCursor.getString(
                    recipeCursor.getColumnIndexOrThrow(
                            DatabaseHelper.RECIPE_NAME
                    )
            );

            String method = recipeCursor.getString(
                    recipeCursor.getColumnIndexOrThrow(
                            DatabaseHelper.RECIPE_METHOD
                    )
            );

            Recipe recipe = new Recipe(
                    recipeId,
                    recipeName,
                    method
            );

            if (canMakeRecipe(recipe.getId())) {

                displayRecipe(recipe);
                matches++;
            }
        }

        recipeCursor.close();

        if (matches == 0) {
            showNoRecipesMessage();
        }
    }

    private boolean canMakeRecipe(int recipeId) {

        Cursor ingredientCursor =
                databaseHelper.getRecipeIngredients(recipeId);

        boolean canMake = true;

        while (ingredientCursor.moveToNext()) {

            String ingredientName =
                    ingredientCursor.getString(
                            ingredientCursor.getColumnIndexOrThrow(
                                    DatabaseHelper.INGREDIENT_NAME
                            )
                    );

            int requiredQuantity =
                    ingredientCursor.getInt(
                            ingredientCursor.getColumnIndexOrThrow(
                                    DatabaseHelper.INGREDIENT_QUANTITY
                            )
                    );

            String requiredUnit =
                    ingredientCursor.getString(
                            ingredientCursor.getColumnIndexOrThrow(
                                    DatabaseHelper.INGREDIENT_UNIT
                            )
                    );

            if (!hasEnoughIngredient(
                    ingredientName,
                    requiredQuantity,
                    requiredUnit)) {

                canMake = false;
                break;
            }
        }

        ingredientCursor.close();

        return canMake;
    }

    private boolean hasEnoughIngredient(
            String requiredName,
            int requiredQuantity,
            String requiredUnit) {

        String cleanRequiredName =
                cleanIngredientName(requiredName);

        double totalAvailable = 0;

        String requiredType =
                getUnitType(requiredUnit);

        for (PantryItem item : pantryItems) {

            String pantryName =
                    cleanIngredientName(
                            item.getName()
                    );

            if (pantryName.equals(cleanRequiredName)) {

                String pantryType =
                        getUnitType(item.getUnit());

                if (!pantryType.equals(requiredType)) {
                    continue;
                }

                totalAvailable += convertQuantity(
                        item.getQuantity(),
                        item.getUnit()
                );
            }
        }

        double requiredAmount =
                convertQuantity(
                        requiredQuantity,
                        requiredUnit
                );

        return totalAvailable >= requiredAmount;
    }

    private double convertQuantity(
            int quantity,
            String unit) {

        String cleanUnit =
                unit.trim().toLowerCase();

        if (cleanUnit.equals("kg")) {
            return quantity * 1000.0;
        }

        if (cleanUnit.equals("l")) {
            return quantity * 1000.0;
        }

        return quantity;
    }

    private String getUnitType(String unit) {

        String cleanUnit =
                unit.trim().toLowerCase();

        if (cleanUnit.equals("g")
                || cleanUnit.equals("kg")) {

            return "weight";
        }

        if (cleanUnit.equals("ml")
                || cleanUnit.equals("l")) {

            return "volume";
        }

        return "pieces";
    }

    private String cleanIngredientName(String name) {

        String cleanName =
                name.trim().toLowerCase();

        if (cleanName.equals("eggs")) {
            return "egg";
        }

        if (cleanName.equals("tomatoes")) {
            return "tomato";
        }

        if (cleanName.equals("potatoes")) {
            return "potato";
        }

        if (cleanName.endsWith("s")
                && !cleanName.equals("cheese")
                && !cleanName.equals("rice")) {

            cleanName = cleanName.substring(
                    0,
                    cleanName.length() - 1
            );
        }

        return cleanName;
    }

    private void displayRecipe(Recipe recipe) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(30, 25, 30, 25);
        card.setBackgroundColor(Color.WHITE);
        card.setClickable(true);
        card.setFocusable(true);

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

        TextView nameText = new TextView(this);

        nameText.setText(recipe.getName());
        nameText.setTextSize(19);

        nameText.setTextColor(
                getResources().getColor(
                        R.color.text_dark
                )
        );

        TextView availableText = new TextView(this);

        availableText.setText(
                "You have all the required ingredients"
        );

        availableText.setTextSize(14);

        availableText.setTextColor(
                getResources().getColor(
                        R.color.expiry_safe
                )
        );

        availableText.setPadding(
                0,
                10,
                0,
                5
        );

        TextView viewText = new TextView(this);

        viewText.setText(
                "Tap to view recipe"
        );

        viewText.setTextSize(13);

        viewText.setTextColor(
                getResources().getColor(
                        R.color.primary_green
                )
        );

        card.addView(nameText);
        card.addView(availableText);
        card.addView(viewText);

        card.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RecipesActivity.this,
                    RecipeDetailActivity.class
            );

            intent.putExtra(
                    "recipeId",
                    recipe.getId()
            );

            intent.putExtra(
                    "recipeName",
                    recipe.getName()
            );

            intent.putExtra(
                    "recipeMethod",
                    recipe.getMethod()
            );

            startActivity(intent);
        });

        recipesContainer.addView(card);
    }

    private void showNoRecipesMessage() {

        TextView message = new TextView(this);

        message.setText(
                "No recipes match your pantry yet - add more ingredients"
        );

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

        recipesContainer.addView(message);
    }
}