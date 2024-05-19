import "./login.scss"
import PersonIcon from '@mui/icons-material/Person';
import LockIcon from '@mui/icons-material/Lock';
import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { postLogin } from '../../services/apiServices';
import { toast } from 'react-toastify';


const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();
    useEffect(() => {
        let token = localStorage.getItem("token");
        if (token) {
            navigate('/bangdieukhien')
        }
    }, []);


    const handleLogin = (e) => {
        e.preventDefault();
        const callAPI = async () => {
            try {

                const data = await postLogin(username, password);
                console.log("check res", data);
                if (data && data.EC === 0) {
                    if (data.role === "Admin") {
                        localStorage.setItem("token", data.token);
                        localStorage.setItem("userData", JSON.stringify(data.user));
                        toast.success(data.message);
                        setTimeout(() => {
                            navigate('/bangdieukhien');
                        }, 1000);
                    } else {
                        toast.error("Bạn không có quyền truy cập.");
                    }
                }
                if (data && data.EC !== 0) {
                    toast.error(data.message);
                }

            }
            catch (e) {
                toast.error('Login failed');
            }
            finally {
            }
        };
        callAPI();

    }


    return (
        <div className="login">
            <div className="login-wrapper">
                <form action="">
                    <h1>Login</h1>
                    <div className="login-input-box">
                        {/* <input type="text" placeholder="Username" required /> */}
                        <input type="text" required value={username} onChange={(event) => setUsername(event.target.value)} />
                        <span htmlFor="">Username</span>
                        <PersonIcon className="login-icon" />
                    </div>
                    <div className="login-input-box">
                        <input type="password" required value={password} onChange={(event) => setPassword(event.target.value)} />
                        <span htmlFor="">Password</span>
                        <LockIcon className="login-icon" />
                    </div>
                    <div className="login-remember-forgot">
                        <label><input type="checkbox" /> Duy trì đăng nhập </label>
                        <a href="#">Quên mật khẩu?</a>
                    </div>
                    {/* <Link to="/bangdieukhien" style={{ textDecoration: "none" }}>
                        
                    </Link> */}

                    <button type="submit" onClick={handleLogin}>Đăng nhập</button>

                </form>

            </div>
        </div>
    )
}

export default Login;