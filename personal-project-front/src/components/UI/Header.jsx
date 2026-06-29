import useAuthStore from "../../utils/useAuthStore";
import styles from "./Header.module.css";

const Header = () => {
  const { loginStatus } = useAuthStore();
  return (
    loginStatus && (
      <>
        <div>
          <h1></h1>
        </div>
        <div>햄버거</div>
        <button
          onClick={() => {
            Swal.fire({
              icon: "question",
              text: "정말로 로그아웃하시겠습니까?",
              showCancelButton: true,
              cancelButtonText: "취소",
              confirmButtonText: "로그아웃",
            }).then((result) => {
              if (result.isConfirmed) {
                logout();
                Swal.fire({
                  icon: "success",
                  text: "로그아웃되었습니다.",
                });
                navigate("/");
              }
            });
          }}
        >
          로그아웃
        </button>
      </>
    )
  );
};
export default Header;
