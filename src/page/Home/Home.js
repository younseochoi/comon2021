import React, { Component } from "react";
import { MainNavbar } from "../index.js";
import { Main } from "../index.js";

function Home() {
  return (
    <div className="Home">
      <div>
        <div>
          <MainNavbar />
        </div>

        <div>
          <Main />
        </div>
      </div>
    </div>
  );
}
export default Home;
