package SegundUM.Productos.servicio.categorias;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import SegundUM.Productos.dominio.Categoria;
import SegundUM.Productos.repositorio.EntidadNoEncontrada;

public interface ServicioCategorias {

    /**
     * Carga una jerarquía de categorías desde un fichero XML.
     * Lanza RuntimeException si hay un error de infraestructura al leer el fichero.
     */
    void cargarJerarquia(String ruta);

    /**
     * Carga automáticamente todas las categorías que existan en la carpeta 'categoriasXML'.
     * @return int número de ficheros cargados correctamente.
     */
    int cargarTodas();

    /**
     * Modifica la descripción de una categoría.
     */
    void modificarDescripcion(String categoriaId, String nuevaDescripcion) throws EntidadNoEncontrada;

    /**
     * Devuelve las categorías raíz (categoriaPadre == null).
     */
    List<Categoria> getCategoriasRaiz();

    /**
     * Devuelve todos los descendientes (directos e indirectos) de la categoría indicada.
     */
    List<Categoria> getDescendientes(String categoriaId);

    /**
     * Busca una categoría por su nombre (o parte de él).
     * Devuelve la primera coincidencia o null si no existe.
     */
    Categoria buscarCategoriaPorNombre(String nombre);

    Categoria getCategoriaById(String id) throws EntidadNoEncontrada;

    @Deprecated
    List<Categoria> getCategorias();

    Page<Categoria> getCategoriasPaginado(Pageable pageable);

}
