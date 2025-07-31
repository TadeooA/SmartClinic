# Smart Clinic Management System

A comprehensive medical clinic management system built with Spring Boot, featuring JWT authentication, MySQL database, and RESTful APIs.

## Features

- **Doctor Management**: Complete CRUD operations for doctors with specialty and availability tracking
- **Patient Management**: Patient registration and search capabilities
- **Appointment Booking**: Schedule and manage appointments with availability checking
- **Prescription Management**: Create and manage prescriptions with JWT token validation
- **JWT Authentication**: Secure token-based authentication system
- **MySQL Database**: Robust data persistence with JPA/Hibernate
- **Docker Support**: Full containerization with Docker Compose
- **CI/CD Pipeline**: GitHub Actions for automated testing and deployment

## Technology Stack

- **Backend**: Spring Boot 3.5.4, Java 17
- **Database**: MySQL 8.0
- **Security**: Spring Security, JWT
- **Build Tool**: Maven
- **Containerization**: Docker, Docker Compose
- **CI/CD**: GitHub Actions

## API Endpoints

### Doctor Endpoints

- `GET /api/doctors` - Get all doctors
- `GET /api/doctors/availability/{doctorId}?date=YYYY-MM-DD` - Get doctor availability
- `POST /api/doctors/validate` - Validate doctor credentials
- `GET /api/doctors/specialty/{specialty}` - Get doctors by specialty
- `POST /api/doctors` - Create new doctor

### Patient Endpoints

- `GET /api/patients/search?email=&phone=` - Search patient by email or phone
- `POST /api/patients` - Create new patient
- `GET /api/patients/search-by-name?name=` - Search patients by name

### Appointment Endpoints

- `POST /api/appointments` - Book new appointment
- `GET /api/appointments/doctor/{doctorId}/date/{date}` - Get appointments by doctor and date
- `GET /api/appointments/patient/search?email=&phone=` - Get appointments by patient credentials
- `PUT /api/appointments/{id}/status` - Update appointment status

### Prescription Endpoints

- `POST /api/prescriptions` - Create prescription (requires JWT token)
- `GET /api/prescriptions/doctor/{doctorId}` - Get prescriptions by doctor
- `GET /api/prescriptions/patient/{patientId}` - Get prescriptions by patient
- `DELETE /api/prescriptions/{id}` - Delete prescription

## Setup and Installation

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose (optional)
- MySQL 8.0 (if not using Docker)

### Running with Docker (Recommended)

1. Clone the repository
2. Navigate to the project directory
3. Run with Docker Compose:

```bash
docker-compose up -d
```

The application will be available at `http://localhost:8080`

### Running Locally

1. Set up MySQL database:
   - Create database `smart_clinic_db`
   - Update connection details in `application.yml`

2. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

## Database Schema

The system includes four main entities:

- **doctors**: Doctor information with specialties and availability
- **patients**: Patient details and contact information  
- **appointments**: Appointment scheduling with status tracking
- **prescriptions**: Medical prescriptions linked to doctors and patients

## Authentication

The system uses JWT tokens for secure API access:

1. Validate doctor credentials at `/api/doctors/validate`
2. Use returned JWT token in Authorization header: `Bearer <token>`
3. Token required for prescription creation

## Sample Data

The system includes sample data for testing:
- 3 sample doctors with different specialties
- 3 sample patients
- Sample appointments and prescriptions

## Configuration

Key configuration in `application.yml`:
- Database connection settings
- JWT secret and expiration
- Server port (default: 8080)

## Contributing

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## License

This project is licensed under the MIT License.