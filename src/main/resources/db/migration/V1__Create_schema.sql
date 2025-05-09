CREATE TABLE addresses (
                           id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                           region VARCHAR(255) NOT NULL,
                           city VARCHAR(255) NOT NULL,
                           street VARCHAR(255) NOT NULL,
                           house_number VARCHAR(50) NOT NULL,
                           apartment VARCHAR(50),
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
                       id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100),
                       middle_name VARCHAR(100),
                       phone VARCHAR(20) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       birth_date DATE NOT NULL,
                       address_id BIGINT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (address_id) REFERENCES addresses (id)
);

CREATE INDEX idx_addresses_search ON addresses(region, city, street, house_number, apartment);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_search ON users(first_name, last_name, middle_name, phone, email);
CREATE INDEX idx_users_address_id ON users(address_id);