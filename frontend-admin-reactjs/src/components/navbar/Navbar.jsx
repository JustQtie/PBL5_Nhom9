import "./navbar.scss"
import SearchOutlinedIcon from '@mui/icons-material/SearchOutlined';
import LanguageOutlinedIcon from '@mui/icons-material/LanguageOutlined';
import FullscreenExitOutlinedIcon from '@mui/icons-material/FullscreenExitOutlined';
import NotificationsNoneOutlinedIcon from '@mui/icons-material/NotificationsNoneOutlined';
import ChatBubbleOutlineOutlinedIcon from '@mui/icons-material/ChatBubbleOutlineOutlined';
import Switch from "@mui/material/Switch";
import ListOutlinedIcon from '@mui/icons-material/ListOutlined';
import { DarkModeContext } from "../../context/darkModeContext";
import { useContext, useState, useEffect } from "react";





const Navbar = () => {
    const { dispatch } = useContext(DarkModeContext)
    const [previewImage, setPreviewImage] = useState("");


    useEffect(() => {

        const userDataString = localStorage.getItem("userData");
        if (userDataString) {
            const userDataObject = JSON.parse(userDataString);
            setPreviewImage(userDataObject.thumbnail ? `${process.env.REACT_APP_API_URL}api/v1/users/images/${userDataObject.thumbnail}` : "https://i.imgur.com/1nORATT.png");
        }
    }, []);


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