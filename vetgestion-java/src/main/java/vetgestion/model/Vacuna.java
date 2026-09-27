package vetgestion.model;

import java.time.LocalDate;

public class Vacuna extends Insumo {

    private String enfermedadPrevenida;
    private int dosisRequeridas;

    public Vacuna() { }

    public Vacuna(String nombre, int cantidadActual, int cantidadMinima, LocalDate fechaVencimiento,
                  String enfermedadPrevenida, int dosisRequeridas) {
        super(nombre, cantidadActual, cantidadMinima, fechaVencimiento);
        this.enfermedadPrevenida = enfermedadPrevenida;
        this.dosisRequeridas = dosisRequeridas;
    }

    public String getEnfermedadPrevenida() { return enfermedadPrevenida; }
    public int getDosisRequeridas() { return dosisRequeridas; }

    @Override
    public String getTipo() { return "vacuna"; }

    @Override
    public void registrarAlerta() {
        if (estaEnStockMinimo()) {
            System.out.println("[ALERTA VACUNA] \"" + nombre + "\" (previene " + enfermedadPrevenida
                    + ") esta en stock minimo: " + cantidadActual + " unidades disponibles.");
        }
        if (estaProximoAVencer()) {
            System.out.println("[ALERTA VACUNA] \"" + nombre + "\" vence pronto (" + fechaVencimiento
                    + "). Priorizar su aplicacion o gestionar reposicion.");
        }
    }
}
