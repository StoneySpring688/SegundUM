package SegundUM.Usuarios.usuarios.repositorio;

import SegundUM.Usuarios.usuarios.modelo.Usuario;
import SegundUM.Usuarios.repositorio.EntidadNoEncontrada;
import SegundUM.Usuarios.repositorio.RepositorioException;
import SegundUM.Usuarios.repositorio.RepositorioString;

/**
 * Repositorio específico para Usuarios con operaciones AdHoc.
 */
public interface RepositorioUsuarios extends RepositorioString<Usuario> {
    
    /**
     * Busca un usuario por su email.
     */
    Usuario getByEmail(String email) throws RepositorioException, EntidadNoEncontrada;
    
    /**
     * Verifica si existe un usuario con el email dado.
     */
    boolean existeEmail(String email) throws RepositorioException;

    /**
     * Busca un usuario por su identificador de GitHub.
     */
    Usuario getByIdGitHub(String idGitHub) throws RepositorioException, EntidadNoEncontrada;
    }