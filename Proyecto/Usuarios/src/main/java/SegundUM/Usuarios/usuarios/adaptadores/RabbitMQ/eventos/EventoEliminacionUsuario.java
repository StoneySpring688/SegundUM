package SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos;

import java.time.LocalDateTime;

public class EventoEliminacionUsuario extends Evento {
    private String idUsuario;

    public EventoEliminacionUsuario() {}

    public EventoEliminacionUsuario(String idUsuario) {
        super("usuario-eliminado", LocalDateTime.now().toString());
        this.idUsuario = idUsuario;
    }

    public String getIdUsuario() { return idUsuario; }
}