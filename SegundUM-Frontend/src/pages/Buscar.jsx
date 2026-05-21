import { useState } from "react";
import Header from "../components/Header"
import FormularioFiltros from "../components/FormularioFiltros"
import ListaProductos from "../components/ListaProductos";

function Buscar(){
    const [productos, setProductos] = useState([]);
    const [paginas, setPaginas] = useState({});

    const actualizarProductos = (nuevosProductos, paginas) => {
        setProductos(nuevosProductos);
        setPaginas(paginas);
    }

    return(
    <div>
        <Header/>
        <FormularioFiltros onResultadoBusqueda={actualizarProductos}/>
        <ListaProductos lista={productos}/>
    </div>)
}
export default Buscar