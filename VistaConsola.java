package vetgestion.mvc.vista;

import vetgestion.mvc.modelo.ItemStock;

import java.util.ArrayList;
import java.util.Scanner;

/** VISTA por consola: solo lee y muestra datos; no contiene logica de negocio ni de base de datos. */
public class VistaConsola implements VistaStock {

    private final Scanner sc;

    public VistaConsola(Scanner sc) {
        this.sc = sc;
    }

    @Override
    public int mostrarMenu() {
        System.out.println("\n--- Modulo Stock de insumos (MVC) ---");
        System.out.println("1. Listar todos los insumos");
        System.out.println("2. Listar insumos a reponer");
        System.out.println("3. Registrar ingreso o egreso de stock");
        System.out.println("4. Eliminar insumo");
        System.out.println("5. Estadisticas de stock");
        System.out.println("6. Exportar lista de reposicion a archivo CSV");
        System.out.println("7. Importar movimientos de stock desde archivo CSV");
        System.out.println("0. Volver al menu principal");
        return pedirEntero("Opcion: ");
    }

    @Override
    public int pedirEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un numero entero.");
            }
        }
    }

    @Override
    public String pedirTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    @Override
    public void mostrarItems(ArrayList<ItemStock> lista) {
        if (lista.isEmpty()) {
            System.out.println("Sin insumos para mostrar.");
            return;
        }
        for (ItemStock i : lista) System.out.println(i);
        System.out.println("Total: " + lista.size());
    }

    @Override
    public void mostrarEstadisticas(String[] niveles, int[] cantidades, int totalUnidades) {
        System.out.println("Unidades totales en stock: " + totalUnidades);
        System.out.println("Insumos por nivel de stock:");
        for (int i = 0; i < niveles.length; i++) {
            System.out.printf("  %-11s | %-10s %d%n", niveles[i], "#".repeat(cantidades[i]), cantidades[i]);
        }
    }

    @Override
    public void mostrarMensaje(String mensaje) { System.out.println(mensaje); }

    @Override
    public void mostrarError(String mensaje) { System.out.println("Error: " + mensaje); }
}
