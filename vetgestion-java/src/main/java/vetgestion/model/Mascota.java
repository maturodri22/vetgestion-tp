package vetgestion.model;

import java.time.LocalDate;

public class Mascota {

    private int mascotaId;
    private int clienteId;
    private String nombre;
    private String especie;
    private String raza;
    private LocalDate fechaNacimiento;

    public Mascota() { }

    public Mascota(int clienteId, String nombre, String especie, String raza, LocalDate fechaNacimiento) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Mascota(int mascotaId, int clienteId, String nombre, String especie, String raza, LocalDate fechaNacimiento) {
        this(clienteId, nombre, especie, raza, fechaNacimiento);
        this.mascotaId = mascotaId;
    }

    public int getMascotaId() { return mascotaId; }
    public void setMascotaId(int mascotaId) { this.mascotaId = mascotaId; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    @Override
    public String toString() {
        return mascotaId + " | " + nombre + " (" + especie + (raza != null ? ", " + raza : "") + ")";
    }
}
