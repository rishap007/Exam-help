Perfect — thanks for sharing `CourseController`.
Here’s a **beginner-friendly, documentation-ready breakdown** you can directly use in your project documentation.

---

# 📘 CourseController Documentation

**Location:** `com.eduplatform.controller.CourseController`
**Purpose:**
This controller provides REST endpoints for **course management** in the LMS.
It allows fetching, creating, updating, publishing, uploading thumbnails, getting statistics, and deleting courses.
Security is enforced so that **only instructors or admins** can manage their own courses.

---

## 📂 Imports & Concepts

* **Spring MVC**

  * `@RestController` → Exposes endpoints returning JSON.
  * `@RequestMapping("/courses")` → All routes start with `/courses/...`.
  * `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` → Map HTTP verbs to controller methods.
  * `@RequestBody` → Maps JSON body into DTOs (e.g., `CreateCourseRequest`).
  * `@RequestParam` → Reads form-data / query params.
  * `@PathVariable` → Reads dynamic parts of URL.

* **Spring Data**

  * `Page<T>` & `Pageable` → Used for pagination (retrieving courses page by page).
  * `@PageableDefault` → Provides default pagination (20 items per page if none specified).

* **Spring Security**

  * `@PreAuthorize(...)` → Restricts endpoints to roles/conditions.

    * `"hasAnyRole('INSTRUCTOR', 'ADMIN')"` → Only instructors and admins can call.
    * `"@courseSecurityService.isOwnerOrAdmin(#courseId, authentication)"` → Custom check: only the course owner or admin can modify.
  * `Authentication` → Holds logged-in user’s details.
  * `UserPrincipal` → Custom security user object holding `UUID id`, email, etc.

* **Validation**

  * `@Valid` → Validates request DTOs (`CreateCourseRequest`, `UpdateCourseRequest`).
  * `@ValidFileExtension(...)` → Custom annotation validating uploaded files (e.g., only jpg/png).

* **Swagger / OpenAPI**

  * `@Operation`, `@Tag`, `@Parameter` → Automatically generates interactive API documentation.

* **Response Wrappers**

  * `ApiResponse<T>` → Standard API response wrapper (data + message + status).
  * `buildSuccessResponse(...)`, `buildCreatedResponse(...)`, `buildPageResponse(...)` → Helper methods from `BaseController`.

* **DTOs**

  * `CourseDto` → Represents a course in responses.
  * `CourseStatsDto` → Detailed course statistics.
  * `CreateCourseRequest`, `UpdateCourseRequest` → DTOs for creating/updating courses.

* **Other**

  * `MultipartFile` → Represents uploaded files.
  * `UUID` → Unique identifier for courses and instructors.

---

## 🏗️ Class Overview

```java
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Courses", description = "Course management endpoints")
public class CourseController extends BaseController {
    
    private final CourseService courseService;
    ...
}
```

* **Extends `BaseController`** → gives access to reusable response-building methods.
* **Depends on `CourseService`** → actual business logic lives here.
* **Tagged with "Courses"** → appears under a “Courses” section in Swagger docs.

---

## 🔑 Endpoints

### 1. **Get Published Courses**

```java
@GetMapping
public ResponseEntity<ApiResponse<Page<CourseDto>>> getPublishedCourses(
        @PageableDefault(size = 20) Pageable pageable)
```

* **Path:** `GET /courses`
* **Input:** `page`, `size` query params (default 20 per page).
* **Output:** Paginated list of `CourseDto`.
* **Use case:** Students browsing available courses.

---

### 2. **Get Course by Slug**

```java
@GetMapping("/slug/{slug}")
public ResponseEntity<ApiResponse<CourseDto>> getCourseBySlug(@PathVariable String slug)
```

* **Path:** `GET /courses/slug/{slug}`
* **Input:** Course slug (human-readable identifier).
* **Output:** `CourseDto`
* **Errors:** `ResourceNotFoundException` if slug not found.

---

### 3. **Create Course**

```java
@PostMapping
@PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
public ResponseEntity<ApiResponse<CourseDto>> createCourse(
        @Valid @RequestBody CreateCourseRequest request,
        Authentication authentication)
```

* **Path:** `POST /courses`
* **Input:** `CreateCourseRequest` JSON body (title, description, etc.).
* **Security:** Instructor/Admin only.
* **Uses:** Extracts `instructorId` from logged-in user.
* **Output:** Newly created `CourseDto`.
* **Status:** `201 CREATED`.

---

### 4. **Update Course**

