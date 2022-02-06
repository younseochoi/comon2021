import React, { Component, useEffect, useState } from "react";
import { Route, Routes } from "react-router-dom";
import { Head } from "./page/index.js";
import { Main } from "./page/index.js";
import { Home } from "./page/index.js";
import { Write } from "./page/index.js";
import { Lists } from "./page/index.js";
import { Register } from "./page/index.js";
import { Login } from "./page/index.js";

function App() {
  const [isLogin, setIsLogin] = useState(false);

  useEffect(() => {
    if (sessionStorage.getItem("ID") === null) {
      console.log("isLogin ?? :: ", isLogin);
    } else {
      setIsLogin(true);
      console.log("isLogin ?? :: ", isLogin);
    }
  });
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/Write" element={<Write />} />
      <Route path="/Lists" element={<Lists />} />
      <Route path="/Register" element={<Register />} />
      <Route path="/Login" element={<Login />} />
    </Routes>
  );
}
export default App;
