import { useState } from 'react'
import { BrowserRouter, Routes, Route, Link } from "react-router";
import Login from './pages/Login';
import Home from './pages/Home';

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Login/>}/>
        <Route path='/home/' element={<Home/>}/>
      </Routes>
    </BrowserRouter>
  )
}

export default App
