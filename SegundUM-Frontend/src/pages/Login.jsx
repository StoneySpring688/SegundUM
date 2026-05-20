import React, { useState } from "react";
import { authService } from "../js/authService";
import { apiUsuarios } from "../js/apiUsuarios";
import { dataService } from "../js/dataService";
import { useNavigate } from "react-router";
import { Form, Button, Alert, Container, Row, Col, Card } from 'react-bootstrap';

function Login() {
    const [email, setEmail] = useState("");
    const [clave, setClave] = useState("");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {

            const data = await authService.login(email, clave);
            const usuario = await apiUsuarios.getById(data.id);
            dataService.setUsuario(usuario);
            console.log("usuario almacenado: ", dataService.getUsuario());

            navigate("/home");

        } catch (err) {
            setError("Credenciales incorrectas. Inténtalo de nuevo.");
        }finally{
            setLoading(false);
        }
    };

    const handleRegister = () => {
        navigate("/register")
    }

    return (
        <>
            <Container className="mt-5">
                <Row className="justify-content-center">
                    <Col md={8} lg={6}>
                        <Card className="shadow">
                            <Card.Body className="p-4">
                                <h2 className="text-center mb-4">Login</h2>

                                {error && <Alert variant="danger">{error}</Alert>}

                                <Form onSubmit={handleLogin}>
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

                                    <div className="d-grid gap-2 mt-4">
                                        <Button type="submit" variant="primary" size="lg" disabled={loading}>
                                            {loading ? 'Iniciando Sesión...' : 'Iniciar Sesión'}
                                        </Button>
                                        <Button variant="link" onClick={handleRegister}>
                                            ¿No tienes cuenta? Registrate
                                        </Button>
                                    </div>
                                </Form>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>

            
        </>

    );
}

export default Login;