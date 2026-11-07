package vetgestion.mvc.modelo;

import java.time.LocalDate;

/** Entidad del modelo: situacion de stock de un insumo (vacuna o medicamento) de la veterinaria. */
public class ItemStock {

    private final int id;
    private final String nombre;
    private final String tipo;
    private final int cantidadActual;
    private final int cantidadMinima;
    private final LocalDate vencimiento;

    public ItemStock(int id, String nombre, String tipo, int cantidadActual, int cantidadMinima, LocalDate vencimiento) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidadActual = cantidadActual;
        this.cantidadMinima = cantidadMinima;
        this.vencimiento = vencimiento;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getCantidadActual() { return cantidadActual; }
    public int getCantidadMinima() { return cantidadMinima; }
    public LocalDate getVencimiento() { return vencimiento; }

    /** Nivel de stock: 0 = sin stock, 1 = a reponer (en o bajo el minimo), 2 = suficiente. */
    public int getNivel() {
        if (cantidadActual == 0) return 0;
        return cantidadActual <= cantidadMinima ? 1 : 2;
    }

    @Override
    public String toString() {
        return String.format("#%d | %-18s | %-11s | stock %3d (min %d) | vence: %s",
                id, nombre, tipo, cantidadActual, cantidadMinima, vencimiento == null ? "-" : vencimiento.toString());
    }
}
