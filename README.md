# Task Management API

A modern task management REST API built with Spring Boot and enhanced with AI capabilities. This project demonstrates best practices in Java backend development while providing practical task management functionality.

## Features

- Complete CRUD operations for tasks
- Advanced filtering and search capabilities
- Task prioritization (1-3 levels)
- Pagination and sorting support
- Task completion statistics
- AI-powered task advice (using OpenAI)
- Cross-origin request support

## Tech Stack

- Java 23
- Spring Boot 3.5.0
- Spring AI 1.0.0
- Maven
- JUnit 5 for testing

## API Endpoints

### Task Management
- `GET /api/tasks` - List all tasks
- `GET /api/tasks/{id}` - Get task by ID
- `POST /api/tasks` - Create new task
- `PUT /api/tasks/update/{id}` - Update task
- `DELETE /api/tasks/delete/{id}` - Delete task
- `PATCH /api/tasks/done` - Update task completion status

### Advanced Features
- `GET /api/tasks/filter` - Filter tasks by name, priority, and completion status
- `GET /api/tasks/paginated` - Get paginated and sorted task list
- `GET /api/tasks/stats` - Get task completion statistics
- `GET /api/tasks/advice` - Get AI-generated advice for task completion

## Getting Started

### Prerequisites
- Java 23 or higher
- Maven
- OpenAI API key (for AI features)

### Installation

1. Clone the repository
bash git clone [repository-url]

2. Set up OpenAI API key
bash export SPRING_AI_OPENAI_API_KEY=your-api-key

3. Build the project
bash mvn clean install

4. Run the application
bash mvn spring-boot:run

## Testing

Run the test suite:
bash mvn test

## API Documentation

### Task Object Structure
json { "id": 1, "name": "Task name", "done": false, "priority": 1, "dueDate": "2025-01-01T12:00:00", "doneDate": null, "creationDate": "2024-01-01T10:00:00" }


### Task Priority Levels
- 1: High priority
- 2: Medium priority
- 3: Low priority

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Authors

- Tuda Alonzo Roberto Samuel
