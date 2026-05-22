// Página: portada con los primeros 12 productos y precarga de categorías en memoria
import { useEffect, useState } from "react";
import { Container, Row, Col, Card, Button, Badge } from "react-bootstrap";
import Header from "../components/Header";
import ListaProductos from "../components/ListaProductos";
import { apiProductos } from "../js/apiProductos";
import { apiUsuarios } from "../js/apiUsuarios";
import {dataService} from "../js/dataService";

function Home() {
    const [productos, setProductos] = useState([]);

    useEffect(() => {
        const init = async () => {
            try {
                let categorias = dataService.getCategorias();
                if(categorias.length === 0){
                    categorias = await apiProductos.getAllCategorias();
                    dataService.setCategorias(categorias);
                    console.log("Categorias obtenidas: ", dataService.getCategorias());
                }
                console.log("categorias de memoria: ", categorias);
                
                const {productos: productosReales} = await apiProductos.buscarProductos({
                    page: 0,
                    size: 12
                });
                
                

                setProductos(productosReales);
                dataService.setProductos(productosReales);
                console.log("productos almacenados: ", dataService.getProductos())
            } catch (error) {
                console.error("Error al inicializar la Home:", error);
            }
            
            
        };

        init();
    }, []);

    return (
        <div className="bg-light" style={{ minHeight: '100vh' }}>
            <Header />
            <ListaProductos lista = {productos} titulo={"Productos Destacados"}/>
        </div>
    );
}

export default Home;
