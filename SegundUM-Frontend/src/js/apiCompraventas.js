import api from "./axiosConfig";

export const apiCompraventas = {
    hacerCompraventas: async (idProducto) => {
        try {
            const compraventaDto = { idProducto };
            console.log(compraventaDto);
            const response = await api.post("/compraventas", compraventaDto);
            console.log("respuesta hacer compraventa: ", response);
        } catch (error) {
            console.error("Error al hacer una compraventa: ", error);
            throw error;
        }
    },
    getCompras: async (page) => {
        try {
            const response = await api.get("/compraventas", {params: { page:page }});

            const data = response.data;
            console.log("respuesta de consulta de compras: ", data);

            if (data && data._embedded && data._embedded.compraventaDTOList) {
                return {
                    compras: data._embedded.compraventaDTOList,
                    paginas: data.page
                };
            }

            return {
                compras: [],
                paginas: data?.page || null
            };

        } catch (error) {
            console.error("Error al consultar las compras del usuario: ", error);
        }
    },
    getVentas: async (page) => {
        try {
            const response = await api.get("/compraventas/vendedor", {params: { page:page }});

            const data = response.data;
            console.log("respuesta de consulta de ventas: ", data);

            if (data && data._embedded && data._embedded.compraventaDTOList) {
                return {
                    ventas: data._embedded.compraventaDTOList,
                    paginas: data.page
                };
            }

            return {
                ventas: [],
                paginas: data?.page || null
            };

        } catch (error) {
            console.error("Error al consultar las compras del usuario: ", error);
        }
    },
};