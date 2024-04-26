import Bangdieukhien from "./pages/bangdieukhien/Bangdieukhien";
import Login from "./pages/login/Login";
import Qlnguoidung from "./pages/qlnguoidung/Qlnguoidung"
import Qlgiaotrinh from "./pages/qlgiaotrinh/Qlgiaotrinh";
import Thongkegiaodich from "./pages/thongkegiaodich/Thongkegiaodich";
import Hoso from "./pages/hoso/Hoso";
import Single from "./pages/single/Single"
import New from "./pages/new/New"
import {
  BrowserRouter,
  Routes,
  Route
} from "react-router-dom";
import { userInputs, productInputs } from "./formSource";
import "./style/dark.scss"
import { useContext } from "react";
import { DarkModeContext } from "./context/darkModeContext";

function App() {
  const { darkMode } = useContext(DarkModeContext)
  return (
    <div className={darkMode ? "app dark" : "app"}>
      <BrowserRouter>
        <Routes>
          <Route path="/">
            <Route index element={<Bangdieukhien />} />
            <Route path="login" element={<Login />} />
            <Route path="qlnguoidung">
              <Route index element={<Qlnguoidung />} />
              <Route path=":userId" element={<Single />} />
              <Route path="new" element={<New inputs={userInputs} title="Thêm Mới Người Dùng" />} />
            </Route>
            <Route path="qlgiaotrinh">
              <Route index element={<Qlgiaotrinh />} />
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
    </div>
  );
}

export default App;
