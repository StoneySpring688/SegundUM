// Store en memoria: guarda el usuario activo, productos y categorías durante la sesión
let usuario = {};
let productos = [];
let categorias = [];

export const dataService = {
    getUsuario: () => ({ ...usuario }),
    setUsuario: (nuevosDatos) => { usuario = { ...nuevosDatos }; },
    clearUsuario: () => { usuario = {}; productos = []; },

    getProductos: () => [...productos],
    addProducto: (producto) => { productos.push(producto); },
    setProductos: (nuevosProductos) => { productos = nuevosProductos; },
    removeProducto: (id) => { productos = productos.filter(p => p.id !== id); },
    getProductoById: (id) => productos.find(p => p.id === id),

    getCategorias: () => categorias,
    setCategorias: (nuevasCategorias) => { categorias = nuevasCategorias; }
};
