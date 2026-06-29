import axios from "axios";
import { useState } from "react";
import Swal from "sweetalert2";
// import { login } from "../utils/useAuthStore";
// import useAuthStore from "../utils/useAuthStore";
import { useNavigate } from "react-router-dom";
import useAuthStore from "../utils/useAuthStore";

const LoginPage = (Login) => {
  const { login } = useAuthStore();
  // const checkForm = () => {
  //   //아이디는 영어문자 4개 숫자 2개가 필수로 들어간 6~8 사이의 문자열
  //   const idForm = /^(?=(?:.*[a-zA-Z]){4})(?=(?:.*\d){2})[a-zA-Z0-9]{6,8}$/;
  //   //비밀번호는 영어문자 4개, 숫자 2개가 필히 들어간 최소길이 6문자 이상이기만 하면 되는 특수문자 가능한 문자열
  //   const pwForm = /^(?=(?:.*[a-zA-Z]){4})(?=(?:.*\d){2}).{8,}$/;
  //   if (pwForm.test(Login.userId)) {
  //     Swal.fire({
  //       text:"아이디의 형식이 올바르지 않습니다."
  //     })
  //   }
  // };
  const [login1, setLogin1] = useState({
    userId: "",
    password: "",
  });
  const navigate = useNavigate();
  const sendForm = () => {
    axios
      .post(`${import.meta.env.VITE_BACKSERVER}/user/login`, login1, {
        withCredentials: true,
      })
      .then((res) => {
        if (res.data.loginStatus) {
          Swal.fire({
            icon: "success",
            text: "로그인 성공",
          });
          login({
            userId: res.data.userId,
            role: res.data.role,
          });
          navigate("/main");
        } else {
          Swal.fire({
            text: res.data.returnMessage,
            icon: "error",
          });
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };
  return (
    <>
      <div>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            // console.log(login);
            sendForm();
          }}
        >
          <label htmlFor="userId">아이디</label>
          <input
            id="userId"
            name="userId"
            value={login1.userId}
            onChange={(e) => {
              setLogin1({ ...login1, ["userId"]: e.target.value });
            }}
          />
          <label htmlFor="password">비밀번호</label>
          <input
            type="password"
            id="password"
            name="password"
            value={login1.password}
            onChange={(e) => {
              setLogin1({ ...login1, [e.target.name]: e.target.value });
            }}
          />
          <button type="submit">로그인</button>
        </form>
      </div>
    </>
  );
};
export default LoginPage;
