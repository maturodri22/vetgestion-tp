package vetgestion.model;

import java.time.LocalDate;

/**
 * Clase de entidad abstracta: generaliza el comportamiento comun de
 * control de stock. Vacuna y Medicamento la especializan (generalizacion
 * UML), ya que cada tipo tiene atributos propios y podria requerir una
 * logica de alerta distinta.
 */
public abstract class Insumo {

    protected int insumoId;
    protected String nombre;
    protected int cantidadActual;
    protected int cantidadMinima;
    protected LocalDate fechaVencimiento;

    public Insumo() { }

    public Insumo(String nombre, int cantidadActual, int cantidadMinima, LocalDate fechaVencimiento) {
        this.nombre = nombre;
        this.cantidadActual = cantidadActual;
        this.cantidadMinima = cantidadMinima;
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getInsumoId() { return insumoId; }
    public void setInsumoId(int insumoId) { this.insumoId = insumoId; }
    public String getNombre() { return nombre; }
    public int getCantidadActual() { return cantidadActual; }
    public void setCantidadActual(int cantidadActual) { this.cantidadActual = cantidadActual; }
    public int getCantidadMinima() { return cantidadMinima; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }

    public abstract String getTipo();

    public boolean estaEnStockMinimo() {
        return cantidadActual <= cantidadMinima;
    }

    public boolean estaProximoAVencer() {
        if (fechaVencimiento == null) return false;
        return !fechaVencimiento.isAfter(LocalDate.now().plusDays(30));
    }

    /** Mensaje polimorfico: cada subtipo de insumo redacta su alerta de forma distinta. */
    public abstract void registrarAlerta();

    @Override
    public String toString() {
        return insumoId + " | " + nombre + " (" + getTipo() + ") | actual: " + cantidadActual
                + " | minimo: " + cantidadMinima
                + (fechaVencimiento != null ? " | vence: " + fechaVencimiento : "");
    }
}
