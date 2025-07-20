-- =====================================================
-- Script de inicialización de la base de datos
-- Book Library Application
-- =====================================================

-- Crear la base de datos (si no existe)
-- CREATE DATABASE book_library;

-- Usar la base de datos
-- USE book_library;

-- Crear la tabla books
CREATE TABLE IF NOT EXISTS books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    year INT,
    isbn VARCHAR(13) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_books_title ON books(title);
CREATE INDEX IF NOT EXISTS idx_books_author ON books(author);
CREATE INDEX IF NOT EXISTS idx_books_year ON books(year);
CREATE INDEX IF NOT EXISTS idx_books_isbn ON books(isbn);

-- Insertar algunos datos de ejemplo
INSERT INTO books (title, author, year, isbn) VALUES
    ('Don Quijote de la Mancha', 'Miguel de Cervantes', 1605, '978-84-376-0494-7'),
    ('El Señor de los Anillos', 'J.R.R. Tolkien', 1954, '978-84-450-7179-3'),
    ('Cien años de soledad', 'Gabriel García Márquez', 1967, '978-84-397-2077-8'),
    ('1984', 'George Orwell', 1949, '978-84-397-2078-5'),
    ('El Principito', 'Antoine de Saint-Exupéry', 1943, '978-84-397-2079-2')
ON CONFLICT (isbn) DO NOTHING;

-- Crear tabla de usuarios (para futuras funcionalidades)
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de préstamos (para futuras funcionalidades)
CREATE TABLE IF NOT EXISTS loans (
    id SERIAL PRIMARY KEY,
    book_id INT NOT NULL,
    user_id INT NOT NULL,
    loan_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    return_date TIMESTAMP,
    due_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Crear índices para la tabla de préstamos
CREATE INDEX IF NOT EXISTS idx_loans_book_id ON loans(book_id);
CREATE INDEX IF NOT EXISTS idx_loans_user_id ON loans(user_id);
CREATE INDEX IF NOT EXISTS idx_loans_status ON loans(status);

-- Insertar un usuario de ejemplo
INSERT INTO users (username, email, password_hash, full_name) VALUES
    ('admin', 'admin@booklibrary.com', '$2a$10$example.hash.here', 'Administrador del Sistema')
ON CONFLICT (username) DO NOTHING;

-- Crear función para actualizar el timestamp de updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Crear triggers para actualizar automáticamente updated_at
CREATE TRIGGER update_books_updated_at 
    BEFORE UPDATE ON books 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Verificar que las tablas se crearon correctamente
SELECT 'Tabla books creada con ' || COUNT(*) || ' registros' as status FROM books;
SELECT 'Tabla users creada con ' || COUNT(*) || ' registros' as status FROM users; 