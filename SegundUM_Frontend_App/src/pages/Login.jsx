import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useApp } from '../context/AppContext'
import './login.css'

export default function Login() {
  const { login } = useApp()
  const navigate = useNavigate()

  const [email, setEmail] = useState('')
  const [clave, setClave] = useState('')
  const [error, setError] = useState(null)
  const [cargando, setCargando] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setCargando(true)
    try {
      await login(email, clave)
      navigate('/')
    } catch {
      setError('Email o contraseña incorrectos')
    } finally {
      setCargando(false)
    }
  }

  return (
    <div className="caja-login">
      <h2 className="titulo-login">
        Segund<span>UMU</span>
      </h2>
      <p className="subtitulo-login">Inicia sesión en tu cuenta</p>

      <form onSubmit={handleSubmit}>
        <div className="campo">
          <label htmlFor="email">Email</label>
          <input id="email" type="email" value={email}
            onChange={e => setEmail(e.target.value)}
            required autoComplete="email" />
        </div>

        <div className="campo">
          <label htmlFor="clave">Contraseña</label>
          <input id="clave" type="password" value={clave}
            onChange={e => setClave(e.target.value)}
            required autoComplete="current-password" />
        </div>

        {error && <div className="error-login" role="alert">{error}</div>}

        <button type="submit" className="btn-primario" disabled={cargando}>
          {cargando ? 'Entrando…' : 'Iniciar sesión'}
        </button>
      </form>
    </div>
  )
}
