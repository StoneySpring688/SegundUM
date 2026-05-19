import { useState, useEffect, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { useApp } from '../context/AppContext'
import TarjetaProducto from '../components/TarjetaProducto'
import Filtros from '../components/Filtros'
import Paginacion from '../components/Paginacion'
import * as api from '../services/api'
import './PaginaProductos.css'

const FILTROS_VACIOS = { categoriaId: '', precioMaximo: '' }

export default function PaginaProductos() {
  const navigate = useNavigate()
  const { categorias } = useApp()

  const [texto, setTexto] = useState('')
  const [filtros, setFiltros] = useState(FILTROS_VACIOS)
  const [pagina, setPagina] = useState(0)
  const [resultado, setResultado] = useState({ productos: [], totalPaginas: 0, total: 0 })
  const [cargando, setCargando] = useState(false)
  const [error, setError] = useState(null)

  const buscar = useCallback(async (pg = 0) => {
    setCargando(true)
    setError(null)
    try {
      const data = await api.getProductos({
        texto,
        categoriaId: filtros.categoriaId,
        precioMaximo: filtros.precioMaximo,
        page: pg,
        size: 8,
      })
      const lista = data?._embedded?.productoDTOList ?? data?.content ?? []
      const totalPaginas = data?.page?.totalPages ?? data?.totalPages ?? 1
      const total = data?.page?.totalElements ?? data?.totalElements ?? lista.length
      setResultado({ productos: lista, totalPaginas, total })
      setPagina(pg)
    } catch (e) {
      setError(e.message)
    } finally {
      setCargando(false)
    }
  }, [texto, filtros])

  useEffect(() => { buscar(0) }, [filtros])

  function handleBuscar(e) {
    e.preventDefault()
    buscar(0)
  }

  function limpiar() {
    setTexto('')
    setFiltros(FILTROS_VACIOS)
  }

  return (
    <div className="container">
      <form className="barra-busqueda mb-4" onSubmit={handleBuscar}>
        <input
          type="text"
          className="form-control"
          placeholder="Buscar productos por título..."
          value={texto}
          onChange={e => setTexto(e.target.value)}
        />
        <button type="submit" className="btn btn-primary">
          <i className="bi bi-search"></i>
        </button>
      </form>

      <div className="layout-listado">
        <aside>
          <Filtros
            filtros={filtros}
            categorias={categorias}
            onChange={f => { setFiltros(f); setPagina(0) }}
            onLimpiar={limpiar}
          />
        </aside>

        <section>
          {cargando && <p className="text-muted">Cargando...</p>}
          {error && <div className="alert alert-danger">{error}</div>}

          {!cargando && !error && (
            <>
              <p className="text-muted">{resultado.total} producto(s) encontrados</p>

              {resultado.productos.length === 0 ? (
                <div className="alert alert-warning">
                  No hay productos que coincidan con los filtros.
                </div>
              ) : (
                <div className="rejilla-productos">
                  {resultado.productos.map(p => (
                    <TarjetaProducto
                      key={p.id}
                      producto={p}
                      onClick={() => navigate(`/productos/${p.id}`)}
                    />
                  ))}
                </div>
              )}

              <Paginacion
                pagina={pagina}
                totalPaginas={resultado.totalPaginas}
                onChange={pg => buscar(pg)}
              />
            </>
          )}
        </section>
      </div>
    </div>
  )
}
