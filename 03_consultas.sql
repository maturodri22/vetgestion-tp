USE vetgestion;

-- Consulta 1: Turnos agendados en una fecha especifica
SELECT m.nombre AS mascota, c.nombre AS cliente, v.nombre AS veterinario, t.hora, t.estado
FROM turnos t
JOIN mascotas m ON t.mascota_id = m.mascota_id
JOIN clientes c ON m.cliente_id = c.cliente_id
JOIN veterinarios v ON t.veterinario_id = v.veterinario_id
WHERE t.fecha = '2026-10-02';

-- Consulta 2: Historial clinico completo de una mascota
SELECT c.fecha, v.nombre AS veterinario, c.diagnostico, c.tratamiento, c.peso
FROM consultas c
JOIN veterinarios v ON c.veterinario_id = v.veterinario_id
WHERE c.mascota_id = 1
ORDER BY c.fecha DESC;

-- Consulta 3: Insumos en stock minimo (alerta, RF08)
SELECT insumo_id, nombre, tipo, cantidad_actual, cantidad_minima
FROM insumos
WHERE cantidad_actual <= cantidad_minima;

-- Consulta 4: Vacunas aplicadas y proximo refuerzo
SELECT m.nombre AS mascota, i.nombre AS vacuna, vc.fecha_aplicacion, vc.fecha_refuerzo
FROM vacunaciones vc
JOIN consultas c ON vc.consulta_id = c.consulta_id
JOIN mascotas m ON c.mascota_id = m.mascota_id
JOIN insumos i ON vc.insumo_id = i.insumo_id
WHERE m.mascota_id = 1;
