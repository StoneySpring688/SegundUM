import { useState, useEffect } from "react";
import { Form, Button, Row, Col, Card, ListGroup } from "react-bootstrap";
import { dataService } from "../js/dataService";
import { apiProductos } from "../js/apiProductos";

function FormularioFiltros({ onResultadoBusqueda }) {
  const [categorias, setCategorias] = useState([]);
  const [busquedaCat, setBusquedaCat] = useState("");
  const [mostrarSugerencias, setMostrarSugerencias] = useState(false);

  const [filtros, setFiltros] = useState({
    texto: "",
    categoriaId: "",
    estadoMinimo: "",
    precioMaximo: ""
  });

  const categoriasFiltradas = categorias.filter(cat =>
    cat.nombre.toLowerCase().includes(busquedaCat.toLowerCase())
  );

  useEffect( () => {
    const init = async () => {
        let cats = dataService.getCategorias();
        if(cats.length === 0){
            cats = await apiProductos.getAllCategorias();
            dataService.setCategorias(cats);
            console.log("Categorias obtenidas: ", dataService.getCategorias());
        }
        setCategorias(cats);
    };

    init();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFiltros({
      ...filtros,
      [name]: value
    });
  };

  const handleSeleccionarCategoria = (id, nombre) => {
    setBusquedaCat(nombre);
    setFiltros({ ...filtros, categoriaId: id });
    setMostrarSugerencias(false);
  };

  const handleInputCatChange = (e) => {
    const valor = e.target.value;
    setBusquedaCat(valor);
    setMostrarSugerencias(true);
    
    if (valor === "") {
      setFiltros({ ...filtros, categoriaId: "" });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log("filtros de busqueda: ", filtros);
    try {
        const {paginas, productos: productosEncontrados} = await apiProductos.buscarProductos(filtros);
        console.log("Productos encontrados: ", productosEncontrados);
        console.log("paginas encontradas para la busqueda: ", paginas);
        onResultadoBusqueda(productosEncontrados, paginas);
    } catch (error) {
        console.error("Error al buscar productos:", error);
    }
    
  };

  return (
    <Card className="shadow-sm border-0 mb-4 p-4">
      <Form onSubmit={handleSubmit}>
        <Row className="g-3">
          
          {/* Campo: Texto */}
          <Col xs={12} md={6} lg={3}>
            <Form.Group controlId="filtroTexto">
              <Form.Label className="small fw-bold text-muted">¿Qué buscas?</Form.Label>
              <Form.Control 
                type="text" 
                name="texto" 
                placeholder="Ej. Bicicleta, consola..." 
                value={filtros.texto}
                onChange={handleChange}
              />
            </Form.Group>
          </Col>

          {/* Campo: Categoría (Desplegable) */}
          <Col xs={12} md={6} lg={3} className="position-relative">
            <Form.Group controlId="filtroCategoriaBusqueda">
              <Form.Label className="small fw-bold text-muted">Categoría</Form.Label>
              <Form.Control 
                type="text"
                placeholder="Escribe para buscar categoría..."
                value={busquedaCat}
                onChange={handleInputCatChange}
                onFocus={() => setMostrarSugerencias(true)}
                onBlur={() => setTimeout(() => setMostrarSugerencias(false), 200)} // sin esto un click fuera y rápidamente marcar algo hace que no se registre
              />
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
                  <ListGroup.Item className="text-muted small">No se encontraron categorías</ListGroup.Item>
                )}
              </ListGroup>
            )}
          </Col>

          {/* Campo: Estado Mínimo (Desplegable) */}
          <Col xs={12} md={6} lg={3}>
            <Form.Group controlId="filtroEstado">
              <Form.Label className="small fw-bold text-muted">Estado mínimo</Form.Label>
              <Form.Select 
                name="estadoMinimo" 
                value={filtros.estadoMinimo} 
                onChange={handleChange}
              >
                <option value="">Cualquiera</option>
                <option value="NUEVO">Nuevo</option>
                <option value="COMO_NUEVO">Como nuevo</option>
                <option value="BUEN_ESTADO">Buen estado</option>
                <option value="ACEPTABLE">Aceptable</option>
                <option value="PARA_PIEZAS_O_REPARAR">Para piezas o Reparar</option>
              </Form.Select>
            </Form.Group>
          </Col>

          {/* Campo: Precio Máximo */}
          <Col xs={12} md={6} lg={3}>
            <Form.Group controlId="filtroPrecio">
              <Form.Label className="small fw-bold text-muted">Precio máx (€)</Form.Label>
              <Form.Control 
                type="number" 
                name="precioMaximo" 
                placeholder="Ej. 50" 
                min="0"
                value={filtros.precioMaximo}
                onChange={handleChange}
              />
            </Form.Group>
          </Col>

        </Row>

        {/* Botón de envío */}
        <Row className="mt-4">
          <Col className="text-end">
            <Button variant="primary" type="submit" className="px-4 py-2 fw-bold">
              <i className="bi bi-search me-2"></i> Aplicar Filtros
            </Button>
          </Col>
        </Row>

      </Form>
    </Card>
  );
}

export default FormularioFiltros;