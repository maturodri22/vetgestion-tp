package vetgestion.control;

import vetgestion.dao.InsumoDAO;
import vetgestion.excepcion.StockExcepcion;
import vetgestion.model.Insumo;
import vetgestion.util.Algoritmos;

import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Locale;

/**
 * Clase de control: coordina el caso de uso "Gestionar stock de insumos",
 * incluyendo el disparo automatico de "Emitir alerta de stock" («extend»,
 * actor Sistema) cuando corresponde.
 *
 * Estructura de datos:
 *  - PILA (LIFO, ArrayDeque): historial de movimientos de stock, para poder
 *    deshacer el ultimo movimiento registrado.
 * Algoritmos: ordena los insumos por nombre (insercion) y los busca (binaria).
 */
public class GestorStock {

    /** Registro inmutable de un movimiento de stock (se apila para poder deshacerlo). */
    private record MovimientoStock(int insumoId, int cantidadAnterior, int cantidadNueva) { }

    /** Compara nombres ignorando mayusculas y acentos (Antirrabica = antirrábica). */
    private static final Collator COLLATOR = Collator.getInstance(Locale.forLanguageTag("es-AR"));
    static {
        COLLATOR.setStrength(Collator.PRIMARY);
    }

    private static final Comparator<Insumo> POR_NOMBRE =
            (a, b) -> COLLATOR.compare(a.getNombre(), b.getNombre());

    private final InsumoDAO insumoDAO = new InsumoDAO();
    private final Deque<MovimientoStock> pilaMovimientos = new ArrayDeque<>();

    /** Fija una nueva cantidad (alta, ajuste), apila el movimiento y dispara la alerta si corresponde. */
    public void registrarMovimiento(int insumoId, int nuevaCantidad) throws SQLException {
        Insumo insumo = insumoDAO.buscarPorId(insumoId);
        if (insumo == null) {
            System.out.println("No existe un insumo con id " + insumoId + ".");
            return;
        }
        int anterior = insumo.getCantidadActual();
        insumo.setCantidadActual(nuevaCantidad);          // valida: no admite negativos
        insumoDAO.actualizarCantidad(insumoId, nuevaCantidad);
        pilaMovimientos.push(new MovimientoStock(insumoId, anterior, nuevaCantidad));
        emitirAlertasSiCorresponde(insumo);
    }

    /** Descuenta unidades de un insumo. Si no alcanza el stock lanza StockExcepcion. */
    public void dispensar(int insumoId, int cantidad) throws SQLException, StockExcepcion {
        Insumo insumo = insumoDAO.buscarPorId(insumoId);
        if (insumo == null) {
            System.out.println("No existe un insumo con id " + insumoId + ".");
            return;
        }
        int anterior = insumo.getCantidadActual();
        insumo.retirar(cantidad);                          // puede lanzar StockExcepcion
        insumoDAO.actualizarCantidad(insumoId, insumo.getCantidadActual());
        pilaMovimientos.push(new MovimientoStock(insumoId, anterior, insumo.getCantidadActual()));
        System.out.println("Se dispensaron " + cantidad + " unidades de \"" + insumo.getNombre()
                + "\". Stock actual: " + insumo.getCantidadActual());
        emitirAlertasSiCorresponde(insumo);
    }

    /** Deshace el ultimo movimiento de stock (desapila y restaura la cantidad anterior). */
    public void deshacerUltimoMovimiento() throws SQLException {
        if (pilaMovimientos.isEmpty()) {
            System.out.println("No hay movimientos de stock para deshacer en esta sesion.");
            return;
        }
        MovimientoStock ultimo = pilaMovimientos.pop();
        insumoDAO.actualizarCantidad(ultimo.insumoId(), ultimo.cantidadAnterior());
        System.out.println("Se deshizo el movimiento del insumo " + ultimo.insumoId() + ": stock "
                + ultimo.cantidadNueva() + " -> " + ultimo.cantidadAnterior());
    }

    /** Recorre todo el inventario y emite las alertas correspondientes (RF08). */
    public void verificarAlertasDeStock() throws SQLException {
        boolean huboAlertas = false;
        for (Insumo i : insumoDAO.listarTodos()) {
            if (i.estaEnStockMinimo() || i.estaProximoAVencer()) {
                i.registrarAlerta();
                huboAlertas = true;
            }
        }
        if (!huboAlertas) {
            System.out.println("No hay alertas de stock pendientes.");
        }
    }

    public List<Insumo> listarOrdenadosPorNombre() throws SQLException {
        List<Insumo> insumos = insumoDAO.listarTodos();
        Algoritmos.ordenarPorInsercion(insumos, POR_NOMBRE);
        return insumos;
    }

    /** Busca por nombre con busqueda binaria y devuelve todas las coincidencias. */
    public List<Insumo> buscarPorNombre(String nombre) throws SQLException {
        List<Insumo> ordenados = listarOrdenadosPorNombre();
        List<String> nombres = new ArrayList<>();
        for (Insumo i : ordenados) {
            nombres.add(i.getNombre());
        }
        List<Insumo> coincidencias = new ArrayList<>();
        int pos = Algoritmos.busquedaBinaria(nombres, nombre, COLLATOR::compare);
        if (pos < 0) {
            return coincidencias;
        }
        int desde = pos;
        while (desde > 0 && COLLATOR.compare(nombres.get(desde - 1), nombre) == 0) {
            desde--;
        }
        int hasta = pos;
        while (hasta < nombres.size() - 1 && COLLATOR.compare(nombres.get(hasta + 1), nombre) == 0) {
            hasta++;
        }
        for (int k = desde; k <= hasta; k++) {
            coincidencias.add(ordenados.get(k));
        }
        return coincidencias;
    }

    private void emitirAlertasSiCorresponde(Insumo insumo) {
        // Mensaje polimorfico: Vacuna y Medicamento redactan su propia alerta
        if (insumo.estaEnStockMinimo() || insumo.estaProximoAVencer()) {
            insumo.registrarAlerta();
        } else {
            System.out.println("Stock actualizado. \"" + insumo.getNombre() + "\" sin alertas pendientes.");
        }
    }
}
