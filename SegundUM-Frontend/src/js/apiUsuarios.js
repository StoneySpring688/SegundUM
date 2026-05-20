import api from './axiosConfig';
export const apiUsuarios = {
  /**
   * Obtiene un usuario por su ID y lo muestra en consola
   * @param {string} id - El identificador único del usuario
   * @returns {Promise<Object>} Datos del usuario devueltos por el servidor
   */
  getById: async (id) => {
    try {
      console.log(`Iniciando consulta para obtener el usuario con ID: ${id}`);
      
      const response = await api.get(`/usuarios/${id}`);
      
      console.log("Respuesta del servidor para usuario:", response.data);
      return response.data;
    } catch (error) {
      console.error(`Error al obtener el usuario con ID ${id}:`, error);
      throw error;
    }
  }
};