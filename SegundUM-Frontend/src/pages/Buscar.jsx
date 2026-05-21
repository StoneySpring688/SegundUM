import { useState } from "react";
import Header from "../components/Header"
import FormularioFiltros from "../components/FormularioFiltros"
import ListaProductos from "../components/ListaProductos";

function Buscar(){
    const [productos, setProductos] = useState([]);

    const actualizarProductos = (nuevosProductos) => {
        setProductos(nuevosProductos);
    }

    return(
    <div>
        <Header/>
        <FormularioFiltros onResultadoBusqueda={actualizarProductos}/>
        <ListaProductos lista={productos}/>
    </div>)
}
export default Buscar