package SegundUM.Productos.rest.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SegundUM.Productos.rest.CategoriaAssembler;
import SegundUM.Productos.rest.ResumenCategoria;
import SegundUM.Productos.rest.api.CategoriaApi;
import SegundUM.Productos.rest.dto.CategoriaDTO;
import SegundUM.Productos.servicio.categorias.ServicioCategorias;

import org.springdoc.api.annotations.ParameterObject;

/** Controlador REST que expone la API de categorías con soporte HATEOAS. */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaRestController implements CategoriaApi {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaRestController.class);

    private final ServicioCategorias servicioCategorias;

    @Autowired
    private PagedResourcesAssembler<ResumenCategoria> pagedResourcesAssembler;

    @Autowired
    private CategoriaAssembler categoriaAssembler;

    @Autowired
    public CategoriaRestController(ServicioCategorias servicioCategorias) {
        this.servicioCategorias = servicioCategorias;
    }

    /** POST /api/categorias/cargar — Carga masiva de categorías (ADMIN) */
    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<String> cargarTodas() throws Exception {
        logger.info("Peticion recibida: POST /api/categorias/cargar (ADMIN)");
        
        int totalCargadas = servicioCategorias.cargarTodas();
        return ResponseEntity.ok("Se han cargado " + totalCargadas + " ficheros de categorías correctamente.");
    }


    /** GET /categorias/{id} — Obtener una categoría por ID */
    @GetMapping("/{id}")
    public EntityModel<CategoriaDTO> getCategoria(@PathVariable String id) throws Exception {
        logger.info("Peticion recibida: GET /categorias/{}", id);
        CategoriaDTO dto = CategoriaDTO.fromEntity(servicioCategorias.getCategoriaById(id));
        return EntityModel.of(dto,
                linkTo(methodOn(CategoriaRestController.class).getCategoria(id)).withSelfRel(),
                linkTo(methodOn(CategoriaRestController.class).getCategoriasPaginado(null)).withRel("todas"));
    }


    /** GET /api/categorias — Listado paginado de categorías (resumen) */
    @GetMapping
    public PagedModel<EntityModel<ResumenCategoria>> getCategoriasPaginado(
            @ParameterObject Pageable paginacion) throws Exception {
        logger.info("Peticion recibida: GET /categorias (paginado)");
        Page<ResumenCategoria> resumen = servicioCategorias.getCategoriasPaginado(paginacion)
                .map(ResumenCategoria::fromEntity);
        return pagedResourcesAssembler.toModel(resumen, categoriaAssembler);

    }
}
