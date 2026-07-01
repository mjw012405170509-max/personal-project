import axios from "axios";
import { useEffect, useState } from "react";
import useAuthStore from "../utils/useAuthStore";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import Header from "../components/UI/Header";
import Footer from "../components/UI/Footer";

const MainPage = () => {
  const navigate = useNavigate();
  const [withFile, setWithFile] = useState(false);

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
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (!file) {
      return;
    }

    if (file.type.startsWith("image/")) {
      //image/jpeg,image/jpg,image/png 형식으로 나옴
      console.log("img");
      setWithFile(file);
    } else if (file.type === "application/pdf") {
      //pdf는 저형식으로 나옴
      console.log("pdf");
      setWithFile(file);
    } else {
      Swal.fire({
        icon: "warning",
        text: "이미지 또는 pdf형식의 파일만 지원합니다.",
      });
      setWithFile(null);
      e.target.value = "";
      return;
    }
  };
  const validateFile = () => {
    if (!withFile) {
      Swal.fire({
        icon: "warning",
        text: "파일이 업로드 되지 않았습니다",
      });
      return;
    }
    const data = new FormData();
    data.append("file", withFile);
    axios
      .post(`${import.meta.env.VITE_BACKSERVER}/file/inspect`, data, {
        withCredentials: true,
      })
      .then((result) => {
        console.log(result);
      })
      .catch((err) => {
        console.log(err);
      });
  };
  return (
    <div>
      <>메인 화면</>
      <Header />
      <div>
        <div>
          <h2>상업송장 검사기</h2>
        </div>
        <div>
          <input
            type="file"
            accept="jpg,pdf,jpeg,png"
            onChange={handleFileChange}
          />
        </div>
        <button
          onClick={() => {
            validateFile();
          }}
        >
          검사하기
        </button>
      </div>
      <Footer />
    </div>
  );
};
export default MainPage;
