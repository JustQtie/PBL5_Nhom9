import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import Widget from "../../components/widgets/Widget";
import "./bangdieukhien.scss"
import Featured from "../../components/featured/Featured";
import Chart3 from "../../components/chart3_bangdieukhien/Chart3";
import Table from "../../components/table/Table";
import React, { useState, useEffect } from "react";
import { toast } from "react-toastify";
import { getAllUsers, getAllGiaoDichThanhCong } from "../../services/apiServices";

const Bangdieukhien = () => {

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

        const fetchGiaoDichCount = async () => {

            try {
                // Lấy token từ localStorage
                const token = localStorage.getItem("token");

                // Nếu không có token, không gọi API và kết thúc hàm
                if (!token) {
                    toast.error("Token not found");
                    return;
                }

                const res = await getAllGiaoDichThanhCong(token);

                if (res && res.EC === "0") {
                    setOrderCount(res.total_order_success);
                    setBalanceCount(res.total_order_canceled)

                }
            } catch (error) {
                toast.error("Error fetching list of giao dich", error);
            }
        }


        fetchUserCount();
        fetchGiaoDichCount()

    }, []);

    return (
        <div className="bangdieukhien">
            <Sidebar />
            <div className="bangdieukhien-Container">
                <Navbar />
                <div className="bangdieukhien-widgets">
                    <Widget type="user" userCount={userCount} />
                    <Widget type="order" orderCount={orderCount} />
                    <Widget type="balance" balanceCount={balanceCount} />
                </div>

                <div className="bangdieukhien-charts">
                    <Featured />
                    <Chart3 title="Lịch sử giao dịch trong năm" aspect={2 / 1} orderCount={orderCount} balanceCount={balanceCount} />
                </div>
                <div className="bangdieukhien-listContainer">
                    <div className="bangdieukhien-listTitle">Giao dịch mới nhất</div>
                    <Table />
                </div>

            </div>
        </div>
    )
}

export default Bangdieukhien;