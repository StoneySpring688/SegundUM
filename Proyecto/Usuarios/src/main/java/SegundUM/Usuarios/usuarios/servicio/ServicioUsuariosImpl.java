package SegundUM.Usuarios.usuarios.servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import SegundUM.Usuarios.usuarios.modelo.Usuario;
import SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos.EventoEliminacionUsuario;
import SegundUM.Usuarios.usuarios.adaptadores.RabbitMQ.eventos.EventoModificacionUsuario;
import SegundUM.Usuarios.usuarios.puertos.PuertoEntradaEventos;
import SegundUM.Usuarios.usuarios.puertos.PuertoSalidaEventos;
import SegundUM.Usuarios.repositorio.EntidadNoEncontrada;
import SegundUM.Usuarios.repositorio.FactoriaRepositorios;
import SegundUM.Usuarios.repositorio.RepositorioException;
import SegundUM.Usuarios.usuarios.repositorio.RepositorioUsuarios;

/** Implementación del servicio de negocio de usuarios; actúa también como puerto de entrada de eventos RabbitMQ. */
public class ServicioUsuariosImpl implements ServicioUsuarios, PuertoEntradaEventos {
	private final Logger logger = LoggerFactory.getLogger(ServicioUsuariosImpl.class);

	private final RepositorioUsuarios repositorioUsuarios;
	private PuertoSalidaEventos puertoSalidaEventos;

	public ServicioUsuariosImpl() {
		this.repositorioUsuarios = FactoriaRepositorios.getRepositorio(Usuario.class);
	}

	public void setPuertoSalidaEventos(PuertoSalidaEventos puertoSalidaEventos) {
		this.puertoSalidaEventos = puertoSalidaEventos;
	}
	
	@Override
	public List<Usuario> getAllUsuarios() throws RepositorioException {
		return repositorioUsuarios.getAll();
	}

