const BASE = import.meta.env.VITE_API_URL

function authHeader() {
  const token = localStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
}

async function request(method, path, body) {
  const res = await fetch(`${BASE}${path}`, {
    method,
    headers: { 'Content-Type': 'application/json', ...authHeader() },
    body: body ? JSON.stringify(body) : undefined,
  })
  if (!res.ok) {
    const msg = await res.text().catch(() => res.statusText)
    throw new Error(msg || `Error ${res.status}`)
  }
  const text = await res.text()
  return text ? JSON.parse(text) : null
}

// Auth
export const login = (email, clave) =>
  request('POST', '/auth/login', { email, clave })

export const logout = () =>
  request('POST', '/auth/logout')

// Productos
export const getProductos = ({ texto = '', categoriaId = '', precioMaximo = '', page = 0, size = 8 } = {}) => {
  const params = new URLSearchParams({ page, size })
  if (texto)       params.set('texto', texto)
  if (categoriaId) params.set('categoriaId', categoriaId)
  if (precioMaximo) params.set('precioMaximo', precioMaximo)
  return request('GET', `/productos/buscar?${params}`)
}

export const getProducto = (id) =>
  request('GET', `/productos/${id}`)

export const createProducto = (data) =>
  request('POST', '/productos', data)

export const updateProducto = (id, data) =>
  request('PUT', `/productos/${id}`, data)

export const deleteProducto = (id) =>
  request('DELETE', `/productos/${id}`)

export const getMisProductos = (userId, page = 0, size = 6) =>
  request('GET', `/productos/vendedor/${userId}?page=${page}&size=${size}`)

// Categorías
export const getCategorias = () =>
  request('GET', '/categorias')

// Compraventas
export const getCompraventasVendedor = (id, page = 0, size = 8) =>
  request('GET', `/compraventas/vendedor/${id}?page=${page}&size=${size}`)

export const getCompraventasComprador = (id, page = 0, size = 8) =>
  request('GET', `/compraventas/comprador/${id}?page=${page}&size=${size}`)

export const getCompraventasEntre = (idComprador, idVendedor) =>
  request('GET', `/compraventas/entre?idComprador=${idComprador}&idVendedor=${idVendedor}`)

export const comprar = (idProducto, idComprador) =>
  request('POST', '/compraventas', { idProducto, idComprador })

// Usuarios
export const getUsuario = (id) =>
  request('GET', `/usuarios/${id}`)

export const updateUsuario = (id, data) =>
  request('PUT', `/usuarios/${id}`, data)

export const getUsuarios = () =>
  request('GET', '/usuarios')
