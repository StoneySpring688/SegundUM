import { useState, useEffect } from 'react'
import * as api from '../services/api'

export default function AdminUsuarios() {
  const [usuarios, setUsuarios] = useState([])
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    api.getUsuarios()
      .then(data => {
        const lista = data?._embedded?.resumenUsuarioList ?? data?.content ?? []
        setUsuarios(lista)
      })
      .catch(e => setError(e.message))
      .finally(() => setCargando(false))
  }, [])

  return (
    <div className="container">
      <h1 className="mb-4">Usuarios registrados</h1>

      {cargando && <p className="text-muted">Cargando...</p>}
      {error && <div className="alert alert-danger">{error}</div>}

      {!cargando && !error && (
        <div className="tabla-admin table-responsive">
          <table className="table mb-0">
            <thead>
              <tr>
                <th>#</th>
                <th>Nombre</th>
                <th>Apellidos</th>
                <th>Email</th>
                <th>Admin</th>
              </tr>
            </thead>
            <tbody>
              {usuarios.map((u, i) => (
                <tr key={u.id ?? i}>
                  <td>{i + 1}</td>
                  <td>{u.nombre}</td>
                  <td>{u.apellidos}</td>
                  <td>{u.email}</td>
                  <td>
                    {u.administrador
                      ? <span className="badge bg-danger">Admin</span>
                      : <span className="badge bg-secondary">Usuario</span>}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
