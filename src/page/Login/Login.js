import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "./Login.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Navbar } from "../index.js";

function Login() {
  const navigate = useNavigate();

  const [id, setId] = useState("");
  const [pwd, setPwd] = useState("");

  const onIdHandler = (event) => {
    setId(event.currentTarget.value);
  };

  const onPwdHandler = (event) => {
    setPwd(event.currentTarget.value);
  };

  const onSubmit = () => {
    console.log("ID: ", id);
    console.log("PWD: ", pwd);
    axios({
      method: "POST",
      url: "https://comon2022.herokuapp.com/boards",
      data: {
        id: id,
        pwd: pwd,
      },
    })
      .then((response) => {
        console.log(response);
        if (response.data.id === undefined) {
          console.log("입력하신 id 가 일치하지 않습니다.");
          alert("입력하신 id 가 일치하지 않습니다.");
        } else if (response.data.id === null) {
          console.log("입력하신 비밀번호 가 일치하지 않습니다.");
          alert("입력하신 비밀번호 가 일치하지 않습니다.");
        } else if (response.data.id === id) {
          console.log("======================", "로그인 성공");
          sessionStorage.setItem("ID", id);
        }
        navigate("/");
      })
      .catch();
  };

  useEffect(() => {
    axios
      .get("https://comon2022.herokuapp.com/boards")
      .then((response) => console.log(response))
      .catch();
  }, []);

  return (
    <div>
      <div>
        <Navbar />
      </div>
      <div class="Login">
        <div className="Login_text">
          <text>로그인</text>
        </div>
        <form>
          <div class="Login_form">
            <input
              name="id"
              type="text"
              placeholder="아이디"
              value={id}
              onChange={onIdHandler}
              class="login_input"
            />
          </div>
          <div>
            <input
              name="pwd"
              type="password"
              placeholder="비밀번호"
              value={pwd}
              onChange={onPwdHandler}
              class="login_input"
            />
          </div>
          <div>
            <button type="submit" onSubmit={onSubmit} class="login_submit">
              come on
            </button>
          </div>
        </form>

        <div>
          <Link to="/Register">
            <button class="go_Register">코몽이 처음이시라구요?</button>
          </Link>
        </div>
      </div>
    </div>
  );
}
export default Login;
