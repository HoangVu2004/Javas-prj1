# Help Support Application

This is a simple Spring Boot application that provides a REST API for a help support system.

## Project Structure

The project is organized as follows:

- `src/main/java/AI_PRJ/WEBAPP`: Contains the main application source code.
  - `controller`: Contains the REST API controllers.
  - `model`: Contains the data models.
  - `repository`: Contains the database repositories.
  - `service`: Contains the business logic.
- `src/main/resources`: Contains the application configuration files.
- `src/test/java/AI_PRJ/WEBAPP`: Contains the unit and integration tests.

## How to Run

1. **Create the database:**
   - Make sure you have MySQL installed and running.
   - Run the `create_database.sql` script to create the `help_support_db` database and the `help` table.

2. **Configure the database connection:**
   - Open the `src/main/resources/application.properties` file.
   - Update the `spring.datasource.username` and `spring.datasource.password` properties with your MySQL credentials.

3. **Fix MySQL access issues (if any):**
   - If you encounter an "access denied" error, follow the instructions in the `mysql_access_fix_guide.txt` file.

4. **Build and run the application:**
   - Open a terminal in the project root directory.
   - Run the following command to build and run the application:
     ```
     mvn spring-boot:run
     ```

The application will start on port 8080 by default.