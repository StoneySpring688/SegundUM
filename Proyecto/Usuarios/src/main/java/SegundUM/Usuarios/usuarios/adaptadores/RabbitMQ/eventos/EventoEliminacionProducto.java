package SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos;

public class EventoEliminacionProducto extends Evento {
    private String idProducto;
    private String vendedorId;

    public EventoEliminacionProducto() {}

    public String getIdProducto() { return idProducto; }
    public String getVendedorId() { return vendedorId; }
}