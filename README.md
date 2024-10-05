# Kirana Store Transaction Management

## Overview
The Kirana Store Transaction Management project is a backend service developed using Java, Spring Boot, and MongoDB. It aims to facilitate the management of transactions in Kirana stores, incorporating features such as user authentication, currency conversion, reporting, and caching.

## Features
- **User Authentication**: Role-based authentication system for users (owners and customers).
- **Transaction Management**: APIs for creating and managing transactions.
- **Reporting**: Monthly, weekly, and yearly transaction reports.
- **Currency Conversion**: Efficient currency conversion with caching to minimize lookup times.
- **Rate Limiting**: Controls the number of requests to prevent abuse.
- **Code Quality**: Follows SOLID principles and includes unit tests for reliability.

## Technologies Used
- **Java**: Backend programming language.
- **Spring Boot**: Framework for building production-ready applications.
- **MongoDB**: NoSQL database for storing transaction and user data.
- **JWT (JSON Web Tokens)**: For secure user authentication.
- **Maven**: Dependency management and build tool.

## Getting Started

### Prerequisites
- Java JDK 11 or higher
- Maven
- MongoDB
- Git

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/username/repo-name.git
    cd repo-name
    ```

2. **Install dependencies**:
    ```bash
    mvn install
    ```

3. **Set up MongoDB**:
   - Ensure MongoDB is running locally or configure the connection in `application.properties`.

4. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

### API Endpoints
- **Authenticate User**: `POST /api/authenticate`
    - Request Body:
        ```json
        {
            "email": "user@example.com",
            "password": "yourpassword"
        }
        ```
    - Response:
        ```json
        {
            "token": "your_jwt_token"
        }
        ```

- **Create Transaction**: `POST /api/transactions`
    - Request Body:
        ```json
        {
            "amount": 100,
            "currency": "INR",
            "type": "sale"
        }
        ```

- **Get Transaction Reports**: `GET /api/reports`
    - Query Parameters: `?period=monthly|weekly|yearly`
    - Response:
        ```json
        {
            "report": [
                {
                    "date": "YYYY-MM-DD",
                    "totalAmount": 1000,
                    "currency": "INR"
                },
                ...
            ]
        }
        ```

## Contributing
Contributions are welcome! Please fork the repository and create a pull request for any improvements or bug fixes.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## Contact
For any inquiries or issues, please contact [Sparsh Chaudhary](mailto:Sparsh.Chaudhary08@gmail.com).
