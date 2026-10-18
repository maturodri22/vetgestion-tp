package vetgestion;

import vetgestion.control.GestorSalaEspera;
import vetgestion.control.GestorStock;
import vetgestion.control.GestorTurnos;
import vetgestion.dao.*;
import vetgestion.excepcion.StockExcepcion;
import vetgestion.model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
    private static final GestorSalaEspera gestorSala = new GestorSalaEspera();

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
                    case 12 -> dispensarInsumo();
                    case 13 -> gestorStock.deshacerUltimoMovimiento();
                    case 14 -> registrarLlegada();
                    case 15 -> gestorSala.atenderSiguiente();
                    case 16 -> verSalaEspera();
                    case 17 -> listarInsumosOrdenados();
                    case 18 -> buscarInsumoPorNombre();
                    case 19 -> listarDescripciones();
                    case 0 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Dato invalido: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n===== VetGestion - Prototipo =====");
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
        System.out.println("12. Dispensar insumo (descuenta stock)");
        System.out.println("13. Deshacer ultimo movimiento de stock (pila)");
        System.out.println("14. Registrar llegada a la sala de espera (cola)");
        System.out.println("15. Atender siguiente en la sala de espera (cola)");
        System.out.println("16. Ver sala de espera");
        System.out.println("17. Listar insumos ordenados por nombre");
        System.out.println("18. Buscar insumo por nombre (busqueda binaria)");
        System.out.println("19. Listar insumos y mascotas (interfaz Describible)");
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
        LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
        LocalTime hora = leerHora("Hora (HH:MM): ");
        gestorTurnos.agendarTurno(mascotaId, veterinarioId, fecha, hora);
    }

    private static void registrarConsulta() throws SQLException {
        int mascotaId = leerEntero("Id de mascota: ");
        int veterinarioId = leerEntero("Id de veterinario: ");
        System.out.print("Diagnostico: "); String diagnostico = sc.nextLine();
        System.out.print("Tratamiento: "); String tratamiento = sc.nextLine();
        double peso = leerDouble("Peso (kg): ");
        System.out.print("Observaciones: "); String observaciones = sc.nextLine();
        // El constructor valida el peso y lanza IllegalArgumentException si esta fuera de rango
        ConsultaMedica consulta = new ConsultaMedica(mascotaId, veterinarioId, LocalDate.now(),
                diagnostico, tratamiento, peso, observaciones);
        int id = consultaDAO.insertar(consulta);
        System.out.println("Consulta registrada con id: " + id);
    }

    private static void altaInsumo() throws SQLException {
        System.out.print("Nombre del insumo: "); String nombre = sc.nextLine();
        String tipo;
        do {
            System.out.print("Tipo (vacuna/medicamento): ");
            tipo = sc.nextLine().trim().toLowerCase();
        } while (!tipo.equals("vacuna") && !tipo.equals("medicamento"));
        int cantidad = leerEntero("Cantidad actual: ");
        int minimo = leerEntero("Cantidad minima: ");
        LocalDate vencimiento = leerFechaOpcional("Fecha de vencimiento (AAAA-MM-DD, Enter si no vence): ");
        if (tipo.equals("vacuna")) {
            System.out.print("Enfermedad prevenida: "); String enfermedad = sc.nextLine();
            int dosis = leerEntero("Dosis requeridas: ");
            int id = insumoDAO.insertarVacuna(new Vacuna(nombre, cantidad, minimo, vencimiento, enfermedad, dosis));
            System.out.println("Vacuna creada con id: " + id);
        } else {
            System.out.print("Principio activo: "); String principio = sc.nextLine();
            boolean receta = leerSiNo("Requiere receta? (s/n): ");
            int id = insumoDAO.insertarMedicamento(new Medicamento(nombre, cantidad, minimo, vencimiento, principio, receta));
            System.out.println("Medicamento creado con id: " + id);
        }
    }

    private static void registrarMovimientoStock() throws SQLException {
        int insumoId = leerEntero("Id de insumo: ");
        int nuevaCantidad = leerEntero("Nueva cantidad: ");
        gestorStock.registrarMovimiento(insumoId, nuevaCantidad);
    }

    /** Descuenta stock; si no alcanza, el gestor lanza StockExcepcion y se informa al usuario. */
    private static void dispensarInsumo() throws SQLException {
        int insumoId = leerEntero("Id de insumo: ");
        int cantidad = leerEntero("Cantidad a dispensar: ");
        try {
            gestorStock.dispensar(insumoId, cantidad);
        } catch (StockExcepcion e) {
            System.out.println("Aviso: " + e.getMessage());
            System.out.println("Se pueden dispensar como maximo " + e.getDisponible()
                    + " unidades; considere reponer el insumo " + e.getInsumoId() + ".");
        }
    }

    private static void registrarLlegada() throws SQLException {
        int turnoId = leerEntero("Id de turno: ");
        gestorSala.registrarLlegada(turnoId);
    }

    private static void verSalaEspera() throws SQLException {
        List<String> sala = gestorSala.describirSala();
        System.out.println("Sala de espera (orden de llegada):");
        if (sala.isEmpty()) {
            System.out.println("(vacia)");
        } else {
            sala.forEach(l -> System.out.println("  " + l));
        }
    }

    private static void consultarTurnosPorFecha() throws SQLException {
        LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
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

    private static void listarInsumosOrdenados() throws SQLException {
        System.out.println("Insumos ordenados por nombre:");
        for (Insumo i : gestorStock.listarOrdenadosPorNombre()) {
            System.out.println(i);
        }
    }

    private static void buscarInsumoPorNombre() throws SQLException {
        System.out.print("Nombre a buscar: ");
        String nombre = sc.nextLine().trim();
        List<Insumo> encontrados = gestorStock.buscarPorNombre(nombre);
        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron insumos con ese nombre.");
        } else {
            System.out.println("Coincidencias:");
            encontrados.forEach(System.out::println);
        }
    }

    /** Una sola lista de Describible guarda objetos de clases sin herencia comun (polimorfismo por interfaz). */
    private static void listarDescripciones() throws SQLException {
        List<Describible> elementos = new ArrayList<>();
        elementos.addAll(insumoDAO.listarTodos());
        elementos.addAll(mascotaDAO.listarTodas());
        for (Describible d : elementos) {
            System.out.println(" - " + d.getDescripcion());
        }
    }

    // ---------- Lectura segura de datos: repite la consulta hasta recibir un valor valido ----------

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un valor numerico entero.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un numero (ej. 12.5).");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalDate.parse(sc.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Error: la fecha debe tener el formato AAAA-MM-DD.");
            }
        }
    }

    private static LocalDate leerFechaOpcional(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("Error: la fecha debe tener el formato AAAA-MM-DD.");
            }
        }
    }

    private static LocalTime leerHora(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalTime.parse(sc.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Error: la hora debe tener el formato HH:MM.");
            }
        }
    }

    private static boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String r = sc.nextLine().trim().toLowerCase();
            if (r.equals("s")) return true;
            if (r.equals("n")) return false;
            System.out.println("Error: responda s o n.");
        }
    }
}
