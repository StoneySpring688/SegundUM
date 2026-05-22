package SegundUM.Compraventas.servicio.compraventa;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import SegundUM.Compraventas.dominio.Compraventa;
import SegundUM.Compraventas.puertos.PuertoAutenticacion;
import SegundUM.Compraventas.puertos.PuertoEntradaEventos;
import SegundUM.Compraventas.adaptadores.RabbitMQ.eventos.EventoCreacionCompraventa;
import SegundUM.Compraventas.puertos.PuertoSalidaEventos;
import SegundUM.Compraventas.puertos.PuertoProductos;
import SegundUM.Compraventas.puertos.PuertoUsuarios;
import SegundUM.Compraventas.repositorio.compraventa.RepositorioCompraventa;
import SegundUM.Compraventas.rest.dto.ProductoDTO;
import SegundUM.Compraventas.rest.dto.UsuarioDTO;

/** Implementación de la lógica de negocio de compraventas; actúa también como receptor de eventos del bus. */
@Service
public class ServicioCompraventaImpl implements ServicioCompraventa, PuertoEntradaEventos {

	private static final Logger logger = LoggerFactory.getLogger(ServicioCompraventaImpl.class);

	private final RepositorioCompraventa repositorio;
    private final PuertoProductos puertoProductos;
    private final PuertoUsuarios puertoUsuarios;
    private final PuertoSalidaEventos puertoEventos;

    @Autowired
    public ServicioCompraventaImpl(RepositorioCompraventa repositorio,
                                   PuertoProductos puertoProductos,
                                   PuertoUsuarios puertoUsuarios,
                                   PuertoAutenticacion puertoAutenticacion,
                                   PuertoSalidaEventos puertoEventos) {
        this.repositorio = repositorio;
        this.puertoProductos = puertoProductos;
        this.puertoUsuarios = puertoUsuarios;
        this.puertoEventos = puertoEventos;
    }

    @Override
    public Compraventa realizarCompra(String idProducto, String idComprador, String tokenCrudo)  {
        if (idProducto == null || idProducto.isBlank())
            throw new IllegalArgumentException("El campo idProducto no puede estar vacío.");
        if (idComprador == null || idComprador.isBlank())
            throw new IllegalArgumentException("El campo idComprador no puede estar vacío.");
        if (tokenCrudo == null || tokenCrudo.isBlank())
            throw new IllegalArgumentException("El token de autenticación no puede estar vacío.");

        // Añadir prefijo Bearer para las llamadas internas a Usuarios
        String token = "Bearer " + tokenCrudo;

        ProductoDTO producto = puertoProductos.obtenerDatosProducto(idProducto);

        logger.info("Producto Obtenido = {}", producto.toString());
        
        if(producto.getIdVendedor().equals(idComprador)) {
        	logger.warn("No puedes comprar tu producto");
        	throw new IllegalArgumentException("No puedes comprar tu producto");
        }

        if (producto.isVendido()) {
        	logger.warn("El producto {} ya ha sido vendido", producto.toString());
            throw new IllegalStateException("El producto ya ha sido vendido");
        }

        UsuarioDTO comprador = puertoUsuarios.obtenerDatosVendedor(idComprador, token);

        logger.info("Comprador Obtenido = {}", comprador.toString());

        UsuarioDTO vendedor = puertoUsuarios.obtenerDatosVendedor(producto.getIdVendedor(), token);

        // Extraer descripción del lugar de recogida; puede ser null si el producto no la define
        String descripcionRecogida = producto.getRecogida() != null ? producto.getRecogida().getDescripcion() : null;

        Compraventa nuevaCompraventa = new Compraventa(
                null,
                idProducto,
                producto.getTitulo(),
                descripcionRecogida,
                producto.getPrecio(),
                producto.getIdVendedor(),
                vendedor.getNombre(),
                idComprador,
                comprador.getNombre(),
                LocalDateTime.now()
        );

        Compraventa guardada = repositorio.save(nuevaCompraventa);

        puertoEventos.publicar(new EventoCreacionCompraventa(guardada));

        return guardada;
    }

    @Override
    public Page<Compraventa> recuperarComprasDeUsuario(String idComprador, Pageable pageable) {
        if (idComprador == null || idComprador.isBlank())
            throw new IllegalArgumentException("El campo idComprador no puede estar vacío.");
        return repositorio.findByIdComprador(idComprador, pageable);
    }

    @Override
    public Page<Compraventa> recuperarVentasDeUsuario(String idVendedor, Pageable pageable) {
        if (idVendedor == null || idVendedor.isBlank())
            throw new IllegalArgumentException("El campo idVendedor no puede estar vacío.");
        return repositorio.findByIdVendedor(idVendedor, pageable);
    }

    @Override
    public Page<Compraventa> recuperarCompraventasEntre(String idComprador, String idVendedor, Pageable pageable) {
        if (idComprador == null || idComprador.isBlank())
            throw new IllegalArgumentException("El campo idComprador no puede estar vacío.");
        if (idVendedor == null || idVendedor.isBlank())
            throw new IllegalArgumentException("El campo idVendedor no puede estar vacío.");
        return repositorio.findByIdCompradorAndIdVendedor(idComprador, idVendedor, pageable);
    }

    // --- Implementacion PuertoEntradaEventos ---

    @Override
    public void manejarUsuarioModificado(String idUsuario, String nuevoNombre) {
        logger.info("Evento recibido: usuario-modificado para usuario {}", idUsuario);

        List<Compraventa> comoVendedor = repositorio.findByIdVendedor(idUsuario);
        for (Compraventa c : comoVendedor) {
            c.setNombreVendedor(nuevoNombre);
            repositorio.save(c);
        }

        List<Compraventa> comoComprador = repositorio.findByIdComprador(idUsuario);
        for (Compraventa c : comoComprador) {
            c.setNombreComprador(nuevoNombre);
            repositorio.save(c);
        }

        logger.info("Nombre actualizado en {} compraventas como vendedor y {} como comprador",
                comoVendedor.size(), comoComprador.size());
    }

    @Override
    public void manejarUsuarioEliminado(String idUsuario) {
        logger.info("Evento recibido: usuario-eliminado para usuario {}", idUsuario);

        List<Compraventa> comoVendedor = repositorio.findByIdVendedor(idUsuario);
        for (Compraventa c : comoVendedor) {
            c.setIdVendedor(null);
            c.setNombreVendedor(null);
            repositorio.save(c);
        }

        List<Compraventa> comoComprador = repositorio.findByIdComprador(idUsuario);
        for (Compraventa c : comoComprador) {
            c.setIdComprador(null);
            c.setNombreComprador(null);
            repositorio.save(c);
        }

        logger.info("Datos de usuario eliminado en {} compraventas como vendedor y {} como comprador",
                comoVendedor.size(), comoComprador.size());
    }

    @Override
    public void manejarProductoEliminado(String idProducto) {
        logger.info("Evento recibido: producto-eliminado para producto {}", idProducto);

        List<Compraventa> compraventas = repositorio.findByIdProducto(idProducto);

        if (compraventas.isEmpty()) {
            logger.info("Producto {} no tiene compraventas asociadas, evento ignorado", idProducto);
            return;
        }

        for (Compraventa c : compraventas) {
            c.setIdProducto(null);
            c.setTitulo(null);
            c.setRecogida(null);
            repositorio.save(c);
        }

        logger.info("Datos de producto eliminado en {} compraventas", compraventas.size());
    }
}
