// Página: listado de todos los usuarios registrados (solo administradores)
import { useState, useEffect } from "react";
import { Container, Spinner } from "react-bootstrap";
import Header from "../components/Header";
import ListadoUsuarios from "../components/ListadoUsuarios";
import { apiUsuarios } from "../js/apiUsuarios";

function ListaUsuarios(){
    const [usuarios, setUsuarios] = useState([]);
    const [isAdmin, toggleAdmin] = useState(false);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() =>{
        const init = async () => {
            try {
                const usuario = await apiUsuarios.getById();
                //console.log(usuario);
                if(usuario.administrador) {
                    const nuevosUsuarios = await apiUsuarios.getUsuarios();
                    setUsuarios(nuevosUsuarios);
                    toggleAdmin(true);
                }
            } catch (error) {
                setError("No se pudo cargar la información del producto.");
                console.error("Error al inicializar mis productos: ", error);
            }finally {
                setCargando(false);
            }
        };
        init();
    },[]);

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

  if (!isAdmin) {
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

    return(
    <>
        <Header/>
        <ListadoUsuarios lista={usuarios} titulo={"Usuarios registrados"}/>
    </>
    );
}
export default ListaUsuarios