package SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos;

public class EventoCreacionProducto extends Evento {
    private String idProducto;
    private String vendedorId;

    public EventoCreacionProducto() {}

    public String getIdProducto() { return idProducto; }
    public String getVendedorId() { return vendedorId; }
}