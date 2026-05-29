// Página: formulario para publicar un nuevo producto
import { useState, useEffect } from "react";
import { Container, Card, Form, Button, Row, Col, ListGroup, Alert } from "react-bootstrap";
import Header from "../components/Header";
import { dataService } from "../js/dataService";
import { apiProductos } from "../js/apiProductos";
import { useNavigate } from "react-router";


function PublicarProducto() {
    const [validado, setValidado] = useState(false);
    const [publicado, setPublicado] = useState(false);
    const navigate = useNavigate();

    const [categorias, setCategorias] = useState([]);
    const [busquedaCat, setBusquedaCat] = useState("");
    const [mostrarSugerencias, setMostrarSugerencias] = useState(false);

    const [producto, setProducto] = useState({
        titulo: "",
        descripcion: "",
        precio: "",
        estado: "NUEVO",
        envioDisponible: false,
        categoriaId: "",
        recogida: {
            descripcion: "",
            longitud: "",
            latitud: ""
        }
    });

    useEffect(() => {
        const initCategorias = async () => {
            try {
                 let cats = dataService.getCategorias();
            if (cats.length === 0) {
                cats = await apiProductos.getAllCategorias();
                dataService.setCategorias(cats);
            }
            setCategorias(cats);
            } catch (error) {
                navigate("/");
                console.log(error);
            }
           
        };
        initCategorias();

    }, []);

    const categoriasFiltradas = categorias.filter(cat =>
        cat.nombre.toLowerCase().includes(busquedaCat.toLowerCase())
    );

    const handleInputCatChange = (e) => {
        const valor = e.target.value;
        setBusquedaCat(valor);
        setMostrarSugerencias(true);

        if (valor === "") {
            setProducto(prev => ({ ...prev, categoriaId: "" }));
        }
    };

    const handleSeleccionarCategoria = (id, nombre) => {
        setBusquedaCat(nombre);
        setProducto(prev => ({ ...prev, categoriaId: id }));
        setMostrarSugerencias(false);
    };

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;

        setPublicado(false);

        if (type === "checkbox") {
            setProducto({ ...producto, [name]: checked });
        } else if (name.startsWith("recogida.")) {
            const subCampo = name.split(".")[1];
            setProducto({
                ...producto,
                recogida: {
                    ...producto.recogida,
                    [subCampo]: value
                }
            });
        } else {
            setProducto({ ...producto, [name]: value });
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const form = e.currentTarget;

        if (!producto.categoriaId) {
            e.stopPropagation();
            setValidado(true);
            return;
        }

        if (form.checkValidity() === false) {
            e.stopPropagation();
            setValidado(true);
            return;
        }

        const productoJSON = {
            ...producto,
            precio: parseFloat(producto.precio),
            recogida: {
                ...producto.recogida,
                longitud: parseFloat(producto.recogida.longitud),
                latitud: parseFloat(producto.recogida.latitud)
            }
        };

        console.log("JSON listo para enviar a la API:", productoJSON);
        apiProductos.publicarProducto(productoJSON);
        setPublicado(true);
    };

    return (
        <>
            <Header />

            {publicado &&(<Alert key="success" variant="success">
                    Producto publicado con exito
                </Alert>)}

            <Container className="mt-4 mb-5">
                <Card className="shadow-sm border-2 border-secondary bg-white p-4 mx-auto" style={{ maxWidth: "800px" }}>
                    <Card.Body>
                        <Card.Title className="h3 border-bottom pb-3 mb-4">
                            Publicar Nuevo Producto
                        </Card.Title>
                        
                        <Form noValidate validated={validado} onSubmit={handleSubmit}>
                            <h5 className="text-primary mb-3">Datos Básicos</h5>
                            <Row className="g-3 mb-4">
                                <Col xs={12} md={8}>
                                    <Form.Group controlId="titulo">
                                        <Form.Label className="small fw-bold text-muted">Titulo del Producto</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="titulo"
                                            placeholder="Ej. Bicicleta de montaña"
                                            value={producto.titulo}
                                            onChange={handleChange}
                                            required
                                        />
                                        <Form.Control.Feedback type="invalid">El titulo es obligatorio.</Form.Control.Feedback>
                                    </Form.Group>
                                </Col>

                                <Col xs={12} md={4}>
                                    <Form.Group controlId="precio">
                                        <Form.Label className="small fw-bold text-muted">Precio (€)</Form.Label>
                                        <Form.Control
                                            type="number"
                                            name="precio"
                                            placeholder="0.00"
                                            min="0.01"
                                            step="0.01"
                                            value={producto.precio}
                                            onChange={handleChange}
                                            required
                                        />
                                        <Form.Control.Feedback type="invalid">Introduce un precio válido (mín 0.01€).</Form.Control.Feedback>
                                    </Form.Group>
                                </Col>

                                
                                <Col xs={12} md={6} className="position-relative">
                                    <Form.Group controlId="categoriaId">
                                        <Form.Label className="small fw-bold text-muted">Categoria</Form.Label>
                                        <Form.Control
                                            type="text"
                                            placeholder="Escribe para buscar categoría..."
                                            value={busquedaCat}
                                            onChange={handleInputCatChange}
                                            onFocus={() => setMostrarSugerencias(true)}
                                            onBlur={() => setTimeout(() => setMostrarSugerencias(false), 200)}
                                            required={!producto.categoriaId}
                                        />
                                        
                                        {validado && !producto.categoriaId && (
                                            <div className="text-danger small mt-1">
                                                Debes seleccionar una categoria de la lista.
                                            </div>
                                        )}
                                    </Form.Group>

                                    {mostrarSugerencias && busquedaCat.length > 0 && (
                                        <ListGroup
                                            className="position-absolute w-100 shadow-sm overflow-auto"
                                            style={{ maxHeight: "200px", zIndex: 1000, top: "100%" }}
                                        >
                                            {categoriasFiltradas.slice(0, 10).map((cat) => (
                                                <ListGroup.Item
                                                    key={cat.id}
                                                    action
                                                    onClick={() => handleSeleccionarCategoria(cat.id, cat.nombre)}
                                                    className="small"
                                                >
                                                    {cat.nombre}
                                                </ListGroup.Item>
                                            ))}
                                            {categoriasFiltradas.length === 0 && (
                                                <ListGroup.Item className="text-muted small">No se encontraron categorias</ListGroup.Item>
                                            )}
                                        </ListGroup>
                                    )}
                                </Col>

                                <Col xs={12} md={6}>
                                    <Form.Group controlId="estado">
                                        <Form.Label className="small fw-bold text-muted">Estado del Producto</Form.Label>
                                        <Form.Select name="estado" value={producto.estado} onChange={handleChange} required>
                                            <option value="NUEVO">Nuevo</option>
                                            <option value="COMO_NUEVO">Como nuevo</option>
                                            <option value="BUEN_ESTADO">Buen estado</option>
                                            <option value="ACEPTABLE">Aceptable</option>
                                            <option value="PARA_PIEZAS_O_REPARAR">Para piezas o reparar</option>
                                        </Form.Select>
                                    </Form.Group>
                                </Col>

                                <Col xs={12}>
                                    <Form.Group controlId="descripcion">
                                        <Form.Label className="small fw-bold text-muted">Descripcion</Form.Label>
                                        <Form.Control
                                            as="textarea"
                                            rows={3}
                                            name="descripcion"
                                            placeholder="Describe el producto con detalle..."
                                            value={producto.descripcion}
                                            onChange={handleChange}
                                            required
                                        />
                                        <Form.Control.Feedback type="invalid">La descripción es obligatoria.</Form.Control.Feedback>
                                    </Form.Group>
                                </Col>
                            </Row>

                            <h5 className="text-primary mb-3 border-top pt-4">Envío y Recogida</h5>
                            <Row className="g-3 mb-4">
                                <Col xs={12} className="mb-2">
                                    <Form.Check
                                        type="switch"
                                        id="envioDisponible"
                                        name="envioDisponible"
                                        label="Envío disponible para este producto"
                                        checked={producto.envioDisponible}
                                        onChange={handleChange}
                                        className="fw-bold text-secondary"
                                    />
                                </Col>

                                <Col xs={12}>
                                    <Form.Group controlId="recogida.descripcion">
                                        <Form.Label className="small fw-bold text-muted">Lugar de Recogida (Ciudad, Barrio...)</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="recogida.descripcion"
                                            placeholder="Ej. Madrid, Centro"
                                            value={producto.recogida.descripcion}
                                            onChange={handleChange}
                                            required
                                        />
                                        <Form.Control.Feedback type="invalid">Debes indicar un lugar de recogida.</Form.Control.Feedback>
                                    </Form.Group>
                                </Col>

                                <Col xs={12} md={6}>
                                    <Form.Group controlId="recogida.latitud">
                                        <Form.Label className="small fw-bold text-muted">Latitud (-90 a 90)</Form.Label>
                                        <Form.Control
                                            type="number"
                                            name="recogida.latitud"
                                            placeholder="Ej. 40.4168"
                                            min="-90"
                                            max="90"
                                            step="any"
                                            value={producto.recogida.latitud}
                                            onChange={handleChange}
                                            required
                                        />
                                        <Form.Control.Feedback type="invalid">Introduce una latitud válida.</Form.Control.Feedback>
                                    </Form.Group>
                                </Col>

                                <Col xs={12} md={6}>
                                    <Form.Group controlId="recogida.longitud">
                                        <Form.Label className="small fw-bold text-muted">Longitud (-180 a 180)</Form.Label>
                                        <Form.Control
                                            type="number"
                                            name="recogida.longitud"
                                            placeholder="Ej. -3.7038"
                                            min="-180"
                                            max="180"
                                            step="any"
                                            value={producto.recogida.longitud}
                                            onChange={handleChange}
                                            required
                                        />
                                        <Form.Control.Feedback type="invalid">Introduce una longitud válida.</Form.Control.Feedback>
                                    </Form.Group>
                                </Col>
                            </Row>

                            <Row className="mt-5">
                                <Col className="text-end">
                                    <Button variant="success" type="submit" className="px-4 py-2 fw-bold" disabled={publicado}>
                                        <i className="bi bi-cloud-arrow-up me-2"></i>Publicar Producto
                                    </Button>
                                </Col>
                            </Row>
                        </Form>
                    </Card.Body>
                </Card>
            </Container>
        </>
    );
}

export default PublicarProducto;