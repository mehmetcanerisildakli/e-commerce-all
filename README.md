# E-Commerce Project

This project is an e-commerce application developed for practice purposes. It follows a YouTube tutorial and aims to provide a basic understanding of building an e-commerce system using Java, Spring Boot, JPA, and Docker.

NOTE: I will update this file. It is not ready now

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgements](#acknowledgements)

## Features

- User registration and authentication
- Product listing and search
- Shopping cart management
- Order placement and tracking
- Inventory management

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Docker
- Maven
- Lombok

## Prerequisites

- JDK 17 or higher
- Maven
- Docker and Docker Compose

## Getting Started

1. **Clone the repository**:
    ```sh
    git clone https://github.com/your-username/e-commerce-project.git
    cd e-commerce-project
    ```

2. **Build the project**:
    ```sh
    mvn clean install
    ```

## Running the Application

### Using Docker Compose

1. **Create a Docker network**:
    ```sh
    docker network create my-network
    ```

2. **Run Docker Compose**:
    ```sh
    docker-compose up
    ```

### Without Docker

1. **Start MySQL**: Ensure MySQL is running and create databases `order-service` and `inventory-service`.

2. **Update application.properties**: Modify `application.properties` files in each service with your MySQL credentials.

3. **Run the services**:
    ```sh
    cd order-service
    mvn spring-boot:run
    ```

    ```sh
    cd inventory-service
    mvn spring-boot:run
    ```

## API Endpoints

### Order Service

- **POST /api/orders**: Create a new order.
    ```json
    {
        "orderNumber": "123456",
        "orderLineItemsDtoList": [
            {
                "id": 1,
                "skuCode": "ABC123",
                "price": 99.99,
                "quantity": 2
            }
        ]
    }
    ```

### Inventory Service

- **GET /api/inventory/{skuCode}**: Check if a product is in stock.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgements

- [YouTube Tutorial](https://www.youtube.com/your-tutorial-link)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Hibernate Documentation](https://hibernate.org/)
- [Docker Documentation](https://docs.docker.com/)

