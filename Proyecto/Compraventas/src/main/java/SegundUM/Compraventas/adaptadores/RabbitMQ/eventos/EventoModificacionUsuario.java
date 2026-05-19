package SegundUM.Compraventas.adaptadores.RabbitMQ.eventos;

public class EventoModificacionUsuario extends Evento {
    private String idUsuario;
    private String nombre;

    public EventoModificacionUsuario() {}

    public String getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
}