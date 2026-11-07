package vetgestion.mvc.vista;

import vetgestion.mvc.modelo.ItemStock;

import java.util.ArrayList;

/**
 * Contrato de la VISTA. El controlador depende de esta interfaz y no de una
 * implementacion concreta: la consola actual podria reemplazarse por una
 * interfaz grafica sin tocar controlador ni modelo.
 */
public interface VistaStock {

    int mostrarMenu();

    int pedirEntero(String mensaje);

    String pedirTexto(String mensaje);

    void mostrarItems(ArrayList<ItemStock> lista);

    void mostrarEstadisticas(String[] niveles, int[] cantidades, int totalUnidades);

    void mostrarMensaje(String mensaje);

    void mostrarError(String mensaje);
}
