package vetgestion.model;

import java.time.LocalDate;

public class ConsultaMedica {

    private int consultaId;
    private int mascotaId;
    private int veterinarioId;
    private LocalDate fecha;
    private String diagnostico;
    private String tratamiento;
    private double peso;
    private String observaciones;

    public ConsultaMedica() { }

    public ConsultaMedica(int mascotaId, int veterinarioId, LocalDate fecha, String diagnostico,
                           String tratamiento, double peso, String observaciones) {
        this.mascotaId = mascotaId;
        this.veterinarioId = veterinarioId;
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.peso = validarPeso(peso);
        this.observaciones = observaciones;
    }

    /** Regla de negocio (misma que el CHECK de la base): el peso debe estar entre 0 (excluido) y 200 kg. */
    private static double validarPeso(double peso) {
        if (peso <= 0 || peso > 200) {
            throw new IllegalArgumentException("El peso debe ser mayor a 0 y no superar 200 kg: " + peso);
        }
        return peso;
    }

    public int getConsultaId() { return consultaId; }
    public void setConsultaId(int consultaId) { this.consultaId = consultaId; }
    public int getMascotaId() { return mascotaId; }
    public int getVeterinarioId() { return veterinarioId; }
    public LocalDate getFecha() { return fecha; }
    public String getDiagnostico() { return diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public double getPeso() { return peso; }
    public String getObservaciones() { return observaciones; }

    @Override
    public String toString() {
        return consultaId + " | " + fecha + " | " + diagnostico + " | peso: " + peso + "kg";
    }
}
