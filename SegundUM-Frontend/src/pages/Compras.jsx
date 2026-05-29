// Página: historial de compras del usuario autenticado
import { useState, useEffect } from "react";
import Header from "../components/Header";
import ListaCompraventas from "../components/ListaCompraventas";
import Paginador from "../components/Paginador";
import { apiCompraventas } from "../js/apiCompraventas";
import { useNavigate } from "react-router";

function Compras() {
    const [compras, setCompras] = useState([]);
    const [paginas, setPaginas] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const init = async () => {
            try {
                const { paginas: nuevaInfoPaginas, compras: comprasRespuesta } = await apiCompraventas.getCompras(0);
                setCompras(comprasRespuesta);
                setPaginas(nuevaInfoPaginas);
            } catch (error) {
                navigate("/");
                console.error("Error al inicializar la Home:", error);
            }
        };

        init();
    }, []);

    const cambiarPagina = async (nuevaPagina) => {
        console.log("El usuario quiere ir a la página:", nuevaPagina);
        try {
            const { paginas: nuevaInfoPaginas, compras: nuevasCompras } = await apiCompraventas.getCompras(nuevaPagina);

            console.log("nuevas compras: ", nuevasCompras);
            console.log("nuevas paginas: ", nuevaInfoPaginas);
            setCompras(nuevasCompras);
            setPaginas(nuevaInfoPaginas);

            window.scrollTo({ top: 0, behavior: 'smooth' });
        } catch (error) {
            console.error("Error al cambiar de página: ", error);
        }
    };

    return (
        <>
            <Header />
            <ListaCompraventas titulo={"Compras"} lista={compras} />
            <Paginador paginacion={paginas} onCambioPagina={cambiarPagina}/>
        </>
    );
}

export default Compras