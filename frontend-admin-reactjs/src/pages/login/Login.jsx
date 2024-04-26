import "./login.scss"
import { Link } from "react-router-dom";
import PersonIcon from '@mui/icons-material/Person';
import LockIcon from '@mui/icons-material/Lock';
const Login = () => {
    return (
        <div className="tongquat">
            <div className="wrapper">
                <form action="">
                    <h1>Login</h1>
                    <div className="input-box">
                        <input type="text" placeholder="Username" required />
                        <PersonIcon className="icon" />
                    </div>
                    <div className="input-box">
                        <input type="password" placeholder="Password" required />
                        <LockIcon className="icon" />
                    </div>
                    <div className="remember-forgot">
                        <label><input type="checkbox" /> Duy trì đăng nhập </label>
                        <a href="#">Quên mật khẩu?</a>
                    </div>
                    <Link to="/" style={{ textDecoration: "none" }}>
                        <button type="submit">Đăng nhập</button>
                    </Link>
                </form>

            </div>
        </div>
    )
}

export default Login;