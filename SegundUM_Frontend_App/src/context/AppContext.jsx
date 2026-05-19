import { createContext, useContext, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as api from '../services/api'

const AppContext = createContext(null)

export function AppProvider({ children }) {
  const navigate = useNavigate()

  const [usuario, setUsuario] = useState(() => {
    const stored = localStorage.getItem('usuario')
    return stored ? JSON.parse(stored) : null
  })

  const [categorias, setCategorias] = useState([])

  useEffect(() => {
    api.getCategorias()
      .then(data => {
        const lista = data?._embedded?.resumenCategoriaList ?? data?.content ?? data ?? []
        setCategorias(Array.isArray(lista) ? lista : [])
      })
      .catch(() => {})
  }, [])

  async function loginFn(email, clave) {
    const data = await api.login(email, clave)
    localStorage.setItem('token', data.token)
    const u = { id: data.id, nombre: data.nombre, roles: data.roles }
    localStorage.setItem('usuario', JSON.stringify(u))
    setUsuario(u)
    return u
  }

  async function logoutFn() {
    await api.logout().catch(() => {})
    localStorage.removeItem('token')
    localStorage.removeItem('usuario')
    setUsuario(null)
    navigate('/login')
  }

  return (
    <AppContext.Provider value={{ usuario, categorias, login: loginFn, logout: logoutFn }}>
      {children}
    </AppContext.Provider>
  )
}

export function useApp() {
  return useContext(AppContext)
}
