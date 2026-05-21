import { Card, Button, Badge } from "react-bootstrap";
import { useNavigate } from "react-router";
import { dataService } from "../js/dataService";

function TarjetaProducto({ producto }) {

    const navigate = useNavigate();

    async function handleDetails(id) {
        //console.log("Se ha hecho click en el producto con ID:", dataService.getProductoById(id));
        //const producto = await apiProductos.getProductoById(id);
        //console.log("Producto: ", producto);

        // TODO hay que incrementar el numero de visualizaciones en uno.
        navigate(`/producto/${id}`);
    }

    return (

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
                        <span className="h4 mb-0 text-primary">{producto.precio} $</span>
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

                    <Button variant="primary" className="w-100" onClick={() => handleDetails(producto.id)}>
                        Ver Detalles
                    </Button>
                </div>
            </Card.Body>
        </Card>

    );
}

export default TarjetaProducto