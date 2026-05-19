package SegundUM.Productos.adaptadores.RabbitMQ.eventos;

public class EventoEliminacionUsuario extends Evento {
    private String idUsuario;

    public EventoEliminacionUsuario() {}

    public String getIdUsuario() { return idUsuario; }
}