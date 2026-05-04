# 📚 Library Management System
**B.Tech Mini Project | Java Servlets + JSP + MySQL + Apache Tomcat**

---

## 🗂️ Project Structure

```
LibraryManagementSystem/
│
├── src/
│   ├── util/
│   │   └── DatabaseConnection.java    ← JDBC connection utility
│   ├── model/
│   │   ├── Book.java                  ← Book POJO
│   │   └── IssuedBook.java            ← IssuedBook POJO
│   ├── dao/
│   │   ├── AdminDAO.java              ← Admin authentication DAO
│   │   ├── BookDAO.java               ← Book CRUD + search DAO
│   │   └── IssuedBookDAO.java         ← Issue/return DAO (with DB transactions)
│   └── servlet/
│       ├── LoginServlet.java          ← Admin login
│       ├── LogoutServlet.java         ← Session logout
│       ├── DashboardServlet.java      ← Dashboard stats
│       ├── AddBookServlet.java        ← Add new book
│       ├── ViewBooksServlet.java      ← View all books
│       ├── UpdateBookServlet.java     ← Edit book
│       ├── DeleteBookServlet.java     ← Delete book (with safety check)
│       ├── IssueBookServlet.java      ← Issue book to student
│       ├── ReturnBookServlet.java     ← Return issued book
│       └── SearchBookServlet.java     ← Search by title/author
│
├── WebContent/
│   ├── css/
│   │   └── style.css                 ← Main stylesheet (dark sidebar design)
│   ├── jsp/
│   │   ├── nav.jsp                   ← Reusable sidebar navigation
│   │   ├── dashboard.jsp             ← Stat cards + quick actions
│   │   ├── addBook.jsp               ← Add book form
│   │   ├── viewBooks.jsp             ← Books table with edit/delete
│   │   ├── editBook.jsp              ← Pre-filled edit form
│   │   ├── issueBook.jsp             ← Issue form with available books
│   │   ├── returnBook.jsp            ← Active issues + return action
│   │   └── searchBook.jsp            ← Search bar + results
│   ├── WEB-INF/
│   │   ├── web.xml                   ← Servlet mappings
│   │   └── lib/                      ← Place mysql-connector-java.jar here
│   ├── index.jsp                     ← Auto-redirect (login / dashboard)
│   └── login.jsp                     ← Login page
│
└── library_management.sql            ← Complete database setup script
```

---

## ⚙️ Setup Instructions

### Step 1 — Software Required

