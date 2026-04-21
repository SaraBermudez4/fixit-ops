INSERT INTO users.users (dni, name, last_name, email, password, phone_number, role)
SELECT '0000000000', 'Admin', 'Fixit', 'admin@fixit.com',
       '$2a$10$3GNo4y25G55v/ggLutS2NOyRKmURS.HajmAwfmhnqDd1OpSep4/KW',
       '+573012171281', 'ADMIN'
    WHERE NOT EXISTS (
    SELECT 1 FROM users.users WHERE email = 'admin@fixit.com'
);