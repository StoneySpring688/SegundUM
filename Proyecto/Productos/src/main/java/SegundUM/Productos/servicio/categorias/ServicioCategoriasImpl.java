package SegundUM.Productos.servicio.categorias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SegundUM.Productos.dominio.Categoria;
import SegundUM.Productos.repositorio.EntidadNoEncontrada;
import SegundUM.Productos.repositorio.categorias.RepositorioCategorias;
import SegundUM.Productos.repositorio.categorias.RepositorioCategoriasXML;
import SegundUM.Productos.repositorio.categorias.RepositorioCategoriasXMLImpl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

/** Implementación del servicio de categorías; gestiona la carga desde XML y las consultas JPA. */
@Service
@Transactional
public class ServicioCategoriasImpl implements ServicioCategorias {
	private static final Logger logger = LoggerFactory.getLogger(ServicioCategoriasImpl.class);
    private static final String CARPETA_CATEGORIAS = "categoriasXML";

    private final RepositorioCategorias repositorioCategorias;
    private final RepositorioCategoriasXML repositorioCategoriasXML;

    @Autowired
    public ServicioCategoriasImpl(RepositorioCategorias repositorioCategorias) {
        this.repositorioCategorias = repositorioCategorias;
        this.repositorioCategoriasXML = new RepositorioCategoriasXMLImpl();
    }

    @Override
    public void cargarJerarquia(String ruta) {
        try {
            Categoria raiz = repositorioCategoriasXML.cargar(ruta);
            if (!repositorioCategorias.existsById(raiz.getId())) {
                repositorioCategorias.save(raiz);
                logger.info("Jerarquía de categorías cargada: " + raiz.toString());
            } else {
                logger.warn("La categoría " + raiz.getNombre() + " ya existe. No se cargará.");
            }
        } catch (Exception e) {
        	logger.error("Error al cargar la jerarquía desde el XML: " + ruta, e);
            throw new CargaCategoriaXMLException("Error al cargar la jerarquía desde el XML: " + ruta, e);
        }
    }

    @Override
    public int cargarTodas() {
        logger.info("Iniciando carga masiva de categorías desde directorio: " + CARPETA_CATEGORIAS);

        File directorio = new File(CARPETA_CATEGORIAS);

        if (!directorio.exists()) {
            logger.error("ERROR: No existe la carpeta: " + directorio.getAbsolutePath());
            throw new IllegalStateException("El directorio " + CARPETA_CATEGORIAS + " no existe");
        }

        // Filtrar solo ficheros con extensión .xml
        File[] archivosXML = directorio.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });

        int cargados = 0;
        int fallidos = 0;
        if (archivosXML != null) {
            for (File archivo : archivosXML) {
                try {
                    cargarJerarquia(archivo.getName());
                    cargados++;
                } catch (CargaCategoriaXMLException e) {
                    fallidos++;
                    logger.warn("FALLO al cargar: " + archivo.getName());
                }
            }
            logger.info("=== FIN CARGA: " + cargados + " cargados, " + fallidos + " fallidos ===");
        }
        return cargados;
    }

    @Override
    public void modificarDescripcion(String categoriaId, String nuevaDescripcion) throws EntidadNoEncontrada {
        Categoria c = repositorioCategorias.findById(categoriaId)
                .orElseThrow(() -> new EntidadNoEncontrada("Categoría no encontrada: " + categoriaId));
        c.setDescripcion(nuevaDescripcion);
        repositorioCategorias.save(c);
    }

    @Override
    public List<Categoria> getDescendientes(String categoriaId) {
    	logger.info("Recuperando descendientes de la categoría con ID " + categoriaId);
        List<Categoria> descendientes = repositorioCategorias.getDescendientes(categoriaId);
        if (descendientes.isEmpty()) {
            logger.warn("No se encontraron descendientes para la categoría con ID " + categoriaId);
        }
        return descendientes;
    }

    @Override
    public Categoria buscarCategoriaPorNombre(String nombre) {
    	List<Categoria> resultados = repositorioCategorias.findByNombreContainingIgnoreCase(nombre);
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    @Override
    public Categoria getCategoriaById(String id) throws EntidadNoEncontrada {
    	return repositorioCategorias.findById(id)
                .orElseThrow(() -> new EntidadNoEncontrada("La categoría con ID " + id + " no existe."));
    }

    @Override
    public Page<Categoria> getCategoriasPaginado(Pageable pageable) {
        return repositorioCategorias.getCategoriasRaiz(pageable);
    }

}
