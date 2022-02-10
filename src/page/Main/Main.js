import React, { Component } from "react";
import { Link } from "react-router-dom";
import "./Main.css";

function Main() {
  return (
    <div className="Main">
      <div className="main_comment">
        <h2>comon</h2>
        <h3>당신의 코드를 마음껏 뽐내주세요!</h3>
      </div>

      <div className="go_btn">
        <Link to="./Write">
          <button>COME ON!</button>
        </Link>
      </div>
    </div>
  );
}
export default Main;
