package SegundUM.Compraventas.rest.api;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import SegundUM.Compraventas.rest.dto.CompraventaDTO;
import SegundUM.Compraventas.rest.dto.NuevaCompraventaDTO;

@Tag(name = "Compraventas", description = "Operaciones de gestion de compraventas entre usuarios")
public interface CompraventaApi {

    @Operation(summary = "Realizar una compra", description = "Registra una nueva compraventa verificando stock y usuarios")
    ResponseEntity<EntityModel<CompraventaDTO>> realizarCompra(NuevaCompraventaDTO dto) throws Exception;

    @Operation(summary = "Historial de compras", description = "Devuelve paginadas todas las compras realizadas por un usuario")
    PagedModel<EntityModel<CompraventaDTO>> recuperarComprasDeUsuario(Pageable paginacion) throws Exception;

    @Operation(summary = "Historial de ventas", description = "Devuelve paginadas todas las ventas realizadas por un usuario")
    PagedModel<EntityModel<CompraventaDTO>> recuperarVentasDeUsuario(String idVendedor, Pageable paginacion) throws Exception;

    @Operation(summary = "Historial entre dos usuarios", description = "Devuelve las operaciones exactas entre un comprador y un vendedor. Requiere rol ADMINISTRADOR")
    PagedModel<EntityModel<CompraventaDTO>> recuperarCompraventasEntre(String idComprador, String idVendedor, Pageable paginacion) throws Exception;
}
