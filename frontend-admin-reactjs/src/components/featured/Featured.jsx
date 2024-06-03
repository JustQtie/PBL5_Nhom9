import React, { useEffect, useState } from "react";
import "./featured.scss";
import MoreVertIcon from '@mui/icons-material/MoreVert';
import { CircularProgressbar, buildStyles } from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpOutlinedIcon from '@mui/icons-material/KeyboardArrowUpOutlined';
import ChangingProgressProvider from "./ChangingProgressProvider";
import { getAllUsers } from "../../services/apiServices";
import { toast } from "react-toastify";

const Featured = () => {
    const [activeUserPercentage, setActiveUserPercentage] = useState(0);

    const fetchListUsers = async () => {
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                toast.error("Token không được tìm thấy");
                return;
            }

            const res = await getAllUsers(token);

            if (res && res.EC === 0) {
                const users = res.userResponseList;
                const totalUsers = users.length;
                const activeUsers = users.filter(user => user.active).length;
                const percentage = Math.ceil((activeUsers / totalUsers) * 100);

                setActiveUserPercentage(percentage);
            }
        } catch (error) {
            toast.error("Lỗi khi lấy danh sách người dùng", error);
        }
    };

    fetchListUsers();

    return (
        <div className="featured">
            <div className="bottom">
                <div className="featuredChart">
                    <ChangingProgressProvider
                        values={[activeUserPercentage]}
                    >
                        {(percentage) => (
                            <CircularProgressbar
                                value={percentage}
                                text={`${percentage}%`}
                                styles={buildStyles({
                                    pathTransitionDuration: 0.85,
                                    trailColor: "#82ca9d",
                                    pathColor: "#210876",
                                    textColor: "#737173",
                                })}
                            />
                        )}
                    </ChangingProgressProvider>
                </div>
                <p className="title">Số lượng người dùng đang hoạt động</p>

                <div className="summary">
                    <div className="item">
                        <div className="itemTitle">Hôm nay</div>
                        <div className="itemResult positive">
                            <KeyboardArrowUpOutlinedIcon fontSize="small" />
                            <div className="resultAmount">{activeUserPercentage}%</div>
                        </div>
                    </div>
                    <div className="item">
                        <div className="itemTitle">Hôm qua</div>
                        <div className="itemResult positive">
                            <KeyboardArrowUpOutlinedIcon fontSize="small" />
                            <div className="resultAmount">90%</div>
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
    );
}

export default Featured;
