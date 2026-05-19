package SegundUM.Compraventas.adaptadores.RabbitMQ.eventos;

import java.time.LocalDateTime;

import SegundUM.Compraventas.dominio.Compraventa;

public class EventoCreacionCompraventa extends Evento {
    private String idCompraventa;
    private String idProducto;
    private String tituloProducto;
    private String precio;
    private String idComprador;
    private String nombreComprador;
    private String idVendedor;
    private String nombreVendedor;

    public EventoCreacionCompraventa() {}

    public EventoCreacionCompraventa(Compraventa c) {
        super("compraventa-creada", LocalDateTime.now().toString());
        this.idCompraventa = c.getId();
        this.idProducto = c.getIdProducto();
        this.tituloProducto = c.getTitulo();
        this.precio = String.valueOf(c.getPrecio());
        this.idComprador = c.getIdComprador();
        this.nombreComprador = c.getNombreComprador();
        this.idVendedor = c.getIdVendedor();
        this.nombreVendedor = c.getNombreVendedor();
    }

    public String getIdCompraventa() { return idCompraventa; }
    public String getIdProducto() { return idProducto; }
    public String getTituloProducto() { return tituloProducto; }
    public String getPrecio() { return precio; }
    public String getIdComprador() { return idComprador; }
    public String getNombreComprador() { return nombreComprador; }
    public String getIdVendedor() { return idVendedor; }
    public String getNombreVendedor() { return nombreVendedor; }
}