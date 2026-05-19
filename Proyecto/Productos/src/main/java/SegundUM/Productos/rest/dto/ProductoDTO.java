package SegundUM.Productos.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;

import SegundUM.Productos.dominio.EstadoProducto;
import SegundUM.Productos.dominio.Producto;

public class ProductoDTO extends RepresentationModel<ProductoDTO> implements Serializable {
    
    private static final long serialVersionUID = 7064953061294088595L;
	public String id;
	
	
    public String titulo;
	
    public String descripcion;

    public BigDecimal precio;
	
    public EstadoProducto estado;

    public LocalDateTime fechaPublicacion;
	
    public Integer visualizaciones;
	
    public boolean envioDisponible;
    
    public String vendedorId;
    
    public String categoriaId;
    public String categoriaNombre;
    
    public LugarRecogidaDTO recogida;

    public boolean vendido;

    public ProductoDTO() {}

    public static ProductoDTO fromEntity(Producto p) {
        if (p == null) return null;

        ProductoDTO dto = new ProductoDTO();
        dto.id = p.getId();
        dto.titulo = p.getTitulo();
        dto.descripcion = p.getDescripcion();
        dto.precio = p.getPrecio();
        dto.estado = p.getEstado();
        dto.fechaPublicacion = p.getFechaPublicacion();
        dto.visualizaciones = p.getVisualizaciones();
        dto.envioDisponible = p.isEnvioDisponible();
        dto.vendedorId = p.getVendedorId();

        if (p.getCategoria() != null) {
            dto.categoriaId = p.getCategoria().getId();
            dto.categoriaNombre = p.getCategoria().getNombre();
        }

        dto.recogida = LugarRecogidaDTO.fromEntity(p.getRecogida());
        dto.vendido = p.isVendido();

        return dto;
    }
    
    @Override
    public String toString() {
    			return "ProductoDTO{" +
				"id='" + id + '\'' +
				", titulo='" + titulo + '\'' +
				", descripcion='" + descripcion + '\'' +
				", precio=" + precio +
				", estado=" + estado +
				", fechaPublicacion=" + fechaPublicacion +
				", visualizaciones=" + visualizaciones +
				", envioDisponible=" + envioDisponible +
				", vendedorId='" + vendedorId + '\'' +
				", categoriaId='" + categoriaId + '\'' +
				", categoriaNombre='" + categoriaNombre + '\'' +
				", recogida=" + recogida +
				'}';
    			}
}