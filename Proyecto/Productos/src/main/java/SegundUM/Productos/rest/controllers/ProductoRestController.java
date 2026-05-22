package SegundUM.Productos.rest.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.math.BigDecimal;
import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import SegundUM.Productos.dominio.EstadoProducto;
import SegundUM.Productos.dominio.Producto;
import SegundUM.Productos.rest.ResumenProducto;
import SegundUM.Productos.rest.ResumenProductoAssembler;
import SegundUM.Productos.rest.api.ProductoApi;
import SegundUM.Productos.rest.dto.AltaProductoDTO;
import SegundUM.Productos.rest.dto.LugarRecogidaDTO;
import SegundUM.Productos.rest.dto.ProductoDTO;
import SegundUM.Productos.rest.dto.ProductoUpdateDTO;
import SegundUM.Productos.servicio.productos.ServicioProductos;

/** Controlador REST que expone la API de productos con soporte HATEOAS. */
@RestController
@RequestMapping("/api/productos")
public class ProductoRestController implements ProductoApi {

    private static final Logger logger = LoggerFactory.getLogger(ProductoRestController.class);

    private final ServicioProductos servicioProductos;

    @Autowired
    private PagedResourcesAssembler<ProductoDTO> pagedResourcesAssembler;

    @Autowired
    private PagedResourcesAssembler<ResumenProducto> pagedResourcesAssemblerRP;

    @Autowired
    private ResumenProductoAssembler resumenProductoAssembler;

    @Autowired
    public ProductoRestController(ServicioProductos servicioProductos) {
        this.servicioProductos = servicioProductos;
    }

    /** GET /productos/{id} — Obtener un producto por ID */
    @GetMapping("/{id}")
    public EntityModel<ProductoDTO> getProducto(@PathVariable String id) throws Exception {
        Producto p = servicioProductos.getProductoPorId(id);
        ProductoDTO dto = ProductoDTO.fromEntity(p);
        logger.info("Producto obtenido: {}", dto.toString());
        return EntityModel.of(dto,
                linkTo(methodOn(ProductoRestController.class).getProducto(id)).withSelfRel(),
                linkTo(methodOn(ProductoRestController.class).modificarProducto(id, null, null)).withRel("modificar"),
                linkTo(methodOn(ProductoRestController.class).asociarLugarRecogida(id, null)).withRel("recogida"),
                linkTo(methodOn(ProductoRestController.class).registrarVisualizacion(id)).withRel("visualizacion"),
                linkTo(methodOn(ProductoRestController.class).eliminarProducto(id)).withRel("eliminar"));
    }

    /** POST /productos — Dar de alta un producto  */
    @PostMapping
    @PreAuthorize("hasAuthority('USUARIO')")
    public ResponseEntity<String> altaProducto(@Valid @RequestBody AltaProductoDTO dto, Authentication authentication) throws Exception {
        String vendedorId = authentication.getName();
        String id = servicioProductos.altaProducto(dto.titulo, dto.descripcion, dto.precio, dto.estado,
                dto.categoriaId, dto.envioDisponible, vendedorId,
                dto.recogida != null ? dto.recogida.toEntity() : null);

        // Construir la URI del recurso recién creado para devolverla en la cabecera Location
        URI nuevaURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(nuevaURI).body(id);
    }

    /** PUT /productos/{id} — Modificar precio y/o descripción (con verificación de propietario) */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO')")
    public ResponseEntity<Void> modificarProducto(
            @PathVariable("id") String productoId,
            @Valid @RequestBody ProductoUpdateDTO dto,
            Authentication authentication) throws Exception {

        String vendedorId = authentication.getName();
        servicioProductos.modificarProducto(productoId, dto.descripcion, dto.precio, vendedorId);
        return ResponseEntity.noContent().build();
    }

    /** PUT /productos/{id}/recogida — Asociar lugar de recogida a un producto */
    @PutMapping("/{id}/recogida")
    @PreAuthorize("hasAuthority('USUARIO')")
    public ResponseEntity<Void> asociarLugarRecogida(
            @PathVariable("id") String productoId,
            @Valid @RequestBody LugarRecogidaDTO dto) throws Exception {

        Producto producto = servicioProductos.getProductoPorId(productoId);
        // Verificar que el usuario autenticado es el propietario del producto
        String usuarioLogueado = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!producto.getVendedorId().equals(usuarioLogueado)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        servicioProductos.asignarLugarRecogida(productoId, dto.descripcion, dto.longitud, dto.latitud);
        return ResponseEntity.noContent().build();
    }

    /** PUT /productos/{id}/visualizaciones — Registrar una nueva visualización */
    @PutMapping("/{id}/visualizaciones")
    public ResponseEntity<Void> registrarVisualizacion(@PathVariable("id") String productoId) throws Exception {
        servicioProductos.anadirVisualizacion(productoId);
        return ResponseEntity.noContent().build();
    }

    /** GET /productos/buscar — Buscar productos con filtros opcionales */
    @GetMapping("/buscar")
    public PagedModel<EntityModel<ProductoDTO>> buscarProductos(
            @RequestParam(required = false) String categoriaId,
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) EstadoProducto estadoMinimo,
            @RequestParam(required = false) BigDecimal precioMaximo,
            @ParameterObject Pageable paginacion) throws Exception {
        Page<Producto> productos = servicioProductos.buscarProductos(categoriaId, texto, estadoMinimo, precioMaximo, paginacion);
        return pagedResourcesAssembler.toModel(productos.map(ProductoDTO::fromEntity));
    }

    /** GET /productos/vendedor/{vendedorId} — Obtener productos de un vendedor */
    @GetMapping("/vendedor")
    public PagedModel<EntityModel<ProductoDTO>> getProductosPorVendedor(
            @ParameterObject Pageable paginacion, Authentication authentication) throws Exception {
    	String vendedorId = authentication.getName();
        Page<Producto> productos = servicioProductos.getProductosPorVendedor(vendedorId, paginacion);
        return pagedResourcesAssembler.toModel(productos.map(ProductoDTO::fromEntity));
    }

    /** GET /productos/historial?mes=X&anio=Y — Resumen mensual de productos */
    @GetMapping("/historial")
    public PagedModel<EntityModel<ResumenProducto>> historialMes(
            @RequestParam int mes,
            @RequestParam int anio,
            @ParameterObject Pageable paginacion) throws Exception {
        Page<ResumenProducto> resumen = servicioProductos.historialMes(mes, anio, paginacion);
        return pagedResourcesAssemblerRP.toModel(resumen, resumenProductoAssembler);
    }

    /** GET /productos/historial/{email}?mes=X&anio=Y — Resumen mensual de un vendedor */
    @GetMapping("/historial/{email}")
    public PagedModel<EntityModel<ResumenProducto>> historialMesVendedor(
            @PathVariable("email") String emailVendedor,
            @RequestParam int mes,
            @RequestParam int anio,
            @ParameterObject Pageable paginacion) throws Exception {
        Page<ResumenProducto> resumen = servicioProductos.historialMesVendedor(mes, anio, emailVendedor, paginacion);
        return pagedResourcesAssemblerRP.toModel(resumen, resumenProductoAssembler);
    }

    /** DELETE /productos/{id} — Eliminar un producto */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable("id") String productoId) throws Exception {
        Producto producto = servicioProductos.getProductoPorId(productoId);
        String usuarioLogueado = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!producto.getVendedorId().equals(usuarioLogueado)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        servicioProductos.eliminarProducto(productoId);
        return ResponseEntity.noContent().build();
    }
}
