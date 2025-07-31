# Smart Clinic Management System - Database Schema Design

## 📋 Overview

This document describes the MySQL database schema design for the Smart Clinic Management System. The database is designed to support a comprehensive medical clinic management platform with patient management, doctor scheduling, appointment booking, and prescription tracking.

## 🏗️ Database Architecture

### Database Information
- **Database Name:** `smart_clinic_db`
- **Database Engine:** MySQL 8.0+
- **Character Set:** utf8mb4
- **Collation:** utf8mb4_unicode_ci

## 📊 Entity Relationship Diagram

```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│     DOCTORS     │       │  APPOINTMENTS   │       │    PATIENTS     │
├─────────────────┤       ├─────────────────┤       ├─────────────────┤
│ id (PK)         │◄──────┤ doctor_id (FK)  │──────►│ id (PK)         │
│ name            │       │ patient_id (FK) │       │ name            │
│ email           │       │ appointment_time│       │ email           │
│ specialty       │       │ status          │       │ phone           │
│ phone           │       │ notes           │       │ address         │
│ available_times │       │ created_at      │       │ created_at      │
│ created_at      │       └─────────────────┘       └─────────────────┘
└─────────────────┘                │                         │
        │                          │                         │
        │                          │                         │
        │         ┌─────────────────┴─────────────────┐       │
        │         │         PRESCRIPTIONS            │       │
        │         ├─────────────────┬─────────────────┤       │
        └─────────┤ doctor_id (FK)  │ patient_id (FK) │───────┘
                  │ medication      │ dosage          │
                  │ instructions    │ created_at      │
                  │ id (PK)         │                 │
                  └─────────────────┴─────────────────┘
```

## 📝 Table Definitions

### 1. DOCTORS Table

Stores information about medical doctors and their specialties.

```sql
CREATE TABLE doctors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    specialty VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    available_times TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_doctors_email (email),
    INDEX idx_doctors_specialty (specialty),
    INDEX idx_doctors_phone (phone)
);
```

**Columns:**
- `id`: Primary key, auto-incrementing integer
- `name`: Doctor's full name (required)
- `email`: Unique email address for authentication
- `specialty`: Medical specialty (Cardiología, Pediatría, etc.)
- `phone`: Contact phone number
- `available_times`: Working hours (e.g., "09:00-17:00")
- `created_at`: Record creation timestamp

**Sample Data:**
```sql
INSERT INTO doctors (name, email, specialty, phone, available_times) VALUES
('Dr. María González', 'maria.gonzalez@smartclinic.com', 'Cardiología', '+52-555-0101', '09:00-17:00'),
('Dr. Juan Pérez', 'juan.perez@smartclinic.com', 'Medicina General', '+52-555-0102', '08:00-16:00'),
('Dra. Ana López', 'ana.lopez@smartclinic.com', 'Pediatría', '+52-555-0103', '10:00-18:00');
```

### 2. PATIENTS Table

Stores patient information and contact details.

```sql
CREATE TABLE patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50) NOT NULL,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_patients_email (email),
    INDEX idx_patients_phone (phone),
    INDEX idx_patients_name (name)
);
```

**Columns:**
- `id`: Primary key, auto-incrementing integer
- `name`: Patient's full name (required)
- `email`: Unique email address for patient login
- `phone`: Contact phone number for authentication
- `address`: Patient's residential address (optional)
- `created_at`: Record creation timestamp

**Sample Data:**
```sql
INSERT INTO patients (name, email, phone, address) VALUES
('Pedro Martínez', 'pedro.martinez@email.com', '+52-555-1001', 'Av. Reforma 123, CDMX'),
('Laura Hernández', 'laura.hernandez@email.com', '+52-555-1002', 'Calle Juárez 456, Guadalajara'),
('Miguel Torres', 'miguel.torres@email.com', '+52-555-1003', 'Av. Universidad 789, Monterrey');
```

### 3. APPOINTMENTS Table

Manages medical appointments between doctors and patients.

```sql
CREATE TABLE appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    appointment_time TIMESTAMP NOT NULL,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED') DEFAULT 'SCHEDULED',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    
    INDEX idx_appointments_doctor (doctor_id),
    INDEX idx_appointments_patient (patient_id),
    INDEX idx_appointments_time (appointment_time),
    INDEX idx_appointments_status (status),
    INDEX idx_appointments_date (DATE(appointment_time))
);
```

**Columns:**
- `id`: Primary key, auto-incrementing integer
- `doctor_id`: Foreign key referencing doctors table
- `patient_id`: Foreign key referencing patients table
- `appointment_time`: Scheduled date and time for the appointment
- `status`: Appointment status (SCHEDULED, COMPLETED, CANCELLED)
- `notes`: Additional notes or observations
- `created_at`: Record creation timestamp

