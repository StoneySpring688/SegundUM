package SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos;

public class EventoCreacionCompraventa extends Evento {
    private String idProducto;
    private String idComprador;
    private String idVendedor;

    public EventoCreacionCompraventa() {}

    public String getIdProducto() { return idProducto; }
    public String getIdComprador() { return idComprador; }
    public String getIdVendedor() { return idVendedor; }
}