import React from "react";
import { Navbar } from "react-bootstrap";
import Container from "react-bootstrap/Container";
import "./navbar.css";

function NavBar() {
    return (
        <Navbar className="nav-bar">
            <Container bsPrefix="container">
                <Navbar.Brand bsPrefix="nav-bar-main" href="/">
                    <div>Project SidStar</div>
                </Navbar.Brand>
            </Container>
        </Navbar>
    )
}

export default NavBar;