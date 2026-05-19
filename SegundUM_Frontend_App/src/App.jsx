import { Routes, Route, Navigate } from 'react-router-dom'
import { useApp } from './context/AppContext'
import Navbar from './components/Navbar'
import Footer from './components/Footer'
import Login from './pages/Login'
import PaginaProductos from './pages/PaginaProductos'
import Detalle from './pages/Detalle'
import FormProducto from './pages/FormProducto'
import MisProductos from './pages/MisProductos'
import ListadoVentas from './pages/ListadoVentas'
import Perfil from './pages/Perfil'
import AdminUsuarios from './pages/AdminUsuarios'
import AdminVentas from './pages/AdminVentas'

function Protegida({ children }) {
  const { usuario } = useApp()
  return usuario ? children : <Navigate to="/login" replace />
}

function SoloAdmin({ children }) {
  const { usuario } = useApp()
  if (!usuario) return <Navigate to="/login" replace />
  if (!usuario.roles?.includes('ADMINISTRADOR')) return <Navigate to="/" replace />
  return children
}

export default function App() {
  const { usuario } = useApp()

  return (
    <>
      {usuario && <Navbar />}
      <main className="contenido-principal">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<Protegida><PaginaProductos /></Protegida>} />
          <Route path="/productos/nuevo" element={<Protegida><FormProducto /></Protegida>} />
          <Route path="/productos/:id/editar" element={<Protegida><FormProducto /></Protegida>} />
          <Route path="/productos/:id" element={<Protegida><Detalle /></Protegida>} />
          <Route path="/mis-productos" element={<Protegida><MisProductos /></Protegida>} />
          <Route path="/mis-ventas" element={<Protegida><ListadoVentas modo="vendidos" /></Protegida>} />
          <Route path="/mis-compras" element={<Protegida><ListadoVentas modo="comprados" /></Protegida>} />
          <Route path="/perfil" element={<Protegida><Perfil /></Protegida>} />
          <Route path="/admin/usuarios" element={<SoloAdmin><AdminUsuarios /></SoloAdmin>} />
          <Route path="/admin/ventas" element={<SoloAdmin><AdminVentas /></SoloAdmin>} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
      {usuario && <Footer />}
    </>
  )
}
