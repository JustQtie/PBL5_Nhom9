import "./thongkegiaodich.scss"
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import WidgetThongKe from "../../components/widgets/WidgetThongKe";
import Chart3 from "../../components/chart3_thongke/Chart3";
import Table from "../../components/table/Table";
import React, { useState, useEffect } from "react";
import { toast } from "react-toastify";
import { getAllGiaoDichThanhCong } from "../../services/apiServices";

const Thongkegiaodich = () => {

    const [orderCount, setOrderCount] = useState(0);
    const [balanceCount, setBalanceCount] = useState(0);

    useEffect(() => {

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

        fetchGiaoDichCount()

    }, []);


    return (
        <div className="thongkegiaodich">
            <Sidebar />
            <div className="thongkegiaodichContainer">
                <Navbar />
                <div className="thongkegiaodichTitle">
                    Thống kê giao dịch
                </div>
                <div className="widgets-thongkegiaodich">
                    <WidgetThongKe type="order" orderCount={orderCount} />
                    <WidgetThongKe type="balance" balanceCount={balanceCount} />
                </div>

                <div className="charts-thongkegiaodich">
                    <Chart3 title="Lịch sử giao dịch trong năm" aspect={3 / 1} orderCount={orderCount} balanceCount={balanceCount} />
                </div>
                <div className="listContainer-thongkegiaodich">
                    <div className="listTitle-thongkegiaodich">Danh sách giáo trình được mua nhiều nhất</div>
                    <Table />
                </div>

            </div>
        </div>
    );
};

export default Thongkegiaodich;