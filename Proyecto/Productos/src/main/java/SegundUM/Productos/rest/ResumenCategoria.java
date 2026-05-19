package SegundUM.Productos.rest;

import org.springframework.hateoas.RepresentationModel;

import SegundUM.Productos.dominio.Categoria;

public class ResumenCategoria extends RepresentationModel<ResumenCategoria> {

    private String id;
    private String nombre;
    private String descripcion;
    private boolean esRaiz;
    private int numSubcategorias;

    public ResumenCategoria() {}

    public static ResumenCategoria fromEntity(Categoria c) {
        ResumenCategoria r = new ResumenCategoria();
        r.id = c.getId();
        r.nombre = c.getNombre();
        r.descripcion = c.getDescripcion();
        r.esRaiz = c.esRaiz();
        r.numSubcategorias = c.getSubcategorias() != null ? c.getSubcategorias().size() : 0;
        return r;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public boolean isEsRaiz() { return esRaiz; }
    public int getNumSubcategorias() { return numSubcategorias; }
}
