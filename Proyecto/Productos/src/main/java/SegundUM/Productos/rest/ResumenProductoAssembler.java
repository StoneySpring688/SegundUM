package SegundUM.Productos.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import SegundUM.Productos.rest.controllers.ProductoRestController;

/** Ensamblador HATEOAS que añade el enlace self a cada ResumenProducto. */
@Component
public class ResumenProductoAssembler
        implements RepresentationModelAssembler<ResumenProducto, EntityModel<ResumenProducto>> {

    @Override
    public EntityModel<ResumenProducto> toModel(ResumenProducto resumen) {
        try {
            return EntityModel.of(resumen,
                    linkTo(methodOn(ProductoRestController.class).getProducto(resumen.getId()))
                            .withSelfRel());
        } catch (Exception e) {
            return EntityModel.of(resumen);
        }
    }
}
