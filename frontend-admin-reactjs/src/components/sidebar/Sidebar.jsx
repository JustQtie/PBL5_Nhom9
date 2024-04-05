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

const Sidebar = () => {
    return (
        <div className="sidebar">
            <div className="top">
                <span className="logo">HVT Admin</span>
            </div>
            <hr></hr>
            <div className="center">
                <ul>
                    <p className="title">Main</p>
                    <li>
                        <DashboardIcon className="icon" />
                        <span className="span">Bảng điều khiển</span>
                    </li>
                    <p className="title">DANH SÁCH</p>
                    <li>
                        <PersonOutlineIcon className="icon" />
                        <span className="span">Quản lý người dùng</span>
                    </li>
                    <li>
                        <Inventory2OutlinedIcon className="icon" />
                        <span className="span">Quản lý sản phẩm</span>
                    </li>
                    <li>
                        <ViewStreamOutlinedIcon className="icon" />
                        <span className="span">Đơn Đặt hàng</span>
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
                        <span className="span">THông báo</span>
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
                <div className="colorOption"></div>
                <div className="colorOption"></div>
            </div>
        </div>
    );
};

export default Sidebar;