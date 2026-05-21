import api from "./axiosConfig";

export const apiCompraventas = {
    hacerCompraventas: async (idProducto) => {
        try {
            const compraventaDto = {idProducto};
            const response = await api.post("/compraventas", compraventaDto);
            console.log("respuesta hacer compraventa: ", response);
        } catch (error) {
            console.error("Error al hacer una compraventa: ", error);
            throw error;
        }
    },
};