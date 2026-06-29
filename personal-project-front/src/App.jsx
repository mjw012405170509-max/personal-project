import { useEffect, useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "./assets/vite.svg";
import heroImg from "./assets/hero.png";
import "./App.css";
import axios from "axios";
import { Routes, Route, useNavigate, useLocation } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import SignInPage from "./pages/SignInPage";
import MainPage from "./pages/MainPage";
import useAuthStore from "./utils/useAuthStore";
import Swal from "sweetalert2";

function App() {
  // App.jsx

  // loginStatus를 "한 번 읽는 것"이 아니라 Zustand Store에 구독(subscribe)한다.
  // 이후 다른 곳에서
  //
  // set({ loginStatus: false })
  //
  // 가 실행되면 App 컴포넌트가 자동으로 다시 렌더링되고,
  // 아래 loginStatus 값도 최신 값(false)으로 다시 가져오게 된다.
  //
  // 즉,
  // "변수가 바뀌는 것"이 아니라
  // "App 함수가 다시 실행되면서 최신 상태를 다시 읽는 것"이다.
  const loginStatus = useAuthStore((state) => state.loginStatus);
  const location = useLocation();
  useEffect(() => {
    if (
      !loginStatus &&
      location.pathname !== "/" &&
      location.pathname !== "/tickingAwayTheMomentsThatMakeUpTheDullDay"
    ) {
      //location.pathname.startsWith("/main") /main으로 시작하는 모든 경로
      navigate("/");
    }
  }, [loginStatus, location.pathname]);

  // useEffect(() => {
  //   // loginStatus가 변경될 때마다 이 useEffect도 다시 실행된다.
  //   //
  //   // 로그인 상태가 false가 되면
  //   //
  //   // Interceptor
  //   //      ↓
  //   // forceLogout()
  //   //      ↓
  //   // set({ loginStatus: false })
  //   //      ↓
  //   // App 다시 렌더링
  //   //      ↓
  //   // useEffect 재실행
  //   //      ↓
  //   // 로그인 페이지로 이동
  //   if (!loginStatus) {
  //     navigate("/");
  //   }
  // }, [loginStatus]); // loginStatus 변경 감지
  const [count, setCount] = useState("");
  const navigate = useNavigate();
  // useEffect(() => {
  //   console.log("인터셉터 등록");
  //   const interceptor = axios.interceptors.response.use(
  //     (response) => response,
  //     (error) => {
  //       console.log(error);
  //       // 옵셔널 체이닝 없으면 data가 null인 상태에서 .으로 추가접근 > null에 접근 터짐(.?)-> 그래서 .?을 걸어 response미존재시 undefined로 반환
  //       if (error.response?.status === 401) {
  //         console.log("errorCaught");
  //         useAuthStore.getState().logout2();
  //       }
  //       return Promise.reject(error); // locked를 제외한 다른 오류는 각자 axios catch에 돌려줌(에러를 다음 캐치들로 넘김)
  //     },
  //   );
  //   // 컴포넌트 언마운트 시 인터셉터 제거 (중복 방지)(재렌더나 재마운트시 alert여러개 뜰 수 있음)컴포넌트 종료 시 정리(cleanup)
  //   return () => axios.interceptors.response.eject(interceptor);
  // }, []);

  return (
    <div>
      <div>
        {/* <button
          onClick={() => {
            axios
              .get(`${import.meta.env.VITE_BACKSERVER}/file/test`)
              .then((res) => {
                console.log(res.data);
                setCount(res.data);
              })
              .catch((err) => {
                console.log(err);
              });
          }}
        >
          테스트
        </button> */}
        <div>
          <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route
              path="/tickingAwayTheMomentsThatMakeUpTheDullDay"
              element={<SignInPage />}
            />
            <Route path="/main" element={<MainPage />} />
          </Routes>
        </div>
      </div>
    </div>
  );
}

export default App;

// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
//
// loginStatus라는 "변수"가 바뀌는 것이 아니다.
//
// Zustand의 상태(set)가 변경되면
// App 컴포넌트 자체가 다시 렌더링되고,
// 그 과정에서
//
// const loginStatus = useAuthStore(...)
//
//
// 가 다시 실행되어 최신 값을 가져오는 것이다.
//
// React의 useState와 완전히 같은 원리이다.
//
// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
