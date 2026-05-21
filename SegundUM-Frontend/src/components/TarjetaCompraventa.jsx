import { Card, Row, Col } from "react-bootstrap";

function TarjetaCompraventa({ compraventa }) {
  if (!compraventa) return null;

  const fechaFormateada = new Date(compraventa.fechaYHora).toLocaleString('es-ES', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });

  return (
    <Card border="dark" className="shadow-sm border-1 mb-3">
      <Card.Body className="p-4">
        {/* Fila principal: Título y Precio */}
        <Row className="align-items-center mb-3">
          <Col sm={12} md={8}>
            <Card.Title className="mb-0 text-truncate fs-4 text-md-start text-center">
              {compraventa.titulo}
            </Card.Title>
          </Col>
          <Col sm={12} md={4} className="text-md-end text-center">
            <span className="h4 text-primary fw-bold mb-0">
              {compraventa.precio} €
            </span>
          </Col>
        </Row>

        <hr className="text-muted opacity-25" />

        {/* Fila secundaria: Detalles (Vendedor, Comprador, Fecha) */}
        <Row className="text-muted small g-2 align-items-center">
          <Col xs={12} className="text-center">
            <span className="fw-bold me-1">Vendedor:</span> 
            {compraventa.nombreVendedor}
          </Col>
          
          <Col xs={12} className="text-center">
            <span className="fw-bold me-1">Comprador:</span> 
            {compraventa.nombreComprador}
          </Col>

          <Col xs={12} className="text-center">
            <i className="bi bi-calendar3 me-2"></i>
            {fechaFormateada}
          </Col>
        </Row>
      </Card.Body>
    </Card>
  );
}

export default TarjetaCompraventa