package vetgestion.model;

import vetgestion.excepcion.StockExcepcion;

import java.time.LocalDate;

/**
 * Clase de entidad abstracta: generaliza el comportamiento comun de
 * control de stock. Vacuna y Medicamento la especializan (generalizacion
 * UML), ya que cada tipo tiene atributos propios y una alerta distinta.
 *
 * Encapsulamiento: todos los atributos son privados y el stock solo puede
 * modificarse mediante metodos que validan la operacion (retirar, reponer,
 * setCantidadActual). Las subclases acceden al estado por los getters.
 */
public abstract class Insumo implements Describible {

    private int insumoId;
    private String nombre;
    private int cantidadActual;
    private int cantidadMinima;
    private LocalDate fechaVencimiento;

    public Insumo() { }

    public Insumo(String nombre, int cantidadActual, int cantidadMinima, LocalDate fechaVencimiento) {
        this.nombre = validarNombre(nombre);
        this.cantidadActual = validarCantidad(cantidadActual);
        this.cantidadMinima = validarCantidad(cantidadMinima);
        this.fechaVencimiento = fechaVencimiento;
    }

    private static String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del insumo no puede estar vacio.");
        }
        return nombre.trim();
    }

    private static int validarCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa: " + cantidad);
        }
        return cantidad;
    }

    public int getInsumoId() { return insumoId; }
    public void setInsumoId(int insumoId) { this.insumoId = insumoId; }
    public String getNombre() { return nombre; }
    public int getCantidadActual() { return cantidadActual; }
    public void setCantidadActual(int cantidadActual) { this.cantidadActual = validarCantidad(cantidadActual); }
    public int getCantidadMinima() { return cantidadMinima; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }

    public abstract String getTipo();

    /** Descuenta unidades del stock. Lanza StockExcepcion si no alcanza lo disponible. */
    public void retirar(int cantidad) throws StockExcepcion {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a retirar debe ser mayor a cero.");
        }
        if (cantidad > cantidadActual) {
            throw new StockExcepcion("Stock insuficiente de \"" + nombre + "\": disponible "
                    + cantidadActual + ", solicitado " + cantidad + ".", insumoId, cantidadActual);
        }
        cantidadActual -= cantidad;
    }

    /** Suma unidades al stock (reposicion). */
    public void reponer(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reponer debe ser mayor a cero.");
        }
        cantidadActual += cantidad;
    }

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
