import React, { Component } from "react";
import { Link } from "react-router-dom";
import { TiThMenu } from "react-icons/ti";
import "./Navbar.css";

function Navbar() {
  const style = {
    fontSize: "60px",
    padding: "0px 0px 0px 50px",
  };
  return (
    <nav className="navbar">
      <TiThMenu classname="icon" style={style} />
      {/* <text className="navbar_name">Comon</text>
      <text className="navbar_comment">당신의 코드를 마음껏 뽐내보세요!</text>
      <Link to="/Login">
        <button className="navbar_login">login</button>
      </Link> */}
    </nav>
  );
}

export default Navbar;
