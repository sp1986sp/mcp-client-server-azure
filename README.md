# MCP Client-Server Azure Demo

This project demonstrates a modular, context-propagating, AI-powered client-server architecture using Spring Boot and Spring AI, with integration for Azure OpenAI models. It is organized as a Maven multi-module project with two main components:

- **client**: A Spring Boot application that acts as a chat client, forwarding user prompts to the server and handling AI responses.
- **server**: A Spring Boot application that exposes AI-powered tools (such as user management) and supports context propagation for distributed tracing and async operations.

---

## Project Structure

```
mcp-client-server-azure/
  ├── client/   # Chat client (Spring Boot, WebFlux)
  ├── server/   # AI tool server (Spring Boot, WebMVC)
  └── pom.xml   # Maven multi-module parent
```

---

## Features

### Client

- Exposes a `/chat` REST endpoint for sending user messages.
- Forwards requests to the server, propagating headers and context.
- Integrates with Azure OpenAI via Spring AI.
- Supports dynamic tool registration and context-aware responses.

### Server

- Exposes AI-powered tools as REST endpoints (e.g., user CRUD, search, time).
- Implements context propagation for headers, MDC, locale, and request attributes.
- Integrates with Spring AI's MCP (Model-Connected Platform) for tool orchestration.
- Provides endpoints for user management (get, add, update, delete, search).

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Azure OpenAI API key and endpoint

### Configuration

#### Client

Edit `client/src/main/resources/application.properties`:

```properties
spring.ai.azure.openai.api-key=<YOUR-KEY>
spring.ai.azure.openai.endpoint=<YOUR-URL>
spring.ai.azure.openai.chat.options.deployment-name=gpt-4o
```

#### Server

Edit `server/src/main/resources/application.properties` as needed (default port: 8081).

---

### Build & Run

From the project root:

```bash
# Build all modules
mvn clean install

# Start the server
cd server
./mvnw spring-boot:run

# In a new terminal, start the client
cd ../client
./mvnw spring-boot:run
```

---

## API Overview

### Client

- `POST /chat`
  - **Body:** Plain text user message
  - **Headers:** Any custom headers to propagate
  - **Response:** AI-generated response (tabular format if possible)

### Server

- Exposes tool endpoints for user management (see `UserService.java` for details).
- Handles context propagation for distributed and async operations.

---

## Dependencies

- Spring Boot 3.x
- Spring AI (MCP, Azure OpenAI)
- WebFlux (client), WebMVC (server)
- Lombok (server)
- JUnit (tests)

---

## Extending

- Add new tools to the server by annotating methods in service classes with `@Tool`.
- Register new tools in `ToolsConfig.java`.
- Customize context propagation by implementing new `ThreadLocalAccessor`s.

---

## License

This project is for demonstration purposes.
