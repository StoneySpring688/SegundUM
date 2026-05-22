// Página: búsqueda de productos con filtros y paginación
import { useState } from "react";
import Header from "../components/Header"
import FormularioFiltros from "../components/FormularioFiltros"
import ListaProductos from "../components/ListaProductos";
import Paginador from "../components/Paginador";
import { apiProductos } from "../js/apiProductos";

function Buscar(){
    const [productos, setProductos] = useState([]);
    const [paginas, setPaginas] = useState(null);
    const [filtrosActuales, setFiltrosActuales] = useState({});

    const actualizarProductos = (nuevosProductos, paginas) => {
        setProductos(nuevosProductos);
        setPaginas(paginas);
    }

    const cambiarPagina = async (nuevaPagina) => {
        console.log("El usuario quiere ir a la página:", nuevaPagina);
        
        const nuevosFiltros = { ...filtrosActuales, page: nuevaPagina };
        
        try {
            const { paginas: nuevaInfoPaginas, productos: nuevosProductos } = await apiProductos.buscarProductos(nuevosFiltros);
            
            console.log("nuevosProductos: ", nuevosProductos);
            console.log("nuevas paginas: ", nuevaInfoPaginas);
            setProductos(nuevosProductos);
            setPaginas(nuevaInfoPaginas);
            setFiltrosActuales(nuevosFiltros);
            
            window.scrollTo({ top: 0, behavior: 'smooth' });
        } catch (error) {
            console.error("Error al cambiar de página:", error);
        }
    };

    return(
    <div>
        <Header/>
        <FormularioFiltros onResultadoBusqueda={actualizarProductos}/>
        <ListaProductos lista={productos} titulo={"Resultados"}/>
        <Paginador paginacion={paginas} onCambioPagina={cambiarPagina} />
    </div>
    )
}
export default Buscar