| Software | Version | Download |
|---|---|---|
| JDK | 8 or higher | [oracle.com](https://www.oracle.com/java/technologies/downloads/) |
| Apache Tomcat | 9.x | [tomcat.apache.org](https://tomcat.apache.org/download-90.cgi) |
| MySQL Server | 5.7 or 8.x | [mysql.com](https://dev.mysql.com/downloads/) |
| MySQL Connector/J | 8.x | [mysql.com/products/connector](https://dev.mysql.com/downloads/connector/j/) |
| Eclipse IDE | 2021+ (Eclipse IDE for Enterprise Java) | [eclipse.org](https://www.eclipse.org/downloads/) |

---

### Step 2 — Database Setup

1. Open **MySQL Workbench** or **MySQL Command Line**
2. Run the SQL script:
   ```bash
   mysql -u root -p < library_management.sql
   ```
   OR open the file in MySQL Workbench → Run All

3. This will create:
   - Database: `library_management`
   - Tables: `admins`, `books`, `issued_books`
   - Default admin: **username = admin** | **password = admin123**
   - 10 sample books

---

### Step 3 — Update Database Password

Open `src/util/DatabaseConnection.java` and update your MySQL password:

```java
private static final String PASSWORD = "root"; // Change this!
```

---

### Step 4 — Add MySQL Connector JAR

1. Download `mysql-connector-java-8.x.x.jar` from MySQL website
2. Copy it to: `WebContent/WEB-INF/lib/mysql-connector-java.jar`
3. This is required for the JDBC connection to work

---

### Step 5 — Import in Eclipse

1. Open Eclipse → **File → Import → Existing Projects into Workspace**
2. Select the `LibraryManagementSystem` folder
3. **Right-click project → Build Path → Configure Build Path**
4. Add the mysql-connector JAR to the build path
5. **Right-click project → Properties → Project Facets**
   - Check "Dynamic Web Module" (version 3.1)
   - Check "Java" (version 1.8+)

---

### Step 6 — Configure Tomcat in Eclipse

1. **Window → Preferences → Server → Runtime Environments**
2. Click **Add** → Select **Apache Tomcat 9.0**
3. Browse to your Tomcat installation folder
4. Click Finish

---

### Step 7 — Deploy and Run

1. Right-click the project → **Run As → Run on Server**
2. Select your Tomcat 9 server
3. Click **Finish**
4. Browser will open at: `http://localhost:8080/LibraryManagementSystem/`
5. Login with: **admin / admin123**

---

## 🔒 Default Admin Credentials

| Field | Value |
|---|---|
| Username | `admin` |
| Password | `admin123` |

---

## 📋 Features Summary

### 🏠 Dashboard
- Shows total book titles, total copies, currently issued, and available copies
- 6 quick action buttons for all main features

### 📖 Book Management
- **View Books** — Table with live filter, availability badges (green/yellow/red)
- **Add Book** — Form with category dropdown and quantity
- **Edit Book** — Pre-filled form; auto-adjusts available copies when quantity changes
- **Delete Book** — Safety check: cannot delete if book is currently issued

### 📤 Issue Book
- Dropdown shows only books with available copies
- Records student ID, name, book, and issue date
- Uses **database transaction** to prevent race conditions

### 📥 Return Book
- Lists all currently issued (not returned) books
- Inline date picker for return date (defaults to today)
- Confirm dialog before processing return

### 🔍 Search Books
- Search by title or author using keyword
- Shows availability status in results
- Quick-issue link from result rows

---

## 🏗️ Architecture Overview (MVC Pattern)

```
Browser (HTML/JSP) ← VIEW
      ↓
Servlet (Java)     ← CONTROLLER
      ↓
DAO (Java)         ← MODEL (Data Access)
      ↓
MySQL Database     ← DATA STORE
```

### Request Flow Example (Issue Book):
1. Student data entered in `issueBook.jsp` (View)
2. Form posts to `/issueBook` → `IssueBookServlet.doPost()` (Controller)
3. Servlet calls `IssuedBookDAO.issueBook()` (Model)
4. DAO runs a DB transaction: inserts record + decrements qty
5. Servlet redirects back to issueBook page with success/error message

---

## 🗄️ Database Schema

### `admins`
| Column | Type | Notes |
|---|---|---|
| id | INT | Primary Key, Auto Increment |
| username | VARCHAR(50) | Unique |
| password | VARCHAR(100) | |
| created_at | TIMESTAMP | Default: now |

### `books`
| Column | Type | Notes |
|---|---|---|
| book_id | INT | Primary Key, Auto Increment |
| title | VARCHAR(200) | |
| author | VARCHAR(100) | |
| category | VARCHAR(100) | |
| quantity | INT | Total copies owned |
| available_qty | INT | Currently available |
| added_on | TIMESTAMP | Default: now |

### `issued_books`
| Column | Type | Notes |
|---|---|---|
| issue_id | INT | Primary Key, Auto Increment |
| student_id | VARCHAR(50) | Roll number |
| student_name | VARCHAR(100) | |
| book_id | INT | FK → books.book_id |
| issue_date | DATE | |
| return_date | DATE | NULL until returned |
| status | ENUM | 'ISSUED' or 'RETURNED' |
| created_at | TIMESTAMP | Default: now |

---

## 🌐 URL Endpoints

| URL | Servlet | Purpose |
|---|---|---|
| `/` | index.jsp | Auto-redirect |
| `/login` | LoginServlet | GET: show form, POST: authenticate |
| `/logout` | LogoutServlet | Destroy session |
| `/dashboard` | DashboardServlet | Stats overview |
| `/viewBooks` | ViewBooksServlet | All books list |
| `/addBook` | AddBookServlet | GET: form, POST: save |
| `/updateBook` | UpdateBookServlet | GET: pre-fill, POST: update |
| `/deleteBook` | DeleteBookServlet | Delete by bookId |
| `/issueBook` | IssueBookServlet | GET: form, POST: issue |
| `/returnBook` | ReturnBookServlet | GET: list, POST: return |
| `/searchBook` | SearchBookServlet | Search by keyword |

---

## 🐛 Troubleshooting

| Problem | Solution |
|---|---|
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | Add mysql-connector JAR to WEB-INF/lib/ |
| `Access denied for user 'root'@'localhost'` | Update DB_PASSWORD in DatabaseConnection.java |
| `HTTP 404 Not Found` | Check servlet annotations match web.xml URL patterns |
| `HTTP 500 Internal Server Error` | Check Tomcat console for stack trace; usually a DB connection issue |
| Page shows blank / not rendering | Ensure Tomcat is Servlet 3.1 compatible (Tomcat 8.5+) |

---

*Library Management System — B.Tech Mini Project 2024*
