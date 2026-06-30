# CodeCrest - Postman API Testing Guide

## Base URL
```
http://localhost:8080/api
```

## Authentication

### 1. Register a New User (Public)
**POST** `{{base}}/auth/register`

```json
{
  "username": "contestant1",
  "password": "password123",
  "email": "contestant1@example.com"
}
```
> Note: Registration auto-creates a ContestantProfile with ACTIVE status. Default role is CONTESTANT.

### 2. Login (Public)
**POST** `{{base}}/auth/login`

```json
{
  "username": "contestant1",
  "password": "password123"
}
```
> Response returns a JWT token. Copy this token for authenticated requests.

### 3. Using Authentication
For all subsequent requests, add header:
```
Authorization: Bearer <your-jwt-token>
```

---

## Challenges (Requires Auth)

### 4. Get All Challenges
**GET** `{{base}}/challenges`

### 5. Get Challenge by ID
**GET** `{{base}}/challenges/1`

### 6. Create Challenge (PROBLEM_SETTER or PLATFORM_ADMIN only)
**POST** `{{base}}/challenges`

```json
{
  "title": "Two Sum",
  "description": "Given an array of integers nums and an integer target, return indices of the two numbers that add up to target.",
  "difficulty": "EASY",
  "basePoints": 100,
  "timeLimitMs": 2000
}
```

### 7. Update Challenge (PROBLEM_SETTER or PLATFORM_ADMIN only)
**PUT** `{{base}}/challenges/1`

```json
{
  "title": "Two Sum Updated",
  "description": "Updated description",
  "difficulty": "MEDIUM",
  "basePoints": 200,
  "timeLimitMs": 1500
}
```

### 8. Delete Challenge (PLATFORM_ADMIN only)
**DELETE** `{{base}}/challenges/1`

---

## Contests (Requires Auth)

### 9. Get All Contests
**GET** `{{base}}/contests`

### 10. Get Contest by ID
**GET** `{{base}}/contests/1`

### 11. Create Contest (PLATFORM_ADMIN only)
**POST** `{{base}}/contests`

```json
{
  "title": "Weekly Contest 1",
  "startTime": "2026-07-10T09:00:00",
  "endTime": "2026-07-10T12:00:00",
  "capacity": 100
}
```

### 12. Update Contest (PLATFORM_ADMIN only)
**PUT** `{{base}}/contests/1`

```json
{
  "title": "Weekly Contest 1 Updated",
  "startTime": "2026-07-10T09:00:00",
  "endTime": "2026-07-10T12:00:00",
  "capacity": 150,
  "status": "UPCOMING"
}
```

### 13. Delete Contest (PLATFORM_ADMIN only)
**DELETE** `{{base}}/contests/1`

### 14. Enroll in Contest (CONTESTANT only)
**POST** `{{base}}/contests/1/enroll`
> Body not required. Uses JWT to identify the contestant.

---

## Submissions (Requires Auth)

### 15. Submit a Solution (CONTESTANT only)
**POST** `{{base}}/submissions/1/submit`

```json
{
  "sourceCode": "class Solution { public int[] twoSum(int[] nums, int target) { ... } }",
  "contestId": 1
}
```
> `contestId` is optional (omit for practice submissions outside a contest).

### 16. Get All Submissions
**GET** `{{base}}/submissions`

### 17. Get My Submissions (CONTESTANT only)
**GET** `{{base}}/submissions/my`

### 18. Delete Submission (PLATFORM_ADMIN only)
**DELETE** `{{base}}/submissions/1`

---

## Leaderboard (Requires Auth)

### 19. Get Global Leaderboard
**GET** `{{base}}/leaderboard/global`

### 20. Get Contest Leaderboard
**GET** `{{base}}/leaderboard/contest/1`

---

## Profiles (Requires Auth)

### 21. Get All Profiles (PLATFORM_ADMIN only)
**GET** `{{base}}/profiles`

### 22. Get My Profile
**GET** `{{base}}/profiles/me`

### 23. Update Profile
**PUT** `{{base}}/profiles/1`

```json
{
  "username": "new_username",
  "bio": "I love competitive programming!"
}
```

### 24. Delete Profile (PLATFORM_ADMIN only)
**DELETE** `{{base}}/profiles/1`

---

## Test Endpoint (Requires Auth)
**GET** `{{base}}/test`

---

## Sample Postman Collection Variables
| Variable | Initial Value |
|----------|---------------|
| `base`   | `http://localhost:8080/api` |
| `token`  | *(paste token after login)* |

## Pre-request Script for Token
If you set the `token` collection variable after login, add this to your collection's **Authorization** tab:
- Type: `Bearer Token`
- Token: `{{token}}`

## Roles Summary
| Role | Description |
|------|-------------|
| `CONTESTANT` | Can view challenges/contests, submit solutions, enroll in contests |
| `PROBLEM_SETTER` | Can create and update challenges |
| `PLATFORM_ADMIN` | Full access: manage users, challenges, contests, submissions |
