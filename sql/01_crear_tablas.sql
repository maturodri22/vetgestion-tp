CREATE DATABASE IF NOT EXISTS vetgestion;
USE vetgestion;

CREATE TABLE clientes (
    cliente_id    INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(60) NOT NULL,
    apellido      VARCHAR(60) NOT NULL,
    telefono      VARCHAR(30) NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE mascotas (
    mascota_id        INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id        INT NOT NULL,
    nombre            VARCHAR(60) NOT NULL,
    especie           VARCHAR(30) NOT NULL,
    raza              VARCHAR(60),
    fecha_nacimiento  DATE,
    CONSTRAINT fk_mascota_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE veterinarios (
    veterinario_id  INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(60) NOT NULL,
    matricula       VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE turnos (
    turno_id        INT AUTO_INCREMENT PRIMARY KEY,
    mascota_id      INT NOT NULL,
    veterinario_id  INT NOT NULL,
    fecha           DATE NOT NULL,
    hora            TIME NOT NULL,
    estado          ENUM('pendiente','confirmado','cancelado','atendido') NOT NULL DEFAULT 'pendiente',
    CONSTRAINT fk_turno_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(mascota_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_turno_veterinario
        FOREIGN KEY (veterinario_id) REFERENCES veterinarios(veterinario_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uq_veterinario_horario UNIQUE (veterinario_id, fecha, hora)
);

CREATE TABLE consultas (
    consulta_id     INT AUTO_INCREMENT PRIMARY KEY,
    mascota_id      INT NOT NULL,
    veterinario_id  INT NOT NULL,
    fecha           DATE NOT NULL,
    diagnostico     VARCHAR(255) NOT NULL,
    tratamiento     VARCHAR(255),
    peso            DECIMAL(5,2) NOT NULL,
    observaciones   TEXT,
    CONSTRAINT fk_consulta_mascota
        FOREIGN KEY (mascota_id) REFERENCES mascotas(mascota_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_consulta_veterinario
        FOREIGN KEY (veterinario_id) REFERENCES veterinarios(veterinario_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_peso_rango CHECK (peso > 0 AND peso <= 200)
);

CREATE TABLE insumos (
    insumo_id             INT AUTO_INCREMENT PRIMARY KEY,
    nombre                VARCHAR(80) NOT NULL,
    tipo                  ENUM('vacuna','medicamento') NOT NULL,
    cantidad_actual       INT NOT NULL DEFAULT 0,
    cantidad_minima       INT NOT NULL DEFAULT 0,
    fecha_vencimiento     DATE,
    enfermedad_prevenida  VARCHAR(100),
    dosis_requeridas      INT,
    principio_activo      VARCHAR(100),
    requiere_receta       BOOLEAN,
    CONSTRAINT chk_cantidad_no_negativa CHECK (cantidad_actual >= 0 AND cantidad_minima >= 0)
);

CREATE TABLE vacunaciones (
    vacunacion_id    INT AUTO_INCREMENT PRIMARY KEY,
    consulta_id      INT NOT NULL,
    insumo_id        INT NOT NULL,
    fecha_aplicacion DATE NOT NULL,
    fecha_refuerzo   DATE,
    CONSTRAINT fk_vacunacion_consulta
        FOREIGN KEY (consulta_id) REFERENCES consultas(consulta_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_vacunacion_insumo
        FOREIGN KEY (insumo_id) REFERENCES insumos(insumo_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);
