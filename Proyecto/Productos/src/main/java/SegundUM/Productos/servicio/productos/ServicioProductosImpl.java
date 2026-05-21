package SegundUM.Productos.servicio.productos;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SegundUM.Productos.dominio.Categoria;
import SegundUM.Productos.dominio.EstadoProducto;
import SegundUM.Productos.dominio.LugarRecogida;
import SegundUM.Productos.dominio.Producto;
import SegundUM.Productos.rest.ResumenProducto;
import SegundUM.Productos.adaptadores.RabbitMQ.eventos.EventoCreacionProducto;
import SegundUM.Productos.adaptadores.RabbitMQ.eventos.EventoEliminacionProducto;
import SegundUM.Productos.puertos.PuertoEntradaEventos;
import SegundUM.Productos.puertos.PuertoSalidaEventos;
import SegundUM.Productos.puertos.PuertoUsuarios;
import SegundUM.Productos.repositorio.EntidadNoEncontrada;
import SegundUM.Productos.repositorio.categorias.RepositorioCategorias;
import SegundUM.Productos.repositorio.productos.RepositorioProductos;


/** Implementación del servicio de productos; también actúa como puerto de entrada de eventos RabbitMQ. */
@Service
@Transactional
public class ServicioProductosImpl implements ServicioProductos, PuertoEntradaEventos {

	private final Logger logger = LoggerFactory.getLogger(ServicioProductosImpl.class);

    private final RepositorioProductos repositorioProductos;
    private final RepositorioCategorias repositorioCategorias;
    private final PuertoSalidaEventos puertoSalidaEventos;
    private final PuertoUsuarios puertoUsuarios;

    @Autowired
    public ServicioProductosImpl(RepositorioCategorias repositorioCategorias,
                                 RepositorioProductos repositorioProductos,
                                 PuertoSalidaEventos puertoSalidaEventos,
                                 PuertoUsuarios puertoUsuarios) {
        this.repositorioProductos = repositorioProductos;
        this.repositorioCategorias = repositorioCategorias;
        this.puertoSalidaEventos = puertoSalidaEventos;
        this.puertoUsuarios = puertoUsuarios;
    }

    @Override
    public String altaProducto(String titulo, String descripcion, BigDecimal precio, EstadoProducto estado,
    		String categoriaId, boolean envioDisponible, String vendedorId, LugarRecogida lugarRecogida) throws EntidadNoEncontrada {

        validarAltaProducto(titulo, descripcion, precio, estado, categoriaId, vendedorId);

    	Categoria categoria = repositorioCategorias.findById(categoriaId)
    			.orElseThrow(() -> new EntidadNoEncontrada("Categoria con ID " + categoriaId + " no encontrada"));

    	String id = UUID.randomUUID().toString();

    	Producto p = new Producto(id, titulo, descripcion, precio, estado, categoria, envioDisponible, vendedorId);
    	p.setRecogida(lugarRecogida);

    	repositorioProductos.save(p);

    	// Notificar al bus de eventos para que otros servicios reaccionen a la creación
    	puertoSalidaEventos.publicar(new EventoCreacionProducto(p));

    	return id;
    }

    @Override
    public void asignarLugarRecogida(String productoId, String descripcion, Double longitud, Double latitud) throws EntidadNoEncontrada {

        if (productoId == null || productoId.isBlank())
            throw new IllegalArgumentException("El ID del producto no puede estar vacío");
        validarLugarRecogida(descripcion, longitud, latitud);

    	Producto p = repositorioProductos.findById(productoId)
    			.orElseThrow(() -> new EntidadNoEncontrada("El producto con ID " + productoId + " no existe en el sistema"));
    	LugarRecogida lugar = new LugarRecogida(descripcion, longitud, latitud);
    	p.setRecogida(lugar);
    	repositorioProductos.save(p);
    }

    @Override
    public void anadirVisualizacion(String productoId) throws EntidadNoEncontrada {

        if (productoId == null || productoId.isBlank())
            throw new IllegalArgumentException("El ID del producto no puede estar vacío");

    	Producto p = repositorioProductos.findById(productoId)
    			.orElseThrow(() -> new EntidadNoEncontrada("El producto con ID " + productoId + " no existe en el sistema"));
    	p.incrementarVisualizaciones();
    	repositorioProductos.save(p);
    }

