import React, { Component } from "react";
import { useState } from "react";
import ReactHtmlParser from "html-react-parser";
import { Lists } from "../index.js";
import { Link } from "react-router-dom";

function Post(data) {
  return (
    <div>
      <h1>{ReactHtmlParser(data.title)}</h1>
      <h3>{ReactHtmlParser(data.content)}</h3>
    </div>
  );
}
export default Post;
