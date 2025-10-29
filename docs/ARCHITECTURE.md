# Architecture Documentation - Collectibles Store API

## System Overview

The Collectibles Store API is a RESTful web service built using the Model-View-Controller (MVC) pattern with Java and the Spark Framework.

---

## Architecture Layers

```
┌─────────────────────────────────────────────┐
│           Client Layer                      │
│  (Browser, Postman, Mobile App, etc.)       │
└─────────────────────────────────────────────┘
                    ↓ HTTP/JSON
┌─────────────────────────────────────────────┐
│         Controller Layer                    │
│  (ItemController, UserController)           │
│  - Request parsing                          │
│  - Response formatting                      │
│  - Input validation                         │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│          Service Layer                      │
│  (ItemService, UserService)                 │
│  - Business logic                           │
│  - Data management                          │
│  - Validation rules                         │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│          Data Layer                         │
│  (In-Memory HashMap Storage)                │
│  - Data persistence (in-memory)             │
│  - Data retrieval                           │
│  - CRUD operations                          │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│          Data Source                        │
│  (items.json, hardcoded users)              │
└─────────────────────────────────────────────┘
```

---

## Component Diagram

```
Main.java
    ├─> ServerConfig.java (Configuration)
    ├─> ItemService.java (Initialization)
    ├─> UserService.java (Initialization)
    └─> RouteConfig.java
            ├─> ItemController.java
            │       └─> ItemService.java
            │               └─> HashMap<String, Item>
            │                       └─> items.json
            └─> UserController.java
                    └─> UserService.java
                            └─> HashMap<String, User>
                                    └─> hardcoded data
```

---

## Package Structure

```
com.collectibles
├── Main.java                    # Application entry point
├── config/
│   └── ServerConfig.java        # Configuration constants
├── controller/
│   ├── ItemController.java      # Items HTTP handling
│   ├── UserController.java      # Users HTTP handling
│   └── RouteConfig.java         # Route configuration
├── model/
│   ├── Item.java                # Item entity/POJO
│   └── User.java                # User entity/POJO
├── service/
│   ├── ItemService.java         # Items business logic
│   └── UserService.java         # Users business logic
└── util/
    └── JsonUtil.java            # JSON utilities
```

---

## Design Patterns

### 1. Model-View-Controller (MVC)

**Model**: `Item.java`, `User.java`
- Plain Old Java Objects (POJOs)
- Encapsulate data
- No business logic

**Controller**: `ItemController.java`, `UserController.java`
- Handle HTTP requests/responses
- Parse request data
- Format responses
- Delegate to services

**Service** (replaces traditional View): `ItemService.java`, `UserService.java`
- Business logic
- Data management
- Validation

### 2. Dependency Injection

Controllers receive services via constructor:
```java
public ItemController(ItemService itemService) {
    this.itemService = itemService;
}
```

## Request Flow

### Example: GET /items/item1

```
1. Browser/Client
   └─> GET http://localhost:4567/items/item1

2. Spark Framework
   └─> Routes request to: ItemController.getItemById()

3. ItemController
   ├─> Extracts parameter: id = "item1"
   ├─> Validates: id is not null/empty
   └─> Calls: itemService.getItemById("item1")

4. ItemService
   ├─> Looks up: itemsMap.get("item1")
   └─> Returns: Item object or null

5. ItemController
   ├─> Checks: if item == null → 404 error
   ├─> Converts: JsonUtil.toJson(item)
   └─> Sets: status 200, content-type JSON

6. Spark Framework
   └─> Returns JSON response to client

7. Browser/Client
   └─> Receives JSON and displays
```

---

## Data Flow

### Items Data Flow

```
Startup:
items.json → JsonUtil.loadListFromJson() 
          → ItemService.loadItemsFromJson() 
          → HashMap<String, Item>

Runtime:
Request → Controller → Service → HashMap → Response
```

### Users Data Flow

```
Startup:
UserService.initializeSampleUsers() 
          → HashMap<String, User>

Runtime:
Request → Controller → Service → HashMap → Response
```