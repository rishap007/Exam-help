Perfect — thanks for pasting the full `AuthController`.

Here’s a **beginner-friendly, documentation-ready explanation** of every part of this class. I’ve structured it in Markdown so you can directly drop it into your docs repo.

---

# 📘 AuthController Documentation

**Location:** `com.eduplatform.controller.AuthController`
**Purpose:**
This controller exposes **all authentication and authorization REST endpoints** in the LMS (Learning Management System). It handles login, registration, password reset, email verification, token refresh, and retrieving the current user profile.

It acts as the **entry point for clients (frontend, Postman, mobile app)** that need authentication-related features. Business logic is delegated to the `AuthService`.

---

## 📂 Imports and Concepts

* **Spring MVC Annotations**

  * `@RestController` → Marks this class as a REST API controller (all methods return JSON).
  * `@RequestMapping("/auth")` → Base URL path for all endpoints (e.g., `/auth/login`, `/auth/register`).
  * `@GetMapping`, `@PostMapping` → Maps HTTP methods and paths to Java methods.
  * `@RequestBody` → Tells Spring to map incoming JSON body to a Java object (DTO).
  * `@RequestParam` → Reads query parameters from URL.

* **Spring Validation**

  * `@Validated` (at class level) → Enables validation.
  * `@Valid` (on request bodies) → Triggers validation of DTO fields (e.g., not null, email format).

* **Spring Security**

  * `SecurityContextHolder` → Stores authentication details of the currently logged-in user.
  * `Authentication` → Represents the authenticated principal (the user).

* **Spring Web Response**

  * `ResponseEntity<T>` → Allows returning both HTTP status codes and JSON bodies.
  * `HttpStatus.CREATED` → Special status (201) for newly created resources.

* **Logging**

  * `@Slf4j` (Lombok) → Provides a `log` object for easy logging (`log.info()`, `log.error()`).

* **Dependency Injection**

  * `@RequiredArgsConstructor` (Lombok) → Generates a constructor for required fields. Here, `authService` is injected automatically.

* **OpenAPI / Swagger**

  * `@Tag`, `@Operation`, `@ApiResponses`, `@Parameter` → Used to auto-generate Swagger API documentation.

* **DTOs**

  * `AuthRequest` → Defines request bodies for login, register, refresh, etc.
  * `AuthResponse` → Defines structured responses (success messages, JWT tokens, etc.).

---

## 🏗️ Class Overview

```java
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "Authentication and authorization operations")
public class AuthController {

    private final AuthService authService;
    ...
}
```

* This is the **Authentication Controller**.
* It depends on **AuthService** to perform the actual logic.
* Endpoints are prefixed with `/auth/...`.

---

## 🔑 Endpoints

### 1. **Login**

```java
@PostMapping("/login")
public ResponseEntity<AuthResponse.Login> login(@Valid @RequestBody AuthRequest.Login request)
```

* **Path:** `POST /auth/login`
* **Input:** `AuthRequest.Login` (email + password)
* **Output:** `AuthResponse.Login` (JWT token + user details)
* **Errors:** 401 (invalid credentials), 423 (account locked)
* **Logs:** `"Login attempt for user: <email>"`

---

### 2. **Register**

```java
@PostMapping("/register")
public ResponseEntity<AuthResponse.Register> register(@Valid @RequestBody AuthRequest.Register request)
```

* **Path:** `POST /auth/register`
* **Input:** `AuthRequest.Register` (email, password, name, etc.)
* **Output:** `AuthResponse.Register` (confirmation, user id)
* **Errors:** 400 (bad input), 409 (user already exists)
* **Status:** `201 CREATED`

---

### 3. **Refresh Token**

```java
@PostMapping("/refresh")
public ResponseEntity<AuthResponse.TokenRefresh> refreshToken(@Valid @RequestBody AuthRequest.RefreshToken request)
```

* **Path:** `POST /auth/refresh`
* **Input:** `AuthRequest.RefreshToken` (refresh token)
* **Output:** `AuthResponse.TokenRefresh` (new access token)
* **Errors:** 401 (invalid refresh token)

---

### 4. **Forgot Password**

```java
@PostMapping("/forgot-password")
public ResponseEntity<AuthResponse.PasswordReset> forgotPassword(@Valid @RequestBody AuthRequest.ForgotPassword request)
```

