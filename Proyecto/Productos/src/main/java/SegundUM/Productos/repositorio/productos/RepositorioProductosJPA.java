package SegundUM.Productos.repositorio.productos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import SegundUM.Productos.dominio.EstadoProducto;
import SegundUM.Productos.dominio.Producto;

/** Repositorio JPA de productos con consultas JPQL personalizadas para búsqueda e historial. */
@Repository
public interface RepositorioProductosJPA extends RepositorioProductos, JpaRepository<Producto, String> {


    /**
     * Busca productos por categoría, descripción, estado y precio máximo.
     * Todos los parámetros son opcionales (pueden ser null).
     */
    @Override
    @Query("SELECT p FROM Producto p WHERE " +
           "(:categoriaId IS NULL OR p.categoria.id = :categoriaId) AND " +
           "(:textoBusqueda IS NULL OR (LOWER(p.titulo) LIKE LOWER(CONCAT('%', :textoBusqueda, '%')) OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :textoBusqueda, '%')))) AND " +
           "(:estadoMinimo IS NULL OR p.estado <= :estadoMinimo) AND " +
           "(:precioMaximo IS NULL OR p.precio <= :precioMaximo)")
    Page<Producto> buscarProductos(
        @Param("categoriaId") String categoriaId,
        @Param("textoBusqueda") String textoBusqueda,
        @Param("estadoMinimo") EstadoProducto estadoMinimo,
        @Param("precioMaximo") BigDecimal precioMaximo,
        Pageable pageable
    );
    
    /**
     * Obtiene el historial del mes de un vendedor, ordenado por visualizaciones.
     */  
    @Override
    @Query("SELECT p FROM Producto p WHERE " +
            "MONTH(p.fechaPublicacion) = :mes " +
            "AND YEAR(p.fechaPublicacion) = :anio " +
            "AND (:vendedorId IS NULL OR p.vendedorId = :vendedorId) " +
            "ORDER BY p.visualizaciones DESC")
    Page<Producto> getHistorialMes(
        @Param("mes") int mes,
        @Param("anio") int anio,
        @Param("vendedorId") String vendedorId,
        Pageable pageable
    );
    
    /**
     * Obtiene productos publicados en un rango de fechas.
     */
    @Override
    @Query("SELECT p FROM Producto p WHERE p.fechaPublicacion >= :inicio AND p.fechaPublicacion <= :fin")
    Page<Producto> getProductosPorFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin, Pageable pageable);
    
    /**
	 * Obtiene los productos de un vendedor con su categoría.
	 */
    @Override
    @EntityGraph(attributePaths = {"categoria"})
    @Query("SELECT p FROM Producto p WHERE p.vendedorId = :vendedorId")
    Page<Producto> getByVendedorConCategoria(@Param("vendedorId") String vendedorId, Pageable pageable);

}