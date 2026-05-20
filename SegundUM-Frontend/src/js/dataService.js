/**
 * dataService.js
 * 
 * Este servicio actúa como un pequeño "almacén" (store) local para gestionar
 * los datos del usuario y sus productos. 
 */

// Datos iniciales (pueden venir de un JSON importado o ser un objeto literal)
let usuario = {};

let productos = [];

let categorias = [];

export const dataService = {
    /**
     * Devuelve los datos del usuario actual.
     */
    getUsuario: () => {
        return { ...usuario };
    },

    /**
     * Actualiza los datos del usuario.
     */
    setUsuario: (nuevosDatos) => {
        usuario = { ...usuario, ...nuevosDatos };
    },

    /**
     * Devuelve la lista de productos.
     */
    getProductos: () => {
        return [...productos];
    },

    /**
     * Añade un nuevo producto a la lista.
     * @param {Object} producto - Objeto con los datos del producto
     */
    addProducto: (producto) => {
        // Generamos un ID simple si no tiene uno
        productos.push(producto);
        console.log("Producto añadido:", producto);
    },

    /**
     * Elimina un producto por su ID.
     * @param {number|string} id - El ID del producto a eliminar
     */
    removeProducto: (id) => {
        const longitudInicial = productos.length;
        productos = productos.filter(p => p.id !== id);
        
        if (productos.length < longitudInicial) {
            console.log(`Producto con ID ${id} eliminado.`);
        } else {
            console.warn(`No se encontró ningún producto con ID ${id}.`);
        }
    },

     /**
     * Devuelve las categorias.
     */
    getCategorias: () => {
        return categorias;
    },

    /**
     * Añade el array de categorias a memoria.
    */
    setCategorias: (nuevasCategorias) => {
        categorias = nuevasCategorias;
    }
};
