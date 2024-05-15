import Bangdieukhien from "./pages/bangdieukhien/Bangdieukhien";
import Login from "./pages/login/Login";
import Qlnguoidung from "./pages/qlnguoidung/Qlnguoidung"
import Qlgiaotrinh from "./pages/qlgiaotrinh/Qlgiaotrinh"
import Thongkegiaodich from "./pages/thongkegiaodich/Thongkegiaodich";
import Hoso from "./pages/hoso/Hoso";
import Single_giaotrinh from "./pages/single/Single_giaotrinh"
import Single from "./pages/single/Single";
import {
  BrowserRouter,
  Navigate,
  Routes,
  Route
} from "react-router-dom";
import "./style/dark.scss"
import { useContext, useState } from "react";
import { DarkModeContext } from "./context/darkModeContext";
import { ToastContainer } from 'react-toastify';
import "react-toastify/dist/ReactToastify.css";

function App() {
  const { darkMode } = useContext(DarkModeContext)
  // const [isLoggedIn, setIsLoggedIn] = useState(false);

  // const handleLogin = () => {
  //   setIsLoggedIn(true);
  // };
  return (

    <div className={darkMode ? "app dark" : "app"}>
      <BrowserRouter>
        <Routes>

          <Route path="/">
            {/* <Route path="/" element={isLoggedIn ? <Navigate to="/bangdieukhien" /> : <Login onLogin={handleLogin} />} />
            <Route path="/bangdieukhien" element={isLoggedIn ? <Bangdieukhien /> : <Navigate to="/" />} /> */}
            <Route index element={<Login />} />
            <Route path="bangdieukhien" element={<Bangdieukhien />} />
            <Route path="qlnguoidung">
              <Route index element={<Qlnguoidung />} />
              <Route path=":userId" element={<Single />} />
            </Route>
            <Route path="qlgiaotrinh">
              <Route index element={<Qlgiaotrinh />} />
              <Route path=":productId" element={<Single_giaotrinh />} />
            </Route>
            <Route path="thongkegiaodich">
              <Route index element={<Thongkegiaodich />} />
            </Route>
            <Route path="hoso">
              <Route index element={<Hoso />} />
            </Route>
          </Route>



        </Routes>
      </BrowserRouter>

      <ToastContainer
        position="top-center"
        autoClose={1000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
    </div>


  );
}

export default App;
