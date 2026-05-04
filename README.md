# Library Management System

This repository contains the complete Library Management System project (Java Servlets, JSP, MySQL, Apache Tomcat).

Project files are located in the `LibraryManagementSystem/` folder. Open that folder to view the source, web content, and deployment scripts.

Quick start

- Import the database schema: `LibraryManagementSystem/library_management.sql` into a MySQL server (database: `library_management`).
- Update the database password in `LibraryManagementSystem/src/util/DatabaseConnection.java` (the `PASSWORD` constant).
- Start Tomcat using the included scripts from the repository root:

```powershell
.\run_tomcat_run.cmd    # run Tomcat in foreground (shows logs)
.\run_tomcat.cmd        # start Tomcat (background)
```

Then open the app at: `http://localhost:8080/LibraryManagementSystem/` and login.

Default admin credentials (seeded in `library_management.sql`):

- username: `admin`
- password: `admin123`

Notes and cleanup

- The repository currently includes built artifacts (`*.class`) and the `WEB-INF/lib` JARs. If you want a cleaner repo, I can add a `.gitignore` to exclude build artifacts and remove them from the history.
- To make the displayed admin name dynamic, update `LoginServlet.java` to set the session attribute from the DB or change the `admins` table username.

Repository structure (top-level):

- `LibraryManagementSystem/` — project source, web content, build/deploy scripts
- `run_tomcat.cmd`, `run_tomcat_run.cmd` — convenience scripts to start Tomcat

If you want, I can:

- Add a `.gitignore` and remove compiled classes and jars from the repo (recommended), or
- Move the project files to the repository root so the README displays in the root view.

Tell me which cleanup option you prefer and I will run it.
