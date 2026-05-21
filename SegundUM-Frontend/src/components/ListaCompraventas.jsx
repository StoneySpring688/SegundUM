import { Container, Row, Col} from "react-bootstrap";

function ListaCompraventas({lista, titulo}){
    return(
        <Container className="py-4">
                <h2 className="mb-4">{titulo}</h2>
                <Row xs={1} md={2} lg={3} xl={4} className="g-4">
                    {lista.map((prod) => (
                        <Col key={prod.id}>
                            <p>{prod.id}</p>
                        </Col>
                    ))}
                </Row>
            </Container>
    );
}

export default ListaCompraventas