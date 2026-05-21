package SegundUM.Productos.repositorio.categorias;

import SegundUM.Productos.dominio.Categoria;
import SegundUM.Productos.repositorio.EntidadNoEncontrada;
import SegundUM.Productos.repositorio.RepositorioException;

public interface RepositorioCategoriasXML {

    Categoria cargar(String nombreFichero) throws RepositorioException, EntidadNoEncontrada;
}
