import './TarjetaProducto.css'

const ICONOS = {
  'Electrónica': 'bi-laptop',
  'Moda':        'bi-bag',
  'Hogar':       'bi-house-door',
  'Libros':      'bi-book',
  'Deportes':    'bi-bicycle',
  'Vehículos':   'bi-car-front',
  'Otros':       'bi-box',
}

export default function TarjetaProducto({ producto, onClick }) {
  const icono = ICONOS[producto.categoriaNombre] ?? 'bi-box'

  return (
    <div className="tarjeta-producto" onClick={onClick} role="button" tabIndex={0}>
      <div className="imagen">
        <i className={`bi ${icono}`}></i>
      </div>
      <div className="cuerpo">
        <div className="titulo">{producto.titulo}</div>
        <div className="precio">{producto.precio} €</div>
        <div className="meta">
          {producto.recogida?.descripcion ?? ''}
          {producto.envioDisponible ? ' · Envío disponible' : ''}
        </div>
      </div>
    </div>
  )
}
