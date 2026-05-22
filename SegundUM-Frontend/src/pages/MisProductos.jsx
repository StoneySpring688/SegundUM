import { useState, useEffect } from "react";
import Header from "../components/Header"
import ListaProductos from "../components/ListaProductos";
import Paginador from "../components/Paginador";
import { apiProductos } from "../js/apiProductos";

function MisProductos(){
    const [productos, setProductos] = useState([]);
    const [paginas, setPaginas] = useState(null);

    useEffect(() =>{
        const init = async (params) => {
            try {
                const { paginas: nuevaInfoPaginas, productos: nuevosProductos } = await apiProductos.getProductosPublicados(0);
                setProductos(nuevosProductos);
                setPaginas(nuevaInfoPaginas);
            } catch (error) {
                console.error("Error al inicializar mis productos: ", error);
            }
        };
        init();
    },[]);

    const cambiarPagina = async (nuevaPagina) => {
        console.log("El usuario quiere ir a la página:", nuevaPagina);
        
        try {
            const { paginas: nuevaInfoPaginas, productos: nuevosProductos } = await apiProductos.getProductosPublicados(nuevaPagina);
            
            console.log("nuevosProductos: ", nuevosProductos);
            console.log("nuevas paginas: ", nuevaInfoPaginas);
            setProductos(nuevosProductos);
            setPaginas(nuevaInfoPaginas);
            
            window.scrollTo({ top: 0, behavior: 'smooth' });
        } catch (error) {
            console.error("Error al cambiar de página:", error);
        }
    };

    return(
    <div>
        <Header/>
        <ListaProductos lista={productos} titulo={"Mis productos"}/>
        <Paginador paginacion={paginas} onCambioPagina={cambiarPagina} />
    </div>
    )
}
export default MisProductos