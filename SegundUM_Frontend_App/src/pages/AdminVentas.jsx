import { useState, useEffect } from 'react'
import * as api from '../services/api'

export default function AdminVentas() {
  const [usuarios, setUsuarios] = useState([])
  const [idComprador, setIdComprador] = useState('')
  const [idVendedor, setIdVendedor] = useState('')
  const [ventas, setVentas] = useState(null)
  const [buscando, setBuscando] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    api.getUsuarios()
      .then(data => {
        const lista = data?._embedded?.resumenUsuarioList ?? data?.content ?? []
        setUsuarios(lista)
      })
      .catch(() => {})
  }, [])

  async function handleBuscar(e) {
    e.preventDefault()
    if (!idComprador || !idVendedor) return
    setBuscando(true)
    setError(null)
    try {
      const data = await api.getCompraventasEntre(idComprador, idVendedor)
      const lista = data?._embedded?.compraventaDTOList ?? data?.content ?? []
      setVentas(lista)
    } catch (e) {
      setError(e.message)
      setVentas([])
    } finally {
      setBuscando(false)
    }
  }

  return (
    <div className="container">
      <h1 className="mb-4">Compraventas entre usuarios</h1>

      <form onSubmit={handleBuscar} className="bg-white p-4 rounded shadow-sm mb-4" style={{ maxWidth: '600px' }}>
        <div className="row g-3">
          <div className="col-md-6">
            <label className="form-label">Comprador</label>
            <select className="form-select" value={idComprador}
              onChange={e => setIdComprador(e.target.value)} required>
              <option value="">Selecciona comprador</option>
              {usuarios.map(u => (
                <option key={u.id} value={u.id}>
                  {u.nombre} {u.apellidos}
                </option>
              ))}
            </select>
          </div>
          <div className="col-md-6">
            <label className="form-label">Vendedor</label>
            <select className="form-select" value={idVendedor}
              onChange={e => setIdVendedor(e.target.value)} required>
              <option value="">Selecciona vendedor</option>
              {usuarios.map(u => (
                <option key={u.id} value={u.id}>
                  {u.nombre} {u.apellidos}
                </option>
              ))}
            </select>
          </div>
        </div>
        <hr className="my-3" />
        <button type="submit" className="btn btn-primary" disabled={buscando}>
          <i className="bi bi-search"></i> {buscando ? 'Buscando...' : 'Buscar'}
        </button>
      </form>

      {error && <div className="alert alert-danger">{error}</div>}

      {ventas !== null && (
        ventas.length === 0 ? (
          <div className="alert alert-info">No hay compraventas entre estos usuarios.</div>
        ) : (
          <div className="tabla-admin table-responsive">
            <table className="table mb-0">
              <thead>
                <tr>
                  <th>Producto</th>
                  <th>Vendedor</th>
                  <th>Comprador</th>
                  <th>Fecha</th>
                  <th className="text-end">Precio</th>
                </tr>
              </thead>
              <tbody>
                {ventas.map(v => (
                  <tr key={v.id}>
                    <td>{v.titulo}</td>
                    <td>{v.nombreVendedor}</td>
                    <td>{v.nombreComprador}</td>
                    <td>{v.fechaYHora ? new Date(v.fechaYHora).toLocaleDateString() : '—'}</td>
                    <td className="text-end"><strong>{v.precio} €</strong></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )
      )}
    </div>
  )
}
