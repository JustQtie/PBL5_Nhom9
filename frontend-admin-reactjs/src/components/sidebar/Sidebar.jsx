import "./sidebar.scss"
import DashboardIcon from '@mui/icons-material/Dashboard';
import PersonOutlineIcon from '@mui/icons-material/PersonOutline';
import Inventory2OutlinedIcon from '@mui/icons-material/Inventory2Outlined';
import ViewStreamOutlinedIcon from '@mui/icons-material/ViewStreamOutlined';
import LocalShippingOutlinedIcon from '@mui/icons-material/LocalShippingOutlined';
import PollIcon from '@mui/icons-material/Poll';
import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import SettingsSystemDaydreamOutlinedIcon from '@mui/icons-material/SettingsSystemDaydreamOutlined';
import PsychologyOutlinedIcon from '@mui/icons-material/PsychologyOutlined';
import SettingsApplicationsOutlinedIcon from '@mui/icons-material/SettingsApplicationsOutlined';
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import ExitToAppOutlinedIcon from '@mui/icons-material/ExitToAppOutlined';
import { Link } from "react-router-dom";
import { DarkModeContext } from "../../context/darkModeContext";
import { useContext } from "react";

// import {PersonOutlineIcon, LocalShippingOutlinedIcon, } from "@mui/icons-material"

const Sidebar = () => {
    const { dispatch } = useContext(DarkModeContext)
    return (
        <div className="sidebar">
            <div className="top">
                <Link to="/" style={{ textDecoration: "none" }}>
                    <span className="logo">HVT Admin</span>
                </Link>
            </div>
            <hr />
            <div className="center">
                <ul>
                    <p className="title">MAIN</p>
                    <li>
                        <DashboardIcon className="icon" />
                        <span className="span">Bảng điều khiển</span>
                    </li>
                    <p className="title">DANH SÁCH</p>
                    <Link to="/users" style={{ textDecoration: "none" }}>
                        <li>
                            <PersonOutlineIcon className="icon" />
                            <span className="span">Quản lý người dùng</span>
                        </li>
                    </Link>

                    <Link to="/products" style={{ textDecoration: "none" }}>
                        <li>
                            <Inventory2OutlinedIcon className="icon" />
                            <span className="span">Quản lý sản phẩm</span>
                        </li>
                    </Link>

                    <li>
                        <ViewStreamOutlinedIcon className="icon" />
                        <span className="span">Đơn đặt hàng</span>
                    </li>
                    <li>
                        <LocalShippingOutlinedIcon className="icon" />
                        <span className="span">Vận chuyển</span>
                    </li>
                    <p className="title">HỮU ÍCH</p>
                    <li>
                        <PollIcon className="icon" />
                        <span className="span">Thống kê</span>
                    </li>
                    <li>
                        <NotificationsNoneIcon className="icon" />
                        <span className="span">Thông báo</span>
                    </li>
                    <p className="title">DỊCH VỤ</p>
                    <li>
                        <SettingsSystemDaydreamOutlinedIcon className="icon" />
                        <span className="span">Tình trạng hệ thống</span>
                    </li>
                    <li>
                        <PsychologyOutlinedIcon className="icon" />
                        <span className="span">Nhật kí</span>
                    </li>
                    <li>
                        <SettingsApplicationsOutlinedIcon className="icon" />
                        <span className="span">Cài đặt</span>
                    </li>
                    <p className="title">NGƯỜI DÙNG</p>
                    <li>
                        <AccountCircleOutlinedIcon className="icon" />
                        <span className="span">Hồ sơ</span>
                    </li>
                    <li>
                        <ExitToAppOutlinedIcon className="icon" />
                        <span className="span">Đăng xuất</span>
                    </li>
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