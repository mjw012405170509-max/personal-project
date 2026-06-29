import axios from "axios";
import { useEffect } from "react";
import useAuthStore from "../utils/useAuthStore";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import Header from "../components/UI/Header";
import Footer from "../components/UI/Footer";

const MainPage = () => {
  const { logout } = useAuthStore();
  const navigate = useNavigate();
  useEffect(() => {
    axios
      .get(`${import.meta.env.VITE_BACKSERVER}/main/test`, {
        withCredentials: true,
      })
      .then((res) => {
        console.log("here");
        console.log(res.data);
        console.log(res.status);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
  return (
    <div>
      <>메인 화면</>
      <Header />
      <div>
        <div>
          <h2>상업송장 검사기</h2>
        </div>
        <div>
          <input type="file" />
        </div>
      </div>
      <Footer />
    </div>
  );
};
export default MainPage;
