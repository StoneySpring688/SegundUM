package SegundUM.Productos.rest.api;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import SegundUM.Productos.rest.ResumenCategoria;
import SegundUM.Productos.rest.dto.CategoriaDTO;

@Tag(name = "Categorias", description = "Operaciones de consulta y carga de categorias de productos")
public interface CategoriaApi {

    @Operation(summary = "Cargar categorias", description = "Carga masiva de categorias desde ficheros. Requiere rol ADMINISTRADOR")
    ResponseEntity<String> cargarTodas() throws Exception;

    @Operation(summary = "Obtener categoria", description = "Devuelve una categoria por su ID con enlaces HATEOAS")
    EntityModel<CategoriaDTO> getCategoria(String id) throws Exception;

    @Operation(summary = "Listar categorias", description = "Devuelve paginado el resumen de todas las categorias disponibles")
    PagedModel<EntityModel<CategoriaDTO>> getCategoriasPaginado(Pageable paginacion) throws Exception;
}
