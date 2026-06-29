import axios from "axios";
import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";

const useAuthStore = create(
  // persist는 상태를 브라우저 저장소(localStorage) 에 저장해서

  // 새로고침(F5)
  // 탭 닫았다가 다시 열기
  // 브라우저 재실행

  // 해도 상태가 유지되게 해주는 미들웨어
  persist(
    //객체 안에 함수, 객체 변수를 기재
    (set) => {
      // set() : Zustand store의 상태(state)를 변경하는 함수
      return {
        // 로그인 사용자 아이디
        userId: "",

        // 권한
        role: "",

        // 로그인 여부
        loginStatus: false,

        // 로그인 처리
        login: (userId, role) => {
          // Zustand가 제공하는 상태 변경 함수
          // set() 안에 넣은 값으로 store 상태를 갱신한다.
          // React의 useState의 setState()와 비슷한 역할
          set({
            loginStatus: true,
            userId: userId,
            role: role,
          });
        },

        // 로그아웃 처리
        logout: () => {
          // 저장된 로그인 정보를 초기화
          // set() 호출 시 store의 상태가 변경되며
          // persist가 적용되어 있으면 localStorage에도 반영됨
          // axios요청을 보내서 추가로 토큰(현재 토큰은 쿠키에 보관되어 있음)
          axios
            .post(
              `${import.meta.env.VITE_BACKSERVER}/user/logout`,
              {},
              {
                withCredentials: true,
              },
            )
            .then((res) => {
              console.log(res);
              if (res.data) {
                set({
                  loginStatus: false,
                  userId: "",
                  role: "",
                });
              }
            })
            .catch((err) => {
              console.log(err);
            });
        },
        logout2: () => {
          console.log("로그아웃 함수 동작시작");
          set({
            loginStatus: false,
            userId: "",
            role: "",
          });
          console.log("로그아웃 함수 동작함");
        },
      };
    },
    {
      // localStorage에 저장될 이름
      name: "auth-storage",
    },
  ),
);
export default useAuthStore;
