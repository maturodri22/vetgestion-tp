package vetgestion.control;

import vetgestion.dao.MascotaDAO;
import vetgestion.dao.TurnoDAO;
import vetgestion.model.Mascota;
import vetgestion.model.Turno;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Clase de control: sala de espera de la clinica.
 *
 * Estructura de datos:
 *  - COLA (FIFO, LinkedList): las mascotas que llegan se atienden por orden
 *    de llegada. El primero que ingresa a la sala es el primero en pasar
 *    al consultorio.
 */
public class GestorSalaEspera {

    private final TurnoDAO turnoDAO = new TurnoDAO();
    private final MascotaDAO mascotaDAO = new MascotaDAO();
    private final Queue<Turno> salaEspera = new LinkedList<>();

    /** Registra la llegada de la mascota de un turno (offer: agrega al final de la cola). */
    public boolean registrarLlegada(int turnoId) throws SQLException {
        Turno turno = turnoDAO.buscarPorId(turnoId);
        if (turno == null) {
            System.out.println("No existe un turno con id " + turnoId + ".");
            return false;
        }
        if (!turno.puedeIngresarASala()) {
            System.out.println("El turno " + turnoId + " esta en estado \"" + turno.getEstado()
                    + "\" y no puede ingresar a la sala de espera.");
            return false;
        }
        for (Turno t : salaEspera) {
            if (t.getTurnoId() == turnoId) {
                System.out.println("Ese turno ya se encuentra en la sala de espera.");
                return false;
            }
        }
        salaEspera.offer(turno);
        System.out.println(nombreDeMascota(turno) + " ingreso a la sala de espera (posicion "
                + salaEspera.size() + ").");
        return true;
    }

    /** Llama al primero de la cola (poll) y marca su turno como atendido. */
    public Turno atenderSiguiente() throws SQLException {
        Turno turno = salaEspera.poll();
        if (turno == null) {
            System.out.println("La sala de espera esta vacia.");
            return null;
        }
        turnoDAO.actualizarEstado(turno.getTurnoId(), "atendido");
        turno.atender();
        System.out.println("Pasa al consultorio: " + nombreDeMascota(turno) + " (turno de las " + turno.getHora() + ").");
        return turno;
    }

    /** Devuelve, en orden de llegada, una linea por cada mascota que espera. */
    public List<String> describirSala() throws SQLException {
        List<String> lineas = new ArrayList<>();
        int posicion = 1;
        for (Turno t : salaEspera) {
            lineas.add(posicion + ". " + nombreDeMascota(t) + " - turno " + t.getTurnoId() + " (" + t.getHora() + ")");
            posicion++;
        }
        return lineas;
    }

    private String nombreDeMascota(Turno turno) throws SQLException {
        Mascota m = mascotaDAO.buscarPorId(turno.getMascotaId());
        return m != null ? m.getNombre() : "Mascota " + turno.getMascotaId();
    }
}
