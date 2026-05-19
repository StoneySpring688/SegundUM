package SegundUM.Compraventas.adaptadores.RabbitMQ.eventos;

public class EventoEliminacionProducto extends Evento {
    private String idProducto;

    public EventoEliminacionProducto() {}

    public String getIdProducto() { return idProducto; }
}