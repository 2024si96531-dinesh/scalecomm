USE patient_db;

INSERT INTO patients (patient_id, external_reference, first_name, last_name, date_of_birth, gender, phone, email, status)
VALUES
    ('p-001', 'EXT-PAT-001', 'Ananya', 'Sharma', '1992-04-11', 'FEMALE', '+91-900000001', 'ananya.sharma@healthcare.local', 'ACTIVE'),
    ('p-002', 'EXT-PAT-002', 'Rahul', 'Verma', '1988-09-24', 'MALE', '+91-900000002', 'rahul.verma@healthcare.local', 'ACTIVE')
ON DUPLICATE KEY UPDATE
    updated_at = CURRENT_TIMESTAMP,
    status = VALUES(status);

USE appointment_db;

INSERT INTO appointments (appointment_id, patient_id, provider_reference, scheduled_at, status, reason)
VALUES
    ('a-001', 'p-001', 'DR-KAPOOR', DATE_ADD(NOW(), INTERVAL 1 DAY), 'BOOKED', 'General consultation'),
    ('a-002', 'p-002', 'DR-SEN', DATE_ADD(NOW(), INTERVAL 2 DAY), 'BOOKED', 'Follow-up visit')
ON DUPLICATE KEY UPDATE
    updated_at = CURRENT_TIMESTAMP,
    status = VALUES(status),
    reason = VALUES(reason);

USE health_record_db;

INSERT INTO health_records (record_id, patient_id, encounter_reference, record_type, summary, status)
VALUES
    ('r-001', 'p-001', 'ENC-001', 'CONSULTATION', 'Mild seasonal allergy. Prescribed antihistamine.', 'ACTIVE'),
    ('r-002', 'p-002', 'ENC-002', 'FOLLOW_UP', 'Blood pressure under observation.', 'ACTIVE')
ON DUPLICATE KEY UPDATE
    updated_at = CURRENT_TIMESTAMP,
    status = VALUES(status),
    summary = VALUES(summary);

USE billing_db;

INSERT INTO invoices (invoice_id, patient_id, appointment_id, amount, currency, status, issued_at, due_at)
VALUES
    ('inv-001', 'p-001', 'a-001', 1200.00, 'INR', 'ISSUED', NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY)),
    ('inv-002', 'p-002', 'a-002', 850.00, 'INR', 'ISSUED', NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY))
ON DUPLICATE KEY UPDATE
    updated_at = CURRENT_TIMESTAMP,
    status = VALUES(status),
    amount = VALUES(amount);

INSERT INTO payments (payment_id, invoice_id, amount, status, paid_at)
VALUES
    ('pay-001', 'inv-001', 1200.00, 'COMPLETED', NOW())
ON DUPLICATE KEY UPDATE
    updated_at = CURRENT_TIMESTAMP,
    status = VALUES(status),
    paid_at = VALUES(paid_at);

USE auth_db;

INSERT INTO users (user_id, username, password_hash, status)
VALUES
    ('u-admin-001', 'admin1', 'secret123', 'ACTIVE'),
    ('u-partner-001', 'partner1', 'secret123', 'ACTIVE'),
    ('u-patient-001', 'patient1', 'secret123', 'ACTIVE')
ON DUPLICATE KEY UPDATE
    updated_at = CURRENT_TIMESTAMP,
    password_hash = VALUES(password_hash),
    status = VALUES(status);

INSERT INTO roles (role_id, role_name)
VALUES
    ('role-admin', 'ADMIN'),
    ('role-doctor', 'DOCTOR'),
    ('role-partner', 'PARTNER'),
    ('role-patient', 'PATIENT')
ON DUPLICATE KEY UPDATE
    role_name = VALUES(role_name);

INSERT INTO user_roles (user_id, role_id)
VALUES
    ('u-admin-001', 'role-admin'),
    ('u-admin-001', 'role-doctor'),
    ('u-partner-001', 'role-partner'),
    ('u-patient-001', 'role-patient')
ON DUPLICATE KEY UPDATE
    role_id = VALUES(role_id);

USE pharmacy_db;

INSERT INTO prescriptions (prescription_id, patient_id, record_id, medication_name, dosage, status)
VALUES
    ('pr-001', 'p-001', 'r-001', 'Cetirizine', '10mg once daily', 'ACTIVE'),
    ('pr-002', 'p-002', 'r-002', 'Amlodipine', '5mg once daily', 'ACTIVE')
ON DUPLICATE KEY UPDATE
    updated_at = CURRENT_TIMESTAMP,
    status = VALUES(status),
    dosage = VALUES(dosage);

INSERT INTO dispensations (dispensation_id, prescription_id, quantity, status, dispensed_at)
VALUES
    ('disp-001', 'pr-001', 10, 'DISPENSED', NOW())
ON DUPLICATE KEY UPDATE
    updated_at = CURRENT_TIMESTAMP,
    status = VALUES(status),
    dispensed_at = VALUES(dispensed_at);

USE insurance_db;

INSERT INTO policies (policy_id, patient_id, provider_name, policy_number, status, effective_from, effective_to)
VALUES
    ('pol-001', 'p-001', 'GoodHealth Insurance', 'GH-1001', 'ACTIVE', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 YEAR)),
    ('pol-002', 'p-002', 'SecureLife Insurance', 'SL-2002', 'ACTIVE', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 YEAR))
ON DUPLICATE KEY UPDATE
    updated_at = CURRENT_TIMESTAMP,
    status = VALUES(status),
    effective_to = VALUES(effective_to);

INSERT INTO claims (claim_id, policy_id, invoice_id, status, claimed_amount, approved_amount, submitted_at, processed_at)
VALUES
    ('clm-001', 'pol-001', 'inv-001', 'APPROVED', 1200.00, 1000.00, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    updated_at = CURRENT_TIMESTAMP,
    status = VALUES(status),
    approved_amount = VALUES(approved_amount);

USE notification_db;

INSERT INTO notifications (notification_id, channel, recipient, subject, body, status, sent_at)
VALUES
    ('n-001', 'EMAIL', 'ananya.sharma@healthcare.local', 'Appointment Reminder', 'Your appointment is scheduled for tomorrow.', 'DELIVERED', NOW()),
    ('n-002', 'SMS', '+91-900000002', 'Invoice Issued', 'Your invoice inv-002 has been generated.', 'QUEUED', NULL)
ON DUPLICATE KEY UPDATE
    status = VALUES(status),
    sent_at = VALUES(sent_at);

INSERT INTO delivery_attempts (attempt_id, notification_id, attempt_number, status, attempted_at, response_message)
VALUES
    ('da-001', 'n-001', 1, 'SUCCESS', NOW(), 'Delivered by SMTP provider')
ON DUPLICATE KEY UPDATE
    status = VALUES(status),
    attempted_at = VALUES(attempted_at),
    response_message = VALUES(response_message);
