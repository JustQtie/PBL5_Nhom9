import "./navbar.scss"
import SearchOutlinedIcon from '@mui/icons-material/SearchOutlined';
import LanguageOutlinedIcon from '@mui/icons-material/LanguageOutlined';
import FullscreenExitOutlinedIcon from '@mui/icons-material/FullscreenExitOutlined';
import NotificationsNoneOutlinedIcon from '@mui/icons-material/NotificationsNoneOutlined';
import ChatBubbleOutlineOutlinedIcon from '@mui/icons-material/ChatBubbleOutlineOutlined';
import Switch from "@mui/material/Switch";
import ListOutlinedIcon from '@mui/icons-material/ListOutlined';
import { DarkModeContext } from "../../context/darkModeContext";
import { useContext } from "react";





const Navbar = () => {
    const { dispatch } = useContext(DarkModeContext)

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
                        <img
                            src="https://i.imgur.com/1nORATT.png"
                            alt=""
                            className="navbar-avatar"
                        />
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Navbar;