package vetgestion.mvc.controlador;

import vetgestion.excepcion.PersistenciaException;
import vetgestion.mvc.modelo.ArchivoStock;
import vetgestion.mvc.modelo.ItemStock;
import vetgestion.mvc.modelo.ModeloStock;
import vetgestion.mvc.vista.VistaStock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * CONTROLADOR del patron MVC: recibe las acciones elegidas en la vista, invoca al
 * modelo y le ordena a la vista que muestre el resultado. Aqui se capturan las
 * excepciones de persistencia y de archivos para informarlas al usuario.
 */
public class ControladorStock {

    private static final File CARPETA = new File("datos");

    private final ModeloStock modelo;
    private final ArchivoStock archivos;
    private final VistaStock vista;

    public ControladorStock(ModeloStock modelo, ArchivoStock archivos, VistaStock vista) {
        this.modelo = modelo;
        this.archivos = archivos;
        this.vista = vista;
    }

    /** Bucle del modulo: atiende las opciones hasta que el usuario elige volver. */
    public void iniciar() {
        int opcion;
        do {
            opcion = vista.mostrarMenu();
            try {
                switch (opcion) {
                    case 1 -> vista.mostrarItems(modelo.listarTodos());
                    case 2 -> vista.mostrarItems(modelo.listarAReponer());
                    case 3 -> ajustar();
                    case 4 -> eliminar();
                    case 5 -> estadisticas();
                    case 6 -> exportar();
                    case 7 -> importar();
                    case 0 -> vista.mostrarMensaje("Volviendo al menu principal...");
                    default -> vista.mostrarError("Opcion invalida.");
                }
            } catch (PersistenciaException e) {
                vista.mostrarError(e.getMessage());
            } catch (IOException e) {
                vista.mostrarError("Problema con el archivo: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void ajustar() throws PersistenciaException {
        int id = vista.pedirEntero("Id de insumo: ");
        int delta = vista.pedirEntero("Cantidad (positiva = ingreso, negativa = egreso): ");
        int nuevo = modelo.ajustarStock(id, delta);
        vista.mostrarMensaje("Stock actualizado. Cantidad actual: " + nuevo);
    }

    private void eliminar() throws PersistenciaException {
        int id = vista.pedirEntero("Id de insumo a eliminar: ");
        vista.mostrarMensaje(modelo.eliminar(id) ? "Insumo eliminado." : "No existe el insumo " + id + ".");
    }

    private void estadisticas() throws PersistenciaException {
        ArrayList<ItemStock> lista = modelo.listarTodos();
        vista.mostrarEstadisticas(ModeloStock.NIVELES, modelo.distribucion(lista), modelo.totalUnidades(lista));
    }

    private void exportar() throws PersistenciaException, IOException {
        ArrayList<ItemStock> lista = modelo.listarAReponer();
        File destino = new File(CARPETA, "reposicion_insumos.csv");
        archivos.exportar(lista, destino);
        vista.mostrarMensaje("Se exportaron " + lista.size() + " insumos a reponer en " + destino.getPath());
    }

    private void importar() throws IOException {
        String ruta = vista.pedirTexto("Ruta del CSV (ej: datos/movimientos_ejemplo.csv): ");
        File origen = new File(ruta);
        if (!origen.isFile()) {
            vista.mostrarError("El archivo '" + ruta + "' no existe.");
            return;
        }
        ArrayList<String> errores = new ArrayList<>();
        ArrayList<ArchivoStock.Movimiento> filas = archivos.leerMovimientos(origen, errores);
        int aplicados = 0;
        for (ArchivoStock.Movimiento m : filas) {
            try {
                modelo.ajustarStock(m.insumoId(), m.cantidad());
                aplicados++;
            } catch (PersistenciaException e) {
                errores.add("Insumo " + m.insumoId() + " (" + m.cantidad() + "): " + e.getMessage());
            }
        }
        archivos.registrarErrores(errores, new File(CARPETA, "importacion_errores.log"));
        vista.mostrarMensaje("Importacion terminada: " + aplicados + " aplicados, " + errores.size() + " rechazados.");
        if (!errores.isEmpty()) vista.mostrarMensaje("El detalle quedo en datos/importacion_errores.log");
    }
}
