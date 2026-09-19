package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipesActivity extends AppCompatActivity {

    private LinearLayout recipesContainer;
    private String pantryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        recipesContainer = findViewById(R.id.recipesContainer);

        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> finish());

        SharedPreferences preferences =
                getSharedPreferences("PantryData", MODE_PRIVATE);

        pantryItems = preferences.getString("pantryItems", "").toLowerCase();

        showRecipes();
    }

    private void showRecipes() {
        recipesContainer.removeAllViews();

        addRecipe(
                "Cheese Omelette",
                "Eggs, Cheese",
                "Beat the eggs, add grated cheese and cook in a frying pan.",
                new String[]{"eggs", "cheese"}
        );

        addRecipe(
                "Chicken and Rice",
                "Chicken, Rice",
                "Cook the chicken and serve with cooked rice.",
                new String[]{"chicken", "rice"}
        );

        addRecipe(
                "Cheese Toast",
                "Bread, Cheese",
                "Add cheese to bread and toast until the cheese has melted.",
                new String[]{"bread", "cheese"}
        );

        addRecipe(
                "Egg Fried Rice",
                "Eggs, Rice",
                "Cook the rice, add beaten eggs and fry together in a pan.",
                new String[]{"eggs", "rice"}
        );

        addRecipe(
                "Chicken Sandwich",
                "Chicken, Bread",
                "Cook the chicken, slice it and serve between two slices of bread.",
                new String[]{"chicken", "bread"}
        );
    }

    private void addRecipe(String name, String ingredients,
                           String instructions, String[] requiredItems) {

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

        TextView nameText = new TextView(this);
        nameText.setText(name);
        nameText.setTextSize(19);
        nameText.setTextColor(
                getResources().getColor(R.color.text_dark)
        );

        TextView ingredientsText = new TextView(this);
        ingredientsText.setText("Ingredients: " + ingredients);
        ingredientsText.setTextSize(15);
        ingredientsText.setTextColor(
                getResources().getColor(R.color.text_grey)
        );
        ingredientsText.setPadding(0, 10, 0, 8);

        TextView instructionsText = new TextView(this);
        instructionsText.setText(instructions);
        instructionsText.setTextSize(15);
        instructionsText.setTextColor(
                getResources().getColor(R.color.text_dark)
        );

        TextView availableText = new TextView(this);
        availableText.setTextSize(14);
        availableText.setPadding(0, 12, 0, 0);

        if (hasIngredients(requiredItems)) {
            availableText.setText("You have the ingredients");
            availableText.setTextColor(
                    getResources().getColor(R.color.expiry_safe)
            );
        } else {
            availableText.setText("Some ingredients are missing");
            availableText.setTextColor(
                    getResources().getColor(R.color.expiry_warning)
            );
        }

        card.addView(nameText);
        card.addView(ingredientsText);
        card.addView(instructionsText);
        card.addView(availableText);

        recipesContainer.addView(card);
    }

    private boolean hasIngredients(String[] requiredItems) {

        for (String item : requiredItems) {
            if (!pantryItems.contains(item.toLowerCase())) {
                return false;
            }
        }

        return true;
    }
}