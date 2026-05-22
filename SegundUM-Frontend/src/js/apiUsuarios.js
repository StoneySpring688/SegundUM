// Servicio de usuarios: consulta y modificación del usuario autenticado
import api from './axiosConfig';

export const apiUsuarios = {
  getById: async () => {
    try {
      const response = await api.get(`/usuarios/logged`);
      return response.data;
    } catch (error) {
      console.error(`Error al obtener el usuario logeado:`, error);
      throw error;
    }
  },

  modificarUsuario: async (usuarioModificacion) => {
    try {
      const usuarioDTO = {
        email: usuarioModificacion.email,
        nombre: usuarioModificacion.nombre,
        apellidos: usuarioModificacion.apellidos,
        clave: usuarioModificacion.clave,
        fechaNacimiento: usuarioModificacion.fechaNacimiento,
        telefono: usuarioModificacion.telefono
      };
      api.put("/usuarios", usuarioDTO);
    } catch (error) {
      console.error(`Error al modificar el usuario:`, error);
      throw error;
    }
  },

  getUsuarios: async () => {
    try {
      const usuarios = await api.get("/usuarios");
      return usuarios.data.resumenEntidad.map(u => u.resumen);
    } catch (error) {
      console.error("Error al recuperar usuarios:", error);
      throw error;
    }
  }
};
