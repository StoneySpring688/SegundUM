package SegundUM.Productos.rest.api;

import java.math.BigDecimal;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import SegundUM.Productos.dominio.EstadoProducto;
import SegundUM.Productos.rest.ResumenProducto;
import SegundUM.Productos.rest.dto.AltaProductoDTO;
import SegundUM.Productos.rest.dto.LugarRecogidaDTO;
import SegundUM.Productos.rest.dto.ProductoDTO;
import SegundUM.Productos.rest.dto.ProductoUpdateDTO;

@Tag(name = "Productos", description = "Operaciones de gestion del catalogo de productos")
public interface ProductoApi {

    @Operation(summary = "Obtener producto", description = "Devuelve un producto por su ID con enlaces HATEOAS")
    EntityModel<ProductoDTO> getProducto(String id) throws Exception;

    @Operation(summary = "Dar de alta un producto", description = "Crea un nuevo producto asociado al usuario autenticado")
    ResponseEntity<String> altaProducto(AltaProductoDTO dto, Authentication authentication) throws Exception;

    @Operation(summary = "Modificar producto", description = "Actualiza precio y/o descripcion. Solo el vendedor propietario puede modificarlo")
    ResponseEntity<Void> modificarProducto(String productoId, ProductoUpdateDTO dto, Authentication authentication) throws Exception;

    @Operation(summary = "Asociar lugar de recogida", description = "Asigna coordenadas de recogida a un producto. Solo el vendedor propietario puede modificarlo")
    ResponseEntity<Void> asociarLugarRecogida(String productoId, LugarRecogidaDTO dto) throws Exception;

    @Operation(summary = "Registrar visualizacion", description = "Incrementa el contador de visualizaciones del producto")
    ResponseEntity<Void> registrarVisualizacion(String productoId) throws Exception;

    @Operation(summary = "Buscar productos", description = "Busqueda paginada con filtros opcionales de categoria, texto, estado y precio maximo")
    PagedModel<EntityModel<ProductoDTO>> buscarProductos(String categoriaId, String texto, EstadoProducto estadoMinimo, BigDecimal precioMaximo, Pageable paginacion) throws Exception;

    @Operation(summary = "Productos de un vendedor", description = "Devuelve paginados todos los productos publicados por un vendedor")
    PagedModel<EntityModel<ProductoDTO>> getProductosPorVendedor(Pageable paginacion, Authentication authentication) throws Exception;

    @Operation(summary = "Historial mensual", description = "Resumen de productos publicados en un mes y anio concretos")
    PagedModel<EntityModel<ResumenProducto>> historialMes(int mes, int anio, Pageable paginacion) throws Exception;

    @Operation(summary = "Historial mensual de un vendedor", description = "Resumen de productos publicados por un vendedor en un mes y anio concretos")
    PagedModel<EntityModel<ResumenProducto>> historialMesVendedor(String emailVendedor, int mes, int anio, Pageable paginacion) throws Exception;

    @Operation(summary = "Eliminar producto", description = "Elimina un producto. Solo el vendedor propietario puede eliminarlo")
    ResponseEntity<Void> eliminarProducto(String productoId) throws Exception;
}
