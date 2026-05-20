import api from "./axiosConfig";
import { apiUsuarios } from "./apiUsuarios";

export const authService = {
  /**
   * Envía las credenciales al servidor para iniciar sesión.
   * Como el servidor responde con una Cookie JWT, no necesitamos guardar 
   * el token manualmente; el navegador (y withCredentials) lo adjuntan automáticamente.
   * 
   * @param {string} email - Correo del usuario
   * @param {string} clave - Contraseña del usuario
   * @returns {Promise<Object>} La respuesta de la API (ej. datos del usuario si el backend los devuelve)
   */
  login: async (email, clave) => {
    const credenciales = { email, clave };

    console.log(credenciales);
    
    const response = await api.post("/auth/login", credenciales);
    console.log(response);
    const idUsuario = response.data?.id;
    console.log(idUsuario);
    
    return response.data; 
  },

  register: async (email,
                    nombre,
                    apellidos,
                    clave,
                    fechaNacimiento,
                    telefono) => {
    const userData = {email, nombre, apellidos, clave, fechaNacimiento, telefono};
    console.log("Datos de registro:", userData);
    const response = await api.post("/usuarios/", {}, { params: userData });
    return response.data;
  },

  logout: async () => {
    const response = await api.post("/auth/logout");
    return response.data;
  }
};