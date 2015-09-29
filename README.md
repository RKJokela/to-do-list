# to-do-list
Final Project for Google Android Development Class, UCLA Extension, Summer 2015

This app provides a simple todo list. The main activity is a scrolling list of all the items on the list. Tapping an item brings up more details, and a long press deletes it from the list.

The list can be sorted by name, by due date, or in order of creation.

The "+" button in the menu bar adds an item to the list. The title and due date are required; other fields are optional. Entering the date brings up a spinner-style date picker dialog that uses today's date as a default.

The list items are stored in a SQLite database, so the list will persist when the user backs out or the OS closes the app. The selected sort option is also saved in a shared preference.
