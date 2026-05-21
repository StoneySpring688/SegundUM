package SegundUM.Compraventas.rest.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Este DTO es para los POST 
 **/
public class NuevaCompraventaDTO implements Serializable {
    
    private static final long serialVersionUID = 1565480739304972359L;

	@NotBlank(message = "El ID del producto es obligatorio")
    private String idProducto;

    // Getters y Setters
    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
    
}