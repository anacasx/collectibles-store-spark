# Collectibles Store API - Sprint 3: Price Filtering & Real-Time Updates

A RESTful and template-based web application built with Java and Spark Framework as part of the Digital NAO Backend Developer Certification.  
This sprint adds advanced price filtering and real-time price updates via WebSocket.

---

## Project Overview

### Challenge Context
Rafael, a recent Systems Engineering graduate, is developing a website for his friend Ramon to sell collectible items online. This API serves as the backend foundation for the collectibles marketplace, enabling item browsing, user management, and real-time price tracking.

### Sprint 1
- Complete REST API with 10 endpoints
- Items catalog management (7 collectibles)
- Full CRUD operations for users
- Maven project configuration
- CORS-enabled for web applications
- Comprehensive documentation
- Postman test collection (45 tests)

### Sprint 2
- Implemented exception handling module (404 & 500)
- Configured Mustache template engine
- Created items and offers templates
- Integrated CSS and JS from instructor package
- Implemented offer submission and viewing workflow
- Ensured backward compatibility with API endpoints

### Sprint 3 (Current)
- **Price filtering by range (minPrice, maxPrice)**
- **WebSocket integration for real-time price updates**
- **Improved data model (String → double for prices)**
- **Admin panel for price management**
- **Automatic price simulation system**
- **Enhanced template with live price updates**

---

## Quick Start

### Prerequisites
- Java 8 or higher
- Maven 3.6+
- Git
- Postman (optional, for testing)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/anacasx/collectibles-store-spark.git
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

5. **Access the web interface**
- Items catalog: http://localhost:4567/items/view
- Admin panel: http://localhost:4567/admin

---

## API Endpoints

### Utility Endpoints
- `GET /` - API information
- `GET /health` - Health check

### Items Endpoints
- `GET /items` - Get all items (JSON) with optional filtering
  - Query params: `minPrice`, `maxPrice`
- `GET /items/:id` - Get specific item by ID (JSON)
- `GET /items/view` - Items catalog page (HTML) with filtering
- `GET /items/view/:id` - Item details page (HTML)

### Offers Endpoints
- `GET /offers/form` - Offer submission form
- `GET /offers/view` - View all offers (HTML)
- `POST /offers` - Submit new offer
- `GET /offers` - Get offers (JSON API)

### Users Endpoints
- `GET /users` - Get all users
- `GET /users/:id` - Get specific user by ID
- `POST /users/:id` - Create new user
- `PUT /users/:id` - Update existing user
- `DELETE /users/:id` - Delete user
- `OPTIONS /users/:id` - Check if user exists

### WebSocket Endpoints
- `WS /ws/prices` - Real-time price updates WebSocket

### Admin Endpoints
- `GET /admin` - Admin panel (HTML)
- `POST /admin/update-price/:id` - Update item price
  - Body param: `price`
- `POST /admin/auto-updates/toggle` - Toggle automatic price updates
- `GET /admin/ws-stats` - WebSocket connection statistics

**Total Endpoints**: 18 (4 new in Sprint 3)

---

## New Features (Sprint 3)

### 1. Price Filtering
Filter items by price range using query parameters:

```bash
# Filter items between $400 and $700
curl "http://localhost:4567/items?minPrice=400&maxPrice=700"

# Filter items above $500
curl "http://localhost:4567/items?minPrice=500"

# Filter items below $600
curl "http://localhost:4567/items?maxPrice=600"
```

**Web Interface**: Use the filtering form on `/items/view`

### 2. WebSocket Real-Time Updates
Connect to the WebSocket endpoint to receive live price updates:

```javascript
const ws = new WebSocket('ws://localhost:4567/ws/prices');

ws.onmessage = (event) => {
  const data = JSON.parse(event.data);
  if (data.type === 'priceUpdate') {
    console.log(`Price updated for ${data.itemId}: ${data.oldPrice} → ${data.newPrice}`);
  }
};
```

### 3. Admin Panel
Access the admin panel at `/admin` to:
- Manually update item prices
- Toggle automatic price simulation
- View WebSocket connection statistics

