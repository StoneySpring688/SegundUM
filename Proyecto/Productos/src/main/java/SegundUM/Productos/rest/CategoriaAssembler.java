package SegundUM.Productos.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import SegundUM.Productos.rest.controllers.CategoriaRestController;

/** Ensamblador HATEOAS que añade el enlace self a cada ResumenCategoria. */
@Component
public class CategoriaAssembler implements RepresentationModelAssembler<ResumenCategoria, EntityModel<ResumenCategoria>> {

    @Override
    public EntityModel<ResumenCategoria> toModel(ResumenCategoria resumen) {
        try {
            return EntityModel.of(resumen,
                    linkTo(methodOn(CategoriaRestController.class).getCategoria(resumen.getId())).withSelfRel());
        } catch (Exception e) {
            return EntityModel.of(resumen);
        }
    }
}
