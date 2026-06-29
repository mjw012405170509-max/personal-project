import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import { BrowserRouter } from "react-router-dom";

import axios from "axios";
import useAuthStore from "./utils/useAuthStore";
import Swal from "sweetalert2";

// React 렌더링 전에 등록
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    console.log("인터셉터 실행됨");
    if (error.response?.status === 401) {
      Swal.fire({
        text: "타임아웃으로 로그아웃되었습니다.",
        icon: "warning",
      }).then(() => {
        useAuthStore.getState().logout2();
      });
    }
    return Promise.reject(error);
  },
);

createRoot(document.getElementById("root")).render(
  <BrowserRouter>
    <App />
  </BrowserRouter>,
);
