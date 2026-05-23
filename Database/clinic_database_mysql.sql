-- =====================================================
-- CLINIC APPOINTMENT SYSTEM - DATABASE SCHEMA (MySQL)
-- =====================================================

-- Create database
CREATE DATABASE IF NOT EXISTS clinic_db;
USE clinic_db;

-- =====================================================
-- Table 1: users (for login authentication)
-- =====================================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'receptionist',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin account
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'admin'),
('receptionist', 'recep123', 'receptionist');

-- =====================================================
-- Table 2: patients
-- =====================================================
CREATE TABLE patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    address VARCHAR(200),
    date_of_birth DATE,
    medical_history TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample patient data
INSERT INTO patients (first_name, last_name, contact_number, email, address, date_of_birth, medical_history) VALUES 
('Juan', 'Dela Cruz', '09171234567', 'juan.delacruz@email.com', '123 Main St, Quezon City', '1990-05-15', 'Hypertension'),
('Maria', 'Santos', '09187654321', 'maria.santos@email.com', '456 Oak Ave, Manila', '1985-08-22', 'Diabetes Type 2'),
('Pedro', 'Reyes', '09191112233', 'pedro.reyes@email.com', '789 Pine Rd, Pasig', '1995-12-10', 'None');

-- =====================================================
-- Table 3: doctors
-- =====================================================
CREATE TABLE doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    schedule VARCHAR(100),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample doctor data
INSERT INTO doctors (first_name, last_name, specialization, contact_number, schedule) VALUES 
('Dr. Anna', 'Lopez', 'General Practitioner', '09201234567', 'Mon-Fri 9:00AM-5:00PM'),
('Dr. Carlos', 'Garcia', 'Cardiologist', '09207654321', 'Mon-Wed-Fri 10:00AM-4:00PM'),
('Dr. Elena', 'Mendoza', 'Pediatrician', '09209876543', 'Tue-Thu-Sat 8:00AM-3:00PM');

-- =====================================================
-- Table 4: appointments
-- =====================================================
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'scheduled',
    reason_for_visit TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE
);

-- Sample appointment data
INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, reason_for_visit) VALUES 
(1, 1, '2026-05-25', '10:00:00', 'scheduled', 'Regular checkup'),
(2, 2, '2026-05-25', '11:30:00', 'scheduled', 'Cardiac consultation'),
(3, 3, '2026-05-26', '09:00:00', 'scheduled', 'Child vaccination');

-- =====================================================
-- Useful Queries for Testing
-- =====================================================

-- View all appointments with patient and doctor details
SELECT 
    a.appointment_id,
    CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
    CONCAT(d.first_name, ' ', d.last_name) AS doctor_name,
    d.specialization,
    a.appointment_date,
    a.appointment_time,
    a.status,
    a.reason_for_visit
FROM appointments a
JOIN patients p ON a.patient_id = p.patient_id
JOIN doctors d ON a.doctor_id = d.doctor_id
ORDER BY a.appointment_date, a.appointment_time;

-- Count appointments per doctor
SELECT 
    CONCAT(d.first_name, ' ', d.last_name) AS doctor_name,
    COUNT(a.appointment_id) AS total_appointments
FROM doctors d
LEFT JOIN appointments a ON d.doctor_id = a.doctor_id
GROUP BY d.doctor_id;
