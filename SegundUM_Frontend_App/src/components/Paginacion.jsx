import './Paginacion.css'

export default function Paginacion({ pagina, totalPaginas, onChange }) {
  if (totalPaginas <= 1) return null

  return (
    <div className="paginacion">
      <button className="btn btn-sm btn-outline-secondary"
        disabled={pagina === 0}
        onClick={() => onChange(pagina - 1)}>
        &laquo;
      </button>

      {Array.from({ length: totalPaginas }, (_, i) => (
        <button key={i}
          className={`btn btn-sm ${i === pagina ? 'btn-primary' : 'btn-outline-secondary'}`}
          onClick={() => onChange(i)}>
          {i + 1}
        </button>
      ))}

      <button className="btn btn-sm btn-outline-secondary"
        disabled={pagina === totalPaginas - 1}
        onClick={() => onChange(pagina + 1)}>
        &raquo;
      </button>
    </div>
  )
}
