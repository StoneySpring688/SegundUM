import { useState } from 'react'
import { BrowserRouter, Routes, Route, Link } from "react-router";
import Login from './pages/Login';
import Home from './pages/Home';
import Register from './pages/Register';
import Producto from './pages/Producto';
import Buscar from './pages/Buscar';
import PerfilUsuario from './pages/PerfilUsuario';

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Login/>}/>
        <Route path='/register/' element={<Register/>}/>
        <Route path='/home/' element={<Home/>}/>
        <Route path="/producto/:id" element={<Producto />} />
        <Route path='/buscar/' element={<Buscar/>}/>
        <Route path='/user/' element={<PerfilUsuario/>}/>
      </Routes>
    </BrowserRouter>
  )
}

export default App
