import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router";
import { Container, Card, Button, Badge, Spinner, Alert } from "react-bootstrap";
import Header from "../components/Header";
import { dataService } from "../js/dataService";
import { apiProductos } from "../js/apiProductos";
import api from "../js/axiosConfig";

function Producto() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [producto, setProducto] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);


  // TODO aquí tengo un dilema, resgistrar pprimero la visualización y ver el detalle con esa visualización, o hacerlo después y no verla hasta volveer al home.
  useEffect(() => {
    const cargarDetalle = async () => {
      try {
        setCargando(true);
        console.log("Cargando datos con el id del producto:", id);

        let prod = dataService.getProductoById(id);

        if (!prod) {
          console.log("Producto no encontrado en memoria. Solicitando a la API...");
          prod = await apiProductos.getProductoById(id);
        }

        if (prod) {
          setProducto(prod);
          await apiProductos.registrarVisualizacion(id);
        } else {
          setError("El producto solicitado no existe.");
        }
      } catch (err) {
        setError("No se pudo cargar la información del producto.");
        console.error(err);
      } finally {
        setCargando(false);
      }
    };

    if (id) {
      cargarDetalle();
    }
  }, [id]);

  if (cargando) {
    return (
      <div className="bg-light" style={{ minHeight: '100vh' }}>
        <Header />
        <Container className="text-center py-5">
          <Spinner animation="border" variant="primary" />
          <p className="mt-2 text-muted">Cargando detalles del producto...</p>
        </Container>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-light" style={{ minHeight: '100vh' }}>
        <Header />
        <Container className="py-5">
          <Alert variant="danger">
            <Alert.Heading>¡Uy! Hubo un problema {":("}</Alert.Heading>
            <p>{error}</p>
            <hr />
            <Button variant="outline-danger" onClick={() => navigate("/home")}>
              Volver al Inicio
            </Button>
          </Alert>
        </Container>
      </div>
    );
  }

  return (
    <div className="bg-light" style={{ minHeight: '100vh' }}>
      <Header />
      <Container className="py-5">
        <Button variant="outline-secondary" className="mb-4" onClick={() => navigate(-1)}>
          {"<- Volver atrás"}
        </Button>

        <Card className="shadow-sm border-0 overflow-hidden">
          <div className="row g-0">
            <div className="col-md-6">
              <Card.Img 
                src={`https://picsum.photos/seed/${producto.id}/600/400`} 
                alt={producto.titulo}
                className="h-100 object-fit-cover"
              />
            </div>
            <div className="col-md-6">
              <Card.Body className="p-4 d-flex flex-column h-100">
                <div className="d-flex justify-content-between align-items-center mb-3">
                  <Badge bg="primary">{producto.categoriaNombre}</Badge>
                  <Badge bg={producto.estado === "NUEVO" ? "success" : "warning"} text={producto.estado === "NUEVO" ? "white" : "dark"}>
                    {producto.estado.replace("_", " ")}
                  </Badge>
                </div>

                <Card.Title className="h2 mb-2">{producto.titulo}</Card.Title>
                <h3 className="text-primary display-6 fw-bold mb-4">{producto.precio} $</h3>

                <h5 className="text-muted small uppercase fw-bold">Descripción</h5>
                <Card.Text className="text-secondary flex-grow-1">
                  {producto.descripcion || "Este producto no tiene descripción."}
                </Card.Text>

                <div className="border-top pt-3 mt-3 bg-white small text-muted">
                  <div className="d-flex justify-content-between mb-2">
                    <span>Ubicación de recogida:</span>
                    <strong className="text-dark">{producto.recogida?.descripcion || "No especificada"}</strong>
                  </div>
                  <div className="d-flex justify-content-between mb-2">
                    <span>Vendedor:</span>
                    <strong className="text-dark">{producto.vendedorId}</strong>
                  </div>
                  <div className="d-flex justify-content-between">
                    <span>Visualizaciones:</span>
                    <span>{producto.visualizaciones} visitas</span>
                  </div>
                </div>

                <Button variant="success" className="w-100 mt-4 size-lg">
                  Comprar
                </Button>
              </Card.Body>
            </div>
          </div>
        </Card>
      </Container>
    </div>
  );
}

export default Producto;