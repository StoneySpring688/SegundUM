// Servicio de productos: llamadas a /productos y /categorias
import api from './axiosConfig';

export const apiProductos = {
  // Recorre todas las páginas de categorías y las aplana en una lista plana (incluye subcategorías)
  getAllCategorias: async () => {
    let todasLasCategorias = [];
    let paginaActual = 0;
    let tieneMasPaginas = true;

    const extraerSubcategorias = (categorias) => {
      let listaPlana = [];
      for (const cat of categorias) {
        listaPlana.push(cat);
        if (cat.subcategorias && cat.subcategorias.length > 0) {
          listaPlana = listaPlana.concat(extraerSubcategorias(cat.subcategorias));
        }
      }
      return listaPlana;
    };

    try {
      while (tieneMasPaginas) {
        const response = await api.get('/categorias', { params: { page: paginaActual } });
        const data = response.data;

        if (data && data._embedded && data._embedded.categoriaDTOList) {
          todasLasCategorias = todasLasCategorias.concat(extraerSubcategorias(data._embedded.categoriaDTOList));
        }

        if (data && data.page) {
          const { number, totalPages } = data.page;
          if (number < totalPages - 1) paginaActual++;
          else tieneMasPaginas = false;
        } else {
          tieneMasPaginas = false;
        }
      }
      return todasLasCategorias;
    } catch (error) {
      console.error("Error al precargar categorías:", error);
      throw error;
    }
  },

  // Solo incluye en la query los filtros que tengan valor
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
        return { productos: data._embedded.productoDTOList, paginas: data.page };
      }
      return { productos: [], paginas: data?.page || null };
    } catch (error) {
      console.error("Error al buscar productos:", error);
      throw error;
    }
  },

  getProductoById: async (id) => {
    try {
      const response = await api.get(`/productos/${id}`);
      return response.data ?? null;
    } catch (error) {
      console.error(`Error al obtener producto ${id}:`, error);
      throw error;
    }
  },

  registrarVisualizacion: async (id) => {
    try {
      await api.put(`/productos/${id}/visualizaciones`);
    } catch (error) {
      console.error(`Error al registrar visualización del producto ${id}:`, error);
    }
  },

  publicarProducto: async (producto) => {
    try {
      await api.post(`/productos`, producto);
    } catch (error) {
      console.error("Error al publicar producto:", error);
    }
  },

  getProductosPublicados: async (page) => {
    try {
      const response = await api.get("/productos/vendedor", { params: { page } });
      const data = response.data;

      if (data && data._embedded && data._embedded.productoDTOList) {
        return { productos: data._embedded.productoDTOList, paginas: data.page };
      }
      return { productos: [], paginas: data?.page || null };
    } catch (error) {
      console.error("Error al recuperar productos publicados:", error);
    }
  }
};
