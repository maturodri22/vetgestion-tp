USE vetgestion;

INSERT INTO clientes (nombre, apellido, telefono, email) VALUES
('Laura', 'Méndez', '11-4455-6677', 'laura.mendez@mail.com'),
('Diego', 'Suárez', '11-2233-4455', 'diego.suarez@mail.com');

INSERT INTO veterinarios (nombre, matricula) VALUES
('Dr. Martín Paz', 'MP-1234'),
('Dra. Carla Ibáñez', 'MP-5678');

INSERT INTO mascotas (cliente_id, nombre, especie, raza, fecha_nacimiento) VALUES
(1, 'Rocky', 'Perro', 'Labrador', '2021-05-10'),
(2, 'Mishi', 'Gato', 'Siamés', '2022-08-22');

INSERT INTO insumos (nombre, tipo, cantidad_actual, cantidad_minima, fecha_vencimiento, enfermedad_prevenida, dosis_requeridas) VALUES
('Antirrábica', 'vacuna', 15, 5, '2027-01-15', 'Rabia', 1),
('Quíntuple canina', 'vacuna', 8, 5, '2026-11-30', 'Parvovirus/Moquillo/Hepatitis', 3);

INSERT INTO insumos (nombre, tipo, cantidad_actual, cantidad_minima, principio_activo, requiere_receta) VALUES
('Amoxicilina 500mg', 'medicamento', 20, 10, 'Amoxicilina', TRUE);

INSERT INTO turnos (mascota_id, veterinario_id, fecha, hora, estado) VALUES
(1, 1, '2026-10-02', '10:00:00', 'confirmado'),
(2, 2, '2026-10-02', '11:30:00', 'pendiente');

INSERT INTO consultas (mascota_id, veterinario_id, fecha, diagnostico, tratamiento, peso, observaciones) VALUES
(1, 1, '2026-10-02', 'Control de rutina', 'Ninguno', 28.50, 'Paciente sano, sin novedades.');

INSERT INTO vacunaciones (consulta_id, insumo_id, fecha_aplicacion, fecha_refuerzo) VALUES
(1, 1, '2026-10-02', '2027-10-02');
