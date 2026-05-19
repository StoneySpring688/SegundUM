import './Filtros.css'

export default function Filtros({ filtros, categorias, onChange, onLimpiar }) {
  return (
    <div className="caja-filtros">
      <h5>Filtros</h5>

      <div className="mb-3">
        <label className="form-label">Categoría</label>
        <select className="form-select form-select-sm"
          value={filtros.categoriaId}
          onChange={e => onChange({ ...filtros, categoriaId: e.target.value })}>
          <option value="">Todas</option>
          {categorias.map(c => (
            <option key={c.id} value={c.id}>{c.nombre}</option>
          ))}
        </select>
      </div>

      <div className="mb-3">
        <label className="form-label">Precio máx (€)</label>
        <input type="number" className="form-control form-control-sm" min="0"
          value={filtros.precioMaximo}
          onChange={e => onChange({ ...filtros, precioMaximo: e.target.value })} />
      </div>

      <button className="btn btn-sm btn-outline-secondary w-100" onClick={onLimpiar}>
        Limpiar filtros
      </button>
    </div>
  )
}
