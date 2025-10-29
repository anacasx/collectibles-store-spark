# Validation Rules Documentation

## Overview
This document describes all validation rules applied to user data in the Collectibles Store API.

## User Validation Rules

### Field Requirements

| Field | Required | Type | Validation Rules |
|-------|----------|------|------------------|
| id | Yes | String | - Provided in URL parameter<br>- Cannot be empty<br>- Must be unique (for POST) |
| name | Yes | String | - Cannot be null or empty<br>- Whitespace trimmed |
| email | Yes | String | - Cannot be null or empty<br>- Must contain "@"<br>- Must contain "."<br>- Basic format validation |
| role | Yes | String | - Cannot be null or empty<br>- Must be one of: "admin", "buyer", "seller"<br>- Case-sensitive |

### POST /users/:id Validation Sequence

1. **URL Parameter Validation**
   - Check if ID is provided
   - Check if ID is not empty

2. **Request Body Validation**
   - Check if body is provided
   - Check if body is not empty
   - Check if JSON is parseable

3. **Field Presence Validation**
   - Check if name is provided
   - Check if email is provided
   - Check if role is provided

4. **Field Format Validation**
   - Validate email format
   - Validate role is allowed value

5. **Business Logic Validation**
   - Check if user ID already exists (409 Conflict)

### PUT /users/:id Validation Sequence

1. **URL Parameter Validation**
   - Check if ID is provided
   - Check if ID is not empty

2. **Existence Validation**
   - Check if user exists (404 Not Found)

3. **Request Body Validation**
   - Check if body is provided
   - Check if body is not empty
   - Check if JSON is parseable

4. **Field Presence Validation**
   - Check if name is provided
   - Check if email is provided
   - Check if role is provided

5. **Field Format Validation**
   - Validate email format
   - Validate role is allowed value

## Error Messages

### Standard Error Messages

| Validation Failure | Error Message | Status Code |
|-------------------|---------------|-------------|
| Missing URL ID | "User ID is required" | 400 |
| Empty request body | "Request body is required" | 400 |
| Invalid JSON | "Invalid user data" | 400 |
| Missing name | "User name is required" | 400 |
| Missing email | "User email is required" | 400 |
| Invalid email | "Invalid email format" | 400 |
| Missing role | "User role is required" | 400 |
| Invalid role | "Invalid role. Must be: admin, buyer, or seller" | 400 |
| Duplicate user (POST) | "User with ID {id} already exists" | 409 |
| User not found (PUT) | "User not found with ID: {id}" | 404 |

## Email Validation

### Current Implementation
Basic email validation checks:
- Contains "@" symbol
- Contains "." (dot)
- Not null or empty

### Example Valid Emails
- user@example.com
- john.doe@company.co.uk
- test_user+tag@domain.org

### Example Invalid Emails
- invalidemail (no @)
- user@domain (no dot)
- @domain.com (no local part)
- user@.com (no domain)

### Future Enhancement
Consider implementing RFC 5322 compliant email validation for production.

## Role Validation

### Allowed Roles
- **admin**: System administrator
- **buyer**: Can browse and purchase items
- **seller**: Can list and sell items

### Case Sensitivity
Roles are case-sensitive. "Admin", "ADMIN", and "admin" are NOT the same.

## ID Enforcement

### POST Behavior
- ID in URL parameter is authoritative
- Any ID in request body is ignored and overwritten
- Example: POST /users/user123 with body {"id": "user999", ...} → Creates user123

### PUT Behavior
- ID cannot be changed
- ID from URL is used to identify user
- ID in request body is overwritten with URL ID

## Validation Best Practices

1. **Validate Early**: Check parameters before calling service layer
2. **Clear Messages**: Provide specific error messages
3. **Appropriate Status Codes**: Use correct HTTP codes
4. **Security**: Don't expose internal error details
5. **Consistency**: Use same validation across all endpoints

## Testing Validation

Each validation rule should be tested with:
1. Valid data (should succeed)
2. Missing field (should fail with 400)
3. Invalid format (should fail with 400)
4. Edge cases (empty strings, special characters)