```java
@PutMapping("/{courseId}")
@PreAuthorize("@courseSecurityService.isOwnerOrAdmin(#courseId, authentication)")
public ResponseEntity<ApiResponse<CourseDto>> updateCourse(
        @PathVariable UUID courseId,
        @Valid @RequestBody UpdateCourseRequest request,
        Authentication authentication)
```

* **Path:** `PUT /courses/{courseId}`
* **Input:** `UpdateCourseRequest` JSON body.
* **Security:** Only course owner or admin.
* **Output:** Updated `CourseDto`.

---

### 5. **Upload Thumbnail**

```java
@PostMapping("/{courseId}/thumbnail")
@PreAuthorize("@courseSecurityService.isOwnerOrAdmin(#courseId, authentication)")
public ResponseEntity<ApiResponse<CourseDto>> uploadThumbnail(
        @PathVariable UUID courseId,
        @RequestParam("file") @ValidFileExtension(extensions = {"jpg","jpeg","png"}) MultipartFile file,
        Authentication authentication)
```

* **Path:** `POST /courses/{courseId}/thumbnail`
* **Input:** Multipart file (jpg/jpeg/png, ≤ 5MB).
* **Security:** Course owner or admin.
* **Output:** `CourseDto` with updated thumbnail.

---

### 6. **Publish Course**

```java
@PostMapping("/{courseId}/publish")
@PreAuthorize("@courseSecurityService.isOwnerOrAdmin(#courseId, authentication)")
public ResponseEntity<ApiResponse<CourseDto>> publishCourse(
        @PathVariable UUID courseId,
        Authentication authentication)
```

* **Path:** `POST /courses/{courseId}/publish`
* **Input:** Course ID.
* **Security:** Owner/Admin only.
* **Output:** `CourseDto` with status changed to **published**.

---

### 7. **Get Course Statistics**

```java
@GetMapping("/{courseId}/stats")
@PreAuthorize("@courseSecurityService.isOwnerOrAdmin(#courseId, authentication)")
public ResponseEntity<ApiResponse<CourseStatsDto>> getCourseStatistics(@PathVariable UUID courseId)
```

* **Path:** `GET /courses/{courseId}/stats`
* **Output:** `CourseStatsDto` (enrollments, revenue, engagement).
* **Security:** Owner/Admin only.

---

### 8. **Delete Course**

```java
@DeleteMapping("/{courseId}")
@PreAuthorize("@courseSecurityService.isOwnerOrAdmin(#courseId, authentication)")
public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable UUID courseId)
```

* **Path:** `DELETE /courses/{courseId}`
* **Behavior:** Soft delete (marks inactive instead of permanent deletion).
* **Security:** Owner/Admin only.
* **Output:** `ApiResponse<Void>` (no body, success message).

---

## 📊 Example Requests / Responses

### Create Course

```http
POST /courses
Authorization: Bearer <jwt>
Content-Type: application/json

{
  "title": "Spring Boot for Beginners",
  "description": "Learn Spring Boot step by step",
  "category": "Programming"
}
```

Response:

```json
{
  "success": true,
  "message": "Course created successfully",
  "data": {
    "id": "1f3e56c2-5f9b-4c7a-92af-123abc...",
    "title": "Spring Boot for Beginners",
    "slug": "spring-boot-for-beginners",
    "status": "DRAFT",
    "instructorId": "42de1c77-4a9e-4891-9b77-123xyz..."
  }
}
```

---

## 🔍 Key Concepts for Beginners

* **Slug vs ID:**

  * `UUID` = machine identifier.
  * `slug` = human-friendly course identifier in URLs.

* **Role-based security:**

  * Instructors/Admins manage courses.
  * Students can only view published courses.

* **Soft delete:**

  * Instead of removing from DB, mark as deleted. Keeps history.

* **File validation:**

  * Only JPG/PNG thumbnails allowed. Prevents malicious file uploads.

* **Pagination:**

  * Helps avoid loading thousands of courses at once.

---

## ✅ Testing Ideas

1. `GET /courses` with no auth → returns published courses.
2. `POST /courses` as instructor → creates course.
3. `PUT /courses/{id}` as different instructor → forbidden (403).
4. `POST /courses/{id}/thumbnail` with `.exe` file → rejected (validation).
5. `GET /courses/{id}/stats` as course owner → returns stats.
6. `DELETE /courses/{id}` as admin → course marked deleted.

---

## 💡 Suggested Improvements

* Allow filtering courses by category, price, difficulty level.
* Add caching for `GET /courses` to improve performance.
* Support video uploads (with file validation).
* Standardize error response format (code + message).
* Add instructor metadata to course response (name, profile picture).

---

👉 That’s the **complete documentation for `CourseController`**.
Would you like me to also prepare docs for **`CourseService`** (since that’s where the real business rules live)?