### 4. Improved Data Model
**Breaking Change**: Price field changed from `String` to `double`

**Before (Sprint 2)**:
```json
{
  "id": "item1",
  "name": "Vintage Comic",
  "price": "$621.34 USD"
}
```

**Now (Sprint 3)**:
```json
{
  "id": "item1",
  "name": "Vintage Comic",
  "price": 621.34
}
```

The `Item` class now includes a `getFormattedPrice()` method for display purposes.

---

## Project Structure

```
collectibles-store-spark/
├── src/
│   └── main/
│       ├── java/com/collectibles/
│       │   ├── config/
│       │   │   ├── ServerConfig.java         # Server configuration
│       │   │   └── WebSocketConfig.java      # WebSocket handler ⭐ NEW
│       │   │
│       │   ├── controller/
│       │   │   ├── ItemController.java       # Item routes (updated)
│       │   │   ├── OfferController.java      # Offer routes
│       │   │   ├── RouteConfig.java          # Route configuration (updated)
│       │   │   ├── TemplateController.java   # Template routes (updated)
│       │   │   └── UserController.java       # User routes
│       │   │
│       │   ├── exception/
│       │   │   ├── ExceptionHandler.java     # Global error handler
│       │   │   ├── NotFoundException.java    # 404 exception
│       │   │   └── ServerException.java      # Server error exception
│       │   │
│       │   ├── model/
│       │   │   ├── Item.java                 # Item entity (v2.0) ⭐ UPDATED
│       │   │   ├── Offer.java                # Offer entity
│       │   │   └── User.java                 # User entity
│       │   │
│       │   ├── service/
│       │   │   ├── ItemService.java          # Item logic (v2.0) ⭐ UPDATED
│       │   │   ├── OfferService.java         # Offer logic
│       │   │   ├── PriceUpdateService.java   # Price updates ⭐ NEW
│       │   │   └── UserService.java          # User logic
│       │   │
│       │   └── util/
│       │       ├── JsonUtil.java             # JSON helper
│       │       └── Main.java                 # App entry point
│       │
│       ├── resources/
│       │   ├── data/
│       │   │   └── items.json                # Sample data (updated format)
│       │   │
│       │   ├── public/
│       │   │   ├── css/
│       │   │   │   ├── styles-forms.css      # Form styles
│       │   │   │   └── styles.css            # Global styles
│       │   │   └── js/
│       │   │       ├── offer-form.js         # Offer form script
│       │   │       └── scripts.js            # Main JS
│       │   │
│       │   └── templates/
│       │       ├── admin-price-update.mustache  # Admin panel ⭐ NEW
│       │       ├── error.mustache            # Error page
│       │       ├── item-detail.mustache      # Item view
│       │       ├── items.mustache            # Items list (updated) ⭐
│       │       ├── offer-form.mustache       # Offer form
│       │       └── offers-list.mustache      # Offers list
│       │
│       └── logback.xml                       # Logging setup
│
├── test/                                     # Tests
├── target/                                   # Build output
├── docs/                                     # Documentation
├── postman/                                  # Postman collection
├── screenshots/                              # Testing screenshots
├── .gitignore
├── pom.xml                                   # Maven config
└── README.md                                 # Project info
```

---

## Testing

### Price Filtering Tests

```bash
# Get all items (no filter)
curl http://localhost:4567/items

# Filter: $400 - $700
curl "http://localhost:4567/items?minPrice=400&maxPrice=700"

# Filter: Above $500
curl "http://localhost:4567/items?minPrice=500"

# Filter: Below $600
curl "http://localhost:4567/items?maxPrice=600"

# Invalid range (should return error)
curl "http://localhost:4567/items?minPrice=700&maxPrice=400"
```

### WebSocket Tests

1. Open browser console on `/items/view`
2. Watch for connection status indicator (top-right)
3. Use admin panel to update a price
4. Observe real-time update on the page

### Admin Panel Tests

