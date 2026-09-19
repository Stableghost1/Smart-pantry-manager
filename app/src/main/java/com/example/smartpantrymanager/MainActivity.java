package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button viewPantryButton = findViewById(R.id.btnViewPantry);
        Button addItemButton = findViewById(R.id.btnAddItem);
        Button shoppingListButton = findViewById(R.id.btnShoppingList);
        Button expiryButton = findViewById(R.id.btnExpiry);
        Button recipesButton = findViewById(R.id.btnRecipes);

        viewPantryButton.setOnClickListener(v ->
                startActivity(new Intent(this, ViewPantryActivity.class)));

        addItemButton.setOnClickListener(v ->
                startActivity(new Intent(this, AddItemActivity.class)));

        shoppingListButton.setOnClickListener(v ->
                startActivity(new Intent(this, ShoppingListActivity.class)));

        expiryButton.setOnClickListener(v ->
                startActivity(new Intent(this, ExpiryTrackerActivity.class)));

        recipesButton.setOnClickListener(v ->
                startActivity(new Intent(this, RecipesActivity.class)));
    }
}