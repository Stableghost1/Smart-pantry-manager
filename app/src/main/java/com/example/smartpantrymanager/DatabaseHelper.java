package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartpantry.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_PANTRY = "pantry";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_EXPIRY = "expiry";

    public static final String TABLE_RECIPES = "recipes";

    public static final String RECIPE_ID = "id";
    public static final String RECIPE_NAME = "name";
    public static final String RECIPE_METHOD = "method";

    public static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public static final String INGREDIENT_ID = "id";
    public static final String INGREDIENT_RECIPE_ID = "recipe_id";
    public static final String INGREDIENT_NAME = "ingredient_name";
    public static final String INGREDIENT_QUANTITY = "quantity";
    public static final String INGREDIENT_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createPantryTable(db);
        createRecipeTables(db);
        addRecipes(db);
    }

    private void createPantryTable(SQLiteDatabase db) {

        String createTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL DEFAULT 'pieces', " +
                        COLUMN_CATEGORY + " TEXT NOT NULL, " +
                        COLUMN_EXPIRY + " TEXT)";

        db.execSQL(createTable);
    }

    private void createRecipeTables(SQLiteDatabase db) {

        String createRecipes =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        RECIPE_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RECIPE_NAME + " TEXT NOT NULL, " +
                        RECIPE_METHOD + " TEXT NOT NULL)";

        db.execSQL(createRecipes);

        String createIngredients =
                "CREATE TABLE " +
                        TABLE_RECIPE_INGREDIENTS + " (" +
                        INGREDIENT_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        INGREDIENT_RECIPE_ID +
                        " INTEGER NOT NULL, " +
                        INGREDIENT_NAME +
                        " TEXT NOT NULL, " +
                        INGREDIENT_QUANTITY +
                        " INTEGER NOT NULL, " +
                        INGREDIENT_UNIT +
                        " TEXT NOT NULL DEFAULT 'pieces')";

        db.execSQL(createIngredients);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        if (oldVersion < 2) {

            createRecipeTables(db);
            addRecipes(db);
        }

        if (oldVersion < 3) {

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_PANTRY +
                            " ADD COLUMN " +
                            COLUMN_UNIT +
                            " TEXT NOT NULL DEFAULT 'pieces'"
            );

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_RECIPE_INGREDIENTS +
                            " ADD COLUMN " +
                            INGREDIENT_UNIT +
                            " TEXT NOT NULL DEFAULT 'pieces'"
            );
        }

        if (oldVersion < 4) {
            updateRecipeIngredientUnits(db);
        }
    }

    private void updateRecipeIngredientUnits(SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(INGREDIENT_UNIT, "pieces");

        db.update(
                TABLE_RECIPE_INGREDIENTS,
                values,
                INGREDIENT_NAME + " = ?",
                new String[]{"Eggs"}
        );

        db.update(
                TABLE_RECIPE_INGREDIENTS,
                values,
                INGREDIENT_NAME + " = ?",
                new String[]{"Bread"}
        );


        values.clear();

        values.put(INGREDIENT_UNIT, "g");
        values.put(INGREDIENT_QUANTITY, 200);

        db.update(
                TABLE_RECIPE_INGREDIENTS,
                values,
                INGREDIENT_NAME + " = ?",
                new String[]{"Chicken"}
        );


        values.clear();

        values.put(INGREDIENT_UNIT, "g");
        values.put(INGREDIENT_QUANTITY, 100);

        db.update(
                TABLE_RECIPE_INGREDIENTS,
                values,
                INGREDIENT_NAME + " = ?",
                new String[]{"Cheese"}
        );


        values.clear();

        values.put(INGREDIENT_UNIT, "g");
        values.put(INGREDIENT_QUANTITY, 100);

        db.update(
                TABLE_RECIPE_INGREDIENTS,
                values,
                INGREDIENT_NAME + " = ?",
                new String[]{"Rice"}
        );
    }

    public long addPantryItem(String name,
                              int quantity,
                              String unit,
                              String category,
                              String expiry) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_EXPIRY, expiry);

        return db.insert(
                TABLE_PANTRY,
                null,
                values
        );
    }

    public Cursor getAllPantryItems() {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC"
        );
    }

    public int updatePantryItem(int id,
                                String name,
                                int quantity,
                                String unit,
                                String category,
                                String expiry) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_EXPIRY, expiry);

        return db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public int deletePantryItem(int id) {

        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public Cursor getAllRecipes() {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                RECIPE_NAME + " ASC"
        );
    }

    public Cursor getRecipeIngredients(int recipeId) {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                INGREDIENT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null
        );
    }

    private long addRecipe(SQLiteDatabase db,
                           String name,
                           String method) {

        ContentValues values = new ContentValues();

        values.put(RECIPE_NAME, name);
        values.put(RECIPE_METHOD, method);

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }

    private void addRecipeIngredient(SQLiteDatabase db,
                                     long recipeId,
                                     String name,
                                     int quantity,
                                     String unit) {

        ContentValues values = new ContentValues();

        values.put(
                INGREDIENT_RECIPE_ID,
                recipeId
        );

        values.put(
                INGREDIENT_NAME,
                name
        );

        values.put(
                INGREDIENT_QUANTITY,
                quantity
        );

        values.put(
                INGREDIENT_UNIT,
                unit
        );

        db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }

    private void addRecipes(SQLiteDatabase db) {

        long recipeId;

        recipeId = addRecipe(
                db,
                "Cheese Omelette",
                "Beat the eggs. Add the cheese and cook in a frying pan."
        );

        addRecipeIngredient(
                db, recipeId, "Eggs", 2, "pieces"
        );

        addRecipeIngredient(
                db, recipeId, "Cheese", 100, "g"
        );


        recipeId = addRecipe(
                db,
                "Cheese Toast",
                "Add cheese to the bread and toast until golden."
        );

        addRecipeIngredient(
                db, recipeId, "Bread", 2, "pieces"
        );

        addRecipeIngredient(
                db, recipeId, "Cheese", 100, "g"
        );


        recipeId = addRecipe(
                db,
                "Scrambled Eggs",
                "Beat the eggs and cook slowly in a frying pan."
        );

        addRecipeIngredient(
                db, recipeId, "Eggs", 2, "pieces"
        );


        recipeId = addRecipe(
                db,
                "Egg on Toast",
                "Cook the eggs and serve them on toasted bread."
        );

        addRecipeIngredient(
                db, recipeId, "Eggs", 2, "pieces"
        );

        addRecipeIngredient(
                db, recipeId, "Bread", 2, "pieces"
        );


        recipeId = addRecipe(
                db,
                "Chicken Sandwich",
                "Cook the chicken and serve between slices of bread."
        );

        addRecipeIngredient(
                db, recipeId, "Chicken", 200, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Bread", 2, "pieces"
        );


        recipeId = addRecipe(
                db,
                "Chicken and Rice",
                "Cook the chicken and serve with cooked rice."
        );

        addRecipeIngredient(
                db, recipeId, "Chicken", 200, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Rice", 100, "g"
        );


        recipeId = addRecipe(
                db,
                "Egg Fried Rice",
                "Cook the rice, add the eggs and fry together."
        );

        addRecipeIngredient(
                db, recipeId, "Rice", 100, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Eggs", 2, "pieces"
        );


        recipeId = addRecipe(
                db,
                "Cheesy Rice",
                "Cook the rice and stir in the cheese."
        );

        addRecipeIngredient(
                db, recipeId, "Rice", 100, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Cheese", 100, "g"
        );


        recipeId = addRecipe(
                db,
                "Chicken Omelette",
                "Cook the chicken, add beaten eggs and cook together."
        );

        addRecipeIngredient(
                db, recipeId, "Chicken", 200, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Eggs", 2, "pieces"
        );


        recipeId = addRecipe(
                db,
                "Chicken Cheese Toast",
                "Place cooked chicken and cheese on bread and toast."
        );

        addRecipeIngredient(
                db, recipeId, "Chicken", 200, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Cheese", 100, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Bread", 2, "pieces"
        );


        recipeId = addRecipe(
                db,
                "Cheese Sandwich",
                "Place cheese between two slices of bread."
        );

        addRecipeIngredient(
                db, recipeId, "Cheese", 100, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Bread", 2, "pieces"
        );


        recipeId = addRecipe(
                db,
                "Boiled Eggs",
                "Boil the eggs until cooked and serve."
        );

        addRecipeIngredient(
                db, recipeId, "Eggs", 2, "pieces"
        );


        recipeId = addRecipe(
                db,
                "Chicken and Eggs",
                "Cook the chicken and eggs together in a frying pan."
        );

        addRecipeIngredient(
                db, recipeId, "Chicken", 200, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Eggs", 2, "pieces"
        );


        recipeId = addRecipe(
                db,
                "Cheese and Egg Sandwich",
                "Cook the eggs and place them on bread with cheese."
        );

        addRecipeIngredient(
                db, recipeId, "Eggs", 2, "pieces"
        );

        addRecipeIngredient(
                db, recipeId, "Cheese", 100, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Bread", 2, "pieces"
        );


        recipeId = addRecipe(
                db,
                "Chicken Egg Fried Rice",
                "Cook the chicken, rice and eggs together in a frying pan."
        );

        addRecipeIngredient(
                db, recipeId, "Chicken", 200, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Rice", 100, "g"
        );

        addRecipeIngredient(
                db, recipeId, "Eggs", 2, "pieces"
        );
    }
}