# 🔗 URL Shortener (Spring Boot + Redis + MySQL)

A scalable backend system for generating and managing shortened URLs, <br>
designed using production-grade patterns like caching, rate limiting, and batched writes.

---

## ⭐Features

- URL shortening with Base62 encoding + ID obfuscation
- Redis cache (cache-aside, TTL = 24h)
- Click tracking using Redis `INCR` + batch sync to DB
- Expiry support for URLs with HTTP `410 Gone` handling
- Stats API (clicks, timestamps)
- Per-IP rate limiting (100 requests per minute)
- Scheduled cleanup of expired URLs

## ⭐Highlights

- High read performance using Redis caching
- Reduced DB load via batched click tracking
- Rate limiting to prevent abuse
- Dockerized and ready for scalable deployment


---

## ⚙Tech Stack

- Java 21, Spring Boot
- MySQL (primary datastore)
- Redis (cache + counters + rate limiting)
- Maven, Docker, Docker Compose

## ⚙Architecture
Client → Spring Boot App → Redis → MySQL
```
          ┌──────────────┐
          │    Client    │
          └──────┬───────┘
                 │
                 ▼
        ┌──────────────────┐
        │  Spring Boot App │
        └────────┬─────────┘
                 │
    ┌────────────┼──────────────┐
    │            │              │
    ▼            ▼              ▼
┌──────────┐ ┌──────────┐ ┌────────────┐
│ Redis    │ │ Rate     │ │ Click      │
│ Cache    │ │ Limiter  │ │ Counter    │
└────┬─────┘ └────┬─────┘ └────┬───────┘
     │            │            │
     └────────────┴────────────┘
                  │
                  ▼
            ┌──────────────┐
            │ MySQL        │  
            │ (Source of   │
            │ Truth)       │
            └──────────────┘
```
- Cache-aside pattern for reads
- Batched writes for click tracking
- Stateless service (horizontally scalable)

---

## 🚀 Running the Project

### Note:
Create a ".env" file under /url-shortener and add any random 10 or 12 digit number.<br>
This is used for shortened URL obfuscation.
#### Example:
.env:
```
URL_SHORTENER_SECRET=483920483209
```

### 🔹 Docker (Recommended)

```
git clone <repo-url>

cd url-shortener

mvn clean package -Dmaven.test.skip

docker-compose up --build
```
---

## 🌐API Contract
### Base Path:
```
http://localhost:8080/api
```
### Create Short URL
POST
```
/api/shorten?url=<original_url>
```
### Redirect
GET
```
/api/{shortCode}
```
→ Redirects to original URL
### Statistics
GET
```
/api/stats/{shortCode}
```
#### Response (example):
```json
{
  "originalUrl": "https://www.google.com",
  "shortCode": "8gJe5Mr",
  "createdAt": "2026-03-22T15:13:26.496925",
  "expiresAt": "2026-04-21T15:13:26.496937",
  "clickCount": 6
}
```

---

## 📝 TL;DR

This app takes a long website link and turns it into a short, easy-to-share link. <br>
When someone opens the short link, they are quickly redirected to the original site. <br>
It also keeps track of how many people clicked the link and prevents misuse by limiting too many requests. <br>
The system is built to be fast and handle lots of users at the same time.

---

## Author
#### [DarshanRaoG (Me!)](https://github.com/DarshanRaoG/) <br>
Built as part of backend preparation.
