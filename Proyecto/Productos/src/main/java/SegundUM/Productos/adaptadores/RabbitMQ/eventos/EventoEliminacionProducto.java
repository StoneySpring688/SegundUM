package SegundUM.Productos.adaptadores.RabbitMQ.eventos;

import java.time.LocalDateTime;

public class EventoEliminacionProducto extends Evento {
    private String idProducto;
    private String vendedorId;

    public EventoEliminacionProducto() {}

    public EventoEliminacionProducto(String idProducto, String vendedorId) {
        super("producto-eliminado", LocalDateTime.now().toString());
        this.idProducto = idProducto;
        this.vendedorId = vendedorId;
    }

    public String getIdProducto() { return idProducto; }
    public String getVendedorId() { return vendedorId; }
}