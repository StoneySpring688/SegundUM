import { useEffect, useState } from "react";
import { Container, Row, Col, Card, Button, Badge } from "react-bootstrap";
import Header from "../components/Header";
import { apiProductos } from "../js/apiProductos";
import {dataService} from "../js/dataService";

function Home() {
    const [productos, setProductos] = useState([]);

    useEffect(() => {
        const init = async () => {
            try {
                // 1. Cargamos las categorías en memoria (si no estaban ya)
                const categorias = await apiProductos.getAllCategorias();
                dataService.setCategorias(categorias);
                // console.log("Categorías cargadas:", dataService.getCategorias());

                // 2. Cargamos los productos reales de la API
                // Llamamos a buscarProductos sin filtros, o con paginación inicial
                const productosReales = await apiProductos.buscarProductos({
                    page: 0,
                    size: 12
                });
                
                // 3. Guardamos los productos en el estado para que se rendericen
                setProductos(productosReales);
                productosReales.forEach(p => dataService.addProducto(p));
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

            <Container className="py-4">
                <h2 className="mb-4">Productos Destacados</h2>
                <Row xs={1} md={2} lg={3} xl={4} className="g-4">
                    {productos.map((producto) => (
                        <Col key={producto.id}>
                            <Card className="h-100 shadow-sm border-0">
                                {/*Esto es la locura máxima para sacar imagenes de prueba*/}
                                <Card.Img 
                                    variant="top" 
                                    src={`https://picsum.photos/seed/${producto.id}/400/300`}
                                    alt={producto.titulo}
                                />
                                <Card.Body className="d-flex flex-column">
                                    <div className="d-flex justify-content-between align-items-start mb-2">
                                        <Card.Title className="h5 mb-0 text-truncate" title={producto.titulo}>
                                            {producto.titulo}
                                        </Card.Title>
                                        <Badge bg={producto.estado === "NUEVO" ? "success" : "warning"} text={producto.estado === "NUEVO" ? "white" : "dark"}>
                                            {producto.estado}
                                        </Badge>
                                    </div>
                                    
                                    <Card.Text className="text-muted small flex-grow-1">
                                        {producto.descripcion}
                                    </Card.Text>

                                    <div className="mt-3">
                                        <div className="d-flex justify-content-between align-items-center mb-2">
                                            <span className="h4 mb-0 text-primary">{producto.precio} €</span>
                                            {producto.envioDisponible && (
                                                <Badge pill bg="info" text="dark">Envío disponible</Badge>
                                            )}
                                        </div>
                                        
                                        <div className="d-flex justify-content-between align-items-center mb-3">
                                            <small className="text-muted">
                                                <i className="bi bi-tag-fill me-1"></i>
                                                {producto.categoriaNombre}
                                            </small>
                                            <small className="text-muted">
                                                {producto.visualizaciones} visitas
                                            </small>
                                        </div>

                                        {/*TODO añadir onClick con una función para ir a la vista de detalles*/}
                                        <Button variant="primary" className="w-100">
                                            Ver Detalles
                                        </Button>
                                    </div>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Container>

        </div>
    );
}

export default Home;
