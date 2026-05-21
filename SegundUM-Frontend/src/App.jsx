import { useState } from 'react'
import { BrowserRouter, Routes, Route, Link } from "react-router";
import Login from './pages/Login';
import Home from './pages/Home';
import Register from './pages/Register';
import Producto from './pages/Producto';
import Buscar from './pages/Buscar';

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Login/>}/>
        <Route path='/register/' element={<Register/>}/>
        <Route path='/home/' element={<Home/>}/>
        <Route path="/producto/:id" element={<Producto />} />
        <Route path='/buscar/' element={<Buscar/>}/>
      </Routes>
    </BrowserRouter>
  )
}

export default App