* **Path:** `POST /auth/forgot-password`
* **Input:** `AuthRequest.ForgotPassword` (email)
* **Output:** `AuthResponse.PasswordReset` (confirmation message)
* **Errors:** 404 (user not found)
* **Behavior:** Sends password reset email.

---

### 5. **Reset Password**

```java
@PostMapping("/reset-password")
public ResponseEntity<AuthResponse.Success> resetPassword(@Valid @RequestBody AuthRequest.ResetPassword request)
```

* **Path:** `POST /auth/reset-password`
* **Input:** `AuthRequest.ResetPassword` (reset token + new password)
* **Output:** `AuthResponse.Success` (success message)
* **Errors:** 400 (invalid/expired token)

---

### 6. **Verify Email**

```java
@GetMapping("/verify-email")
public ResponseEntity<AuthResponse.EmailVerification> verifyEmail(@RequestParam("token") String token)
```

* **Path:** `GET /auth/verify-email?token=<token>`
* **Input:** Token in query param
* **Output:** `AuthResponse.EmailVerification`
* **Errors:** 400 (invalid token)

---

### 7. **Change Password**

```java
@PostMapping("/change-password")
public ResponseEntity<AuthResponse.Success> changePassword(@Valid @RequestBody AuthRequest.ChangePassword request)
```

* **Path:** `POST /auth/change-password`
* **Input:** `AuthRequest.ChangePassword` (old password + new password)
* **Output:** `AuthResponse.Success`
* **Errors:** 400 (bad data), 401 (not logged in)
* **Special:** Uses `SecurityContextHolder` to get the current authenticated user.

---

### 8. **Get Current User Profile**

```java
@GetMapping("/me")
public ResponseEntity<AuthResponse.Profile> getCurrentUser()
```

* **Path:** `GET /auth/me`
* **Output:** `AuthResponse.Profile` (user profile)
* **Errors:** 401 (not logged in)

---

### 9. **Logout**

```java
@PostMapping("/logout")
public ResponseEntity<AuthResponse.Success> logout()
```

* **Path:** `POST /auth/logout`
* **Output:** `AuthResponse.Success`
* **Note:** Since JWT is **stateless**, logout is handled client-side. This endpoint is mostly for logging.

---

### 10. **Resend Verification Email**

```java
@PostMapping("/resend-verification")
public ResponseEntity<AuthResponse.Success> resendVerificationEmail(@Valid @RequestBody AuthRequest.ForgotPassword request)
```

* **Path:** `POST /auth/resend-verification`
* **Input:** Email
* **Output:** `AuthResponse.Success`
* **Note:** Currently reuses `forgotPassword()` logic. Could be refactored for clarity.

---

### 11. **Health Check**

```java
@GetMapping("/health")
public ResponseEntity<AuthResponse.Success> healthCheck()
```

* **Path:** `GET /auth/health`
* **Output:** `"Authentication service is running"`
* **Purpose:** Simple monitoring endpoint.

---

## 📊 Request / Response Examples

### Login Request

```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "mypassword"
}
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "email": "user@example.com",
    "role": "STUDENT"
  }
}
```

---

## 🔍 Key Concepts for Beginners

* **Controller → Service → Repository Pattern**

  * Controller: Handles HTTP requests and responses.
  * Service: Contains business logic (AuthService).
  * Repository: Talks to the database (likely `UserRepository`).

* **JWT (JSON Web Tokens):**

  * Used for stateless authentication.
  * Access tokens expire quickly, refresh tokens last longer.

* **Validation:**

  * Ensures bad data (empty email, weak password) is rejected automatically.

* **Swagger/OpenAPI:**

  * Auto-generates API docs (you can test endpoints in browser via Swagger UI).

---

## ✅ Testing Ideas (JUnit + MockMvc)

1. `POST /auth/login` with correct credentials → returns 200 + JWT.
2. `POST /auth/login` with wrong password → returns 401.
3. `POST /auth/register` with duplicate email → returns 409.
4. `GET /auth/me` without token → returns 401.
5. `GET /auth/health` → returns 200 with `"Authentication service is running"`.

---

## 💡 Suggestions / Improvements

* Separate resend verification from forgot password logic (currently reuses it).
* Implement rate limiting on login, forgot-password to prevent abuse.
* Return consistent error payloads (with error codes + messages).
* Add audit logging for sensitive actions (password reset, change password).

---

👉 That’s the **full breakdown of `AuthController`**.
Would you like me to do the **same style of docs** for the `AuthService` class next (since it holds the real business logic)?
