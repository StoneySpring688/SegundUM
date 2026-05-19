package SegundUM.pasarela.rest.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO con los datos necesarios para dar de alta un nuevo usuario en el microservicio Usuarios.
 */
public class NuevoUsuarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public String email;
    public String nombre;
    public String apellidos;
    public String clave;
    public LocalDate fechaNacimiento;
    public String telefono;
    /** Opcional. Sólo se rellena en altas vía OAuth2/GitHub. */
    public String idGitHub;

    public NuevoUsuarioDTO() {}
}
