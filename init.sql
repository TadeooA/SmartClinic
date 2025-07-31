-- Initialize database with sample data
USE smart_clinic_db;

-- Insert sample doctors
INSERT INTO doctors (id, name, email, specialty, phone, available_times, created_at) VALUES
(1, 'Dr. John Smith', 'john.smith@clinic.com', 'Cardiology', '+1-555-0101', '09:00,10:00,11:00,14:00,15:00,16:00', NOW()),
(2, 'Dr. Sarah Johnson', 'sarah.johnson@clinic.com', 'Dermatology', '+1-555-0102', '08:00,09:00,10:00,13:00,14:00,15:00', NOW()),
(3, 'Dr. Michael Brown', 'michael.brown@clinic.com', 'Orthopedics', '+1-555-0103', '10:00,11:00,12:00,15:00,16:00,17:00', NOW());

-- Insert sample patients
INSERT INTO patients (id, name, email, phone, address, created_at) VALUES
(1, 'Alice Wilson', 'alice.wilson@email.com', '+1-555-0201', '123 Main St, Anytown, ST 12345', NOW()),
(2, 'Bob Davis', 'bob.davis@email.com', '+1-555-0202', '456 Oak Ave, Otherville, ST 67890', NOW()),
(3, 'Carol Martinez', 'carol.martinez@email.com', '+1-555-0203', '789 Pine Rd, Somewhere, ST 54321', NOW());

-- Insert sample appointments
INSERT INTO appointments (id, doctor_id, patient_id, appointment_time, status, notes, created_at) VALUES
(1, 1, 1, '2024-01-15 10:00:00', 'SCHEDULED', 'Regular checkup', NOW()),
(2, 2, 2, '2024-01-16 14:00:00', 'CONFIRMED', 'Skin consultation', NOW()),
(3, 3, 3, '2024-01-17 15:00:00', 'SCHEDULED', 'Knee pain evaluation', NOW());

-- Insert sample prescriptions
INSERT INTO prescriptions (id, doctor_id, patient_id, medication, dosage, instructions, created_at) VALUES
(1, 1, 1, 'Lisinopril', '10mg', 'Take once daily with food', NOW()),
(2, 2, 2, 'Hydrocortisone Cream', '1%', 'Apply to affected area twice daily', NOW()),
(3, 3, 3, 'Ibuprofen', '400mg', 'Take as needed for pain, maximum 3 times daily', NOW());