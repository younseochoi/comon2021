import React, { Component } from "react";
import { Link } from "react-router-dom";
import { TiThMenu } from "react-icons/ti";
import "./MainNavbar.css";
import { useNavigate } from "react-router-dom";

function MainNavbar() {
  const style = {
    fontSize: "60px",
    padding: "0px 0px 0px 50px",
  };
  const navigate = useNavigate();
  const onLogoutHandler = () => {
    sessionStorage.removeItem("id");
    navigate("/Login");
  };
  return (
    <nav className="mainNavbar">
      <TiThMenu classname="icon" style={style} />
      <Link to="/Login">
        <button className="mainNavbar_login">Login</button>
      </Link>
      <button
        type="button"
        onClick={onLogoutHandler}
        className="mainNavbar_logout"
      >
        Logout
      </button>
    </nav>
  );
}

export default MainNavbar;
