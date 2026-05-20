import { useState } from "react";
import { useNavigate } from "react-router";
import { Form, Button, Alert, Container, Row, Col, Card } from 'react-bootstrap';
import { authService } from "../js/authService";

function Register() {
    const [email, setEmail] = useState("");
    const [nombre, setNombre] = useState("");
    const [apellidos, setApellidos] = useState("");
    const [clave, setClave] = useState("");
    const [fechaNacimiento, setFechaNacimiento] = useState("");
    const [telefono, setTelefono] = useState("");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleLogin = () => {
        navigate("/")
    }

    const handleRegister = async (e) => {
        e.preventDefault();
        setError(null);
        setLoading(true);
        
        const userData = {
            email,
            nombre,
            apellidos,
            clave,
            fechaNacimiento,
            telefono
        };
        
        try {
            await authService.register(email,
                                    nombre,
                                    apellidos,
                                    clave,
                                    fechaNacimiento,
                                    telefono);
            navigate("/");
        } catch (err) {
            console.error(err);
            setError(err.response?.data?.message || "Se produjo un error en el registro. Intentalo de nuevo más tarde.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <Container className="mt-5">
            <Row className="justify-content-center">
                <Col md={8} lg={6}>
                    <Card className="shadow">
                        <Card.Body className="p-4">
                            <h2 className="text-center mb-4">Registrar Usuario</h2>
                            
                            {error && <Alert variant="danger">{error}</Alert>}

                            <Form onSubmit={handleRegister}>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="formNombre">
                                            <Form.Label>Nombre</Form.Label>
                                            <Form.Control
                                                type="text"
                                                placeholder="Tu nombre"
                                                value={nombre}
                                                onChange={(e) => setNombre(e.target.value)}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="formApellidos">
                                            <Form.Label>Apellidos</Form.Label>
                                            <Form.Control
                                                type="text"
                                                placeholder="Tus apellidos"
                                                value={apellidos}
                                                onChange={(e) => setApellidos(e.target.value)}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                </Row>

                                <Form.Group className="mb-3" controlId="formEmail">
                                    <Form.Label>Correo Electrónico</Form.Label>
                                    <Form.Control
                                        type="email"
                                        placeholder="ejemplo@correo.com"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formPassword">
                                    <Form.Label>Contraseña</Form.Label>
                                    <Form.Control
                                        type="password"
                                        placeholder="Mínimo 6 caracteres"
                                        value={clave}
                                        onChange={(e) => setClave(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="formFechaNacimiento">
                                            <Form.Label>Fecha de Nacimiento</Form.Label>
                                            <Form.Control
                                                type="date"
                                                value={fechaNacimiento}
                                                onChange={(e) => setFechaNacimiento(e.target.value)}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3" controlId="formTelefono">
                                            <Form.Label>Teléfono</Form.Label>
                                            <Form.Control
                                                type="tel"
                                                placeholder="600 000 000"
                                                value={telefono}
                                                onChange={(e) => setTelefono(e.target.value)}
                                                required
                                            />
                                        </Form.Group>
                                    </Col>
                                </Row>

                                <div className="d-grid gap-2 mt-4">
                                    <Button type="submit" variant="primary" size="lg" disabled={loading}>
                                        {loading ? 'Registrando...' : 'Crear Cuenta'}
                                    </Button>
                                    <Button variant="link" onClick={handleLogin}>
                                        ¿Ya tienes cuenta? Inicia sesión
                                    </Button>
                                </div>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}

export default Register;