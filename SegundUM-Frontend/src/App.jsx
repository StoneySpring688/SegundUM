import { useState } from 'react'
import { BrowserRouter, Routes, Route, Link } from "react-router";
import Login from './pages/Login';
import Home from './pages/Home';
import Register from './pages/Register';
import Producto from './pages/Producto';
import Buscar from './pages/Buscar';
import PerfilUsuario from './pages/PerfilUsuario';
import Compras from './pages/Compras';
import Ventas from './pages/Ventas';
import CompraventasEntre from './pages/CompraventasEntre';
import PublicarProducto from './pages/PublicarProducto';
import MisProductos from './pages/MisProductos';
import ListaUsuarios from './pages/ListaUsuarios';

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
        <Route path='/compras/' element={<Compras/>}/>
        <Route path='/ventas/' element={<Ventas/>}/>
        <Route path='/compraventasEntre/' element={<CompraventasEntre/>}/>
        <Route path='/publicar/' element={<PublicarProducto/>}/>
        <Route path='/misProductos/' element={<MisProductos/>}/>
        <Route path='/listaUsuarios/' element={<ListaUsuarios/>}/>
      </Routes>
    </BrowserRouter>
  )
}

export default App
