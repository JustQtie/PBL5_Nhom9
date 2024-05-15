import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import Widget from "../../components/widgets/Widget";
import "./bangdieukhien.scss"
import Featured from "../../components/featured/Featured";
import Chart3 from "../../components/chart3/Chart3";
import Table from "../../components/table/Table";


const Bangdieukhien = () => {
    return (
        <div className="bangdieukhien">
            <Sidebar />
            <div className="bangdieukhien-Container">
                <Navbar />
                <div className="bangdieukhien-widgets">
                    <Widget type="user" />
                    <Widget type="order" />
                    <Widget type="balance" />
                </div>

                <div className="bangdieukhien-charts">
                    <Featured />
                    <Chart3 title="Lịch sử giao dịch trong 6 tháng qua" aspect={2 / 1} />
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