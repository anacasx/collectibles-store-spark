# Collectibles Store API - Sprint 1

A RESTful API service for managing collectible items and users, built with Java and Spark Framework as part of the Digital NAO Backend Developer Certification program.

---

## Project Overview

### Challenge Context
Rafael, a recent Systems Engineering graduate, is developing a website for his friend Ramon to sell collectible items online. This API serves as the backend foundation for the collectibles marketplace, enabling item browsing and user management through RESTful endpoints.

### Sprint 1 Deliverables
-  Complete REST API with 10 endpoints
-  Items catalog management (7 collectibles)
-  Full CRUD operations for users
-  Maven project configuration
-  CORS-enabled for web applications
-  Comprehensive documentation
-  Postman test collection (45 tests)

---

##  Quick Start

### Prerequisites
- Java 8 or higher
- Maven 3.6+
- Git
- Postman (optional, for testing)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/ramsalue/collectibles-store-spark.git
cd collectibles-store-spark
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**

Run `Main.java` or execute
```bash
mvn exec:java
```

4. **Verify it's running**
```bash
curl http://localhost:4567/health
# Expected: {"status": "OK"}
```

### First API Call
```bash
# Get all collectible items
curl http://localhost:4567/items
```

---

## API Endpoints

### Utility Endpoints
- `GET /` - API information
- `GET /health` - Health check

### Items Endpoints
- `GET /items` - Get all collectible items
- `GET /items/:id` - Get specific item by ID

### Users Endpoints
- `GET /users` - Get all users
- `GET /users/:id` - Get specific user by ID
- `POST /users/:id` - Create new user
- `PUT /users/:id` - Update existing user
- `DELETE /users/:id` - Delete user
- `OPTIONS /users/:id` - Check if user exists

**Total Endpoints**: 10

---

## Project Structure

```
collectibles-store-spark/
├── src/
│   └── main/
│       ├── java/com/collectibles/
│       │   ├── Main.java                    # Application entry point
│       │   ├── config/
│       │   │   └── ServerConfig.java        # Configuration constants
│       │   ├── controller/
│       │   │   ├── ItemController.java      # Items HTTP handlers
│       │   │   ├── UserController.java      # Users HTTP handlers
│       │   │   └── RouteConfig.java         # Route configuration
│       │   ├── model/
│       │   │   ├── Item.java                # Item entity
│       │   │   └── User.java                # User entity
│       │   ├── service/
│       │   │   ├── ItemService.java         # Items business logic
│       │   │   └── UserService.java         # Users business logic
│       │   └── util/
│       │       └── JsonUtil.java            # JSON utilities
│       └── resources/
│           ├── data/
│           │   └── items.json               # Collectibles data
│           └── logback.xml                  # Logging configuration
├── docs/                                    # Complete documentation
├── postman/                                 # Postman collection
├── screenshots/                             # Testing screenshots
├── pom.xml                                  # Maven configuration
└── README.md                                # This file
```

---

## Testing

### Using Postman

1. **Import Collection**
   - Open Postman
   - Import `postman/Collectibles-Store-API-Sprint1.postman_collection.json`

2. **Run Tests**
   - Select collection
   - Click "Run"
   - Execute all tests

### Using cURL

```bash
# Get all items
curl http://localhost:4567/items

# Get specific item
curl http://localhost:4567/items/item1

# Create user
curl -X POST http://localhost:4567/users/user10 \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","role":"buyer"}'

# Update user
curl -X PUT http://localhost:4567/users/user10 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated User","email":"updated@example.com","role":"seller"}'

# Delete user
curl -X DELETE http://localhost:4567/users/user10
```

---

## Documentation

### Core Documentation
- [Quick Start Guide](docs/QUICK_START.md) - Get started in 5 minutes
- [API Documentation](docs/API_DOCUMENTATION.md) - Complete endpoint reference
- [Architecture](docs/ARCHITECTURE.md) - System design and patterns

### Development Guides
- [Service Layer](docs/SERVICE_LAYER.md) - Business logic documentation
- [Validation Rules](docs/VALIDATION_RULES.md) - Data validation guide

---

## Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 8+ | Programming language |
| Spark Framework | 2.9.4 | Web framework |
| Maven | 3.6+ | Build tool & dependency management |
| Gson | 2.10.1 | JSON serialization/deserialization |
| Logback | 1.2.11 | Logging framework |
| Postman | Latest | API testing |

---

## Available Data

