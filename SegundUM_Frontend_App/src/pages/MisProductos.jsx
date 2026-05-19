import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useApp } from '../context/AppContext'
import TarjetaProducto from '../components/TarjetaProducto'
import Paginacion from '../components/Paginacion'
import * as api from '../services/api'

export default function MisProductos() {
  const navigate = useNavigate()
  const { usuario } = useApp()

  const [productos, setProductos] = useState([])
  const [pagina, setPagina] = useState(0)
  const [totalPaginas, setTotalPaginas] = useState(0)
  const [cargando, setCargando] = useState(true)

  async function cargar(pg = 0) {
    setCargando(true)
    try {
      const data = await api.getMisProductos(usuario.id, pg)
      const lista = data?._embedded?.productoDTOList ?? data?.content ?? []
      setProductos(lista)
      setTotalPaginas(data?.page?.totalPages ?? data?.totalPages ?? 1)
      setPagina(pg)
    } catch {
      setProductos([])
    } finally {
      setCargando(false)
    }
  }

  useEffect(() => { cargar(0) }, [])

  return (
    <div className="container">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Mis productos en venta</h1>
        <button className="btn btn-primary" onClick={() => navigate('/productos/nuevo')}>
          <i className="bi bi-plus-lg"></i> Nuevo producto
        </button>
      </div>

      {cargando && <p className="text-muted">Cargando...</p>}

      {!cargando && productos.length === 0 && (
        <div className="alert alert-info">Todavía no tienes productos a la venta.</div>
      )}

      {!cargando && productos.length > 0 && (
        <>
          <div className="rejilla-productos">
            {productos.map(p => (
              <TarjetaProducto key={p.id} producto={p}
                onClick={() => navigate(`/productos/${p.id}`)} />
            ))}
          </div>
          <Paginacion pagina={pagina} totalPaginas={totalPaginas} onChange={cargar} />
        </>
      )}
    </div>
  )
}
