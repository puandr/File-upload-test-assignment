

# File Upload Application, test assignment

This is a Spring Boot application for uploading and managing files with Java + Spring Boot. The application supports running in multiple instances with shared caching and provides secure access to most endpoints. It uses OpenAPI for API documentation.

## Features
- Upload files with information such as file name, file type, uploader, and upload time.
- Restricts uploading certain file types (e.g., .jpg, .gif, .png).
- Fetches metadata from a public API and stores it with each uploaded file.
- Caches the list of uploaded files to reduce database load and improve performance.
- Runs in multiple instances, sharing the same cache and database.
- Provides a `/ping` endpoint that is freely accessible without authentication.
- All other endpoints are secured and require authentication.

## Technologies Used
- **Java 21**: Programming language
- **Spring Boot**: Main framework
- **Spring Security**: Securing the API endpoints
- **Redis**: Caching solution
- **H2 Database**: In-memory database for local development
- **OpenAPI 3 (Swagger)**: API documentation and visualization
- **Maven**: Dependency management


## Setup Instructions
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/your-repository.git
   cd your-repository
   ```

2. **Configure Redis**:
   - Make sure Redis is installed and running on the default port (`6379`).

3. **Build the project**:
   ```bash
   mvn clean install
   ```

4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**:
   - The application will be available at `http://localhost:8080`.

6. **View API documentation (Swagger UI)**:
   - Access the Swagger UI at `http://localhost:8080/swagger-ui.html`.

## Running in Multiple Instances
To run the application in multiple instances:
1. **Start the first instance**:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8080
   ```

2. **Start the second instance**:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
   ```

Both instances will share the same cache and database.

## API Endpoints
- **`/ping`**: Returns a simple "pong" response. Accessible without authentication.
- **`/files/upload`**: Uploads a file. Requires authentication.
- **`/files/uploaded-by`**: Retrieves the list of files uploaded by a specific user. Requires authentication.

## Example Usage
1. **Upload a file**:
   ```bash
   curl -X POST "http://localhost:8080/files/upload" -F "file=@path/to/your/file.txt" -F "uploader=Andrei" -u user:user
   ```

2. **Get the list of files uploaded by a user**:
   ```bash
   curl -X GET "http://localhost:8080/files/uploaded-by?uploader=Andrei" -u user:user
   ```

## Security
- Basic authentication is used for securing endpoints.
- Only the `/ping` endpoint is accessible without authentication.

## Caching
- Redis is used to cache the list of files uploaded by users.
- The cache is shared across all running instances.

## Limitations
- The current implementation does not verify the maximum file size.
- Basic authentication is used without fine-grained authorization.
- Error handling is limited.
- User and password hardcoded in the SecurityConfig class
- etc

