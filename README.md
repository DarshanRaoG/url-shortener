# 🔗 URL Shortener (Spring Boot + Angular + Redis + MySQL)

A production-style full stack URL shortener that converts long URLs into short links,
with fast redirects, analytics, and scalable system design using caching, rate limiting, and batched writes.

## 📝 TL;DR

This app takes a long website link → turns it into a short, easy-to-share link. <br>
When someone opens the short link, they are quickly redirected to the original site. <br>
The system is built to be fast and handle lots of users at the same time.<br>
It also keeps track of how many people clicked the link and prevents misuse by limiting too many requests.

![Demo](https://github.com/DarshanRaoG/url-shortener/blob/main/URL%20Shortener.gif)

---

## ⭐Highlights

- Improved read performance using Redis caching
- Reduced DB load via batched click tracking
- Rate limiting to prevent abuse
- Dockerized and ready for scalable deployment

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

Clone the repo:
```
git clone <repo-url>
```
<br>

Switch to url-shortener directory:
```
cd url-shortener
```

<br>

Start docker compose for the first time (skip --build for later runs):
```
docker-compose up --build
```

<br>

Open this in your browser:
> http://localhost:4200

<br>

To stop, run:
```
docker-compose down
```

<br>

---



## ⚙Architecture
Browser → Nginx (UI Container) → /api → Spring Boot → Redis + MySQL

```
          ┌──────────────┐
          │    Nginx     │
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

## ⭐Features
### Backend
- URL shortening with Base62 encoding + ID obfuscation
- Redis cache (cache-aside, TTL = 24h)
- Click tracking using Redis `INCR` + batch sync to DB
- Expiry support for URLs with HTTP `410 Gone` handling
- Stats API (clicks, timestamps)
- Per-IP rate limiting (100 requests per minute)
- Scheduled cleanup of expired URLs

### Frontend
- URL shortening UI
- Analytics dashboard (stats page)
- Copy-to-clipboard support
- Toast notification system (no alerts)
- Input validation and normalization

---
## ⚙Tech Stack

- Docker, Docker Compose
### Backend
- Java 21, Spring Boot
- MySQL (primary datastore)
- Redis (cache + rate limiting + counters)
- Maven
### Frontend
- Angular
- Nginx (serving UI + API reverse proxy)

---

## Author
#### [DarshanRaoG (Me!)](https://github.com/DarshanRaoG/) <br>
Built as part of learning (and for fun).
