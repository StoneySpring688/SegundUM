// Componente: rejilla de tarjetas de compraventa
import { Container, Row, Col} from "react-bootstrap";
import TarjetaCompraventa from "./TarjetaCompraventa";

function ListaCompraventas({lista, titulo}){
    return(
        <Container className="py-4">
                <h2 className="mb-4">{titulo}</h2>
                <Row xs={1} md={2} lg={3} className="g-4">
                    {lista.map((comp) => (
                        <Col key={comp.id}>
                            <TarjetaCompraventa compraventa={comp}/>
                        </Col>
                    ))}
                </Row>
            </Container>
    );
}

export default ListaCompraventas