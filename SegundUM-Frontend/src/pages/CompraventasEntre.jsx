import { useState, useEffect } from "react";
import { Form, Button, Card, Row, Col, Alert, Container, Spinner } from "react-bootstrap";
import Header from "../components/Header";
import ListaCompraventas from "../components/ListaCompraventas";
import Paginador from "../components/Paginador";
import { apiUsuarios } from "../js/apiUsuarios";
import { apiCompraventas } from "../js/apiCompraventas";

function CompraventasEntre() {
    const [administrador, setAdministrador] = useState(false);
    const [cargando, setCargando] = useState(true);
    const [paginas, setPaginas] = useState(null);
    const [compraventas, setCompraventas] = useState([]);

    const [validado, setValidado] = useState(false);
    const [datosFormulario, setDatosFormulario] = useState({
        idComprador: "",
        idVendedor: ""
    });

    useEffect(() => {
        const comprobarAcceso = async () => {
            try {
                const usuarioLoggeado = await apiUsuarios.getById();
                setAdministrador(usuarioLoggeado.administrador);
            } catch (error) {
                console.error("Error al obtener el usuario:", error);
                setAdministrador(false);
            } finally {
                setCargando(false); 
            }
        };

        comprobarAcceso();
    }, []);

    const cambiarPagina = async (nuevaPagina) => {
        console.log("El usuario quiere ir a la página:", nuevaPagina);
        try {
            const { paginas: nuevaInfoPaginas, compraventas: nuevasCompraventas } = await apiCompraventas.getCompraventas(datosFormulario.idComprador, datosFormulario.idVendedor, nuevaPagina);

            console.log("nuevas compraventas: ", nuevasCompraventas);
            console.log("nuevas paginas: ", nuevaInfoPaginas);
            setCompraventas(nuevasCompraventas);
            setPaginas(nuevaInfoPaginas);

            window.scrollTo({ top: 0, behavior: 'smooth' });
        } catch (error) {
            console.error("Error al cambiar de página: ", error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setDatosFormulario({
            ...datosFormulario,
            [name]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const form = e.currentTarget;

        if (form.checkValidity() === false) {
            e.stopPropagation();
            setValidado(true);
            return;
        }

        console.log("Valores del formulario:", datosFormulario);
        const { paginas: nuevaInfoPaginas, compraventas: respuestaCompraventas } = await apiCompraventas.getCompraventas(datosFormulario.idComprador, datosFormulario.idVendedor, 0);
        setCompraventas(respuestaCompraventas);
        setPaginas(nuevaInfoPaginas);
        
        setValidado(false);
    };

    if (cargando) {
        return (
            <>
                <Header />
                <Container className="text-center mt-5">
                    <Spinner animation="border" variant="primary" />
                    <p className="mt-2 text-muted">Comprobando permisos...</p>
                </Container>
            </>
        );
    }

    if (!administrador) {
        return (
            <>
                <Header />
                <Container className="mt-4" style={{ maxWidth: "600px" }}>
                    <Alert variant="danger" className="shadow-sm">
                        <Alert.Heading><i className="bi bi-exclamation-triangle-fill me-2"></i>Acceso Restringido</Alert.Heading>
                        <p className="mb-0">
                            No tienes los permisos necesarios. Solo los administradores pueden usar esta funcionalidad.
                        </p>
                    </Alert>
                </Container>
            </>
        );
    }

    return (
        <>
            <Header />
            <Container className="mt-4">
                <Card className="shadow-sm border-0 border-md border-2 border-secondary bg-white p-4 mx-auto" style={{ maxWidth: "600px" }}>
                    <Card.Body>
                        <Card.Title className="h3 border-bottom pb-3 mb-4">
                            Compraventas entre Usuarios
                        </Card.Title>

                        <Form noValidate validated={validado} onSubmit={handleSubmit}>
                            <Row className="g-3 mb-4">
                                <Col xs={12}>
                                    <Form.Group controlId="idComprador">
                                        <Form.Label className="small fw-bold text-muted">ID del Comprador</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="idComprador"
                                            placeholder="Ej. usuario-123"
                                            value={datosFormulario.idComprador}
                                            onChange={handleChange}
                                            required
                                        />
                                        <Form.Control.Feedback type="invalid">
                                            Por favor, introduce el ID del comprador.
                                        </Form.Control.Feedback>
                                    </Form.Group>
                                </Col>

                                <Col xs={12}>
                                    <Form.Group controlId="idVendedor">
                                        <Form.Label className="small fw-bold text-muted">ID del Vendedor</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="idVendedor"
                                            placeholder="Ej. usuario-345"
                                            value={datosFormulario.idVendedor}
                                            onChange={handleChange}
                                            required
                                        />
                                        <Form.Control.Feedback type="invalid">
                                            Por favor, introduce el ID del vendedor.
                                        </Form.Control.Feedback>
                                    </Form.Group>
                                </Col>
                            </Row>

                            <Row>
                                <Col className="text-end">
                                    <Button variant="primary" type="submit" className="px-4">
                                        Generar Compraventa
                                    </Button>
                                </Col>
                            </Row>
                        </Form>
                    </Card.Body>
                </Card>
            </Container>
            <ListaCompraventas titulo={"Compraventas"} lista={compraventas}/>
            <Paginador paginacion={paginas} onCambioPagina={cambiarPagina}/>
        </>
    );
}

export default CompraventasEntre;