// Componente: tarjeta con los datos de un usuario y sus estadísticas de actividad
import { Card, Badge, Col, Row } from "react-bootstrap";

function TarjetaUsuario({ usuario }) {
    if (!usuario) return null;

    return (
        <Card border="secondary" className="shadow-sm border-2 bg-white h-100">
            <Card.Body className="d-flex flex-column text-center text-md-start">
                
                <Row className="g-2 mb-3">
                    <Col xs={12}>
                        <Card.Subtitle className="text-muted small mb-0">ID</Card.Subtitle>
                        <Card.Text className="mb-1" title={usuario.id}>
                            {usuario.id}
                        </Card.Text>
                    </Col>

                    <Col xs={12}>
                        <Card.Subtitle className="text-muted small mb-0">Email</Card.Subtitle>
                        <Card.Text className="mb-1 text-truncate" title={usuario.email}>
                            {usuario.email || "No consta"}
                        </Card.Text>
                    </Col>

                    <Col xs={12} md={6}>
                        <Card.Subtitle className="text-muted small mb-0">Nombre</Card.Subtitle>
                        <Card.Text className="mb-1">{usuario.nombre || "No consta"}</Card.Text>
                    </Col>

                    <Col xs={12} md={6}>
                        <Card.Subtitle className="text-muted small mb-0">Apellidos</Card.Subtitle>
                        <Card.Text className="mb-1">{usuario.apellidos || "No constan"}</Card.Text>
                    </Col>

                    <Col xs={12} md={6}>
                        <Card.Subtitle className="text-muted small mb-0">Nacimiento</Card.Subtitle>
                        <Card.Text className="mb-1">{usuario.fechaNacimiento || "No consta"}</Card.Text>
                    </Col>

                    <Col xs={12} md={6}>
                        <Card.Subtitle className="text-muted small mb-0">Teléfono</Card.Subtitle>
                        <Card.Text className="mb-1">{usuario.telefono || "No consta"}</Card.Text>
                    </Col>
                </Row>

                <hr className="my-2 text-muted" />

                <Row className="text-center g-2 mb-auto mt-2">
                    <Col xs={6}>
                        <Card.Subtitle className="text-muted small mb-0">Compras</Card.Subtitle>
                        <Card.Text className="h5 fw-bold">{usuario.comprasRealizadas ?? 0}</Card.Text>
                    </Col>
                    <Col xs={6}>
                        <Card.Subtitle className="text-muted small mb-0">Ventas</Card.Subtitle>
                        <Card.Text className="h5 fw-bold">{usuario.ventasRealizadas ?? 0}</Card.Text>
                    </Col>
                </Row>

                {usuario.administrador && (
                    <div className="text-center mt-3 pt-3 border-top">
                        <Badge bg="info" className="px-3 py-2 fs-6 shadow-sm" text="dark">
                            Administrador
                        </Badge>
                    </div>
                )}
            </Card.Body>
        </Card>
    );
}

export default TarjetaUsuario;