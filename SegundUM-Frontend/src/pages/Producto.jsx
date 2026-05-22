import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router";
import { Container, Col, Row, Card, Button, Badge, Spinner, Alert } from "react-bootstrap";
import Header from "../components/Header";
import ModalCustom from "../components/Modal";
import { dataService } from "../js/dataService";
import { apiProductos } from "../js/apiProductos";
import { apiCompraventas } from "../js/apiCompraventas";

function Producto() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [producto, setProducto] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [comprado, setComprado] = useState(false);
  const [error, setError] = useState(null);
  const [succes, setSucces] = useState(false);
  const [mensaje, setMensaje] = useState("");


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

  const hacerCompraventa = async () => {
    try {
      const { succes:exito, mensaje:msg } = await apiCompraventas.hacerCompraventas(id);

      console.log(exito);
      console.log(msg);
      
      setComprado(true);
      setSucces(exito); 
      setMensaje(msg);
      
    } catch (error) {
      setComprado(true);
      setSucces(false);
      setMensaje("Error inesperado al contactar con el servidor. Intentalo de nuevo más tarde.");
    }
  }

  if (cargando) {
    return (
      <div className="bg-light" style={{ minHeight: '100vh' }}>
        <Header />
        <Container className="text-center py-5">
          <Spinner animation="border" variant="primary" />
          <p className="mt-2 text-muted">Cargando...</p>
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

      {comprado && succes &&(<Alert key="success" variant="success">
          {mensaje}
        </Alert>)}

        {comprado && !succes &&(<Alert key="danger" variant="danger">
          {mensaje}
        </Alert>)}

      <Container className="py-5">

        <Row className="mb-3">
          <Col xs={12} md={6}>
            <Button variant="outline-secondary" className="mb-4 px-3 px-md-4 py-2 fs-6 fs-md-5" onClick={() => navigate(-1)}>
              {"<- Volver atrás"}
            </Button>
          </Col>
          <Col xs={12} md={6} className="text-md-end">
            {producto.envioDisponible && (
              <Badge pill bg="info" text="dark" className="p-2 p-md-3 fs-6 fs-md-5">Envío disponible</Badge>
            )}
          </Col>
        </Row>

        <Card className="shadow-sm border-0 overflow-hidden">
          <Row className="g-0">
            <Col md={6}>
              <Card.Img
                src={`https://picsum.photos/seed/${producto.id}/600/400`}
                alt={producto.titulo}
                className="h-100 object-fit-cover"
              />
            </Col>
            <Col md={6}>
              <Card.Body className="p-4">

                <Row className="mb-3">
                  <Col>
                    <Badge bg="primary">{producto.categoriaNombre}</Badge>
                  </Col>
                  <Col className="text-end">
                    <Badge bg={producto.estado === "NUEVO" ? "success" : "warning"} text={producto.estado === "NUEVO" ? "white" : "dark"}>
                      {producto.estado.replace("_", " ")}
                    </Badge>
                  </Col>
                </Row>

                <Card.Title className="h2 mb-2">{producto.titulo}</Card.Title>
                <h3 className="text-primary display-6 fw-bold mb-4">{producto.precio} $</h3>

                <h5 className="text-muted small uppercase fw-bold">Descripción</h5>
                <Card.Text className="text-secondary mb-4">
                  {producto.descripcion || "Este producto no tiene descripción."}
                </Card.Text>

                <div className="border-top pt-3 bg-white small text-muted">
                  <Row className="mb-2">
                    <Col xs={12} sm={6}>Ubicación de recogida:</Col>
                    <Col xs={12} sm={6} className="text-end fw-bold text-dark">
                      {producto.recogida?.descripcion || "No especificada"}
                    </Col>
                  </Row>

                  <Row className="mb-2">
                    <Col xs={12} sm={6}>Vendedor:</Col>
                    <Col xs={12} sm={6} className="text-end fw-bold text-dark">
                      {producto.vendedorId}
                    </Col>
                  </Row>

                  <Row>
                    <Col xs={12} sm={6}>Visualizaciones:</Col>
                    <Col xs={12} sm={6} className="text-end">
                      {producto.visualizaciones} visitas
                    </Col>
                  </Row>
                </div>

                <ModalCustom
                  textoBoton="Comprar" 
                  tituloModal="Confirmación de Compra" 
                  bodyModal="Quieres comprar este producto" 
                  textoBotonSecundario="Cancelar" 
                  textoBotonPrincipal="Comprar" 
                  onClickBotonPrincipal={hacerCompraventa}
                />
              </Card.Body>
            </Col>
          </Row>
        </Card>

      </Container>

    </div>
  );
}

export default Producto;