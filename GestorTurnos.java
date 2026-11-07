package vetgestion.control;

import vetgestion.dao.TurnoDAO;
import vetgestion.model.Turno;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase de control: coordina el caso de uso "Agendar turno", tal como
 * se modelo en el diagrama de secuencia de la Etapa 2.
 */
public class GestorTurnos {

    private final TurnoDAO turnoDAO = new TurnoDAO();

    /**
     * Consulta disponibilidad y, si el horario esta libre, registra el turno.
     * Devuelve el id del turno creado, o -1 si el horario ya estaba ocupado.
     */
    public int agendarTurno(int mascotaId, int veterinarioId, LocalDate fecha, LocalTime hora) throws SQLException {
        if (!turnoDAO.estaDisponible(veterinarioId, fecha, hora)) {
            System.out.println("El horario solicitado ya fue tomado por otro turno. Elegi otro horario.");
            return -1;
        }
        Turno turno = new Turno(mascotaId, veterinarioId, fecha, hora, "pendiente");
        int id = turnoDAO.insertar(turno);
        System.out.println("Turno registrado con id: " + id);
        return id;
    }

    public void confirmarTurno(int turnoId) throws SQLException {
        turnoDAO.actualizarEstado(turnoId, "confirmado");
    }

    public void cancelarTurno(int turnoId) throws SQLException {
        turnoDAO.actualizarEstado(turnoId, "cancelado");
    }
}
