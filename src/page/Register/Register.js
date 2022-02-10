import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Register.css";
import { Navbar } from "../index.js";

function Register(props) {
  const [id, setId] = useState("");
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [birth, setBirth] = useState("");
  const [pwd, setPwd] = useState("");
  const [confirmPwd, setConfirmPwd] = useState("");

  const [idMessage, setIdMessage] = useState("");
  const [nameMessage, setNameMessage] = useState("");
  const [emailMessage, setEmailMessage] = useState("");
  const [birthMessage, setBirthMessage] = useState("");
  const [pwdMessage, setPwdMessage] = useState("");
  const [confirmPwdMessage, setConfirmPwdMessage] = useState("");

  const [isId, setIsId] = useState(false);
  const [isName, setIsName] = useState(false);
  const [isEmail, setIsEmail] = useState(false);
  const [isBirth, setIsBirth] = useState(false);
  const [isPwd, setIsPwd] = useState(false);
  const [isConfirmPwd, setIsConfirmPwd] = useState(false);

  const navigate = useNavigate();

  const onIdHandler = (event) => {
    setId(event.currentTarget.value);
    if (event.currentTarget.value.length < 2 || event.currentTarget.value > 5) {
      setIdMessage("2글자 이상 10글자 미만으로 입력해주세요.");
      setIsId(false);
    } else {
      setIdMessage("올바른 아이디 형식입니다.");
      setIsId(true);
    }
  };

  const onNameHandler = (event) => {
    setName(event.currentTarget.value);
    if (event.currentTarget.value.length < 2 || event.currentTarget.value > 5) {
      setNameMessage("2글자 이상 5글자 미만으로 입력해주세요.");
      setIsName(false);
    } else {
      setNameMessage("올바른 이름 형식입니다.");
      setIsName(true);
    }
  };

  const onEmailHandler = (event) => {
    setEmail(event.currentTarget.value);
    if (event.currentTarget.value.length != 9) {
      setEmailMessage("올바르지 않습니다");
      setIsEmail(false);
    } else {
      setEmailMessage("올바른 이메일 형식입니다");
      setIsEmail(true);
    }
  };

  // const onEmailHandler = (event) => {
  //   const emailRegex =
  //     /([\w-.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
  //   setEmail(event.currentTarget.value);

  //   if (!emailRegex.test(event.currentTarget.value)) {
  //     setEmailMessage("이메일 형식이 잘못되었습니다.");
  //     setIsEmail(false);
  //   } else {
  //     setEmailMessage("올바른 이메일 형식입니다");
  //     setIsEmail(true);
  //   }
  // };
  const onBirthHandler = (event) => {
    setBirth(event.currentTarget.value);
    if (event.currentTarget.value != null) {
      setIsBirth(true);
    }
  };
  //   const onBirthHandler = (event) => {
  //     setBirth(event.currentTarget.value);
  //     if (event.currentTarget.value.length == 8) {
  //       setBirthMessage("올바른 생년월일 형식입니다.");
  //       setIsBirth(true);
  //     } else {
  //       setBirthMessage("생년월일 형식이 잘못되었습니다");
  //       setIsBirth(false);
  //     }
  //   };

  const onPwdHandler = (event) => {
    const pwdRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
    setPwd(event.currentTarget.value);

    if (!pwdRegex.test(event.currentTarget.value)) {
      setPwdMessage("숫자+영문자+특수문자 조합으로 8자리 이상 입력해주세요!");
      setIsPwd(false);
    } else {
      setPwdMessage("올바른 비밀번호 형식입니다.");
      setIsPwd(true);
    }
  };

  const onConfirmPwdHandler = (event) => {
    const confirmPwdCurrent = event.currentTarget.value;
    setConfirmPwd(confirmPwdCurrent);

    if (pwd == confirmPwdCurrent) {
      setConfirmPwdMessage("비밀번호가 일치합니다.");
      setIsConfirmPwd(true);
    } else {
      setConfirmPwdMessage("비밀번호가 일치하지 않습니다.");
      setIsConfirmPwd(false);
    }
  };

  const onSubmit = (event) => {
    event.preventDefault();
    fetch("https://comon2022.herokuapp.com/boards", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        Id: id,
        Pwd: pwd,
        Name: name,
        Email: email,
        Birth: birth,
      }),
    })
      .then((response) => response.json())
      .then((response) => {
        if (response.token) {
          console.log("회원가입 성공");
          navigate("/Login");
        }
      })
      .catch((e) => console.log(e));
  };

  return (
    <div>
      <div>
        <Navbar />
      </div>
      <div class="Register">
        <div className="Register_text">
          <text>회원가입</text>
        </div>
        <form>
          <div class="Register_form">
            <input
              name="id"
              type="text"
              placeholder="아이디"
              value={id}
              onChange={onIdHandler}
              class="register_input"
            />
            {id.length > 0 && (
              <p className={`message ${isId ? "success" : "error"}`}>
                {idMessage}
              </p>
            )}
          </div>
          <div>
            <input
              name="name"
              type="text"
              placeholder="이름"
              value={name}
              onChange={onNameHandler}
              class="register_input"
            />
            {name.length > 0 && (
              <p className={`message ${isName ? "success" : "error"}`}>
                {nameMessage}
              </p>
            )}
          </div>
          <div>
            <input
              name="email"
              type="text"
              placeholder="이메일"
              value={email}
              onChange={onEmailHandler}
              class="register_input"
            />
            <text>@pknu.ac.kr</text>
            {email.length > 0 && (
              <span className={`message ${isEmail ? "success" : "error"}`}>
                {emailMessage}
              </span>
            )}
          </div>
          <div>
            <input
              name="birth"
              type="date"
              value={birth}
              onChange={onBirthHandler}
              class="register_input"
            />
            {/* {birth.length > 0 && (
            <span className={`message ${isBirth ? "success" : "error"}`}>
              {birthMessage}
            </span>
          )} */}
          </div>

          <div>
            <input
              name="pwd"
              type="password"
              placeholder="비밀번호 (숫자+영문자+특수문자로 8자리 이상)"
              value={pwd}
              onChange={onPwdHandler}
              class="register_input"
            />
            {pwd.length > 0 && (
              <span className={`message ${isPwd ? "success" : "error"}`}>
                {pwdMessage}
              </span>
            )}
          </div>

          <div>
            <input
              name="confirmPwd"
              type="password"
              placeholder="비밀번호 확인"
              value={confirmPwd}
              onChange={onConfirmPwdHandler}
              class="register_input"
            />
            {confirmPwd.length > 0 && (
              <span className={`message ${isConfirmPwd ? "success" : "error"}`}>
                {confirmPwdMessage}
              </span>
            )}
          </div>
          <div>
            <button
              type="submit"
              onSubmit={onSubmit}
              class="register_submit"
              disabled={
                !(isId && isName && isEmail && isBirth && isPwd && isConfirmPwd)
              }
            >
              가입하기
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
export default Register;
