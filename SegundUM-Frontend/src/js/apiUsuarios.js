import api from './axiosConfig';
export const apiUsuarios = {
  /**
   * Obtiene un usuario por su ID y lo muestra en consola
   * @returns {Promise<Object>} Datos del usuario devueltos por el servidor
   */
  getById: async () => {
    try {
      //console.log(`Iniciando consulta para obtener el usuario logeado`);
      
      const response = await api.get(`/usuarios/logged`);
      
      console.log("Respuesta del servidor para usuario:", response.data);
      return response.data;
    } catch (error) {
      console.error(`Error al obtener el usuario logeado: `, error);
      throw error;
    }
  },
  modificarUsuario: async (usuarioModificacion) => {
    try {
      console.log("datos para modificar el usuario: ", usuarioModificacion);
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
      console.error(`Error al modificar el usuario: `, error);
      throw error;
    }
  },
  
  getUsuarios: async () => {
    try {
      const usuarios = await api.get("/usuarios");
      const usuariosArray = usuarios.data.resumenEntidad.map(u => u.resumen);

      console.log("Usuarios recuperados de la api: ", usuariosArray);

      return usuariosArray;
    } catch (error) {
      console.error("Error al recuperar los usuarios de la api: ", error);
      throw error;
    }
  }
};