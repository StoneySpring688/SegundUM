import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useApp } from '../context/AppContext'
import * as api from '../services/api'
import './Detalle.css'

const ICONOS = {
  'Electrónica': 'bi-laptop', 'Moda': 'bi-bag', 'Hogar': 'bi-house-door',
  'Libros': 'bi-book', 'Deportes': 'bi-bicycle', 'Vehículos': 'bi-car-front', 'Otros': 'bi-box',
}

export default function Detalle() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { usuario } = useApp()

  const [producto, setProducto] = useState(null)
  const [vendedor, setVendedor] = useState(null)
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(null)
  const [comprando, setComprando] = useState(false)
  const [comprado, setComprado] = useState(false)

  useEffect(() => {
    async function cargar() {
      try {
        const p = await api.getProducto(id)
        setProducto(p)
        if (p.vendedorId) {
          const v = await api.getUsuario(p.vendedorId).catch(() => null)
          setVendedor(v)
        }
      } catch (e) {
        setError(e.message)
      } finally {
        setCargando(false)
      }
    }
    cargar()
  }, [id])

  async function handleComprar() {
    setComprando(true)
    try {
      await api.comprar(producto.id, usuario.id)
      setComprado(true)
    } catch (e) {
      alert(e.message)
    } finally {
      setComprando(false)
    }
  }

  async function handleEliminar() {
    if (!confirm('¿Eliminar este producto?')) return
    await api.deleteProducto(producto.id)
    navigate('/')
  }

  if (cargando) return <div className="container mt-4"><p>Cargando...</p></div>
  if (error)    return <div className="container mt-4"><div className="alert alert-danger">{error}</div></div>
  if (!producto) return null

  const esMio = producto.vendedorId === usuario.id
  const icono = ICONOS[producto.categoriaNombre] ?? 'bi-box'

  return (
    <div className="container">
      <button className="btn btn-sm btn-outline-secondary mb-3" onClick={() => navigate(-1)}>
        <i className="bi bi-arrow-left"></i> Volver
      </button>

      <div className="detalle-producto">
        <div className="row">
          <div className="col-md-5 mb-3">
            <div className="imagen-grande">
              <i className={`bi ${icono}`}></i>
            </div>
          </div>

          <div className="col-md-7">
            <h2>{producto.titulo}</h2>
            <p className="precio-grande">{producto.precio} €</p>

            <div className="mb-3">
              {producto.categoriaNombre && (
                <span className="etiqueta-info">
                  <i className="bi bi-tag"></i> {producto.categoriaNombre}
                </span>
              )}
              {producto.recogida?.descripcion && (
                <span className="etiqueta-info">
                  <i className="bi bi-geo-alt"></i> {producto.recogida.descripcion}
                </span>
              )}
              <span className="etiqueta-info">
                {producto.envioDisponible
                  ? <><i className="bi bi-truck"></i> Envío disponible</>
                  : <><i className="bi bi-x-circle"></i> Solo recogida</>}
              </span>
            </div>

            <h5>Descripción</h5>
            <p>{producto.descripcion}</p>

            <p className="text-muted small">
              {producto.fechaPublicacion && <>Publicado el {new Date(producto.fechaPublicacion).toLocaleDateString()} · </>}
              {producto.visualizaciones ?? 0} visualizaciones ·{' '}
              Vendedor: <strong>{vendedor ? `${vendedor.nombre} ${vendedor.apellidos}` : '—'}</strong>
            </p>

            <hr />

            {comprado ? (
              <div className="mensaje-ok">
                <i className="bi bi-check-circle"></i> Compra realizada correctamente.
              </div>
            ) : esMio ? (
              <div>
                <p className="text-muted">Este producto es tuyo.</p>
                <button className="btn btn-primary me-2"
                  onClick={() => navigate(`/productos/${producto.id}/editar`)}>
                  <i className="bi bi-pencil"></i> Editar
                </button>
                <button className="btn btn-outline-danger" onClick={handleEliminar}>
                  <i className="bi bi-trash"></i> Eliminar
                </button>
              </div>
            ) : (
              <button className="btn btn-warning btn-lg" onClick={handleComprar} disabled={comprando}>
                <i className="bi bi-cart"></i> {comprando ? 'Procesando...' : 'Solicitar compra'}
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
