import { Link, useLocation } from 'react-router-dom'
import { useApp } from '../context/AppContext'
import './Navbar.css'

export default function Navbar() {
  const { usuario, logout } = useApp()
  const { pathname } = useLocation()

  if (!usuario) return null

  const esAdmin = usuario.roles?.includes('ADMINISTRADOR')

  const enlaces = esAdmin
    ? [
        { to: '/admin/usuarios', texto: 'Usuarios' },
        { to: '/admin/ventas',   texto: 'Ventas' },
      ]
    : [
        { to: '/',               texto: 'Productos' },
        { to: '/productos/nuevo', texto: 'Vender' },
        { to: '/mis-productos',  texto: 'Mis productos' },
        { to: '/mis-ventas',     texto: 'Vendidos' },
        { to: '/mis-compras',    texto: 'Comprados' },
      ]

  return (
    <nav className="navbar navbar-expand-md navbar-segundum">
      <div className="container">
        <Link className="navbar-brand logo" to={esAdmin ? '/admin/usuarios' : '/'}>
          Segund<span>UMU</span>
        </Link>

        <button className="navbar-toggler" type="button"
          data-bs-toggle="collapse" data-bs-target="#navMenu">
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navMenu">
          <ul className="navbar-nav me-auto">
            {enlaces.map(e => (
              <li key={e.to} className="nav-item">
                <Link
                  className={`nav-link${pathname === e.to ? ' active' : ''}`}
                  to={e.to}
                >
                  {e.texto}
                </Link>
              </li>
            ))}
          </ul>

          <div className="d-flex align-items-center gap-2">
            <Link className="nav-link text-white" to="/perfil">
              <i className="bi bi-person-circle"></i> {usuario.nombre}
            </Link>
            <button className="btn btn-sm btn-outline-light" onClick={logout}>
              Salir
            </button>
          </div>
        </div>
      </div>
    </nav>
  )
}
