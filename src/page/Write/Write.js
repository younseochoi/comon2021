import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Navbar } from "../index.js";
import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/react-editor";
import styled from "styled-components";

import Prism from "prismjs";
import "prismjs/themes/prism.css";

import "@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight.css";
import colorSyntax from "@toast-ui/editor-plugin-color-syntax";
import codeSyntaxHighlight from "@toast-ui/editor-plugin-code-syntax-highlight";
import tableMergedCell from "@toast-ui/editor-plugin-table-merged-cell";
import chart from "@toast-ui/editor-plugin-chart";
import uml from "@toast-ui/editor-plugin-uml";
import axios from "axios";

// const Styled_text = styled.input`
//     margin: 0px;
//     padding: 0px;
//     width: 1000px;
//     font-size: 30px;
// `;

function Write() {
  const [Writing, setWriting] = useState({
    title: "",
    content: "",
  });

  const [Title, setTitle] = useState([]);

  const [Content, setContent] = useState([]);

  const getValue = (e) => {
    const { name, value } = e.target;
    setWriting({
      ...Writing,
      [name]: value,
    });
    console.log(Writing);
  };

  const editorRef = React.createRef();

  const fetchData = async () => {
    const boards = await axios.get(
      "https://comon2022.herokuapp.com/boards?category=1"
    );
    console.log(boards);
    // const board_data = boards.data.data[0];
    // setTitle(board_data.title);
    // setContent(board_data.content);
  };

  useEffect(() => {
    fetchData();
  }, []);

  const saveBoard = () =>
    fetch("https://comon2022.herokuapp.com/boards", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        title: Writing.title,
        content: Writing.content,
        categoryId: "1", ////////// 게시판 선택 가능 후 변경
        author: "현정", ///////// 로그인 후 변경
      }),
    })
      .then((response) => response.json())
      .then((response) => {
        if (response.token) {
          console.log("저장완료");
        }
      })
      .catch((e) => console.log(e));

  const updateBoard = () =>
    fetch("https://comon2022.herokuapp.com/boards?id=64", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        title: Writing.title,
        content: Writing.content,
        categoryId: "1",
        author: "현정",
      }),
    })
      .then((response) => response.json())
      .then((response) => {
        console.log(response);
      })
      .catch((e) => console.log(e));

  const deleteBoard = () =>
    fetch("https://comon2022.herokuapp.com/boards?id=74", {
      method: "DELETE",
    })
      .then((response) => response.json())
      .then((response) => {
        console.log(response);
      })
      .catch((e) => console.log(e));

  return (
    <div>
      <div>
        <Navbar />
      </div>

      <div>
        <text>title</text>
      </div>

      <div className="title_txt">
        <input
          name="title"
          type="text"
          placeholder="제목"
          onChange={getValue}
          //value={Title}
          class="write_title_input"
        />
      </div>

      <div>
        <text>your code</text>
      </div>

      <div className="content_txt">
        <Editor
          previewStyle="vertical"
          height="50vh"
          width="10px"
          initialEditType="markdown"
          ref={editorRef}
          plugins={[
            colorSyntax,
            [
              codeSyntaxHighlight,
              { highlighter: Prism },
              chart,
              tableMergedCell,
              uml,
            ],
          ]}
          onChange={() => {
            const editorInstance = editorRef.current.getInstance();
            //const getContent_markdown = editorInstance.getMarkdown();
            const getContent_html = editorInstance.getHTML();

            setWriting({
              ...Writing,
              content: getContent_html,
            });
            console.log(Writing);
          }}
          //initialValue="hello"
        />
      </div>

      <div classname="write_Btns">
        <Link to="/Lists">
          <button classname="write_submitBtn" onClick={saveBoard}>
            등록
          </button>
        </Link>
        <Link to="/">
          <button className="write_cancleBtn">취소</button>
        </Link>
        <button onClick={updateBoard}>수정</button>
        <button onClick={deleteBoard}>삭제</button>
      </div>
    </div>
  );
}

export default Write;