**Sample Data:**
```sql
INSERT INTO appointments (doctor_id, patient_id, appointment_time, status, notes) VALUES
(1, 1, '2025-08-01 10:00:00', 'SCHEDULED', 'Consulta de rutina cardiovascular'),
(2, 2, '2025-08-01 14:00:00', 'SCHEDULED', 'Revisión general'),
(3, 3, '2025-08-02 11:00:00', 'SCHEDULED', 'Control pediátrico');
```

### 4. PRESCRIPTIONS Table

Stores medical prescriptions issued by doctors to patients.

```sql
CREATE TABLE prescriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    medication VARCHAR(255) NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    instructions TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    
    INDEX idx_prescriptions_doctor (doctor_id),
    INDEX idx_prescriptions_patient (patient_id),
    INDEX idx_prescriptions_medication (medication),
    INDEX idx_prescriptions_date (DATE(created_at))
);
```

**Columns:**
- `id`: Primary key, auto-incrementing integer
- `doctor_id`: Foreign key referencing doctors table
- `patient_id`: Foreign key referencing patients table
- `medication`: Name of the prescribed medication
- `dosage`: Dosage information (e.g., "20mg", "500mg")
- `instructions`: Detailed instructions for taking the medication
- `created_at`: Record creation timestamp

**Sample Data:**
```sql
INSERT INTO prescriptions (doctor_id, patient_id, medication, dosage, instructions) VALUES
(1, 1, 'Atorvastatina', '20mg', 'Tomar 1 tableta diaria por la noche con alimentos'),
(2, 2, 'Paracetamol', '500mg', 'Tomar 1 tableta cada 8 horas según necesidad para dolor'),
(3, 3, 'Amoxicilina', '250mg', 'Tomar 1 cucharadita cada 8 horas por 7 días');
```

## 🔗 Relationships

### One-to-Many Relationships

1. **Doctor → Appointments**
   - One doctor can have multiple appointments
   - `doctors.id` → `appointments.doctor_id`

2. **Patient → Appointments**
   - One patient can have multiple appointments
   - `patients.id` → `appointments.patient_id`

3. **Doctor → Prescriptions**
   - One doctor can issue multiple prescriptions
   - `doctors.id` → `prescriptions.doctor_id`

4. **Patient → Prescriptions**
   - One patient can receive multiple prescriptions
   - `patients.id` → `prescriptions.patient_id`

### Many-to-Many Relationships (through Appointments)

- **Doctors ↔ Patients**: Many doctors can treat many patients through appointments

## 📈 Indexes and Performance

### Primary Indexes
- All tables have auto-incrementing `id` primary keys
- Unique indexes on `email` fields for doctors and patients

### Secondary Indexes
- **Doctors**: `specialty`, `email`, `phone`
- **Patients**: `email`, `phone`, `name`
- **Appointments**: `doctor_id`, `patient_id`, `appointment_time`, `status`, `date`
- **Prescriptions**: `doctor_id`, `patient_id`, `medication`, `date`

### Performance Considerations
- Indexes on foreign keys for faster joins
- Date-specific indexes for appointment and prescription queries
- Email indexes for authentication lookups
- Phone indexes for patient search functionality

## 🛡️ Data Integrity and Constraints

### Foreign Key Constraints
- **CASCADE DELETE**: When a doctor or patient is deleted, their related appointments and prescriptions are automatically removed
- **NOT NULL** constraints on essential fields
- **UNIQUE** constraints on email addresses

### Data Validation
- Email format validation at application level
- Phone number format validation
- Appointment time must be in the future
- Prescription dosage and instructions cannot be empty

## 🔍 Common Queries

### Find Doctor Availability
```sql
SELECT d.name, d.specialty, d.available_times 
FROM doctors d 
WHERE d.specialty = 'Cardiología' 
AND d.id NOT IN (
    SELECT doctor_id 
    FROM appointments 
    WHERE DATE(appointment_time) = '2025-08-01' 
    AND status = 'SCHEDULED'
);
```

### Patient Appointment History
```sql
SELECT 
    p.name as patient_name,
    d.name as doctor_name,
    d.specialty,
    a.appointment_time,
    a.status,
    a.notes
FROM appointments a
JOIN patients p ON a.patient_id = p.id
JOIN doctors d ON a.doctor_id = d.id
WHERE p.email = 'pedro.martinez@email.com'
ORDER BY a.appointment_time DESC;
```

### Doctor's Daily Schedule
```sql
SELECT 
    TIME(a.appointment_time) as time,
    p.name as patient_name,
    p.phone,
    a.notes
FROM appointments a
JOIN patients p ON a.patient_id = p.id
WHERE a.doctor_id = 1 
AND DATE(a.appointment_time) = '2025-08-01'
AND a.status = 'SCHEDULED'
ORDER BY a.appointment_time;
```

### Prescription History
```sql
SELECT 
    pr.medication,
    pr.dosage,
    pr.instructions,
    d.name as doctor_name,
    pr.created_at
FROM prescriptions pr
JOIN doctors d ON pr.doctor_id = d.id
WHERE pr.patient_id = 1
ORDER BY pr.created_at DESC;
```

