INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role)
SELECT
    '0000000000',
    'Admin',
    'Fixit',
    'admin@fixit.com',
    '$2a$10$KIm8nHRrNszphpDLo80V6e7z9ufobmsreqR0aVWBtBMvvYC7e9Qza',
    '+573001234567',
    'ADMIN'
    WHERE NOT EXISTS (
    SELECT 1 FROM users.users WHERE email = 'admin@fixit.com'
);docker exec -it fixit-postgres psql -U fixit_user -d fixit_db