    @Override
    public void modificarProducto(String idProducto, String nuevaDescripcion, BigDecimal nuevoPrecio, String idUsuarioSolicitante) throws EntidadNoEncontrada {

        if (idProducto == null || idProducto.isBlank())
            throw new IllegalArgumentException("El ID del producto no puede estar vacío");
        if (idUsuarioSolicitante == null || idUsuarioSolicitante.isBlank())
            throw new IllegalArgumentException("El ID del usuario solicitante no puede estar vacío");
        if ((nuevaDescripcion == null || nuevaDescripcion.isBlank()) && nuevoPrecio == null)
            throw new IllegalArgumentException("Debes indicar al menos un campo a modificar (descripción o precio)");
        if (nuevaDescripcion != null && nuevaDescripcion.length() > 2000)
            throw new IllegalArgumentException("La descripción no puede superar los 2000 caracteres");
        if (nuevoPrecio != null && nuevoPrecio.compareTo(BigDecimal.valueOf(0.01)) < 0)
            throw new IllegalArgumentException("El precio debe ser mayor que 0");

		Producto p = repositorioProductos.findById(idProducto)
				.orElseThrow(() -> new EntidadNoEncontrada("El producto con ID " + idProducto + " no existe en el sistema"));

		if (!p.getVendedorId().equals(idUsuarioSolicitante)) {
			logger.warn("Intento de modificacion no autorizada por usuario: " + idUsuarioSolicitante);
			throw new AccessDeniedException("No tienes permiso para editar este producto.");
		}

		if (nuevaDescripcion != null && !nuevaDescripcion.isEmpty()) {
			p.setDescripcion(nuevaDescripcion);
		}

		if (nuevoPrecio != null) {
			p.setPrecio(nuevoPrecio);
		}

		repositorioProductos.save(p);
    }

    @Override
    public Page<ResumenProducto> historialMesVendedor(int mes, int anio, String emailVendedor, Pageable pageable) throws IOException {

        if (emailVendedor == null || emailVendedor.isBlank())
            throw new IllegalArgumentException("El email del vendedor no puede estar vacío");
        if (mes < 1 || mes > 12)
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12");
        if (anio < 1)
            throw new IllegalArgumentException("El año debe ser positivo");

    	// Resolver el ID del vendedor llamando al microservicio de Usuarios
    	String vendedorId = puertoUsuarios.getIdByEmail(emailVendedor);
    	return repositorioProductos.getHistorialMes(mes, anio, vendedorId, pageable).map(ResumenProducto::fromEntity);
    }

    @Override
	public Page<ResumenProducto> historialMes(int mes, int anio, Pageable pageable) {

        if (mes < 1 || mes > 12)
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12");
        if (anio < 1)
            throw new IllegalArgumentException("El año debe ser positivo");

		return repositorioProductos.getHistorialMes(mes, anio, null, pageable).map(ResumenProducto::fromEntity);
	}

