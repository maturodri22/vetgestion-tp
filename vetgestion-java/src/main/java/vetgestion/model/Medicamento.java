package vetgestion.model;

import java.time.LocalDate;

public class Medicamento extends Insumo {

    private String principioActivo;
    private boolean requiereReceta;

    public Medicamento() { }

    public Medicamento(String nombre, int cantidadActual, int cantidadMinima, LocalDate fechaVencimiento,
                        String principioActivo, boolean requiereReceta) {
        super(nombre, cantidadActual, cantidadMinima, fechaVencimiento);
        this.principioActivo = principioActivo;
        this.requiereReceta = requiereReceta;
    }

    public String getPrincipioActivo() { return principioActivo; }
    public boolean isRequiereReceta() { return requiereReceta; }

    @Override
    public String getTipo() { return "medicamento"; }

    @Override
    public void registrarAlerta() {
        if (estaEnStockMinimo()) {
            System.out.println("[ALERTA MEDICAMENTO] \"" + nombre + "\" (" + principioActivo
                    + ") esta en stock minimo: " + cantidadActual + " unidades disponibles."
                    + (requiereReceta ? " Requiere receta para su dispensa." : ""));
        }
        if (estaProximoAVencer()) {
            System.out.println("[ALERTA MEDICAMENTO] \"" + nombre + "\" vence pronto (" + fechaVencimiento + ").");
        }
    }
}
