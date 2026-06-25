-- ==========================================================
-- Lead Management CRM - Database Scripts
-- ==========================================================

-- Create Database
CREATE DATABASE IF NOT EXISTS lead_crm_db;
USE lead_crm_db;

-- ==========================================================
-- Table: counselors
-- ==========================================================
CREATE TABLE IF NOT EXISTS counselors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    department VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================================
-- Table: leads
-- ==========================================================
CREATE TABLE IF NOT EXISTS leads (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    course VARCHAR(255),
    city VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'NEW',
    source VARCHAR(50),
    counselor_id BIGINT,
    next_follow_up DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_lead_counselor FOREIGN KEY (counselor_id) REFERENCES counselors(id) ON DELETE SET NULL
);

-- ==========================================================
-- Table: call_notes
-- ==========================================================
CREATE TABLE IF NOT EXISTS call_notes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lead_id BIGINT NOT NULL,
    counselor_id BIGINT,
    note TEXT NOT NULL,
    call_duration_minutes INT,
    called_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_note_lead FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE,
    CONSTRAINT fk_note_counselor FOREIGN KEY (counselor_id) REFERENCES counselors(id) ON DELETE SET NULL
);

-- ==========================================================
-- Table: lead_history
-- ==========================================================
CREATE TABLE IF NOT EXISTS lead_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lead_id BIGINT NOT NULL,
    changed_by VARCHAR(255),
    field_changed VARCHAR(100),
    old_value VARCHAR(255),
    new_value VARCHAR(255),
    remarks TEXT,
    changed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_history_lead FOREIGN KEY (lead_id) REFERENCES leads(id) ON DELETE CASCADE
);

-- ==========================================================
-- Sample Data
-- ==========================================================

-- Insert Counselors
INSERT INTO counselors (name, email, phone, department) VALUES
('Priya Sharma', 'priya.sharma@educrm.com', '9876543210', 'Engineering'),
('Rahul Verma', 'rahul.verma@educrm.com', '9876543211', 'Management'),
('Anjali Singh', 'anjali.singh@educrm.com', '9876543212', 'Design');

-- Insert Leads
INSERT INTO leads (name, email, phone, course, city, status, source, counselor_id) VALUES
('Arjun Kumar', 'arjun.k@gmail.com', '9000000001', 'B.Tech CSE', 'Bengaluru', 'NEW', 'WEBSITE', 1),
('Sneha Patel', 'sneha.p@gmail.com', '9000000002', 'MBA', 'Mumbai', 'CONTACTED', 'REFERRAL', 2),
('Vikram Nair', 'vikram.n@gmail.com', '9000000003', 'B.Des', 'Hyderabad', 'FOLLOW_UP', 'SOCIAL_MEDIA', 3),
('Meera Iyer', 'meera.i@gmail.com', '9000000004', 'MCA', 'Chennai', 'QUALIFIED', 'EMAIL_CAMPAIGN', 1),
('Rohan Das', 'rohan.d@gmail.com', '9000000005', 'B.Tech ECE', 'Pune', 'NEW', 'WALK_IN', NULL),
('Kavya Reddy', 'kavya.r@gmail.com', '9000000006', 'MBA', 'Bengaluru', 'CONVERTED', 'REFERRAL', 2),
('Aditya Joshi', 'aditya.j@gmail.com', '9000000007', 'B.Tech CSE', 'Delhi', 'LOST', 'WEBSITE', 1),
('Pooja Menon', 'pooja.m@gmail.com', '9000000008', 'B.Des', 'Kochi', 'NEW', 'SOCIAL_MEDIA', 3);

-- Insert Call Notes
INSERT INTO call_notes (lead_id, counselor_id, note, call_duration_minutes, called_at) VALUES
(1, 1, 'Called Arjun. Interested in B.Tech CSE. Will send brochure.', 10, NOW()),
(2, 2, 'Sneha is comparing MBA programs. Scheduled callback.', 15, NOW()),
(3, 3, 'Vikram wants info on placement stats. Follow up next week.', 8, NOW());
