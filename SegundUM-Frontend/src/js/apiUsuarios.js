import api from './axiosConfig';
export const apiUsuarios = {
  /**
   * Obtiene un usuario por su ID y lo muestra en consola
   * @returns {Promise<Object>} Datos del usuario devueltos por el servidor
   */
  getById: async () => {
    try {
      console.log(`Iniciando consulta para obtener el usuario logeado`);
      
      const response = await api.get(`/usuarios/logged`);
      
      console.log("Respuesta del servidor para usuario:", response.data);
      return response.data;
    } catch (error) {
      console.error(`Error al obtener el usuario logeado: `, error);
      throw error;
    }
  }
};