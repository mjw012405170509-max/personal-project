import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

const SignInPage = () => {
  const navigate = useNavigate();
  const [signIn, setSignIn] = useState({
    userId: "",
    password: "",
  });
  const [pwCheck, setPwCheck] = useState("");
  const [idState, setIdState] = useState(false);
  const [pwState, setPwState] = useState(false);
  const checkForm = (Login) => {
    //아이디는 영어문자 4개 숫자 2개가 필수로 들어간 6~8 사이의 문자열
    const idForm = /^(?=(?:.*[a-zA-Z]){4})(?=(?:.*\d){2})[a-zA-Z0-9]{6,8}$/;
    //비밀번호는 영어문자 4개, 숫자 2개가 필히 들어간 최소길이 8문자 이상이기만 하면 되는 특수문자 가능한 문자열
    const pwForm = /^(?=(?:.*[a-zA-Z]){4})(?=(?:.*\d){2}).{8,}$/;
    if (!idForm.test(Login.userId)) {
      Swal.fire({
        text: "아이디의 형식이 올바르지 않습니다.",
        icon: "error",
      });
      return false;
    }
    if (!idState) {
      Swal.fire({
        icon: "warning",
        text: "아이디의 중복체크를 진행해야합니다.",
      });
      return false;
    }
    if (!pwForm.test(Login.password)) {
      Swal.fire({
        text: "비밀번호의 형식이 올바르지 않습니다.",
        icon: "error",
      });
      return false;
    }

    if (!pwState) {
      Swal.fire({
        icon: "warning",
        text: "비밀번호 확인 부분을 완료해 주세요",
      });
      return false;
    }
    return true;
  };
  return (
    <>
      <div>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            if (checkForm(signIn)) {
              axios
                .post(`${import.meta.env.VITE_BACKSERVER}/user/signIn`, signIn)
                .then((res) => {
                  // console.log(res.data);
                  if (res.data === 1) {
                    Swal.fire({
                      icon: "success",
                      text: "회원가입이 완료되었습니다.",
                    });
                    navigate("/");
                  } else {
                    alert("좆됐습니다.");
                  }
                })
                .catch((err) => {
                  if (err.response && err.response.status === 400) {
                    const message = err.response.data.errors[0].defaultMessage;

                    Swal.fire({
                      text: message,
                      icon: "error",
                    });
                  }
                });
            }
          }}
        >
          <label htmlFor="userId">아이디</label>
          <input
            id="userId"
            name="userId"
            value={signIn.userId}
            placeholder="영어문자 4자리에 숫자 2개이상을 포함시켜 6~8자리로 작성해 주세요(특수문자 X)"
            onChange={(e) => {
              setSignIn({ ...signIn, [e.target.name]: e.target.value });
            }}
          />
          <button
            type="button"
            onClick={() => {
              axios
                .get(
                  `${import.meta.env.VITE_BACKSERVER}/user/idCheck?userId=${signIn.userId}`,
                )
                .then((res) => {
                  setIdState(res.data);
                  if (!res.data) {
                    alert("중복된 아이디 존재");
                  }
                })
                .catch((err) => {
                  console.log(err);
                });
            }}
          >
            아이디 중복체크
          </button>
          <p>{idState ? "아이디 중복 확인" : "아이디 중복 미확인"}</p>
          <label htmlFor="password">비밀번호</label>
          <input
            type="password"
            id="password"
            name="password"
            placeholder="영어문자 4자리에 숫자 2자리를 포함하여 최소 6자리 이상으로 작성해주세요"
            value={signIn.password}
            onChange={(e) => {
              setSignIn({ ...signIn, [e.target.name]: e.target.value });
              setPwState(false);
            }}
          />
          <label>비밀번호 확인</label>
          <input
            type="password"
            value={pwCheck}
            onChange={(e) => {
              setPwCheck(e.target.value);
              if (e.target.value === signIn.password && e.target.value !== "") {
                setPwState(true);
              } else {
                setPwState(false);
              }
            }}
          />
          <p>{pwState ? "비밀번호 확인 완료" : "비밀번호 확인 필요"}</p>
          {/* <button>회원가입 신청</button>
          가입 승낙 페이지 하나 더 계획 중 */}
          <button type="submit">회원 가입</button>
        </form>
      </div>
    </>
  );
};
export default SignInPage;
