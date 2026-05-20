package SegundUM.Usuarios.usuarios.servicio;

import java.time.LocalDate;
import java.util.List;

import SegundUM.Usuarios.usuarios.modelo.Usuario;
import SegundUM.Usuarios.repositorio.EntidadNoEncontrada;
import SegundUM.Usuarios.repositorio.RepositorioException;

/**
 * Operaciones de negocio sobre usuarios.
 * Las excepciones se propagan al controlador REST.
 */
public interface ServicioUsuarios {

    List<Usuario> getAllUsuarios() throws RepositorioException;

    /**
     * Da de alta un usuario y devuelve su identificador.
     * Lanza IllegalArgumentException si algún campo obligatorio está vacío o nulo.
     * Lanza IllegalStateException si el email ya está registrado.
     */
    String altaUsuario(String email, String nombre, String apellidos, String clave,
                       LocalDate fechaNacimiento, String telefono) throws RepositorioException;

    /**
     * Modifica los datos de un usuario existente.
     * Sólo los campos no nulos se actualizan.
     */
    void modificarUsuario(String usuarioId, String nombre, String apellidos, String clave,
                          LocalDate fechaNacimiento, String telefono)
            throws RepositorioException, EntidadNoEncontrada;

    /**
     * Verifica las credenciales del usuario.
     * Lanza IllegalArgumentException si las credenciales son inválidas.
     */
    Usuario login(String email, String clave) throws RepositorioException;

    Usuario getUserById(String usuarioId) throws RepositorioException, EntidadNoEncontrada;

    void deleteUserById(String usuarioId) throws RepositorioException, EntidadNoEncontrada;

    Usuario getUsuarioPorIdGitHub(String idGitHub) throws RepositorioException, EntidadNoEncontrada;

    /**
     * Da de alta un usuario a través de GitHub.
     * Lanza IllegalStateException si el email ya está registrado por otro usuario.
     */
    String altaUsuarioGitHub(String idGitHub, String nombre, String email) throws RepositorioException;
}
