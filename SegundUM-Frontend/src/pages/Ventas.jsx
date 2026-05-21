import { useState, useEffect } from "react";
import Header from "../components/Header";
import ListaCompraventas from "../components/ListaCompraventas";
import Paginador from "../components/Paginador";
import { apiCompraventas } from "../js/apiCompraventas";

function Ventas() {
    const [ventas, setVentas] = useState([]);
    const [paginas, setPaginas] = useState(null);

    useEffect(() => {
        const init = async () => {
            try {
                const { paginas: nuevaInfoPaginas, ventas: ventasRespuesta } = await apiCompraventas.getVentas(0);
                setVentas(ventasRespuesta);
                setPaginas(nuevaInfoPaginas);
            } catch (error) {
                console.error("Error al inicializar la Home:", error);
            }
        };

        init();
    }, []);

    const cambiarPagina = async (nuevaPagina) => {
        console.log("El usuario quiere ir a la página:", nuevaPagina);
        try {
            const { paginas: nuevaInfoPaginas, ventas: nuevasVentas } = await apiCompraventas.getVentas(nuevaPagina);

            console.log("nuevas ventas: ", nuevasVentas);
            console.log("nuevas paginas: ", nuevaInfoPaginas);
            setVentas(nuevasVentas);
            setPaginas(nuevaInfoPaginas);

            window.scrollTo({ top: 0, behavior: 'smooth' });
        } catch (error) {
            console.error("Error al cambiar de página: ", error);
        }
    };

    return (
        <>
            <Header />
            <ListaCompraventas titulo={"Ventas"} lista={ventas} />
            <Paginador paginacion={paginas} onCambioPagina={cambiarPagina}/>
        </>
    );
}

export default Ventas