import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useApp } from '../context/AppContext'
import * as api from '../services/api'
import './FormProducto.css'

export default function FormProducto() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { categorias } = useApp()
  const esNuevo = !id

  const [form, setForm] = useState({
    titulo: '', descripcion: '', precio: '',
    categoriaId: '', envioDisponible: false, lugar: '',
  })
  const [guardando, setGuardando] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    if (!esNuevo) {
      api.getProducto(id).then(p => {
        setForm({
          titulo: p.titulo ?? '',
          descripcion: p.descripcion ?? '',
          precio: p.precio ?? '',
          categoriaId: p.categoriaId ?? '',
          envioDisponible: p.envioDisponible ?? false,
          lugar: p.recogida?.descripcion ?? '',
        })
      }).catch(e => setError(e.message))
    }
  }, [id, esNuevo])

  function set(campo, valor) {
    setForm(f => ({ ...f, [campo]: valor }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setGuardando(true)
    setError(null)
    try {
      const payload = {
        titulo: form.titulo,
        descripcion: form.descripcion,
        precio: parseFloat(form.precio),
        categoriaId: form.categoriaId,
        envioDisponible: form.envioDisponible,
        recogida: { descripcion: form.lugar, latitud: 0, longitud: 0 },
      }
      if (esNuevo) {
        await api.createProducto(payload)
        navigate('/')
      } else {
        await api.updateProducto(id, { descripcion: form.descripcion, precio: parseFloat(form.precio) })
        navigate(`/productos/${id}`)
      }
    } catch (e) {
      setError(e.message)
    } finally {
      setGuardando(false)
    }
  }

  return (
    <div className="container">
      <h1>{esNuevo ? 'Poner producto en venta' : 'Editar producto'}</h1>

      <form onSubmit={handleSubmit} className="form-producto mt-3">
        {error && <div className="alert alert-danger">{error}</div>}

        <div className="row g-3">
          <div className="col-md-8">
            <label className="form-label">Título *</label>
            <input type="text" className="form-control"
              value={form.titulo} onChange={e => set('titulo', e.target.value)}
              required disabled={!esNuevo} />
          </div>
          <div className="col-md-4">
            <label className="form-label">Precio (€) *</label>
            <input type="number" step="0.01" min="0" className="form-control"
              value={form.precio} onChange={e => set('precio', e.target.value)} required />
          </div>

          <div className="col-12">
            <label className="form-label">Descripción *</label>
            <textarea className="form-control" rows="4"
              value={form.descripcion} onChange={e => set('descripcion', e.target.value)} required />
          </div>

          {esNuevo && (
            <div className="col-md-6">
              <label className="form-label">Categoría *</label>
              <select className="form-select"
                value={form.categoriaId} onChange={e => set('categoriaId', e.target.value)} required>
                <option value="">Selecciona una categoría</option>
                {categorias.map(c => (
                  <option key={c.id} value={c.id}>{c.nombre}</option>
                ))}
              </select>
            </div>
          )}

          {esNuevo && (
            <div className="col-md-6">
              <label className="form-label">Lugar de recogida *</label>
              <input type="text" className="form-control"
                value={form.lugar} onChange={e => set('lugar', e.target.value)} required />
            </div>
          )}

          {esNuevo && (
            <div className="col-12">
              <div className="form-check">
                <input className="form-check-input" type="checkbox" id="envio"
                  checked={form.envioDisponible}
                  onChange={e => set('envioDisponible', e.target.checked)} />
                <label className="form-check-label" htmlFor="envio">
                  Envío disponible
                </label>
              </div>
            </div>
          )}
        </div>

        <hr className="my-4" />

        <button type="submit" className="btn btn-primary me-2" disabled={guardando}>
          {guardando ? 'Guardando...' : esNuevo ? 'Publicar' : 'Guardar cambios'}
        </button>
        <button type="button" className="btn btn-outline-secondary"
          onClick={() => navigate(-1)}>
          Cancelar
        </button>
      </form>
    </div>
  )
}