	@Override
	public String altaUsuario(String email, String nombre, String apellidos, String clave,
			LocalDate fechaNacimiento, String telefono) throws RepositorioException {

		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("El campo email no puede estar vacio.");
		} else if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El campo nombre no puede estar vacio.");
		} else if (clave == null || clave.isBlank()) {
			throw new IllegalArgumentException("El campo clave no puede estar vacio.");
		}

		if (repositorioUsuarios.existeEmail(email)) {
			logger.warn("Intento de alta con email ya registrado: {}", email);
			throw new IllegalStateException("El email " + email + " ya esta registrado en el sistema");
		}

		String id = UUID.randomUUID().toString();
		Usuario u = new Usuario(id, email, nombre, apellidos, clave, fechaNacimiento, telefono);

		logger.debug("Dando de alta nuevo usuario: {}", u);
		repositorioUsuarios.add(u);

		return u.getId();
	}

	@Override
	public void modificarUsuario(String usuarioId, String nombre, String apellidos, String clave,
			LocalDate fechaNacimiento, String telefono) throws RepositorioException, EntidadNoEncontrada {

		if (usuarioId == null || usuarioId.isBlank()) {
			throw new IllegalArgumentException("El campo usuarioId no puede estar vacio.");
		}

		Usuario u = repositorioUsuarios.getById(usuarioId);

		if (nombre != null) u.setNombre(nombre);
		if (apellidos != null) u.setApellidos(apellidos);
		if (clave != null) u.setClave(clave);
		if (fechaNacimiento != null) u.setFechaNacimiento(fechaNacimiento);
		if (telefono != null) u.setTelefono(telefono);

		repositorioUsuarios.update(u);

		if (puertoSalidaEventos != null) {
			puertoSalidaEventos.publicar(new EventoModificacionUsuario(u));
		}
	}

	@Override
	public Usuario login(String email, String clave) throws RepositorioException, EntidadNoEncontrada {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("El campo email no puede estar vacio.");
		} else if (clave == null || clave.isBlank()) {
			throw new IllegalArgumentException("El campo clave no puede estar vacio.");
		}

			Usuario u = repositorioUsuarios.getByEmail(email);
			if (!u.getClave().equals(clave)) {
				throw new IllegalArgumentException("Credenciales invalidas.");
			}
			return u;
	}

	@Override
	public Usuario getUserById(String usuarioId) throws RepositorioException, EntidadNoEncontrada {
		if (usuarioId == null || usuarioId.isBlank()) {
			throw new IllegalArgumentException("El campo usuarioId no puede estar vacio.");
		}
		return repositorioUsuarios.getById(usuarioId);
	}

	@Override
	public Usuario getUserByEmail(String email) throws RepositorioException, EntidadNoEncontrada {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("El campo email no puede estar vacio.");
		}
		return repositorioUsuarios.getByEmail(email);
	}

	@Override
	public void deleteUserById(String usuarioId) throws RepositorioException, EntidadNoEncontrada {
		if (usuarioId == null || usuarioId.isBlank()) {
			throw new IllegalArgumentException("El campo usuarioId no puede estar vacio.");
		}

		Usuario u = repositorioUsuarios.getById(usuarioId);
		repositorioUsuarios.delete(u);

		if (puertoSalidaEventos != null) {
			puertoSalidaEventos.publicar(new EventoEliminacionUsuario(usuarioId));
		}
	}

	@Override
	public Usuario getUsuarioPorIdGitHub(String idGitHub) throws RepositorioException, EntidadNoEncontrada {
		if (idGitHub == null || idGitHub.isBlank()) {
			throw new IllegalArgumentException("El campo idGitHub no puede estar vacio.");
		}
		return repositorioUsuarios.getByIdGitHub(idGitHub);
	}

	@Override
	public String altaUsuarioGitHub(String idGitHub, String nombre, String email) throws RepositorioException, EntidadNoEncontrada {
		if (idGitHub == null || idGitHub.isBlank()) {
			throw new IllegalArgumentException("El campo idGitHub no puede estar vacio.");
		} else if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El campo nombre no puede estar vacio.");
		}

		// Si no hay email real, se genera uno ficticio basado en el ID de GitHub
		String finalEmail = (email != null && !email.isBlank()) ? email : idGitHub + "@github.com";

		if (repositorioUsuarios.existeEmail(finalEmail)) {
				// El usuario ya existe; se devuelve su ID sin crear un duplicado
				Usuario existente = repositorioUsuarios.getByIdGitHub(idGitHub);
				return existente.getId();
			
		}

		String id = UUID.randomUUID().toString();
		Usuario u = new Usuario(id, finalEmail, nombre, null, null, null, null);
		u.setIdGitHub(idGitHub);

		logger.debug("Dando de alta nuevo usuario desde GitHub: {}", u);
		repositorioUsuarios.add(u);

		return u.getId();
	}

	// Manejadores de eventos entrantes desde el bus RabbitMQ

	@Override
	public void manejarCompraventaCreada(String idComprador, String idVendedor) {
		try {
			Usuario comprador = repositorioUsuarios.getById(idComprador);
			comprador.addCompraRealizada();
			repositorioUsuarios.update(comprador);
			logger.info("Compras realizadas del usuario {} incrementadas a {}", idComprador, comprador.getComprasRealizadas());

			Usuario vendedor = repositorioUsuarios.getById(idVendedor);
			vendedor.addVenta();
			repositorioUsuarios.update(vendedor);
			logger.info("Ventas realizadas del usuario {} incrementadas a {}", idVendedor, vendedor.getVentasRealizadas());
		} catch (RepositorioException | EntidadNoEncontrada e) {
			logger.error("Error al procesar evento compraventa-creada", e);
		}
	}

	@Override
	public void manejarProductoCreado(String idProducto, String vendedorId) {
		try {
			Usuario vendedor = repositorioUsuarios.getById(vendedorId);
			vendedor.addProducto(idProducto);
			repositorioUsuarios.update(vendedor);
			logger.info("Producto {} anadido a la lista de productos del usuario {}", idProducto, vendedorId);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			logger.error("Error al procesar evento producto-creado", e);
		}
	}

	@Override
	public void manejarProductoEliminado(String idProducto, String vendedorId) {
		try {
			Usuario vendedor = repositorioUsuarios.getById(vendedorId);
			vendedor.removeProducto(idProducto);
			repositorioUsuarios.update(vendedor);
			logger.info("Producto {} eliminado de la lista de productos del usuario {}", idProducto, vendedorId);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			logger.error("Error al procesar evento producto-eliminado", e);
		}
	}
}
