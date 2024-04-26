import "./featured.scss"
import MoreVertIcon from '@mui/icons-material/MoreVert';
import { CircularProgressbar, buildStyles } from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpOutlinedIcon from '@mui/icons-material/KeyboardArrowUpOutlined';
import ChangingProgressProvider from "./ChangingProgressProvider";

const Featured = () => {
    return (
        <div className="featured">
            {/* <div className="top">
                <h1 className="title">Số lượng người dùng truy cập hiện tại</h1>
                <MoreVertIcon fontSize="small" />
            </div> */}
            <div className="bottom">
                <div className="featuredChart">
                    <ChangingProgressProvider
                        values={[0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]}
                    >
                        {(percentage) => (
                            <CircularProgressbar
                                value={percentage}
                                text={`${percentage}%`}
                                styles={buildStyles({
                                    pathTransitionDuration: 0.95,
                                    trailColor: "#82ca9d",
                                    pathColor: "#210876",
                                    textColor: "#737173",
                                })}
                            />
                        )}
                    </ChangingProgressProvider>
                </div>
                <p className="title">Số lượng người dùng đang hoạt động</p>
                {/* <p className="amount">500$</p>
                <p className="desc">Xử lý giao dịch trước đó. Các khoản thanh toán cuối cùng có thể không được đưa vào.</p> */}
                <div className="summary">
                    <div className="item">
                        <div className="itemTitle">hôm nay</div>
                        <div className="itemResult negative">
                            <KeyboardArrowDownIcon fontSize="small" />
                            <div className="resultAmount">70%</div>
                        </div>
                    </div>
                    <div className="item">
                        <div className="itemTitle">Hôm qua</div>
                        <div className="itemResult positive">
                            <KeyboardArrowUpOutlinedIcon fontSize="small" />
                            <div className="resultAmount">60%</div>
                        </div>
                    </div>
                    <div className="item">
                        <div className="itemTitle">Hôm kia</div>
                        <div className="itemResult positive">
                            <KeyboardArrowUpOutlinedIcon fontSize="small" />
                            <div className="resultAmount">80%</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Featured;