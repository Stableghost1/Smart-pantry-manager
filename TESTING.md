# Testing

I tested the main functions of the Smart Pantry Manager while developing the app.

## Pantry

I tested adding, editing and deleting pantry items.

- Add item - Working
- Edit item - Working
- Delete item - Working
- Delete confirmation - Working

I also closed and reopened the app to check that the pantry items were still saved.

## Validation

I tested the form with incorrect or missing information.

- Blank item name - Error shown
- Quantity of 0 - Error shown
- Missing unit - Error shown
- Missing category - Error shown
- Wrong expiry date format - Error shown
- No expiry date - Item can still be saved

## Units

I tested different ways of entering units.

For example, entering `piece` is saved as `pieces`.

The app also supports:

- g and kg
- ml and l
- pieces

## Recipe Matching

I tested the recipe matching by changing the amount of chicken in the pantry.

A recipe needed 200 g of chicken. When I changed the pantry amount to 199 g, the chicken recipes were no longer shown.

When I increased the chicken quantity again, the recipes appeared.

## Expiry Tracker

I tested pantry items with and without expiry dates.

The expiry tracker displayed the expiry information correctly.

I also tested switching expiry reminders off and back on in Settings.

## Recipe Details

I opened recipes from the Suggested Recipes screen and checked that the ingredients, quantities and preparation instructions were displayed.

## Result

The main features of the app are working as expected.