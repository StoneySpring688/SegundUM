-- ============================================
-- Inicialización de MySQL para Docker Compose
-- Crea las bases de datos, usuarios y esquema
-- para los microservicios Usuarios y Productos
-- ============================================

-- Base de datos para Usuarios
CREATE DATABASE IF NOT EXISTS segundumUsuarios
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Base de datos para Productos
CREATE DATABASE IF NOT EXISTS segundumProductos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Usuario para el microservicio Usuarios
CREATE USER IF NOT EXISTS 'usuario_usuarios'@'%' IDENTIFIED BY 'usuarios2026';
GRANT ALL PRIVILEGES ON segundumUsuarios.* TO 'usuario_usuarios'@'%';

-- Usuario para el microservicio Productos
CREATE USER IF NOT EXISTS 'usuario_productos'@'%' IDENTIFIED BY 'productos2026';
GRANT ALL PRIVILEGES ON segundumProductos.* TO 'usuario_productos'@'%';

FLUSH PRIVILEGES;

-- ============================================
-- Esquema de segundumUsuarios
-- ============================================

USE segundumUsuarios;

-- Tabla principal de usuarios.
-- APELLIDOS, CLAVE, TELEFONO, fecha_nacimiento e id_github son NULL:
--   - APELLIDOS/TELEFONO/fecha_nacimiento son campos opcionales en el registro.
--   - CLAVE es NULL para usuarios registrados via OAuth2/GitHub.
--   - id_github es NULL para usuarios registrados con email/contraseña.
CREATE TABLE IF NOT EXISTS usuarios (
    ID                  VARCHAR(255)  NOT NULL,
    EMAIL               VARCHAR(255)  NOT NULL,
    NOMBRE              VARCHAR(255)  NOT NULL,
    APELLIDOS           VARCHAR(255)      NULL,
    CLAVE               VARCHAR(255)      NULL,
    fecha_nacimiento    DATE              NULL,
    TELEFONO            VARCHAR(255)      NULL,
    ADMINISTRADOR       TINYINT(1)    NOT NULL DEFAULT 0,
    COMPRASREALIZADAS   INT           NOT NULL DEFAULT 0,
    VENTASREALIZADAS    INT           NOT NULL DEFAULT 0,
    id_github           VARCHAR(255)      NULL,
    PRIMARY KEY (ID),
    UNIQUE KEY uq_usuarios_email    (EMAIL),
    UNIQUE KEY uq_usuarios_github   (id_github)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de IDs de productos asociados a cada usuario (relación ElementCollection).
-- ON DELETE CASCADE: al eliminar un usuario se eliminan sus referencias de producto.
CREATE TABLE IF NOT EXISTS productos_id (
    USUARIO_ID  VARCHAR(255) NOT NULL,
    PRODUCTOS   VARCHAR(255)     NULL,
    CONSTRAINT fk_productos_id_usuario
        FOREIGN KEY (USUARIO_ID) REFERENCES usuarios (ID)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Esquema de segundumProductos
-- ============================================

USE segundumProductos;

-- Tabla de categorías con soporte para jerarquía (subcategorías).
-- descripcion, ruta y categoria_padre_id son NULL (campos opcionales).
CREATE TABLE IF NOT EXISTS categorias (
    id                  VARCHAR(255)    NOT NULL,
    nombre              VARCHAR(255)    NOT NULL,
    descripcion         VARCHAR(1000)       NULL,
    ruta                LONGTEXT            NULL,
    categoria_padre_id  VARCHAR(255)        NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_categoria_padre
        FOREIGN KEY (categoria_padre_id) REFERENCES categorias (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de productos.
-- recogida_* son NULL: el lugar de recogida es un campo opcional al crear un producto.
-- descripcion es NULL: campo opcional.
CREATE TABLE IF NOT EXISTS productos (
    id                  VARCHAR(255)        NOT NULL,
    titulo              VARCHAR(255)        NOT NULL,
    descripcion         VARCHAR(2000)           NULL,
    precio              DECIMAL(10, 2)      NOT NULL,
    estado              VARCHAR(50)         NOT NULL,
    fecha_publicacion   DATETIME            NOT NULL,
    visualizaciones     INT                 NOT NULL DEFAULT 0,
    envio_disponible    TINYINT(1)          NOT NULL DEFAULT 0,
    recogida_descripcion VARCHAR(500)           NULL,
    recogida_longitud   DOUBLE                  NULL,
    recogida_latitud    DOUBLE                  NULL,
    vendedor_id         VARCHAR(255)        NOT NULL,
    vendido             TINYINT(1)          NOT NULL DEFAULT 0,
    categoria_id        VARCHAR(255)        NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (categoria_id) REFERENCES categorias (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
