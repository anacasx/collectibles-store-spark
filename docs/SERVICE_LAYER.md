# Service Layer Documentation

## Overview
The service layer contains business logic and data management for the Collectibles Store application.

## Architecture

```
Controller Layer (Phase 4+)
        ↓
Service Layer (Current Phase)
        ↓
Data Storage (In-Memory Maps)
        ↑
JSON Files (items.json)
```

## ItemService

### Purpose
Manages all operations related to collectible items.

### Key Methods
- `getAllItems()`: Returns list of all items
- `getItemById(String id)`: Retrieves specific item
- `itemExists(String id)`: Checks if item exists
- `saveItem(Item item)`: Adds or updates an item
- `deleteItem(String id)`: Removes an item
- `searchItemsByName(String term)`: Searches items by name
- `getItemCount()`: Returns total number of items

### Data Source
Items are loaded from `src/main/resources/data/items.json` on service initialization.

### Storage
Items are stored in a `HashMap<String, Item>` for O(1) lookup performance.

## UserService

### Purpose
Manages all operations related to users (buyers, sellers, admins).

### Key Methods
- `getAllUsers()`: Returns list of all users
- `getUserById(String id)`: Retrieves specific user
- `userExists(String id)`: Checks if user exists
- `addUser(User user)`: Adds a new user (throws exception if exists)
- `updateUser(String id, User user)`: Updates existing user
- `deleteUser(String id)`: Removes a user
- `isValidEmail(String email)`: Validates email format
- `getUserCount()`: Returns total number of users

### Data Source
Sample users are initialized in memory on service creation. In future sprints, this could load from a JSON file or database.

### Storage
Users are stored in a `HashMap<String, User>` for O(1) lookup performance.

## JsonUtil

### Purpose
Utility class for JSON operations using Gson.

### Key Methods
- `loadListFromJson(String path, TypeToken)`: Loads list from JSON file
- `toJson(Object obj)`: Converts object to JSON string
- `fromJson(String json, Class)`: Converts JSON string to object
- `getGson()`: Returns shared Gson instance

### Features
- UTF-8 encoding support
- Pretty printing enabled
- Type-safe generic parsing
- Error handling with descriptive messages

## Design Decisions

### Why HashMap?
- O(1) average time complexity for get/put operations
- Perfect for ID-based lookups
- Simple and effective for in-memory storage

### Why Load on Startup?
- Fast response times (data already in memory)
- No file I/O during request handling
- Suitable for relatively small datasets

### Why Separate Services?
- Single Responsibility Principle
- Easy to test independently
- Can be replaced with database implementations later
- Clear separation of concerns

## Future Enhancements

1. Add persistence to save changes back to JSON
2. Implement caching strategies
3. Add transaction support
4. Implement repository pattern
5. Add logging for all operations
6. Implement data validation rules
7. Add pagination for large datasets
8. Connect to real database (MongoDB, PostgreSQL, etc.)

## Error Handling

Currently, services throw `RuntimeException` and `IllegalArgumentException` for:
- File not found errors
- Invalid data errors
- Duplicate ID errors
- Null value errors

In future phases, we'll implement custom exception classes for better error handling.