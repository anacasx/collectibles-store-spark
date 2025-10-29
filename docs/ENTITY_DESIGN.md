# Entity Design Documentation

## Overview
This document describes the entity classes used in the Collectibles Store application.

## Item Entity

### Purpose
Represents a collectible item available for sale in the store.

### Fields
- **id** (String): Unique identifier for the item ("item1", "item2")
- **name** (String): Display name of the collectible ("Vintage Guitar")
- **description** (String): Detailed description of the item
- **price** (String): Price in USD format ("$1200.00 USD")

### Design Decisions
- **Price as String**: Stored as string to maintain formatting with currency symbols. Can be parsed later if calculations are needed.
- **ID Format**: Simple string format for easy JSON mapping from items.json
- **Immutable ID**: ID should not change once set (business rule)

### Example
```json
{
  "id": "item1",
  "name": "Cap Snoopy",
  "description": "A cap of Snoopy",
  "price": "$6.34 USD"
}
```

## User Entity

### Purpose
Represents a user (buyer, seller, or admin) in the system.

### Fields
- **id** (String): Unique identifier for the user
- **name** (String): Full name of the user
- **email** (String): Contact email address
- **role** (String): User role ("buyer", "seller", "admin")

### Design Decisions
- **Role-based**: Simple string-based role system, can be enhanced later with enum
- **Email as String**: No validation at entity level (validation will be in service layer)
- **Extensible**: Easy to add more fields (phone, address, etc.) in future sprints

### Example
```json
{
  "id": "user1",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "buyer"
}
```

## Common Patterns

### Encapsulation
All entities follow proper encapsulation with:
- Private fields
- Public getters and setters
- Both default and parameterized constructors

### Object Comparison
Both entities implement:
- **equals()**: Compares objects based on ID
- **hashCode()**: Generates hash based on ID
- **toString()**: Provides readable string representation

### Benefits
- Easy JSON serialization/deserialization with Gson
- Type safety and compile-time checking
- Clear separation of data structure from business logic
- Consistent design patterns across all entities

## Future Enhancements

Improvements for later sprints:
1. Add validation annotations
2. Implement builder pattern for complex object creation
3. Add timestamp fields (createdAt, updatedAt)
4. Convert role to enum type for type safety
5. Add price parsing utility for calculations
6. Implement Comparable interface for sorting
