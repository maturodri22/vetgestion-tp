package vetgestion.model;

import java.time.LocalDate;

public class Vacunacion {

    private int vacunacionId;
    private int consultaId;
    private int insumoId;
    private LocalDate fechaAplicacion;
    private LocalDate fechaRefuerzo;

    public Vacunacion() { }

    public Vacunacion(int consultaId, int insumoId, LocalDate fechaAplicacion, LocalDate fechaRefuerzo) {
        this.consultaId = consultaId;
        this.insumoId = insumoId;
        this.fechaAplicacion = fechaAplicacion;
        this.fechaRefuerzo = fechaRefuerzo;
    }

    public int getVacunacionId() { return vacunacionId; }
    public void setVacunacionId(int vacunacionId) { this.vacunacionId = vacunacionId; }
    public int getConsultaId() { return consultaId; }
    public int getInsumoId() { return insumoId; }
    public LocalDate getFechaAplicacion() { return fechaAplicacion; }
    public LocalDate getFechaRefuerzo() { return fechaRefuerzo; }

    public LocalDate calcularRefuerzo() {
        return fechaRefuerzo;
    }

    @Override
    public String toString() {
        return vacunacionId + " | aplicada: " + fechaAplicacion + " | refuerzo: " + fechaRefuerzo;
    }
}
