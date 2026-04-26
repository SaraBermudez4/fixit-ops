INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role)
SELECT
    '0000000000',
    'Admin',
    'Fixit',
    'admin@fixit.com',
    '$2b$10$lg7DsUUlaW6EbAWVpqYGlet2NV0KvMdZo93wwaWUQ57VF6u9691VG',
    '+573001234567',
    'ADMIN'
    WHERE NOT EXISTS (
    SELECT 1 FROM users.users WHERE email = 'admin@fixit.com'
);