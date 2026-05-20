import { useEffect, useState } from "react";
import { Container, Row, Col, Card, Button, Badge } from "react-bootstrap";
import Header from "../components/Header";
import { apiProductos } from "../js/apiProductos";
import {dataService} from "../js/dataService";

function Home() {
    const [productos, setProductos] = useState([]);

    useEffect(() => {
        // Función de inicialización para cargar datos de prueba
        const init = async () => {
            const productosPrueba = Array.from({ length: 10 }, (_, i) => ({
                id: `prod-${i + 1}`,
                titulo: `Producto de Ejemplo ${i + 1}`,
                descripcion: `Esta es una descripción breve del producto ${i + 1} para probar el diseño de la interfaz.`,
                precio: parseFloat((Math.random() * 200).toFixed(2)),
                estado: i % 2 === 0 ? "NUEVO" : "USADO",
                fechaPublicacion: new Date().toISOString(),
                visualizaciones: Math.floor(Math.random() * 50),
                envioDisponible: i % 3 !== 0,
                vendedorId: "vend-001",
                categoriaId: "cat-001",
                categoriaNombre: "Electrónica",
                recogida: {
                    descripcion: "Calle Mayor, Murcia",
                    longitud: -1.13,
                    latitud: 37.98,
                },
                vendido: false,
            }));
            setProductos(productosPrueba);
            const categorias = await apiProductos.getAllCategorias();
            dataService.setCategorias(categorias);
            console.log(dataService.getCategorias());
            
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
                                                <Badge pill bg="info" text="dark">Envío gratis</Badge>
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
