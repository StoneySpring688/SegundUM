import api from './axiosConfig';

export const apiProductos = {
  /**
   * Obtiene ABSOLUTAMENTE TODAS las categorías de la API, 
   * recorriendo de forma automática todas las páginas disponibles.
   * * @returns {Promise<Array>} Lista unificada con todas las categorías en memoria
   */
  getAllCategorias: async () => {
    let todasLasCategorias = [];
    let paginaActual = 0;
    let tieneMasPaginas = true;

    // Función auxiliar recursiva para leer tanto las categorías raíz como las subcategorías
    const extraerSubcategorias = (categorias) => {
      let listaPlana = [];
      
      for (const cat of categorias) {
        // Agregamos la categoría actual a la lista
        listaPlana.push(cat); 
        
        // Si esta categoría tiene subcategorías anidadas, llamamos a la función recursivamente
        if (cat.subcategorias && cat.subcategorias.length > 0) {
          listaPlana = listaPlana.concat(extraerSubcategorias(cat.subcategorias));
        }
      }
      
      return listaPlana;
    };

    try {
      console.log("Iniciando la carga de todas las categorías en memoria...");

      while (tieneMasPaginas) {
        console.log(`Solicitando página ${paginaActual} de categorías...`);
        
        const response = await api.get('/categorias/', {
          params: { page: paginaActual } 
        });

        const data = response.data;

        if (data && data._embedded && data._embedded.categoriaDTOList) {
          // Extraer las raices y las subcategorias de forma recursiva.
          const categoriasAplanadas = extraerSubcategorias(data._embedded.categoriaDTOList);
          todasLasCategorias = todasLasCategorias.concat(categoriasAplanadas);
        }

        if (data && data.page) {
          const { number, totalPages } = data.page;
          
          if (number < totalPages - 1) {
            paginaActual++;
          } else {
            tieneMasPaginas = false;
          }
        } else {
          tieneMasPaginas = false;
        }
      }

      console.log(`¡Carga completada! Se han guardado ${todasLasCategorias.length} categorías y subcategorías en memoria.`);
      return todasLasCategorias;

    } catch (error) {
      console.error("Error crítico al intentar precargar las categorías:", error);
      throw error;
    }
  },

  /**
   * Busca productos según varios filtros opcionales y paginación.
   * Solo envía en la URL los parámetros que no estén en blanco.
   * * @param {Object} filtros - Parámetros de búsqueda
   * @param {string} [filtros.categoriaId] - ID de la categoría a filtrar
   * @param {string} [filtros.texto] - Texto a buscar en el título o descripción
   * @param {string} [filtros.estadoMinimo] - Estado mínimo requerido (ej: 'NUEVO')
   * @param {number} [filtros.precioMaximo] - Precio máximo del producto
   * @param {number} [filtros.page] - Número de la página a solicitar
   * @param {number} [filtros.size] - Cantidad de elementos por página
   * @returns {Promise<Array>} Lista de productos (DTOs) sin la metainformación
   */
  buscarProductos: async ({ categoriaId, texto, estadoMinimo, precioMaximo, page, size } = {}) => {
    try {
      const params = {};

      if (categoriaId !== undefined && categoriaId !== null && categoriaId !== '') params.categoriaId = categoriaId;
      if (texto !== undefined && texto !== null && texto !== '') params.texto = texto;
      if (estadoMinimo !== undefined && estadoMinimo !== null && estadoMinimo !== '') params.estadoMinimo = estadoMinimo;
      if (precioMaximo !== undefined && precioMaximo !== null && precioMaximo !== '') params.precioMaximo = precioMaximo;
      if (page !== undefined && page !== null && page !== '') params.page = page;
      if (size !== undefined && size !== null && size !== '') params.size = size;

      const response = await api.get('/productos/buscar', { params });
      
      const data = response.data;

      if (data && data._embedded && data._embedded.productoDTOList) {
        return data._embedded.productoDTOList;
      }

      return [];

    } catch (error) {
      console.error("Error al buscar productos:", error);
      throw error; // Propagamos el error para manejarlo desde el componente
    }
  },

  /**
   * Obtiene un producto específico por su ID, limpiando la metainformación.
   * * @param {string} id - El ID del producto a buscar
   * @returns {Promise<Object>} Objeto con los datos del producto (DTO) limpio
   */
  getProductoById: async (id) => {
    try {
      const response = await api.get(`/productos/${id}`);
      const data = response.data;

      if (!data) return null;

      return data;

    } catch (error) {
      console.error(`Error al obtener el producto con ID ${id}:`, error);
      throw error;
    }
  }
};