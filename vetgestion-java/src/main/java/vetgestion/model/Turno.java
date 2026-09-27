package vetgestion.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase de entidad: representa un turno agendado para una mascota
 * con un veterinario en una fecha y hora determinadas.
 */
public class Turno {

    private int turnoId;
    private int mascotaId;
    private int veterinarioId;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado; // pendiente, confirmado, cancelado, atendido

    public Turno() { }

    public Turno(int mascotaId, int veterinarioId, LocalDate fecha, LocalTime hora, String estado) {
        this.mascotaId = mascotaId;
        this.veterinarioId = veterinarioId;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public Turno(int turnoId, int mascotaId, int veterinarioId, LocalDate fecha, LocalTime hora, String estado) {
        this(mascotaId, veterinarioId, fecha, hora, estado);
        this.turnoId = turnoId;
    }

    public int getTurnoId() { return turnoId; }
    public void setTurnoId(int turnoId) { this.turnoId = turnoId; }
    public int getMascotaId() { return mascotaId; }
    public int getVeterinarioId() { return veterinarioId; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public String getEstado() { return estado; }

    public void confirmar() {
        this.estado = "confirmado";
        System.out.println("Turno " + turnoId + " confirmado.");
    }

    public void cancelar() {
        this.estado = "cancelado";
        System.out.println("Turno " + turnoId + " cancelado.");
    }

    public void reprogramar(LocalDate nuevaFecha, LocalTime nuevaHora) {
        this.fecha = nuevaFecha;
        this.hora = nuevaHora;
        this.estado = "pendiente";
        System.out.println("Turno " + turnoId + " reprogramado para " + nuevaFecha + " " + nuevaHora);
    }

    @Override
    public String toString() {
        return turnoId + " | " + fecha + " " + hora + " | estado: " + estado;
    }
}
