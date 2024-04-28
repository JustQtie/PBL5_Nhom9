import "./qlgiaotrinh.scss"
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";


const Qlgiaotrinh = () => {
    return (
        <div className="list">
            <Sidebar />
            <div className="listContainer">
                <Navbar />
                <p>Quản lý giáo trình</p>
            </div>
        </div>
    );
};

export default Qlgiaotrinh;