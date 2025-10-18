# dorce-backend

## Technologies

* **Spring Boot**
* **Database (ORM):** Hiberante for ORM and JPA for data access
* **PostgresSQL**


## Local Setup

1. ### Requirements
   * Installed Docker on your system.

2. ### Build and Run
    ``` 
   docker-compose up --build
   ```
   
3. ### Access the application

    The backend application will be accessible at:
    * ``http://localhost:8080/``

4. ### API Documentation

    The API documentation is available via Swagger
    * ``http://localhost:8080/swagger-ui/index.html``

## Development Workflow
**Pushing directly to the main is disabled**

1. ### Branching Strategy

   * **Separate Branch:** Work on a branch derived from main. 
   * **Pull Request:** Create a PR to merge into main. 
   * **One Approval:** The PR must be accepted by 1 repository member. 
   * **Push to Main:** Merge only after approval.
