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
        this.peso = peso;
        this.observaciones = observaciones;
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
