# 🎓 Lead Management & Counseling CRM

A Spring Boot REST API-based CRM system designed to help education counselors manage prospective student leads efficiently.

---

## 📌 Tech Stack

| Layer        | Technology              |
|-------------|-------------------------|
| Backend     | Spring Boot 3.2, Java 17 |
| Database    | MySQL 8.x               |
| ORM         | Spring Data JPA / Hibernate |
| Build Tool  | Maven                   |
| Design Pattern | Observer Pattern     |
| API Docs    | Postman Collection      |

---

## 🏗️ Project Structure

```
lead-crm/
├── src/main/java/com/crm/lead/
│   ├── controller/          # REST Controllers
│   │   ├── LeadController
│   │   ├── CounselorController
│   │   ├── CallNoteController
│   │   ├── LeadHistoryController
│   │   └── DashboardController
│   ├── service/             # Business Logic
│   │   ├── LeadService
│   │   ├── CounselorService
│   │   ├── CallNoteService
│   │   └── DashboardService
│   ├── repository/          # JPA Repositories
│   ├── model/               # JPA Entities
│   ├── dto/                 # Request/Response DTOs
│   ├── enums/               # LeadStatus, LeadSource
│   ├── observer/            # Observer Pattern Implementation
│   │   ├── LeadObserver          (interface)
│   │   ├── LeadEventPublisher    (Subject)
│   │   ├── LeadHistoryObserver   (Concrete Observer)
│   │   └── LeadNotificationObserver (Concrete Observer)
│   └── exception/           # Global Exception Handling
├── database/
│   └── init.sql             # DB creation + sample data
└── pom.xml
```

---

## 🗄️ Database Design

### Tables

**counselors**
- id, name, email, phone, department, is_active, created_at

**leads**
- id, name, email, phone, course, city, status, source, counselor_id (FK), next_follow_up, created_at, updated_at

**call_notes**
- id, lead_id (FK), counselor_id (FK), note, call_duration_minutes, called_at, created_at

**lead_history**
- id, lead_id (FK), changed_by, field_changed, old_value, new_value, remarks, changed_at

---

## 🔁 Observer Design Pattern

The Observer pattern is used to **automatically react to lead events** without tight coupling.

```
LeadEventPublisher  ──────────►  LeadHistoryObserver
     (Subject)                    (saves history to DB)
         │
         └────────────────────►  LeadNotificationObserver
                                  (logs notifications)
```

**Events fired:**
- `LEAD_CREATED` — when a new lead is added
- `STATUS_CHANGED` — when lead status changes
- `ASSIGNED` — when lead is assigned to a counselor
- `FOLLOW_UP_SCHEDULED` — when follow-up time is set

---

## ⚙️ Setup & Run

### Prerequisites
- Java 17+
- MySQL 8.x
- Maven 3.8+

### Step 1: Create Database
```sql
CREATE DATABASE lead_crm_db;
```
Or run the full script:
```bash
mysql -u root -p < database/init.sql
```

### Step 2: Configure DB credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

### Step 3: Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

App starts at: `http://localhost:8080`

---

## 🔗 API Endpoints Summary

### Leads `/api/leads`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/leads` | Create a new lead |
| GET | `/api/leads` | Get all leads |
| GET | `/api/leads/{id}` | Get lead by ID |
| PUT | `/api/leads/{id}` | Update lead |
| DELETE | `/api/leads/{id}` | Delete lead |
| PATCH | `/api/leads/{id}/status` | Update lead status |
| PATCH | `/api/leads/{leadId}/assign/{counselorId}` | Assign lead to counselor |
| PATCH | `/api/leads/{id}/follow-up` | Schedule follow-up |
| GET | `/api/leads/search` | Search & filter leads |
| GET | `/api/leads/status/{status}` | Get leads by status |
| GET | `/api/leads/counselor/{id}` | Get leads by counselor |

### Counselors `/api/counselors`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/counselors` | Create counselor |
| GET | `/api/counselors` | Get all counselors |
| GET | `/api/counselors/active` | Get active counselors |
| GET | `/api/counselors/{id}` | Get counselor by ID |
| PUT | `/api/counselors/{id}` | Update counselor |
| PATCH | `/api/counselors/{id}/toggle-status` | Toggle active/inactive |

### Call Notes `/api/call-notes`
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/call-notes` | Add call note |
| GET | `/api/call-notes/lead/{leadId}` | Get notes for a lead |
| GET | `/api/call-notes/counselor/{id}` | Get notes by counselor |

### Lead History `/api/lead-history`
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/lead-history/lead/{leadId}` | Get history for a lead |

### Dashboard `/api/dashboard`
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/dashboard` | Get full dashboard stats |

---

## 📊 Lead Status Flow

```
NEW → CONTACTED → FOLLOW_UP → QUALIFIED → CONVERTED
                                        ↘ LOST
```

---

## 🔍 Search & Filter

`GET /api/leads/search?status=NEW&source=WEBSITE&city=Bengaluru&keyword=arjun`

Supported parameters: `status`, `source`, `counselorId`, `course`, `city`, `keyword`

---

## 🚀 Future Enhancements

- Reminder Scheduler (Spring @Scheduled) for follow-up alerts
- Lead Conversion Analytics with time-series data


---

## 👨‍💻 Author

**Hemanth**  
Final Product Development Assignment — Lead Management & Counseling CRM
