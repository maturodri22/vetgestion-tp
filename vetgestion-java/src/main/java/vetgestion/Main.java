package vetgestion;

import vetgestion.control.GestorStock;
import vetgestion.control.GestorTurnos;
import vetgestion.dao.*;
import vetgestion.model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

/**
 * Prototipo operacional de VetGestion (consola).
 * Requiere que la base de datos "vetgestion" ya exista (ver sql/01_crear_tablas.sql
 * y, opcionalmente, sql/02_datos_prueba.sql) y que config.properties tenga
 * los datos de conexion correctos.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final MascotaDAO mascotaDAO = new MascotaDAO();
    private static final VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
    private static final TurnoDAO turnoDAO = new TurnoDAO();
    private static final ConsultaDAO consultaDAO = new ConsultaDAO();
    private static final InsumoDAO insumoDAO = new InsumoDAO();
    private static final VacunacionDAO vacunacionDAO = new VacunacionDAO();
    private static final GestorTurnos gestorTurnos = new GestorTurnos();
    private static final GestorStock gestorStock = new GestorStock();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opcion: ");
            try {
                switch (opcion) {
                    case 1 -> altaCliente();
                    case 2 -> altaMascota();
                    case 3 -> altaVeterinario();
                    case 4 -> agendarTurno();
                    case 5 -> registrarConsulta();
                    case 6 -> altaInsumo();
                    case 7 -> registrarMovimientoStock();
                    case 8 -> consultarTurnosPorFecha();
                    case 9 -> consultarHistorialClinico();
                    case 10 -> consultarInsumosStockMinimo();
                    case 11 -> consultarVacunacionesPorMascota();
                    case 0 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n===== VetGestion — Prototipo =====");
        System.out.println("1. Alta de cliente");
        System.out.println("2. Alta de mascota");
        System.out.println("3. Alta de veterinario");
        System.out.println("4. Agendar turno");
        System.out.println("5. Registrar consulta medica");
        System.out.println("6. Alta de insumo (vacuna/medicamento)");
        System.out.println("7. Registrar movimiento de stock");
        System.out.println("8. Consultar turnos por fecha");
        System.out.println("9. Consultar historial clinico de una mascota");
        System.out.println("10. Consultar insumos en stock minimo");
        System.out.println("11. Consultar vacunaciones de una mascota");
        System.out.println("0. Salir");
    }

    private static void altaCliente() throws SQLException {
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Apellido: "); String apellido = sc.nextLine();
        System.out.print("Telefono: "); String telefono = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        int id = clienteDAO.insertar(new Cliente(nombre, apellido, telefono, email));
        System.out.println("Cliente creado con id: " + id);
    }

    private static void altaMascota() throws SQLException {
        int clienteId = leerEntero("Id de cliente: ");
        System.out.print("Nombre de la mascota: "); String nombre = sc.nextLine();
        System.out.print("Especie: "); String especie = sc.nextLine();
        System.out.print("Raza: "); String raza = sc.nextLine();
        int id = mascotaDAO.insertar(new Mascota(clienteId, nombre, especie, raza, null));
        System.out.println("Mascota creada con id: " + id);
    }

    private static void altaVeterinario() throws SQLException {
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Matricula: "); String matricula = sc.nextLine();
        int id = veterinarioDAO.insertar(new Veterinario(nombre, matricula));
        System.out.println("Veterinario creado con id: " + id);
    }

    private static void agendarTurno() throws SQLException {
        int mascotaId = leerEntero("Id de mascota: ");
        int veterinarioId = leerEntero("Id de veterinario: ");
        System.out.print("Fecha (AAAA-MM-DD): "); LocalDate fecha = LocalDate.parse(sc.nextLine().trim());
        System.out.print("Hora (HH:MM): "); LocalTime hora = LocalTime.parse(sc.nextLine().trim());
        gestorTurnos.agendarTurno(mascotaId, veterinarioId, fecha, hora);
    }

    private static void registrarConsulta() throws SQLException {
        int mascotaId = leerEntero("Id de mascota: ");
        int veterinarioId = leerEntero("Id de veterinario: ");
        System.out.print("Diagnostico: "); String diagnostico = sc.nextLine();
        System.out.print("Tratamiento: "); String tratamiento = sc.nextLine();
        double peso = leerDouble("Peso (kg): ");
        System.out.print("Observaciones: "); String observaciones = sc.nextLine();
        try {
            int id = consultaDAO.insertar(new ConsultaMedica(mascotaId, veterinarioId, LocalDate.now(),
                    diagnostico, tratamiento, peso, observaciones));
            System.out.println("Consulta registrada con id: " + id);
        } catch (SQLException e) {
            System.out.println("No se pudo registrar la consulta (verificar rango de peso 0-200 kg): " + e.getMessage());
        }
    }

    private static void altaInsumo() throws SQLException {
        System.out.print("Nombre del insumo: "); String nombre = sc.nextLine();
        System.out.print("Tipo (vacuna/medicamento): "); String tipo = sc.nextLine();
        int cantidad = leerEntero("Cantidad actual: ");
        int minimo = leerEntero("Cantidad minima: ");
        if (tipo.equalsIgnoreCase("vacuna")) {
            System.out.print("Enfermedad prevenida: "); String enfermedad = sc.nextLine();
            int dosis = leerEntero("Dosis requeridas: ");
            int id = insumoDAO.insertarVacuna(new Vacuna(nombre, cantidad, minimo, null, enfermedad, dosis));
            System.out.println("Vacuna creada con id: " + id);
        } else {
            System.out.print("Principio activo: "); String principio = sc.nextLine();
            int id = insumoDAO.insertarMedicamento(new Medicamento(nombre, cantidad, minimo, null, principio, true));
            System.out.println("Medicamento creado con id: " + id);
        }
    }

    private static void registrarMovimientoStock() throws SQLException {
        int insumoId = leerEntero("Id de insumo: ");
        int nuevaCantidad = leerEntero("Nueva cantidad: ");
        gestorStock.registrarMovimiento(insumoId, nuevaCantidad);
    }

    private static void consultarTurnosPorFecha() throws SQLException {
        System.out.print("Fecha (AAAA-MM-DD): "); LocalDate fecha = LocalDate.parse(sc.nextLine().trim());
        List<String> turnos = turnoDAO.turnosPorFecha(fecha);
        System.out.println("Turnos del " + fecha + ":");
        turnos.forEach(System.out::println);
    }

    private static void consultarHistorialClinico() throws SQLException {
        int mascotaId = leerEntero("Id de mascota: ");
        List<String> historial = consultaDAO.historialPorMascota(mascotaId);
        System.out.println("Historial clinico:");
        historial.forEach(System.out::println);
    }

    private static void consultarInsumosStockMinimo() throws SQLException {
        List<Insumo> insumos = insumoDAO.listarEnStockMinimo();
        System.out.println("Insumos en stock minimo:");
        if (insumos.isEmpty()) {
            System.out.println("(ninguno)");
        } else {
            insumos.forEach(System.out::println);
        }
    }

    private static void consultarVacunacionesPorMascota() throws SQLException {
        int mascotaId = leerEntero("Id de mascota: ");
        List<String> vacunaciones = vacunacionDAO.historialPorMascota(mascotaId);
        System.out.println("Vacunaciones registradas:");
        vacunaciones.forEach(System.out::println);
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return Integer.parseInt(sc.nextLine().trim());
    }

    private static double leerDouble(String mensaje) {
        System.out.print(mensaje);
        return Double.parseDouble(sc.nextLine().trim());
    }
}
