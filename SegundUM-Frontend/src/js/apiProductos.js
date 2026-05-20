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

        // 1. Extraemos SOLO los datos de las categorías (ubicados en _embedded.categoriaDTOList)
        if (data && data._embedded && data._embedded.categoriaDTOList) {
          // 2. Usamos la recursividad para extraer las raíces y todas sus subcategorías
          const categoriasAplanadas = extraerSubcategorias(data._embedded.categoriaDTOList);
          todasLasCategorias = todasLasCategorias.concat(categoriasAplanadas);
        }

        // 3. Usamos la metainformación exclusivamente para el control de la paginación
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
      // Retorna el array unificado únicamente con los datos (sin metainformación)
      return todasLasCategorias;

    } catch (error) {
      console.error("Error crítico al intentar precargar las categorías:", error);
      throw error;
    }
  }
};