    @Override
    public Page<Producto> buscarProductos(String categoriaId, String texto, EstadoProducto estadoMinimo, BigDecimal precioMaximo, Pageable pageable) {
        if (precioMaximo != null && precioMaximo.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El precio máximo debe ser mayor que 0");
        
        logger.debug("Buscando productos (Paginado) - Cat: " + categoriaId + ", Txt: " + texto + ", Pag: " + pageable.getPageNumber());
        return repositorioProductos.buscarProductos(categoriaId, texto, estadoMinimo, precioMaximo, pageable);
    }

	@Override
    public Page<Producto> getProductosPorVendedor(String vendedorId, Pageable pageable) {
        if (vendedorId == null || vendedorId.isBlank())
            throw new IllegalArgumentException("El ID del vendedor no puede estar vacío");
        return repositorioProductos.getByVendedorConCategoria(vendedorId, pageable);
    }

    @Override
    public Producto getProductoPorId(String productoId) throws EntidadNoEncontrada {
        if (productoId == null || productoId.isBlank())
            throw new IllegalArgumentException("El ID del producto no puede estar vacío");
    	return repositorioProductos.findById(productoId)
                .orElseThrow(() -> new EntidadNoEncontrada("El producto con ID " + productoId + " no existe."));
    }

    @Override
    public void eliminarProducto(String productoId) throws EntidadNoEncontrada {

        if (productoId == null || productoId.isBlank())
            throw new IllegalArgumentException("El ID del producto no puede estar vacío");

    	Producto p = repositorioProductos.findById(productoId)
    			.orElseThrow(() -> new EntidadNoEncontrada("El producto con ID " + productoId + " no existe."));

    	// Guardar el vendedorId antes de eliminar la entidad, ya que se necesita en el evento
    	String vendedorId = p.getVendedorId();
		repositorioProductos.delete(p);

		puertoSalidaEventos.publicar(new EventoEliminacionProducto(productoId, vendedorId));
    }

    // --- Implementacion PuertoEntradaEventos ---

    @Override
    public void manejarCompraventaCreada(String idProducto) throws EntidadNoEncontrada {
        if (idProducto == null || idProducto.isBlank())
            throw new IllegalArgumentException("El ID del producto no puede estar vacío");

        logger.info("Evento recibido: marcando producto {} como vendido", idProducto);

        Producto p = repositorioProductos.findById(idProducto)
                .orElseThrow(() -> new EntidadNoEncontrada("El producto con ID " + idProducto + " no existe en el sistema"));

        p.setVendido(true);
        repositorioProductos.save(p);

        logger.info("Producto {} marcado como vendido correctamente", idProducto);
    }

    @Override
    public void manejarUsuarioEliminado(String idUsuario) {
        if (idUsuario == null || idUsuario.isBlank())
            throw new IllegalArgumentException("El ID del usuario no puede estar vacío");

        logger.info("Evento recibido: eliminando productos del usuario {}", idUsuario);

        // Eliminar cada producto y publicar un evento de eliminación para que Compraventas reaccione
        Page<Producto> productos = repositorioProductos.getByVendedorConCategoria(idUsuario, Pageable.unpaged());
        for (Producto p : productos) {
            repositorioProductos.delete(p);
            puertoSalidaEventos.publicar(new EventoEliminacionProducto(p.getId(), idUsuario));
        }

        logger.info("Eliminados {} productos del usuario {}", productos.getTotalElements(), idUsuario);
    }

        // --- Validaciones auxiliares ---

    private void validarAltaProducto(String titulo, String descripcion, BigDecimal precio,
            EstadoProducto estado, String categoriaId, String vendedorId) {
        if (titulo == null || titulo.isBlank())
            throw new IllegalArgumentException("El título no puede estar vacío");
        if (titulo.length() < 3 || titulo.length() > 100)
            throw new IllegalArgumentException("El título debe tener entre 3 y 100 caracteres");
        if (descripcion != null && descripcion.length() > 2000)
            throw new IllegalArgumentException("La descripción no puede superar los 2000 caracteres");
        if (precio == null)
            throw new IllegalArgumentException("El precio es obligatorio");
        if (precio.compareTo(BigDecimal.valueOf(0.01)) < 0)
            throw new IllegalArgumentException("El precio debe ser mayor que 0");
        if (estado == null)
            throw new IllegalArgumentException("El estado del producto es obligatorio");
        if (categoriaId == null || categoriaId.isBlank())
            throw new IllegalArgumentException("El ID de la categoría no puede estar vacío");
        if (vendedorId == null || vendedorId.isBlank())
            throw new IllegalArgumentException("El ID del vendedor no puede estar vacío");
    }

    private void validarLugarRecogida(String descripcion, Double longitud, Double latitud) {
        if (descripcion == null || descripcion.isBlank())
            throw new IllegalArgumentException("La descripción del lugar de recogida no puede estar vacía");
        if (descripcion.length() > 500)
            throw new IllegalArgumentException("La descripción del lugar de recogida no puede superar los 500 caracteres");
        if (longitud == null)
            throw new IllegalArgumentException("La longitud es obligatoria");
        if (longitud < -180 || longitud > 180)
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180");
        if (latitud == null)
            throw new IllegalArgumentException("La latitud es obligatoria");
        if (latitud < -90 || latitud > 90)
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90");
    }
}
