import "./thongkegiaodich.scss"
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import WidgetThongKe from "../../components/widgets/WidgetThongKe";
import Chart3 from "../../components/chart3/Chart3";
import Table from "../../components/table/Table";
const Thongkegiaodich = () => {
    return (
        <div className="thongkegiaodich">
            <Sidebar />
            <div className="thongkegiaodichContainer">
                <Navbar />
                <div className="thongkegiaodichTitle">
                    Thống kê giao dịch
                </div>
                <div className="widgets-thongkegiaodich">
                    <WidgetThongKe type="order" />
                    <WidgetThongKe type="balance" />
                </div>

                <div className="charts-thongkegiaodich">
                    <Chart3 title="Lịch sử giao dịch trong 6 tháng qua" aspect={3 / 1} />
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