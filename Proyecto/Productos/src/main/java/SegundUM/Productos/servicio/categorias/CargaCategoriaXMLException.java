package SegundUM.Productos.servicio.categorias;

/**
 * Excepción lanzada cuando no se puede cargar una jerarquía de categorías desde un fichero XML.
 * El manejador global la traduce a 500 Internal Server Error.
 */
public class CargaCategoriaXMLException extends RuntimeException {

    public CargaCategoriaXMLException(String msg, Throwable causa) {
        super(msg, causa);
    }
}
