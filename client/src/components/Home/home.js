import React from "react";
import { Container, Row, Dropdown, DropdownButton } from "react-bootstrap";
import { useState } from 'react';
import "./home.css";

function Home() {
  const [sidsWaypoints, setSIDSWaypoints] = useState("");
  const [starsWaypoints, setSTARSWaypoints] = useState("");

  const getWaypoints = async (type, airport) => {
    const url = "http://localhost:30009/" + type + "/" + airport;
    var headers = {}
    console.log(url);

    fetch(url, {
      method: "GET",
      headers: headers
    })
      .then(response => {
        console.log(response);
        return response.json();
      })
      .then(data => {
        console.log(JSON.stringify(data));
        if (type === "sids") {
          setSIDSWaypoints(JSON.stringify(data));
        } else if (type === "stars") {
          setSTARSWaypoints(JSON.stringify(data));
        }
      })
      .catch(function (error) {
        console.log(error);
      });
  }

  return (
    <Container className="App">
      <Row>
        <div>
          <DropdownButton id="dropdown-basic-button" title="Select airport for SIDS">
            <Dropdown.Item onClick={() => getWaypoints("sids", "WSSS")}>WSSS</Dropdown.Item>
            <Dropdown.Item onClick={() => getWaypoints("sids", "WSSL")}>WSSL</Dropdown.Item>
            <Dropdown.Item onClick={() => getWaypoints("sids", "WIDD")}>WIDD</Dropdown.Item>
            <Dropdown.Item onClick={() => getWaypoints("sids", "WIDN")}>WIDN</Dropdown.Item>
            <Dropdown.Item onClick={() => getWaypoints("sids", "WMKJ")}>WMKJ</Dropdown.Item>
            <Dropdown.Item onClick={() => getWaypoints("sids", "WSAP")}>WSAP</Dropdown.Item>
          </DropdownButton>
          <div className="sids">{sidsWaypoints && (<div>{sidsWaypoints}</div>)}</div>
        </div>
      </Row>
      <Row>
        <div>
          <DropdownButton id="dropdown-basic-button" title="Select airport for STARS">
            <Dropdown.Item onClick={() => getWaypoints("stars", "WSSS")}>WSSS</Dropdown.Item>
            <Dropdown.Item onClick={() => getWaypoints("stars", "WSSL")}>WSSL</Dropdown.Item>
            <Dropdown.Item onClick={() => getWaypoints("stars", "WIDD")}>WIDD</Dropdown.Item>
            <Dropdown.Item onClick={() => getWaypoints("stars", "WIDN")}>WIDN</Dropdown.Item>
            <Dropdown.Item onClick={() => getWaypoints("stars", "WMKJ")}>WMKJ</Dropdown.Item>
            <Dropdown.Item onClick={() => getWaypoints("stars", "WSAP")}>WSAP</Dropdown.Item>
          </DropdownButton>
          <div className="sids">{starsWaypoints && (<div>{starsWaypoints}</div>)}</div>
        </div>
      </Row>
    </Container>
  )
}

export default Home;