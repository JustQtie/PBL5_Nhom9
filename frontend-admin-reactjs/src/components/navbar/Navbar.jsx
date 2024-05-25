import "./navbar.scss"
import SearchOutlinedIcon from '@mui/icons-material/SearchOutlined';
import LanguageOutlinedIcon from '@mui/icons-material/LanguageOutlined';
import FullscreenExitOutlinedIcon from '@mui/icons-material/FullscreenExitOutlined';
import NotificationsNoneOutlinedIcon from '@mui/icons-material/NotificationsNoneOutlined';
import ChatBubbleOutlineOutlinedIcon from '@mui/icons-material/ChatBubbleOutlineOutlined';
import Switch from "@mui/material/Switch";
import { getUserById } from "../../services/apiServices";
import { toast } from 'react-toastify';
import ListOutlinedIcon from '@mui/icons-material/ListOutlined';
import { DarkModeContext } from "../../context/darkModeContext";
import { useContext, useState, useEffect } from "react";





const Navbar = () => {
    const { dispatch } = useContext(DarkModeContext);
    const [previewImage, setPreviewImage] = useState("");
    const [userDataObject, setUserDataObject] = useState({});

    useEffect(() => {
        const userDataString = localStorage.getItem("userData");
        if (userDataString) {
            const parsedUserData = JSON.parse(userDataString);
            setUserDataObject(parsedUserData);
        }

        const token = localStorage.getItem("token");
        if (!token) {
            return;
        }

        const fetchUser = async () => {
            try {
                const res = await getUserById(userDataObject.id, token);
                if (res.EC === 0) {
                    const user = res;
                    setPreviewImage(user.thumbnail ? `${process.env.REACT_APP_API_URL}api/v1/users/images/${user.thumbnail}` : "https://i.imgur.com/1nORATT.png");
                } else {
                    toast.error("Không thể lấy dữ liệu người dùng");
                }
            } catch (error) {
                console.error("Fetch user error:", error);
                toast.error("Có lỗi xảy ra khi lấy dữ liệu người dùng");
            }
        };

        if (userDataObject.id) {
            fetchUser();
        }
    }, [userDataObject.id]);



    return (
        <div className="navbar">
            <div className="navbar-wrapper">
                <div className="navbar-search">
                    <input type="text" placeholder="Search..." />
                    <SearchOutlinedIcon />
                </div>
                <div className="navbar-item-to">
                    <div className="navbar-item">
                        <LanguageOutlinedIcon className="navbar-icon" />
                        Vietnamese
                    </div>
                    <div className="navbar-item">
                        <Switch
                            style={{ color: "#2F80ED" }}
                            className="navbar-icon"
                            onClick={() => dispatch({ type: "TOGGLE" })}
                        />
                    </div>
                    <div className="navbar-item">
                        <FullscreenExitOutlinedIcon className="navbar-icon" />
                    </div>
                    <div className="navbar-item">
                        <NotificationsNoneOutlinedIcon className="navbar-icon" />
                        <div className="navbar-couter">2</div>
                    </div>
                    <div className="navbar-item">
                        <ChatBubbleOutlineOutlinedIcon className="navbar-icon" />
                        <div className="navbar-couter">1</div>
                    </div>
                    <div className="navbar-item">
                        <ListOutlinedIcon className="navbar-icon" />
                    </div>
                    <div className="navbar-item">
                        <img className="navbar-avatar" src={previewImage || "https://i.imgur.com/1nORATT.png"} alt="profile" />
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Navbar;