### Collectible Items (7 total)
* Cap autographed by Peso Pluma - $621.34 USD
* Helmet autographed by Rosalía - $734.57 USD
* Bad Bunny's Jacket - $521.89 USD
* Fernando Delgadillo's Guitar - $823.12 USD
* Jersey signed by Snoop Dogg - $355.67 USD
* Autographed Cardi B garment - $674.23 USD
* Guitar autographed by Coldplay - $458.91 USD

### Initial Users (3 total)
- user1: Admin User (admin@collectibles.com) - admin role
- user2: John Collector (john@email.com) - buyer role
- user3: Ramon Organizer (ramon@email.com) - seller role

---

## Security Features

### Implemented
-  Input validation (required fields, email format, role values)
-  CORS configuration for web applications
-  Security headers (X-Content-Type-Options, X-Frame-Options, X-XSS-Protection)
- Error handling with safe error messages
- UTF-8 encoding support

### Not Yet Implemented
-  Authentication/Authorization (planned for future sprints)
-  Rate limiting
-  HTTPS/TLS

---

##  Features

### Current (Sprint 1)
-  RESTful API architecture
-  JSON request/response format
-  Full CRUD for users
-  Read operations for items
-  In-memory data storage
-  Error handling with standardized responses
-  CORS support for browsers
-  Request logging

### Planned (Sprint 2)
-  Mustache templates for web views
-  Exception handling module (404, 500 pages)
-  Web forms for offer submission
-  Enhanced error pages

### Planned (Sprint 3)
-  Price range filtering
-  WebSocket for real-time price updates
-  Advanced search capabilities

---

## API Response Status Codes

| Code | Status | Usage |
|------|--------|-------|
| 200 | OK | Successful GET, PUT, OPTIONS |
| 201 | Created | Successful POST |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Invalid input data |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Duplicate resource |
| 500 | Internal Server Error | Server error |

---

## Configuration

### Environment Variables
```bash
# Set custom port (optional, default: 4567)
export PORT=8080
mvn exec:java
```

### Logging Level
Edit `src/main/resources/logback.xml`:
```xml
<root level="INFO">  <!-- Change to DEBUG for verbose logging -->
    <appender-ref ref="STDOUT" />
</root>
```

---

## Troubleshooting

### Common Issues

**Port Already in Use**
```bash
# Kill process using port 4567
lsof -ti:4567 | xargs kill -9
```

**Items Not Loading**
- Verify `items.json` exists in `src/main/resources/data/`
- Rebuild project: `mvn clean compile`

**Maven Dependencies Not Downloading**
```bash
mvn clean install -U
```
---

## Learning Objectives Achieved

### Hard Skills
-  RESTful API design and implementation
-  Java programming with OOP principles
-  Maven project configuration
-  HTTP protocol understanding (methods, status codes)
-  JSON data handling

### Technical Skills
-  Spark Framework usage
-  Web development environment setup
-  API development best practices
-  CRUD operations implementation
-  Error handling strategies

### Soft Skills
-  Problem-solving (debugging, optimization)
-  Effective communication (documentation)
-  Perseverance (completing complex tasks)
-  Adaptability (learning new framework)

---

## Team

- **Rafael** - Lead Developer & System Architect
- **Sofia** - Technical Advisor & Code Reviewer
- **Ramon** - Product Owner & Requirements Provider

---

## License

This project is part of the Digital NAO Backend Developer Certification program.

---

## Acknowledgments

- Digital NAO team for the challenge design
- Spark Framework community for excellent documentation
- Ramon for the business case and requirements
- Sofia for technical guidance

---

## Version History

### Version 1.0.0 (Sprint 1) - Current
- Initial API implementation
- Items and Users endpoints
- CORS configuration
- Complete documentation
- Postman test collection

### Upcoming (Sprint 2)
- Templates with Mustache
- Exception handling
- Web forms

---

## Project Timeline

- **Sprint 1**: 4 days (API Service Foundation) - COMPLETE
- **Sprint 2**: 3 days (Templates & Exceptions) - Planned
- **Sprint 3**: 4 days (Filters & WebSockets) - Planned
- **Final Submission**: 2 days (Integration & Presentation) - Planned

---

## Project Status

**Sprint 1**: COMPLETE  
**Current Version**: 1.0.0  
**Last Updated**: 27/10/2025  
**Next Sprint**: Templates and Exception Handling

---

## Quick Links

- [GitHub Repository](https://github.com/ramsalue/collectibles-store-spark)
- [API Documentation](docs/API_DOCUMENTATION.md)
- [Quick Start Guide](docs/QUICK_START.md)
- [Postman Collection](postman/)
- [Full Documentation](docs/)