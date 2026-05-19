package SegundUM.Usuarios.usuarios.rest.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import SegundUM.Usuarios.usuarios.modelo.Usuario;

/**
 * DTO para exponer los datos de un usuario en respuestas REST.
 * Incluye enlaces HATEOAS en el campo {@code _links}.
 */
public class UsuarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String email;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String telefono;
    private boolean administrador;
    private int ventasRealizadas;
    private int comprasRealizadas;

    private Map<String, String> _links = new HashMap<>();

    public UsuarioDTO() {}

    public static UsuarioDTO fromEntity(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.id = u.getId();
        dto.email = u.getEmail();
        dto.nombre = u.getNombre();
        dto.apellidos = u.getApellidos();
        dto.fechaNacimiento = u.getFechaNacimiento();
        dto.telefono = u.getTelefono();
        dto.administrador = u.isAdministrador();
        dto.ventasRealizadas = u.getVentasRealizadas();
        dto.comprasRealizadas = u.getComprasRealizadas();
        return dto;
    }

    public void addLink(String rel, String href) {
        _links.put(rel, href);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public boolean isAdministrador() { return administrador; }
    public void setAdministrador(boolean administrador) { this.administrador = administrador; }
    public int getVentasRealizadas() { return ventasRealizadas; }
    public void setVentasRealizadas(int ventasRealizadas) { this.ventasRealizadas = ventasRealizadas; }
    public int getComprasRealizadas() { return comprasRealizadas; }
    public void setComprasRealizadas(int comprasRealizadas) { this.comprasRealizadas = comprasRealizadas; }
    public Map<String, String> get_links() { return _links; }
    public void set_links(Map<String, String> _links) { this._links = _links; }
}
