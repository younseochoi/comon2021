import React, { Component, useEffect } from "react";
import { useState } from "react";
import { Link } from "react-router-dom";
import { Write } from "../index.js";
import { Post } from "../index.js";
import { Navbar } from "../index.js";
import axios from "axios";

function Lists() {
  const [Board, setBoard] = useState({
    boards: [],
  });

  const fetchData = async () => {
    const boards = await axios.get(
      "https://comon2022.herokuapp.com/boards?category=1"
    );
    console.log(boards);
    const board_data = boards.data.data;
    setBoard({
      ...Board,
      boards: board_data,
    });
  };

  useEffect(() => {
    fetchData();
    console.log(Board);
  }, []);

  return (
    <div>
      <div>
        <Navbar />
      </div>

      <div>
        <div>
          {Board.boards.map((b) => (
            <Post title={b.title} content={b.content} />
          ))}
        </div>
      </div>
    </div>
  );
}
export default Lists;
