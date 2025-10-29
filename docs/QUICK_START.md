# Quick Start Guide - Collectibles Store API

## Prerequisites

- Java 8 or higher
- Maven 3.6+
- IntelliJ IDEA (or any Java IDE)
- Postman (for testing)
- Git

---

## 5-Minute Setup

### 1. Clone Repository
```bash
git clone https://github.com/ramsalue/collectibles-store-spark.git
cd collectibles-store-spark
```

### 2. Build Project
```bash
mvn clean install
```

### 3. Run Application
```bash
Open Main.java and click Run
```

### 4. Verify Server Started
```bash
curl http://localhost:4567/health
```

Expected response:
```json
{"status": "OK"}
```

---

## First API Calls

### Get All Items
```bash
curl http://localhost:4567/items
```

### Get Specific Item
```bash
curl http://localhost:4567/items/item1
```

### Get All Users
```bash
curl http://localhost:4567/users
```

### Create New User
```bash
curl -X POST http://localhost:4567/users/user10 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "role": "buyer"
  }'
```

---

## Project Structure

```
collectibles-store-spark/
├── src/main/java/com/collectibles/
│   ├── Main.java                    # Application entry point
│   ├── config/
│   │   └── ServerConfig.java        # Configuration
│   ├── controller/
│   │   ├── ItemController.java      # Items endpoints
│   │   ├── UserController.java      # Users endpoints
│   │   └── RouteConfig.java         # Route configuration
│   ├── model/
│   │   ├── Item.java                # Item entity
│   │   └── User.java                # User entity
│   ├── service/
│   │   ├── ItemService.java         # Item business logic
│   │   └── UserService.java         # User business logic
│   └── util/
│       └── JsonUtil.java            # JSON utilities
├── src/main/resources/
│   ├── data/
│   │   └── items.json               # Items data
│   └── logback.xml                  # Logging config
└── pom.xml                          # Maven configuration
```

---

## Common Tasks

### Add New Item (Manual)
Edit `src/main/resources/data/items.json`:
```json
{
  "id": "item8",
  "name": "New Collectible",
  "description": "Description here",
  "price": "$100.00 USD"
}
```
Restart server to load changes.

### Change Server Port
Edit `Main.java` or set environment variable:
```bash
export PORT=8080
mvn exec:java
```

### View Logs
Check console output or configure logging in `logback.xml`

---

## Testing with Postman

### Import Collection
1. Open Postman
2. Click Import
3. Select `postman/Collectibles-Store-API-Sprint1.postman_collection.json`
4. Import environment: `postman/Local-Development.postman_environment.json`

### Run All Tests
1. Click on collection
2. Click Run
3. Click "Run Collectibles Store API - Sprint 1"
4. Wait for results

Expected: All tests pass (150+ assertions)

---

## Troubleshooting

### Port Already in Use
```
Error: Port 4567 already in use
```
Solution:
- Kill process using port: `lsof -ti:4567 | xargs kill`
- Or change port in ServerConfig.java

### Maven Dependencies Not Downloading
```
Error: Could not resolve dependencies
```
Solution:
```bash
mvn clean install -U
```

### Items Not Loading
```
Successfully loaded 0 items from JSON
```
Solution:
- Verify `items.json` exists in `src/main/resources/data/`
- Check JSON syntax with JSONLint
- Rebuild project: `mvn clean compile`