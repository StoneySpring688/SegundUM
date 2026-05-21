import { useState, useEffect } from "react";
import { Card, Form, Button, Row, Col, Badge } from "react-bootstrap";
import Header from "../components/Header";
import { apiUsuarios } from "../js/apiUsuarios";

function PerfilUsuario() {
    const [editando, setEditando] = useState(false);
    const [clave, setClave] = useState("");
    const [administrador, setAdministrador] = useState(false);
    const [validado, setValidado] = useState(false);

    const [datosUsuario, setDatosUsuario] = useState({
        nombre: "",
        apellidos: "",
        email: "",
        telefono: "",
        fechaNacimiento: ""
    });

    const [infoFija, setInfoFija] = useState({
        administrador: false,
        ventasRealizadas: 0,
        comprasRealizadas: 0
    });

    const cargarDatosDesdeAPI = async () => {
        const usuario = await apiUsuarios.getById();

        setDatosUsuario({
            nombre: usuario.nombre || "",
            apellidos: usuario.apellidos || "",
            email: usuario.email || "",
            telefono: usuario.telefono || "",
            fechaNacimiento: usuario.fechaNacimiento || ""
        });

        setInfoFija({
            ventasRealizadas: usuario.ventasRealizadas,
            comprasRealizadas: usuario.comprasRealizadas
        });

        setAdministrador(usuario.administrador);
    };

    useEffect(() => {
        cargarDatosDesdeAPI();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setDatosUsuario({
            ...datosUsuario,
            [name]: value
        });
    };

    const handleChangeClave = (e) => {
        const value = e.target.value;
        setClave(value);
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        const form = e.currentTarget;

        if (form.checkValidity() === false) {
            e.stopPropagation();
            setValidado(true); // No quitar, o no se muestra el feedback :P
            return;
        }

        const objetoParaAPI = {
            ...infoFija,
            ...datosUsuario,
            clave
        };

        await apiUsuarios.modificarUsuario(objetoParaAPI);

        setEditando(false);
        setValidado(false);
        setClave("");
    };

    const handleCancelar = () => {
        setEditando(false);
        setValidado(false);
        setClave("");
        cargarDatosDesdeAPI();
    };

    return (
        <>
            <Header />

            <Card border="secondary" className="shadow-sm border-2 bg-white p-4 mx-auto" style={{ maxWidth: "800px" }}>

                <Card.Body>
                    {/* Cabecera del Perfil */}
                    <Row className="align-items-center border-bottom pb-3 mb-4">
                        <Col xs={12} sm={6}>
                            <Card.Title className="h3 mb-1">Perfil de Usuario</Card.Title>
                        </Col>
                        <Col xs={12} sm={6} className="text-end mb-1">
                            {administrador && (
                                <Badge bg="info" className="px-3 py-2 fs-6" text="dark">Administrador</Badge>
                            )}
                        </Col>
                    </Row>

                    {/* Formulario de Datos */}
                    <Form noValidate validated={validado} onSubmit={handleFormSubmit}>
                        
                        <Row className="g-3 mb-4">
                            <Col xs={12} md={6}>
                                <Form.Group controlId="perfilNombre">
                                    <Form.Label className="small fw-bold text-muted">Nombre</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="nombre"
                                        value={datosUsuario.nombre}
                                        onChange={handleChange}
                                        disabled={!editando}
                                        required
                                    />
                                    <Form.Control.Feedback type="invalid">Por favor, introduce tu nombre.</Form.Control.Feedback>
                                </Form.Group>
                            </Col>

                            <Col xs={12} md={6}>
                                <Form.Group controlId="perfilApellidos">
                                    <Form.Label className="small fw-bold text-muted">Apellidos</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="apellidos"
                                        value={datosUsuario.apellidos}
                                        onChange={handleChange}
                                        disabled={!editando}
                                        required
                                    />
                                    <Form.Control.Feedback type="invalid">Por favor, introduce tus apellidos.</Form.Control.Feedback>
                                </Form.Group>
                            </Col>

                            <Col xs={12} md={6}>
                                <Form.Group controlId="perfilEmail">
                                    <Form.Label className="small fw-bold text-muted">Correo Electrónico</Form.Label>
                                    <Form.Control
                                        type="email"
                                        name="email"
                                        value={datosUsuario.email}
                                        onChange={handleChange}
                                        disabled={!editando}
                                        required
                                    />
                                </Form.Group>
                                <Form.Control.Feedback type="invalid">Introduce un correo electrónico valido.</Form.Control.Feedback>
                            </Col>

                            <Col xs={12} md={6}>
                                <Form.Group controlId="perfilTelefono">
                                    <Form.Label className="small fw-bold text-muted">Teléfono</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="telefono"
                                        placeholder="No especificado"
                                        value={datosUsuario.telefono}
                                        onChange={handleChange}
                                        disabled={!editando}
                                        required
                                    />
                                    <Form.Control.Feedback type="invalid">El número de telefono es obligatorio.</Form.Control.Feedback>
                                </Form.Group>
                            </Col>

                            <Col xs={12} md={6}>
                                <Form.Group controlId="perfilFechaNacimiento">
                                    <Form.Label className="small fw-bold text-muted">Fecha de Nacimiento</Form.Label>
                                    <Form.Control
                                        type="date"
                                        name="fechaNacimiento"
                                        value={datosUsuario.fechaNacimiento}
                                        onChange={handleChange}
                                        disabled={!editando}
                                        required
                                    />
                                    <Form.Control.Feedback type="invalid">Selecciona tu fecha de nacimiento.</Form.Control.Feedback>
                                </Form.Group>
                            </Col>
                        </Row>
                        {/* Formulario para cambiar la contraseña*/}
                        {editando && (<Row>
                            <Col xs={12} className="text-center">
                                <Form.Group controlId="perfilClave">
                                    <Form.Label className="small fw-bold text-muted">Contraseña</Form.Label>
                                    <Form.Control
                                        type="password"
                                        name="clave"
                                        value={clave}
                                        onChange={handleChangeClave}
                                        disabled={!editando}
                                        required
                                    />
                                    <Form.Control.Feedback type="invalid">Debes introducir una contraseña para guardar los cambios.</Form.Control.Feedback>
                                </Form.Group>
                            </Col>
                        </Row>)}

                        {/* Sección Informativa / Estadísticas (Siempre Deshabilitada) */}
                        <Row className="g-3 bg-light p-3 rounded mb-4">
                            <Col xs={6} className="text-center border-end">
                                <div className="small text-muted fw-bold">Compras Realizadas</div>
                                <div className="h3 text-dark mb-0">{infoFija.comprasRealizadas}</div>
                            </Col>
                            <Col xs={6} className="text-center">
                                <div className="small text-muted fw-bold">Ventas Realizadas</div>
                                <div className="h3 text-dark mb-0">{infoFija.ventasRealizadas}</div>
                            </Col>
                        </Row>

                        {/* Botones de Acción */}
                        <Row>
                            <Col className="text-end">
                                {editando ? (
                                    <>
                                        <Button
                                            variant="outline-secondary"
                                            className="me-2 px-4"
                                            onClick={handleCancelar}
                                            type="button"
                                        >
                                            Cancelar
                                        </Button>

                                        <Button
                                            variant="success"
                                            className="px-4"
                                            type="submit"
                                        >
                                            Guardar Cambios
                                        </Button>
                                    </>
                                ) : (
                                    <Button
                                        variant="primary"
                                        className="px-4"
                                        type="button"
                                        onClick={() => setEditando(true)}
                                    >
                                        Editar Perfil
                                    </Button>
                                )}
                            </Col>
                        </Row>

                    </Form>
                </Card.Body>
                
            </Card>

        </>

    );
}

export default PerfilUsuario;