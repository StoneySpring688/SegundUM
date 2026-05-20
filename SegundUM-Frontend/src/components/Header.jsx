import { useState } from 'react';
import { Container, Nav, Navbar, NavDropdown, Offcanvas, Image } from 'react-bootstrap';
import { Link, NavLink } from 'react-router';
import profilePic from '../assets/profilePlaceHolder.png'
import './Header.css'

function Header() {
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleToggle = () => setShow((s) => !s);

    return (
        <Navbar expand="lg" className="Header mb-3">
            <Container fluid>
                <Navbar.Toggle aria-controls="offcanvasNavbar-expand-lg" onClick={handleToggle} />
                <Navbar.Brand as={Link} to="/home/">
                    <h1 className="m-0 d-inline-block">SegundUM</h1>
                </Navbar.Brand>
                <Navbar.Offcanvas
                    show={show}
                    onHide={handleClose}
                    id="offcanvasNavbar-expand-lg"
                    aria-labelledby="offcanvasNavbarLabel-expand-lg"
                    placement="start"
                >
                    <Offcanvas.Header closeButton>
                        <Offcanvas.Title id="offcanvasNavbarLabel-expand-lg">
                            Menu
                        </Offcanvas.Title>
                    </Offcanvas.Header>
                    <Offcanvas.Body>
                        <Nav className="justify-content-end flex-grow-1 pe-3">
                            <Nav.Link as={Link} to="/profile" onClick={handleClose} className='d-sm-none d-auto'>Mi Perfil</Nav.Link>
                            <Nav.Link as={Link} to="/home/" onClick={handleClose}>Home</Nav.Link>
                            {/*El resto de links*/}
                        </Nav>
                    </Offcanvas.Body>
                </Navbar.Offcanvas>
                <NavLink as={Link} to='/profile' className="p-0 ms-auto d-none d-sm-inline-block">
                    <img src={profilePic} alt="imagen de perfil" className="profile-img rounded-circle"/>
                </NavLink>
            </Container>
        </Navbar>
    );
}

export default Header