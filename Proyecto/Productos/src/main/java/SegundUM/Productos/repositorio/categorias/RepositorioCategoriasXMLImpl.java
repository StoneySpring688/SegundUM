package SegundUM.Productos.repositorio.categorias;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import SegundUM.Productos.dominio.Categoria;
import SegundUM.Productos.repositorio.EntidadNoEncontrada;
import SegundUM.Productos.repositorio.RepositorioException;

/** Adaptador que deserializa jerarquías de categorías desde ficheros XML usando JAXB. */
public class RepositorioCategoriasXMLImpl implements RepositorioCategoriasXML {

    private static final String DIRECTORIO = "categoriasXML/";

    @Override
    public Categoria cargar(String nombreFichero) throws RepositorioException, EntidadNoEncontrada {
        File fichero = new File(DIRECTORIO + nombreFichero);
        if (!fichero.exists()) {
            throw new EntidadNoEncontrada("Fichero de categorías no encontrado: " + nombreFichero);
        }
        try {
            // Crear contexto JAXB en cada llamada; no es costoso para ficheros pequeños
            JAXBContext ctx = JAXBContext.newInstance(Categoria.class);
            return (Categoria) ctx.createUnmarshaller().unmarshal(fichero);
        } catch (JAXBException e) {
            throw new RepositorioException("Error al parsear el fichero XML: " + nombreFichero, e);
        }
    }
}
