-- V3__Insert_initial_data.sql
-- Initial data and default users
-- ===========================================

-- Insert default admin user
INSERT INTO users (id, email, password_hash, first_name, last_name, role, status, email_verified) 
VALUES (
    uuid_generate_v4(),
    'admin@eduplatform.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password: admin123
    'System',
    'Administrator',
    'SUPER_ADMIN',
    'ACTIVE',
    true
)