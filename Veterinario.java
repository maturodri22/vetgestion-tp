package vetgestion.model;

public class Veterinario {

    private int veterinarioId;
    private String nombre;
    private String matricula;

    public Veterinario() { }

    public Veterinario(String nombre, String matricula) {
        this.nombre = nombre;
        this.matricula = matricula;
    }

    public Veterinario(int veterinarioId, String nombre, String matricula) {
        this(nombre, matricula);
        this.veterinarioId = veterinarioId;
    }

    public int getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(int veterinarioId) { this.veterinarioId = veterinarioId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    @Override
    public String toString() {
        return veterinarioId + " | " + nombre + " (Mat. " + matricula + ")";
    }
}
