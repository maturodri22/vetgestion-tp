package vetgestion.control;

import vetgestion.dao.InsumoDAO;
import vetgestion.model.Insumo;

import java.sql.SQLException;
import java.util.List;

/**
 * Clase de control: coordina el caso de uso "Gestionar stock de insumos",
 * incluyendo el disparo automatico de "Emitir alerta de stock" («extend»,
 * actor Sistema) cuando corresponde.
 */
public class GestorStock {

    private final InsumoDAO insumoDAO = new InsumoDAO();

    /** Registra un movimiento de stock (alta, baja o ajuste) y dispara la alerta si corresponde. */
    public void registrarMovimiento(int insumoId, int nuevaCantidad) throws SQLException {
        insumoDAO.actualizarCantidad(insumoId, nuevaCantidad);
        Insumo insumo = insumoDAO.buscarPorId(insumoId);
        if (insumo == null) return;

        // Mensaje polimorfico: Vacuna y Medicamento redactan su propia alerta
        if (insumo.estaEnStockMinimo() || insumo.estaProximoAVencer()) {
            insumo.registrarAlerta();
        } else {
            System.out.println("Stock actualizado. \"" + insumo.getNombre() + "\" sin alertas pendientes.");
        }
    }

    /** Recorre todo el inventario y emite las alertas correspondientes (RF08). */
    public void verificarAlertasDeStock() throws SQLException {
        List<Insumo> insumos = insumoDAO.listarTodos();
        boolean huboAlertas = false;
        for (Insumo i : insumos) {
            if (i.estaEnStockMinimo() || i.estaProximoAVencer()) {
                i.registrarAlerta();
                huboAlertas = true;
            }
        }
        if (!huboAlertas) {
            System.out.println("No hay alertas de stock pendientes.");
        }
    }
}
