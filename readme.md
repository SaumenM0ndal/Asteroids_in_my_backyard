# 🌌 Asteroids in my Backyard

A distributed Spring Boot microservices application that tracks potentially hazardous asteroids using NASA's Near Earth Object Web Service (NeoWs) and sends timely email alerts to subscribed users.

&#x20; &#x20;

## 📋 Table of Contents

- [📝 Description](#-description)
- [🏗️ System Architecture](#-system-architecture)
- [✨ Features](#-features)
- [🛠️ Technologies Used](#-technologies-used)
- [🚀 Installation & Setup](#-installation--setup)
  - [Prerequisites](#prerequisites)
  - [Setting Up the Infrastructure](#setting-up-the-infrastructure)
  - [Building the Microservices](#building-the-microservices)
  - [Configuration](#configuration)
- [💻 Usage](#-usage)
  - [Running the Services](#running-the-services)
  - [Accessing the System](#accessing-the-system)
- [⚙️ Configuration](#-configuration)
  - [Alerting Service Configuration](#alerting-service-configuration)
  - [Notification Service Configuration](#notification-service-configuration)
- [📚 API Reference](#-api-reference)
  - [NASA NeoWs API](#nasa-neows-api)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)
- [📞 Contact](#-contact)
- [🙏 Acknowledgements](#-acknowledgements)

## 📝 Description

"Asteroids in my Backyard" is a real-time asteroid threat detection and notification system. It periodically fetches data from NASA's NeoWs API, identifies hazardous near-Earth objects based on size and proximity, and notifies registered users via email.

## 🏗️ System Architecture

The system comprises two Spring Boot microservices:

1. **asteroidalerting**: Fetches NEO data from NASA, filters dangerous asteroids, and publishes Kafka events.
2. **notificationservice**: Consumes Kafka events, persists them in MySQL, and sends email alerts.

## ✨ Features

- ⏱️ Scheduled fetching of asteroid data from NASA
- ⚡ Event-driven architecture using Kafka
- 🔒 Criteria-based filtering of hazardous asteroids
- 📧 Automatic email notifications to subscribed users
- 📊 MySQL-based persistence of events
- 🚚 Docker Compose support for local infrastructure
- 🔍 Logging for event traceability

## 🛠️ Technologies Used

- **Java 21**
- **Spring Boot 3.5.3**
- **Apache Kafka 7.5.0**
- **MySQL 8.3**
- **Spring Kafka / JPA / Mail**
- **Maven**
- **Docker & Docker Compose**

## 🚀 Installation & Setup

### Prerequisites

- Java 21
- Maven 3.8+
- Docker + Docker Compose
- NASA API Key ([Get yours here](https://api.nasa.gov/))

### Setting Up the Infrastructure

1. Clone the repo:

   ```bash
   git clone https://github.com/your-username/Asteroids-in-my-backyard.git
   cd Asteroids-in-my-backyard
   ```

2. Start infrastructure services (MySQL, Kafka, Zookeeper):

   ```bash
   docker compose up -d
   ```

3. Check containers are running:

   ```bash
   docker compose ps
   ```

### Building the Microservices

```bash
cd asteroidalerting
mvn clean install
cd ../notificationservice
mvn clean install
```

### Configuration

- Add your NASA API key and Kafka URL in `asteroidalerting/src/main/resources/application.properties`
- Add your SMTP details in `notificationservice/src/main/resources/application.properties`

## 💻 Usage

### Running the Services

```bash
cd asteroidalerting
mvn spring-boot:run
# In another terminal
cd notificationservice
mvn spring-boot:run
```

### Accessing the System

- **Kafka UI**: `http://localhost:8084`
- **MySQL**: `localhost:3306` (user: `root`, password: `password`)

## ⚙️ Configuration

### Alerting Service Configuration (`application.properties`)

```properties
nasa.neo.api.url=https://api.nasa.gov/neo/rest/v1/feed
nasa.api.key=YOUR_API_KEY
spring.kafka.bootstrap-servers=localhost:9092
```

### Notification Service Configuration (`application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/asteroidalerting
spring.datasource.username=root
spring.datasource.password=password
spring.kafka.consumer.group-id=asteroid-alert
spring.mail.username=your_mailtrap_username
spring.mail.password=your_mailtrap_password
email.service.from.email=your_email@example.com
```

## 📚 API Reference

### NASA NeoWs API

- **Endpoint**: `https://api.nasa.gov/neo/rest/v1/feed`
- **Parameters**:
  - `start_date`
  - `end_date`
  - `api_key`
- [Docs](https://api.nasa.gov/)

## 🤝 Contributing

Feel free to open issues or submit pull requests to enhance the project. Please follow standard commit and formatting guidelines.

## 📄 License

This project is licensed under the [MIT License](LICENSE).

## 📞 Contact

Saumen Mondal - [@SaumenM0ndal](https://github.com/SaumenM0ndal)

## 🙏 Acknowledgements

- [NASA API](https://api.nasa.gov/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Apache Kafka](https://kafka.apache.org/)

