-- =====================================================
-- Script de limpieza de la base de datos
-- Book Library Application - Development Only
-- =====================================================

-- ⚠️ ADVERTENCIA: Este script eliminará todos los datos
-- Solo usar en entorno de desarrollo

-- Eliminar triggers primero
DROP TRIGGER IF EXISTS update_books_updated_at ON books;
DROP TRIGGER IF EXISTS update_users_updated_at ON users;

-- Eliminar función
DROP FUNCTION IF EXISTS update_updated_at_column();

-- Eliminar tablas en orden correcto (por las foreign keys)
DROP TABLE IF EXISTS loans CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS books CASCADE;

-- Eliminar índices (se eliminan automáticamente con las tablas)
-- pero por si acaso:
DROP INDEX IF EXISTS idx_books_title;
DROP INDEX IF EXISTS idx_books_author;
DROP INDEX IF EXISTS idx_books_year;
DROP INDEX IF EXISTS idx_books_isbn;
DROP INDEX IF EXISTS idx_loans_book_id;
DROP INDEX IF EXISTS idx_loans_user_id;
DROP INDEX IF EXISTS idx_loans_status;

-- Verificar que las tablas se eliminaron
SELECT 'Base de datos limpiada correctamente' as status; 