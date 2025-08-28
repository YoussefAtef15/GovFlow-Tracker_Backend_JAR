-- V2__Add_Fee_To_Requests.sql

-- إضافة عمود جديد لتخزين رسوم الخدمة، يمكن أن يكون فارغًا في البداية
ALTER TABLE service_requests
    ADD COLUMN fee NUMERIC(10, 2);

-- إضافة تعليق لتوضيح الغرض من العمود
COMMENT ON COLUMN service_requests.fee IS 'The fee to be paid for this service request upon approval.';


        -- ########## Insert Services Data ##########

-- First, get the ID for the 'Traffic Department'. We assume it's 1.
INSERT INTO services (department_id, name, description) VALUES
                                                            (1, 'Driver License Issuance', 'Apply for a new driver''s license.'),
                                                            (1, 'License Renewal', 'Renew your existing driver''s license.'),
                                                            (1, 'License Replacement', 'Request a replacement for your lost or damaged driver''s license.'),
                                                            (1, 'Pay Traffic Violation', 'View and pay your outstanding traffic violations.'),
                                                            (1, 'Vehicle Registration', 'Register a new vehicle.');


-- ########## Insert Municipality Services Data ##########
-- Make sure to replace '2' with the actual ID of 'Local Municipality' if it's different.
-- You can run: SELECT id FROM departments WHERE name = 'Local Municipality'; to get the correct ID.

INSERT INTO services (department_id, name, description) VALUES
                                                            (2, 'Building Permit', 'Apply for a new building construction or renovation permit.'),
                                                            (2, 'Building Permit Renewal', 'Renew your existing building permit before it expires.'),
                                                            (2, 'Renovation Permit', 'Apply for a permit to renovate your property.'),
                                                            (2, 'Building Violation Complaint', 'Report a building violation to the municipality authorities.'),
                                                            (2, 'Cleanliness Complaint', 'Report cleanliness issues in public areas.');
