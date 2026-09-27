package vetgestion.model;

public class Cliente {

    private int clienteId;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;

    public Cliente() { }

    public Cliente(String nombre, String apellido, String telefono, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
    }

    public Cliente(int clienteId, String nombre, String apellido, String telefono, String email) {
        this(nombre, apellido, telefono, email);
        this.clienteId = clienteId;
    }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return clienteId + " | " + nombre + " " + apellido + " | " + telefono + " | " + email;
    }
}
