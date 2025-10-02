Perfect — thanks for sharing the `EnrollmentController`! 🙌
I’ll break this down in a **beginner-friendly, documentation-ready way** so that you and your teammates can paste it directly into your project docs and fully understand what’s going on.

---

# 📘 `EnrollmentController` Documentation

## ✅ Purpose

This controller manages **course enrollment** in your LMS.
It exposes REST API endpoints that allow:

* Students to **enroll** or **unenroll** in courses.
* Users to **list their enrollments** (active, completed, or all).
* Teachers/Admins to **view enrollment statistics** for their courses.
* Quick checks to see if a user is enrolled in a course.

It works together with:

* `EnrollmentService` (business logic)
* `UserPrincipal` (security & user identity)
* `BaseController` (shared helper methods for consistent API responses)

---

## 📦 Imports & Key Dependencies

* **Spring Boot / Spring MVC**

  * `@RestController`, `@RequestMapping`, `@GetMapping`, etc. → Define REST endpoints.
  * `ResponseEntity` → Standard HTTP responses.
* **Spring Security**

  * `@PreAuthorize` → Restrict access by role.
  * `Authentication` → Gets the logged-in user details.
* **Lombok**

  * `@RequiredArgsConstructor` → Injects `EnrollmentService` automatically.
  * `@Slf4j` → Provides `log.info(...)` for logging.
* **Swagger / OpenAPI**

  * `@Tag`, `@Operation` → Auto-generate API docs.
* **DTOs**

  * `EnrollmentDto`, `EnrollmentStatsDto` → Response data objects.
  * `ApiResponse<T>` → Consistent response wrapper.
* **Custom**

  * `EnrollmentService` → Handles enrollment logic.
  * `UserPrincipal` → Represents the authenticated user.

---

## 🏗️ Annotations on Class

```java
@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Enrollment", description = "Course enrollment management")
```

* `@RestController` → Marks this as a REST API controller.
* `@RequestMapping("/enrollments")` → All endpoints start with `/enrollments`.
* `@RequiredArgsConstructor` → Creates a constructor to inject `enrollmentService`.
* `@Slf4j` → Provides `log.info`, `log.error`, etc. (no need to create a Logger manually).
* `@Tag` → Adds grouping in Swagger docs.

---

## 📚 Endpoints

### 1. 🔎 Get all enrollments for logged-in user

```http
GET /enrollments
```

* **Access:** STUDENT, TEACHER, ADMIN
* **What it does:** Returns paginated list of enrollments for the logged-in user.
* **Implementation:**

  * Gets `userId` from `UserPrincipal`.
  * Calls `enrollmentService.findByStudent(userId, pageable)`.

---

### 2. 📝 Enroll in a course

```http
POST /enrollments/course/{courseId}
```

* **Access:** STUDENT only
* **What it does:** Enrolls the logged-in student in a course.
* **Implementation:**

  * Gets `courseId` from URL.
  * Extracts logged-in `userId`.
  * Calls `enrollmentService.enrollStudent(userId, courseId)`.

---

### 3. ❌ Unenroll from a course

```http
DELETE /enrollments/course/{courseId}
```

* **Access:** STUDENT or ADMIN
* **What it does:** Removes a student’s enrollment.
* **Implementation:**

  * Gets `courseId` and `userId`.
  * Calls `enrollmentService.unenrollStudent(userId, courseId)`.

---

### 4. 📂 Get active enrollments

```http
GET /enrollments/active
```

* **Access:** STUDENT, TEACHER, ADMIN
* **What it does:** Returns a list of **currently active** courses.
* **Implementation:** `enrollmentService.getActiveEnrollments(userId)`.

---

### 5. 🎓 Get completed enrollments

```http
GET /enrollments/completed
```

* **Access:** STUDENT, TEACHER, ADMIN
* **What it does:** Returns a list of **completed** courses.
* **Implementation:** `enrollmentService.getCompletedEnrollments(userId)`.

---

### 6. ✅ Check if user is enrolled

```http
GET /enrollments/course/{courseId}/check
```

* **Access:** STUDENT, TEACHER, ADMIN
* **What it does:** Returns `true/false` if the logged-in user is enrolled in the course.
* **Implementation:** `enrollmentService.isStudentEnrolled(userId, courseId)`.

---

### 7. 📊 Get enrollment statistics for a course

```http
GET /enrollments/course/{courseId}/stats
```

* **Access:** TEACHER, ADMIN
* **What it does:** Returns course statistics (like number of enrollments, completions, etc.).
* **Implementation:** `enrollmentService.getEnrollmentStatistics(courseId)`.

---

## 🔐 Security Summary

* **STUDENT** → Can enroll, unenroll, view own enrollments, check status.
* **TEACHER** → Can view enrollment lists/statistics (but not enroll/unenroll).
* **ADMIN** → Can do everything.

---

## 🌍 Example Requests

### Enroll in a course

```bash
POST /enrollments/course/123e4567-e89b-12d3-a456-426614174000
Authorization: Bearer <JWT_TOKEN>
```

**Response**

```json
{
  "success": true,
  "message": "Successfully enrolled in course",
  "data": {
    "id": "d5a12d...",
    "courseId": "123e4567-e89b-12d3-a456-426614174000",
    "userId": "abc123...",
    "status": "ACTIVE"
  }
}
```

---

## 🧑‍💻 Common Pitfalls

* **Missing JWT in request** → 401 Unauthorized.
* **Wrong role** → 403 Forbidden.
* **Duplicate enrollment** → Service should handle gracefully.
* **Unenroll non-existent course** → Likely throws `ResourceNotFoundException`.

---

## ✅ Improvements

* Add **rate-limiting** for enroll/unenroll to prevent abuse.
* Allow teachers to **enroll students manually** (bulk enrollments).
* Return additional **course metadata** with enrollments for convenience.

---

👉 Would you like me to also generate a **Markdown-formatted doc** for this controller (like `EnrollmentController.md`) so you can add it directly to your project’s `/docs` folder, just like we did for `CourseController`?
