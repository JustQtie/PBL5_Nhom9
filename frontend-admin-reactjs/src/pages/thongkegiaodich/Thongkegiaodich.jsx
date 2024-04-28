import "./thongkegiaodich.scss"
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";


const Thongkegiaodich = () => {
    return (
        <div className="list">
            <Sidebar />
            <div className="listContainer">
                <Navbar />
                <p>thống kê giao dịch</p>
            </div>
        </div>
    );
};

export default Thongkegiaodich;