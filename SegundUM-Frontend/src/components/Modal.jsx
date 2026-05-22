// Componente: modal de confirmación reutilizable con dos botones
import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';

function ModalCustom({textoBoton, tituloModal, bodyModal, textoBotonSecundario, textoBotonPrincipal, onClickBotonPrincipal}) {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const handleAccept = () => {
    setShow(false);
    onClickBotonPrincipal();
  }

  return (
    <>
      <Button variant="primary" onClick={handleShow}>
        {textoBoton}
      </Button>

      <Modal
        show={show}
        onHide={handleClose}
        backdrop="static"
        keyboard={false}
      >
        <Modal.Header closeButton>
          <Modal.Title>{tituloModal}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {bodyModal}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            {textoBotonSecundario}
          </Button>
          <Button variant="primary" onClick={handleAccept}>{textoBotonPrincipal}</Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default ModalCustom;