package SegundUM.Productos.adaptadores.RabbitMQ.eventos;

public class EventoCreacionCompraventa extends Evento {
    private String idProducto;

    public EventoCreacionCompraventa() {}

    public String getIdProducto() { return idProducto; }
}