package vetgestion.mvc.modelo;

import java.io.*;
import java.util.ArrayList;

/**
 * Persistencia en archivos de texto (java.io). Exporta la lista de reposicion a CSV,
 * lee un CSV de movimientos de stock y deja un registro de las filas rechazadas.
 * Formato de importacion (separador ';'): insumo_id;cantidad;motivo
 * (cantidad positiva = ingreso, negativa = egreso).
 */
public class ArchivoStock {

    /** Fila ya validada de un CSV de movimientos. */
    public record Movimiento(int insumoId, int cantidad, String motivo) { }

    /** Escribe los insumos en un CSV con BufferedWriter (se cierra solo: try-with-resources). */
    public void exportar(ArrayList<ItemStock> lista, File destino) throws IOException {
        File carpeta = destino.getAbsoluteFile().getParentFile();
        if (carpeta != null && !carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta " + carpeta);
        }
        try (BufferedWriter out = new BufferedWriter(new FileWriter(destino))) {
            out.write("insumo_id;nombre;tipo;stock_actual;stock_minimo;a_pedir");
            out.newLine();
            for (ItemStock i : lista) {
                int aPedir = Math.max(0, i.getCantidadMinima() * 2 - i.getCantidadActual());
                out.write(i.getId() + ";" + i.getNombre() + ";" + i.getTipo() + ";" +
                        i.getCantidadActual() + ";" + i.getCantidadMinima() + ";" + aPedir);
                out.newLine();
            }
        }
    }

    /**
     * Lee un CSV con BufferedReader. Las filas validas se devuelven en un ArrayList;
     * las invalidas se agregan (con el motivo) a la lista 'rechazadas'.
     */
    public ArrayList<Movimiento> leerMovimientos(File origen, ArrayList<String> rechazadas) throws IOException {
        ArrayList<Movimiento> filas = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(origen))) {
            String linea;
            int numero = 0;
            while ((linea = in.readLine()) != null) {
                numero++;
                if (linea.isBlank() || linea.startsWith("#")) continue;
                String[] campos = linea.split(";");
                try {
                    if (campos.length < 2 || campos.length > 3) {
                        throw new IllegalArgumentException("se esperaban 2 o 3 campos");
                    }
                    filas.add(new Movimiento(Integer.parseInt(campos[0].trim()), Integer.parseInt(campos[1].trim()),
                            campos.length == 3 ? campos[2].trim() : ""));
                } catch (IllegalArgumentException e) {
                    rechazadas.add("Linea " + numero + " (" + linea + "): " + e.getMessage());
                }
            }
        }
        return filas;
    }

    /** Agrega lineas al archivo de errores (modo append). */
    public void registrarErrores(ArrayList<String> errores, File log) throws IOException {
        if (errores.isEmpty()) return;
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(log, true)))) {
            out.println("--- Importacion " + java.time.LocalDateTime.now().withNano(0) + " ---");
            for (String e : errores) out.println(e);
        }
    }
}
