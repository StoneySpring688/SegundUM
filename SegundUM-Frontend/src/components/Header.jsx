import { useState } from 'react';
import { Container, Nav, Navbar, NavDropdown, Offcanvas, Image } from 'react-bootstrap';
import { useNavigate, Link, NavLink } from 'react-router';
import profilePic from '../assets/profilePlaceHolder.png'
import { authService } from '../js/authService';
import { dataService } from '../js/dataService';
import './Header.css'

function Header() {
    const [show, setShow] = useState(false);

    const navigate = useNavigate();
    
    const handleClose = () => setShow(false);
    const handleToggle = () => setShow((s) => !s);
    const handleLogout = async () => {
        await authService.logout();
        handleClose();
        navigate("/");
    }
 
    return (
        <Navbar bg="dark" data-bs-theme="dark" expand="lg" className="Header mb-3">
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
                    data-bs-theme="dark"
                >
                    <Offcanvas.Header closeButton>
                        <Offcanvas.Title id="offcanvasNavbarLabel-expand-lg">
                            Menu
                        </Offcanvas.Title>
                    </Offcanvas.Header>
                    <Offcanvas.Body>
                        <Nav className="justify-content-end flex-grow-1 pe-3">
                            <Nav.Link as={Link} to="/user" onClick={handleClose} className='d-sm-none d-auto'>Mi Perfil</Nav.Link>
                            <Nav.Link as={Link} to="/home/" onClick={handleClose}>Home</Nav.Link>
                            <Nav.Link as={Link} to="/buscar/" onClick={handleClose}>Buscar</Nav.Link>
                            <NavDropdown title="Compraventas" id='compraventas-dopdown'>
                                <NavDropdown.Item as={Link} to="/compras" onClick={handleClose}>Mis Compras</NavDropdown.Item>
                                <NavDropdown.Item as={Link} to="/ventas" onClick={handleClose}>Mis Ventas</NavDropdown.Item>
                                <NavDropdown.Item as={Link} to="/compraventasEntre" onClick={handleClose}>Compraventas entre</NavDropdown.Item>
                            </NavDropdown>
                            <NavDropdown title="Productos" id='productos-dopdown'>
                                <NavDropdown.Item as={Link} to="/publicar" onClick={handleClose}>Publicar Producto</NavDropdown.Item>
                                <NavDropdown.Item as={Link} to="/misProductos" onClick={handleClose}>Mis Productos</NavDropdown.Item>
                            </NavDropdown>
                            <Nav.Link onClick={handleLogout}>Logout</Nav.Link>
                            {/*TODO poner el resto de links aqui*/}
                        </Nav>
                    </Offcanvas.Body>
                </Navbar.Offcanvas>
                <NavLink as={Link} to='/user' className="p-0 ms-auto d-none d-sm-inline-block">
                    <img src={profilePic} alt="imagen de perfil" className="profile-img rounded-circle"/>
                </NavLink>
            </Container>
        </Navbar>
    );
}

export default Header