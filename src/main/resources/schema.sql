DROP TABLE IF EXISTS status CASCADE;
DROP TABLE IF EXISTS type CASCADE;
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS employee_shift CASCADE;
DROP TABLE IF EXISTS dish CASCADE;
DROP TABLE IF EXISTS tips_qr CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS status (
status_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
status_name VARCHAR UNIQUE
);

CREATE TABLE IF NOT EXISTS type (
type_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
type_name VARCHAR UNIQUE
);

CREATE TABLE IF NOT EXISTS tips_qr (
tip_id LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
qr_code VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS employee (
employee_id LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
phone VARCHAR NOT NULL UNIQUE,
first_name VARCHAR NOT NULL,
surname VARCHAR NOT NULL,
user_password VARCHAR NOT NULL,
is_active BOOLEAN DEFAULT TRUE NOT NULL,
is_admin BOOLEAN DEFAULT FALSE NOT NULL,
tip_id BIGINT REFERENCES tips_qr (tip_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dish (
dish_id LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
title VARCHAR NOT NULL UNIQUE,
quantity INTEGER,
time_limit BIGINT NOT NULL,
price REAL,
status_id INTEGER REFERENCES status (status_id) ON DELETE CASCADE,
type_id INTEGER REFERENCES type (type_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
comment_id LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
dish_id LONG REFERENCES dish (dish_id) ON DELETE CASCADE,
comments VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
order_id LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
guests INTEGER NOT NULL,
creation_time DATE NOT NULL,
bill_time DATE,
total REAL NOT NULL,
employee_id BIGINT NOT NULL REFERENCES employee(employee_id) ON DELETE CASCADE,
dish_id LONG NOT NULL REFERENCES dish (dish_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS employee_shift (
employee_id LONG NOT NULL REFERENCES employee (employee_id),
shift_start_time DATE NOT NULL,
shift_end_time DATE NOT NULL
);