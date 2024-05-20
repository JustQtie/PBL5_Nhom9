import "./widget.scss";
import KeyboardArrowUpOutlinedIcon from '@mui/icons-material/KeyboardArrowUpOutlined';
import PersonOutlineOutlinedIcon from '@mui/icons-material/PersonOutlineOutlined';
import AccountBalanceWalletOutlinedIcon from '@mui/icons-material/AccountBalanceWalletOutlined';
import ShoppingCartOutlinedIcon from '@mui/icons-material/ShoppingCartOutlined';
import React, { useState, useEffect } from "react";
import { toast } from "react-toastify";
import { getAllUsers } from "../../services/apiServices";

const Widget = ({ type }) => {

    const [userCount, setUserCount] = useState(0);
    const [orderCount, setOrderCount] = useState(0);
    const [balanceCount, setBalanceCount] = useState(0);
    useEffect(() => {
        const fetchUserCount = async () => {

            try {
                // Lấy token từ localStorage
                const token = localStorage.getItem("token");

                // Nếu không có token, không gọi API và kết thúc hàm
                if (!token) {
                    toast.error("Token not found");
                    return;
                }

                // Gọi API với token được đính kèm trong header Authorization

                const res = await getAllUsers(token);

                if (res && res.EC === 0) {
                    setUserCount(res.userResponseList.length);
                }
            } catch (error) {
                toast.error("Error fetching list of users", error);
            }
        }

        fetchUserCount();
        // const fetchOrderCount = axios.get('YOUR_API_ENDPOINT_FOR_ORDER_COUNT');
        // const fetchBalanceCount = axios.get('YOUR_API_ENDPOINT_FOR_BALANCE_COUNT');


    }, []);

    let data = {};




    switch (type) {
        case "user":
            data = {
                title: "Người dùng",
                amount: userCount,
                icon: (
                    <PersonOutlineOutlinedIcon
                        className="icon"
                        style={{
                            color: "crimson",
                            backgroundColor: "rgba(255, 0, 0, 0.2)",
                        }}
                    />
                ),
            };
            break;
        case "order":
            data = {
                title: "Tổng giao dịch thành công",
                amount: orderCount,
                icon: (
                    <ShoppingCartOutlinedIcon
                        className="icon"
                        style={{
                            color: "goldenrod",
                            backgroundColor: "rgba(218, 165, 32, 0.2)",
                        }} />),
            };
            break;
        case "balance":
            data = {
                title: "Tổng giao dịch thất bại",
                amount: balanceCount,
                icon: (
                    <AccountBalanceWalletOutlinedIcon
                        className="icon"
                        style={{
                            color: "purple",
                            backgroundColor: "rgba(128, 0, 128, 0.2)",
                        }}
                    />
                ),
            };
            break;
        default:
            break;
    }





    return (
        <div className="widget">
            <div className="left">
                <span className="title-widget">{data.title}</span>
                <span className="counter">{data.amount}</span>
            </div>
            <div className="right">
                <div className="percentage positive">
                    <KeyboardArrowUpOutlinedIcon />
                </div>
                {data.icon}
            </div>
        </div>
    )
}

export default Widget;