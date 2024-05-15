import "./sidebar.scss"
import DashboardIcon from '@mui/icons-material/Dashboard';
import PersonOutlineIcon from '@mui/icons-material/PersonOutline';
import Inventory2OutlinedIcon from '@mui/icons-material/Inventory2Outlined';
import PollIcon from '@mui/icons-material/Poll';
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import ExitToAppOutlinedIcon from '@mui/icons-material/ExitToAppOutlined';
import { Link } from "react-router-dom";
import { DarkModeContext } from "../../context/darkModeContext";
import { useContext } from "react";

const Sidebar = () => {
    const { dispatch } = useContext(DarkModeContext)
    return (
        <div className="sidebar">
            <div className="top">
                <Link to="/" style={{ textDecoration: "none" }}>
                    <img src="https://i.imgur.com/21Ur9qr.jpeg" alt="Icon" class="iconlogo" />
                    <span className="logo">Admin</span>
                </Link>
            </div>
            <hr />
            <div className="center">
                <ul>
                    <p className="title">MAIN</p>
                    <Link to="/" style={{ textDecoration: "none" }}>
                        <li>
                            <DashboardIcon className="icon" />
                            <span className="span">Bảng điều khiển</span>
                        </li>
                    </Link>
                    <p className="title">DANH SÁCH</p>
                    <Link to="/qlnguoidung" style={{ textDecoration: "none" }}>
                        <li>
                            <PersonOutlineIcon className="icon" />
                            <span className="span">Quản lý người dùng</span>
                        </li>
                    </Link>
                    <Link to="/qlgiaotrinh" style={{ textDecoration: "none" }}>
                        <li>
                            <Inventory2OutlinedIcon className="icon" />
                            <span className="span">Quản lý giáo trình</span>
                        </li>
                    </Link>
                    <Link to="/thongkegiaodich" style={{ textDecoration: "none" }}>
                        <li>
                            <PollIcon className="icon" />
                            <span className="span">Thống kê giao dịch</span>
                        </li>
                    </Link>
                    <p className="title">NGƯỜI DÙNG</p>
                    <Link to="/hoso" style={{ textDecoration: "none" }}>
                        <li>
                            <AccountCircleOutlinedIcon className="icon" />
                            <span className="span">Hồ sơ</span>
                        </li>
                    </Link>
                    <Link to="/login" style={{ textDecoration: "none" }}>
                        <li>
                            <ExitToAppOutlinedIcon className="icon" />
                            <span className="span">Đăng xuất</span>
                        </li>
                    </Link>
                </ul>
            </div>
            <div className="bottom">
                <div className="colorOption" onClick={() => dispatch({ type: "LIGHT" })}></div>
                <div className="colorOption" onClick={() => dispatch({ type: "DARK" })}></div>
            </div>
        </div>
    );
};

export default Sidebar;