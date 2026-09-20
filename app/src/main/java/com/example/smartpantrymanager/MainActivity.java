package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mainToolbar);

        toolbar.setTitle("Smart Pantry");

        toolbar.inflateMenu(R.menu.main_menu);

        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.menuHome) {
                return true;
            }

            if (item.getItemId() == R.id.menuSettings) {

                Intent intent = new Intent(
                        MainActivity.this,
                        SettingsActivity.class
                );

                startActivity(intent);

                return true;
            }

            return false;
        });

        Button viewPantryButton =
                findViewById(R.id.btnViewPantry);

        Button addItemButton =
                findViewById(R.id.btnAddItem);

        Button shoppingListButton =
                findViewById(R.id.btnShoppingList);

        Button expiryButton =
                findViewById(R.id.btnExpiry);

        Button recipesButton =
                findViewById(R.id.btnRecipes);

        Button settingsButton =
                findViewById(R.id.btnSettings);

        viewPantryButton.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                this,
                                ViewPantryActivity.class
                        )
                )
        );

        addItemButton.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                this,
                                AddItemActivity.class
                        )
                )
        );

        shoppingListButton.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                this,
                                ShoppingListActivity.class
                        )
                )
        );

        expiryButton.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                this,
                                ExpiryTrackerActivity.class
                        )
                )
        );

        recipesButton.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                this,
                                RecipesActivity.class
                        )
                )
        );

        settingsButton.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                this,
                                SettingsActivity.class
                        )
                )
        );
    }
}