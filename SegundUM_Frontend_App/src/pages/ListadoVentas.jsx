import { useState, useEffect } from 'react'
import { useApp } from '../context/AppContext'
import Paginacion from '../components/Paginacion'
import * as api from '../services/api'

export default function ListadoVentas({ modo }) {
  const { usuario } = useApp()

  const [ventas, setVentas] = useState([])
  const [pagina, setPagina] = useState(0)
  const [totalPaginas, setTotalPaginas] = useState(0)
  const [cargando, setCargando] = useState(true)

  const titulo = modo === 'vendidos' ? 'Mis ventas' : 'Mis compras'

  async function cargar(pg = 0) {
    setCargando(true)
    try {
      const fn = modo === 'vendidos'
        ? api.getCompraventasVendedor
        : api.getCompraventasComprador
      const data = await fn(usuario.id, pg)
      const lista = data?._embedded?.compraventaDTOList ?? data?.content ?? []
      setVentas(lista)
      setTotalPaginas(data?.page?.totalPages ?? data?.totalPages ?? 1)
      setPagina(pg)
    } catch {
      setVentas([])
    } finally {
      setCargando(false)
    }
  }

  useEffect(() => { cargar(0) }, [modo])

  return (
    <div className="container">
      <h1 className="mb-4">{titulo}</h1>

      {cargando && <p className="text-muted">Cargando...</p>}

      {!cargando && ventas.length === 0 && (
        <div className="alert alert-info">No hay registros.</div>
      )}

      {!cargando && ventas.length > 0 && (
        <>
          <div className="tabla-admin table-responsive">
            <table className="table mb-0">
              <thead>
                <tr>
                  <th>Producto</th>
                  <th>{modo === 'vendidos' ? 'Comprador' : 'Vendedor'}</th>
                  <th>Fecha</th>
                  <th className="text-end">Precio</th>
                </tr>
              </thead>
              <tbody>
                {ventas.map(v => (
                  <tr key={v.id}>
                    <td>{v.titulo}</td>
                    <td>{modo === 'vendidos' ? v.nombreComprador : v.nombreVendedor}</td>
                    <td>{v.fechaYHora ? new Date(v.fechaYHora).toLocaleDateString() : '—'}</td>
                    <td className="text-end"><strong>{v.precio} €</strong></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <Paginacion pagina={pagina} totalPaginas={totalPaginas} onChange={cargar} />
        </>
      )}
    </div>
  )
}