## 🚀 Database Setup

### Complete Database Creation Script

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS smart_clinic_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE smart_clinic_db;

-- Create doctors table
CREATE TABLE doctors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    specialty VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    available_times TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_doctors_email (email),
    INDEX idx_doctors_specialty (specialty),
    INDEX idx_doctors_phone (phone)
);

-- Create patients table
CREATE TABLE patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50) NOT NULL,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_patients_email (email),
    INDEX idx_patients_phone (phone),
    INDEX idx_patients_name (name)
);

-- Create appointments table
CREATE TABLE appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    appointment_time TIMESTAMP NOT NULL,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED') DEFAULT 'SCHEDULED',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    
    INDEX idx_appointments_doctor (doctor_id),
    INDEX idx_appointments_patient (patient_id),
    INDEX idx_appointments_time (appointment_time),
    INDEX idx_appointments_status (status),
    INDEX idx_appointments_date (DATE(appointment_time))
);

-- Create prescriptions table
CREATE TABLE prescriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    medication VARCHAR(255) NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    instructions TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    
    INDEX idx_prescriptions_doctor (doctor_id),
    INDEX idx_prescriptions_patient (patient_id),
    INDEX idx_prescriptions_medication (medication),
    INDEX idx_prescriptions_date (DATE(created_at))
);

-- Insert sample data
INSERT INTO doctors (name, email, specialty, phone, available_times) VALUES
('Dr. María González', 'maria.gonzalez@smartclinic.com', 'Cardiología', '+52-555-0101', '09:00-17:00'),
('Dr. Juan Pérez', 'juan.perez@smartclinic.com', 'Medicina General', '+52-555-0102', '08:00-16:00'),
('Dra. Ana López', 'ana.lopez@smartclinic.com', 'Pediatría', '+52-555-0103', '10:00-18:00'),
('Dr. Carlos Ramírez', 'carlos.ramirez@smartclinic.com', 'Dermatología', '+52-555-0104', '09:00-15:00'),
('Dra. Sofia Morales', 'sofia.morales@smartclinic.com', 'Ginecología', '+52-555-0105', '08:30-16:30');

INSERT INTO patients (name, email, phone, address) VALUES
('Pedro Martínez', 'pedro.martinez@email.com', '+52-555-1001', 'Av. Reforma 123, CDMX'),
('Laura Hernández', 'laura.hernandez@email.com', '+52-555-1002', 'Calle Juárez 456, Guadalajara'),
('Miguel Torres', 'miguel.torres@email.com', '+52-555-1003', 'Av. Universidad 789, Monterrey'),
('Carmen Ruiz', 'carmen.ruiz@email.com', '+52-555-1004', 'Blvd. Constituyentes 321, Puebla'),
('Roberto Silva', 'roberto.silva@email.com', '+52-555-1005', 'Calle Madero 654, Tijuana');

INSERT INTO appointments (doctor_id, patient_id, appointment_time, status, notes) VALUES
(1, 1, '2025-08-01 10:00:00', 'SCHEDULED', 'Consulta de rutina cardiovascular'),
(2, 2, '2025-08-01 14:00:00', 'SCHEDULED', 'Revisión general'),
(3, 3, '2025-08-02 11:00:00', 'SCHEDULED', 'Control pediátrico'),
(4, 4, '2025-08-02 15:00:00', 'SCHEDULED', 'Consulta dermatológica'),
(5, 5, '2025-08-03 09:00:00', 'SCHEDULED', 'Revisión ginecológica');

INSERT INTO prescriptions (doctor_id, patient_id, medication, dosage, instructions) VALUES
(1, 1, 'Atorvastatina', '20mg', 'Tomar 1 tableta diaria por la noche con alimentos'),
(2, 2, 'Paracetamol', '500mg', 'Tomar 1 tableta cada 8 horas según necesidad para dolor'),
(3, 3, 'Amoxicilina', '250mg', 'Tomar 1 cucharadita cada 8 horas por 7 días'),
(4, 4, 'Hidrocortisona crema', '1%', 'Aplicar delgada capa en área afectada 2 veces al día'),
(5, 5, 'Ácido fólico', '5mg', 'Tomar 1 tableta diaria durante el embarazo');
```

## 📊 Database Statistics

- **Total Tables**: 4
- **Total Relationships**: 4 foreign key constraints
- **Total Indexes**: 15 (including primary keys)
- **Sample Records**: 25 total across all tables

## 🔄 Migration and Versioning

This schema supports Spring Boot JPA with Hibernate auto-DDL generation. The database structure is automatically created and maintained through the application configuration:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

## 📝 Notes

- All timestamps use the server's default timezone
- Email addresses are case-sensitive and must be unique
- Phone numbers support international format
- Appointment times are stored in 24-hour format
- The schema is designed to support future enhancements like patient medical history, billing, and insurance information

---

**Last Updated**: July 31, 2025  
**Version**: 1.0  
**Author**: Smart Clinic Development Team