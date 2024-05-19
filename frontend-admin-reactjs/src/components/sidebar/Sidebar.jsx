import "./sidebar.scss"
import DashboardIcon from '@mui/icons-material/Dashboard';
import PersonOutlineIcon from '@mui/icons-material/PersonOutline';
import Inventory2OutlinedIcon from '@mui/icons-material/Inventory2Outlined';
import PollIcon from '@mui/icons-material/Poll';
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import ExitToAppOutlinedIcon from '@mui/icons-material/ExitToAppOutlined';
import { Link, useNavigate, useLocation } from "react-router-dom";
import { DarkModeContext } from "../../context/darkModeContext";
import { useContext, useState } from "react";
import { toast } from "react-toastify";

const Sidebar = () => {
    const { dispatch } = useContext(DarkModeContext)
    const [activeLink, setActiveLink] = useState("");
    const location = useLocation();
    const navigate = useNavigate();
    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userData');
        navigate("/");
        toast.success("Log out success!");
    };
    const handleMenuClick = (link) => {
        setActiveLink(link);
    };
    return (
        <div className="sidebar">
            <div className="sidebar-top">
                <Link to="/bangdieukhien" style={{ textDecoration: "none" }}>
                    {/* <img src="https://i.imgur.com/21Ur9qr.jpeg" alt="Icon" class="sidebar-iconlogo" /> */}
                    <span className="sidebar-logo">Admin</span>
                </Link>
            </div>
            <hr />
            <div className="sidebar-center">
                <ul>
                    <p className="sidebar-title">MAIN</p>
                    <Link to="/bangdieukhien" style={{ textDecoration: "none" }}>
                        <li className={location.pathname === "/bangdieukhien" ? "active" : ""}>
                            <DashboardIcon className="sidebar-icon" />
                            <span className="sidebar-span">Bảng điều khiển</span>
                        </li>
                    </Link>
                    <p className="sidebar-title">DANH SÁCH</p>
                    <Link to="/qlnguoidung" style={{ textDecoration: "none" }}>
                        <li className={location.pathname === "/qlnguoidung" ? "active" : ""}>
                            <PersonOutlineIcon className="sidebar-icon" />
                            <span className="sidebar-span">Quản lý người dùng</span>
                        </li>
                    </Link>
                    <Link to="/qlgiaotrinh" style={{ textDecoration: "none" }}>
                        <li className={location.pathname === "/qlgiaotrinh" ? "active" : ""}>
                            <Inventory2OutlinedIcon className="sidebar-icon" />
                            <span className="sidebar-span">Quản lý giáo trình</span>
                        </li>
                    </Link>
                    <Link to="/thongkegiaodich" style={{ textDecoration: "none" }}>
                        <li className={location.pathname === "/thongkegiaodich" ? "active" : ""}>
                            <PollIcon className="sidebar-icon" />
                            <span className="sidebar-span">Thống kê giao dịch</span>
                        </li>
                    </Link>
                    <p className="sidebar-title">NGƯỜI DÙNG</p>
                    <Link to="/hoso" style={{ textDecoration: "none" }}>
                        <li className={location.pathname === "/hoso" ? "active" : ""}>
                            <AccountCircleOutlinedIcon className="sidebar-icon" />
                            <span className="sidebar-span">Hồ sơ</span>
                        </li>
                    </Link>
                    {/* <Link to="/" style={{ textDecoration: "none" }}>
 
                    </Link> */}
                    <li onClick={handleLogout}>
                        <ExitToAppOutlinedIcon className="sidebar-icon" />
                        <span className="sidebar-span">Đăng xuất</span>
                    </li>
                </ul>
            </div>
            <div className="sidebar-bottom">
                <div className="sidebar-colorOption" onClick={() => dispatch({ type: "LIGHT" })}></div>
                <div className="sidebar-colorOption" onClick={() => dispatch({ type: "DARK" })}></div>
            </div>
        </div>
    );
};

export default Sidebar;