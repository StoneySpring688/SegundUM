package SegundUM.Compraventas.rest.controllers;

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
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import SegundUM.Compraventas.rest.api.CompraventaApi;
import SegundUM.Compraventas.dominio.Compraventa;
import SegundUM.Compraventas.rest.dto.CompraventaDTO;
import SegundUM.Compraventas.rest.dto.NuevaCompraventaDTO;
import SegundUM.Compraventas.servicio.compraventa.ServicioCompraventa;

/** Controlador REST que expone los endpoints de compraventas con paginación y enlaces HATEOAS. */
@RestController
@RequestMapping(value = "/api/compraventas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompraventaRestController implements CompraventaApi {

    private static final Logger logger = LoggerFactory.getLogger(CompraventaRestController.class);

    private final ServicioCompraventa servicioCompraventa;
    private final PagedResourcesAssembler<CompraventaDTO> pagedResourcesAssembler;

    @Autowired
    public CompraventaRestController(ServicioCompraventa servicioCompraventa,
                                     PagedResourcesAssembler<CompraventaDTO> pagedResourcesAssembler) {
        this.servicioCompraventa = servicioCompraventa;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USUARIO') and #dto.idComprador == authentication.principal")
    public ResponseEntity<EntityModel<CompraventaDTO>> realizarCompra(@Valid @RequestBody NuevaCompraventaDTO dto) throws Exception {
        
        logger.info("Petición REST para realizar compra. Producto: {}, Comprador: {}", dto.getIdProducto(), dto.getIdComprador());
        
        // El token crudo (sin "Bearer ") lo almacena el filtro JWT como credencial
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) auth.getCredentials();
        
        Compraventa guardada;
        
            guardada = servicioCompraventa.realizarCompra(
                    dto.getIdProducto(), 
                    dto.getIdComprador(), 
                    token
            );
            
        CompraventaDTO dtoRespuesta = CompraventaDTO.fromEntity(guardada);
        
        EntityModel<CompraventaDTO> model = EntityModel.of(dtoRespuesta);
        model.add(
        		WebMvcLinkBuilder
        		.linkTo(WebMvcLinkBuilder.methodOn(CompraventaRestController.class)
        				.recuperarCompraventasEntre(guardada.getIdComprador(), guardada.getIdVendedor(), Pageable.ofSize(1)))
        		.withSelfRel());

        // Generamos la URI hacia el recurso creado
        URI nuevaURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardada.getId())
                .toUri();
                
        return ResponseEntity.created(nuevaURI).body(model);

        
       
        
    }

    @GetMapping("/comprador/{idComprador}")
    @PreAuthorize("hasAuthority('USUARIO') and #idComprador == authentication.principal")
    public PagedModel<EntityModel<CompraventaDTO>> recuperarComprasDeUsuario(
            @PathVariable String idComprador,
            @ParameterObject Pageable paginacion) throws Exception {

        Page<Compraventa> pagina = servicioCompraventa.recuperarComprasDeUsuario(idComprador, paginacion);
        Page<CompraventaDTO> paginaDTO = pagina.map(CompraventaDTO::fromEntity);

        return pagedResourcesAssembler.toModel(paginaDTO);
    }

    @GetMapping("/vendedor/{idVendedor}")
    @PreAuthorize("hasAuthority('USUARIO') and #idVendedor == authentication.principal")
    public PagedModel<EntityModel<CompraventaDTO>> recuperarVentasDeUsuario(
            @PathVariable String idVendedor,
            @ParameterObject Pageable paginacion) throws Exception {

        Page<Compraventa> pagina = servicioCompraventa.recuperarVentasDeUsuario(idVendedor, paginacion);
        Page<CompraventaDTO> paginaDTO = pagina.map(CompraventaDTO::fromEntity);

        return pagedResourcesAssembler.toModel(paginaDTO);
    }

    @GetMapping("/entre")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public PagedModel<EntityModel<CompraventaDTO>> recuperarCompraventasEntre(
            @RequestParam String idComprador,
            @RequestParam String idVendedor,
            @ParameterObject Pageable paginacion) throws Exception {

        Page<Compraventa> pagina = servicioCompraventa.recuperarCompraventasEntre(idComprador, idVendedor, paginacion);
        Page<CompraventaDTO> paginaDTO = pagina.map(CompraventaDTO::fromEntity);

        return pagedResourcesAssembler.toModel(paginaDTO);
    }
}
