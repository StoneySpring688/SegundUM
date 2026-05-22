import api from "./axiosConfig";

export const apiCompraventas = {
    hacerCompraventas: async (idProducto) => {
        try {
            const compraventaDto = { idProducto };
            const response = await api.post("/compraventas", compraventaDto);
            console.log("respuesta hacer compraventa: ", response);
            
            return { succes: true, mensaje: "Compra realizada con exito" };
        } catch (error) {
            
            if(error.response && error.response.status === 406) {
                return { succes: false, mensaje: "No puedes comprar tu propio producto" };
            }
            
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

    getCompraventas: async (comprador, vendedor, page) => {
        try {
            const response = await api.get("/compraventas/entre", {params: {idComprador:comprador, idVendedor:vendedor, page:page }});

            console.log("respuesta: ", response);

            const data = response.data;
            console.log("respuesta de consulta de compraventas: ", data);

            if (data && data._embedded && data._embedded.compraventaDTOList) {
                return {
                    compraventas: data._embedded.compraventaDTOList,
                    paginas: data.page
                };
            }

            return {
                compraventas: [],
                paginas: data?.page || null
            };

        } catch (error) {
            console.error("Error al consultar las compras del usuario: ", error);
        }
    },
};