```bash
# Update item price
curl -X POST "http://localhost:4567/admin/update-price/item1?price=650.00"

# Toggle auto-updates
curl -X POST http://localhost:4567/admin/auto-updates/toggle

# Get WebSocket stats
curl http://localhost:4567/admin/ws-stats
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
- [WebSocket Guide](docs/WEBSOCKET.md) - Real-time updates documentation ⭐ NEW

---

## Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 8+ | Programming language |
| Spark Framework | 2.9.4 | Web framework |
| Maven | 3.6+ | Build tool & dependency management |
| Gson | 2.10.1 | JSON serialization/deserialization |
| Logback | 1.2.11 | Logging framework |
| WebSocket | Built-in | Real-time communication ⭐ NEW |
| Postman | Latest | API testing |

---

## Data Format Changes

### items.json Format (Sprint 3)

```json
[
  {
    "id": "item1",
    "name": "Cap autographed by Peso Pluma",
    "description": "Authentic autographed cap from Peso Pluma's 2023 tour",
    "price": 621.34
  },
  {
    "id": "item2",
    "name": "Helmet autographed by Rosalía",
    "description": "Limited edition helmet signed by Rosalía",
    "price": 734.57
  }
]
```

---

## Security Features

### Implemented
- Input validation (required fields, email format, role values)
- CORS configuration for web applications
- Security headers (X-Content-Type-Options, X-Frame-Options, X-XSS-Protection)
- Error handling with safe error messages
- UTF-8 encoding support
- WebSocket connection management

---

## Features Summary

### Sprint 1
- RESTful API architecture
- JSON request/response format
- Full CRUD for users
- Read operations for items
- In-memory data storage

### Sprint 2 
- Web templates with Mustache
- Visual items catalog
- Offer submission system
- User-friendly error pages

### Sprint 3 (Current)
- **Price range filtering**
- **WebSocket real-time updates**
- **Improved price data model**
- **Admin management panel**
- **Automatic price simulation**
- **Live connection status indicator**

---

## Troubleshooting

### WebSocket Connection Issues

**Problem**: WebSocket won't connect
```bash
# Check if port is open
lsof -ti:4567

# Restart server
mvn clean compile exec:java
```

**Problem**: Price updates not showing
- Verify WebSocket connection status (top-right indicator)
- Check browser console for errors
- Ensure JavaScript is enabled

### Price Filtering Issues

**Problem**: Filter not working
- Verify query parameters are numeric
- Check that `minPrice` ≤ `maxPrice`
- Ensure items.json has numeric prices

---

## API Response Status Codes

| Code | Status | Usage |
|------|--------|-------|
| 200 | OK | Successful GET, PUT, OPTIONS |
| 201 | Created | Successful POST |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Invalid input data or price range |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Duplicate resource |
| 500 | Internal Server Error | Server error |

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
- Sofia for technical guidance and Sprint 3 challenges

---

## Version History

### Version 1.0.0 (Sprint 1)
- Initial API implementation
- Items and Users endpoints
- CORS configuration
- Complete documentation

### Version 2.0.0 (Sprint 2)
- Templates with Mustache
- Exception handling
- Web forms and offers system

### Version 3.0.0 (Sprint 3) - Current 
- Price filtering functionality
- WebSocket real-time updates
- Improved data model (double prices)
- Admin panel for price management
- Enhanced user experience with live updates

---

## Project Timeline

- **Sprint 1**: 4 days (API Service Foundation) - COMPLETE
- **Sprint 2**: 3 days (Templates & Exceptions) - COMPLETE
- **Sprint 3**: 4 days (Filters & WebSockets) - COMPLETE
- **Final Submission**: 2 days (Integration & Presentation) - In Progress

---

## Project Status

**Sprint**: 3 of 3  
**Status**: COMPLETE  
**Current Version**: 3.0.0  
**Last Updated**: 11/06/2025  
**Next Phase**: Final Integration & Presentation

---

## 🔗 Quick Links

- [GitHub Repository](https://github.com/anacasx/collectibles-store-spark)
- [API Documentation](docs/API_DOCUMENTATION.md)
- [Quick Start Guide](docs/QUICK_START.md)
- [WebSocket Guide](docs/WEBSOCKET.md)  NEW
- [Postman Collection](postman/)
- [Full Documentation](docs/)