package SegundUM.Productos.adaptadores.RabbitMQ.eventos;

import java.time.LocalDateTime;

import SegundUM.Productos.dominio.Producto;

public class EventoCreacionProducto extends Evento {
    private String idProducto;
    private String titulo;
    private String descripcion;
    private String precio;
    private String estado;
    private String idCategoria;
    private String nombreCategoria;
    private boolean envioDisponible;
    private String vendedorId;

    public EventoCreacionProducto() {}

    public EventoCreacionProducto(Producto p) {
        super("producto-creado", LocalDateTime.now().toString());
        this.idProducto = p.getId();
        this.titulo = p.getTitulo();
        this.descripcion = p.getDescripcion();
        this.precio = p.getPrecio() != null ? p.getPrecio().toString() : null;
        this.estado = p.getEstado() != null ? p.getEstado().name() : null;
        this.idCategoria = p.getCategoria() != null ? p.getCategoria().getId() : null;
        this.nombreCategoria = p.getCategoria() != null ? p.getCategoria().getNombre() : null;
        this.envioDisponible = p.isEnvioDisponible();
        this.vendedorId = p.getVendedorId();
    }

    public String getIdProducto() { return idProducto; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getPrecio() { return precio; }
    public String getEstado() { return estado; }
    public String getIdCategoria() { return idCategoria; }
    public String getNombreCategoria() { return nombreCategoria; }
    public boolean isEnvioDisponible() { return envioDisponible; }
    public String getVendedorId() { return vendedorId; }
}