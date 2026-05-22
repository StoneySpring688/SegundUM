// Componente: rejilla de tarjetas de usuario
import { Container, Row, Col} from "react-bootstrap";
import TarjetaUsuario from "./TarjetaUsuario";

function ListadoUsuarios({lista, titulo}){
    return(
        <Container className="py-4">
                <h2 className="mb-4">{titulo}</h2>
                <Row xs={1} md={2} lg={3} className="g-4">
                    {lista.map((user) => (
                        <Col key={user.id}>
                            <TarjetaUsuario usuario={user}/>
                        </Col>
                    ))}
                </Row>
            </Container>
    );
}

export default ListadoUsuarios