# Smart Pantry Manager

Smart Pantry Manager is an Android application developed in Java for the Mobile App Development 700 practical assignment.

The application allows users to manage ingredients stored in their pantry, monitor expiry dates, maintain a shopping list, and view recipe suggestions based on ingredients they currently have available.

## Main Features

- Add pantry items
- View pantry items
- Edit pantry items
- Delete pantry items
- Store quantity and measurement units
- Optional expiry dates
- Track ingredient expiry dates
- Enable or disable expiry reminders
- Maintain a shopping list
- View suggested recipes
- Strict recipe matching based on available ingredients and quantities
- View recipe ingredients and preparation instructions
- Settings screen
- Toolbar navigation
- Persistent local data storage

## Recipe Matching

The recipe system only displays recipes when all required ingredients are available in the pantry in sufficient quantities.

For example, if a recipe requires 200 g of chicken but only 199 g is available, the recipe will not be displayed.

The application also supports basic unit conversion between:

- g and kg
- ml and l
- pieces

This allows pantry quantities to be compared with the quantities required by recipes.

## Database

The application uses SQLite for its main data storage.

SQLite was selected because the application needs structured local data that remains available after the application is closed. It also allows the pantry system to perform Create, Read, Update and Delete operations.

The SQLite database stores:

- Pantry items
- Recipes
- Recipe ingredients

The application includes 15 recipes which are added to the database when it is first created.

SharedPreferences is used separately for simple application preferences such as the expiry reminder setting and shopping list.

## Technologies Used

- Java
- Android Studio
- XML
- SQLite
- RecyclerView
- SharedPreferences
- Git
- GitHub

## Application Screens

The application includes:

1. Home
2. View Pantry
3. Add / Edit Pantry Item
4. Shopping List
5. Expiry Tracker
6. Suggested Recipes
7. Recipe Detail
8. Settings

## Pantry Management

Each pantry item can contain:

- Item name
- Quantity
- Unit
- Category
- Optional expiry date

The pantry is displayed using a RecyclerView and custom adapter.

## Input Validation

The application validates user input before pantry items are saved.

Examples include:

- Item name cannot be blank
- Quantity must be greater than zero
- A valid measurement unit must be entered
- Category cannot be blank
- Expiry date must use DD/MM/YYYY when a date is entered

## Running the Application

1. Clone or download the repository.
2. Open the project in Android Studio.
3. Allow Gradle to sync.
4. Select an Android emulator or connected Android device.
5. Build the project.
6. Run the application.

The application was developed and tested using a Pixel 7 Android emulator.

## Data Persistence

Pantry information is stored in SQLite and remains available after the application is closed and reopened.

Settings preferences are stored using SharedPreferences.

## Developer

Developed as part of the Mobile App Development 700 practical assignment.