import { useState, useEffect } from 'react'
import { useApp } from '../context/AppContext'
import * as api from '../services/api'

export default function Perfil() {
  const { usuario } = useApp()

  const [form, setForm] = useState({
    nombre: '', apellidos: '', email: '', clave: '',
    fechaNacimiento: '', telefono: '',
  })
  const [guardado, setGuardado] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    api.getUsuario(usuario.id).then(u => {
      setForm({
        nombre: u.nombre ?? '',
        apellidos: u.apellidos ?? '',
        email: u.email ?? '',
        clave: '',
        fechaNacimiento: u.fechaNacimiento ?? '',
        telefono: u.telefono ?? '',
      })
    }).catch(e => setError(e.message))
  }, [usuario.id])

  function set(campo, valor) {
    setForm(f => ({ ...f, [campo]: valor }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    try {
      await api.updateUsuario(usuario.id, form)
      setGuardado(true)
      setTimeout(() => setGuardado(false), 2500)
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <div className="container">
      <h1 className="mb-4">Mi perfil</h1>

      <form onSubmit={handleSubmit} className="bg-white p-4 rounded shadow-sm" style={{ maxWidth: '600px' }}>
        {guardado && <div className="mensaje-ok">Datos actualizados correctamente.</div>}
        {error && <div className="alert alert-danger">{error}</div>}

        <div className="row g-3">
          <div className="col-md-6">
            <label className="form-label">Nombre</label>
            <input type="text" className="form-control"
              value={form.nombre} onChange={e => set('nombre', e.target.value)} required />
          </div>
          <div className="col-md-6">
            <label className="form-label">Apellidos</label>
            <input type="text" className="form-control"
              value={form.apellidos} onChange={e => set('apellidos', e.target.value)} required />
          </div>
          <div className="col-12">
            <label className="form-label">Email</label>
            <input type="email" className="form-control"
              value={form.email} onChange={e => set('email', e.target.value)} required />
          </div>
          <div className="col-12">
            <label className="form-label">Nueva contraseña</label>
            <input type="password" className="form-control"
              value={form.clave} onChange={e => set('clave', e.target.value)} />
          </div>
          <div className="col-md-6">
            <label className="form-label">Fecha de nacimiento</label>
            <input type="date" className="form-control"
              value={form.fechaNacimiento} onChange={e => set('fechaNacimiento', e.target.value)} />
          </div>
          <div className="col-md-6">
            <label className="form-label">Teléfono</label>
            <input type="tel" className="form-control"
              value={form.telefono} onChange={e => set('telefono', e.target.value)} />
          </div>
        </div>

        <hr className="my-4" />
        <button type="submit" className="btn btn-primary">Guardar cambios</button>
      </form>
    </div>
  )
}
