package SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos;

import java.time.LocalDateTime;

import SegundUM.Usuarios.usuarios.modelo.Usuario;

public class EventoModificacionUsuario extends Evento {
    private String idUsuario;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String fechaNacimiento;

    public EventoModificacionUsuario() {}

    public EventoModificacionUsuario(Usuario u) {
        super("usuario-modificado", LocalDateTime.now().toString());
        this.idUsuario = u.getId();
        this.nombre = u.getNombre();
        this.apellidos = u.getApellidos();
        this.telefono = u.getTelefono();
        this.fechaNacimiento = u.getFechaNacimiento() != null ? u.getFechaNacimiento().toString() : null;
    }

    public String getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getTelefono() { return telefono; }
    public String getFechaNacimiento() { return fechaNacimiento; }
}