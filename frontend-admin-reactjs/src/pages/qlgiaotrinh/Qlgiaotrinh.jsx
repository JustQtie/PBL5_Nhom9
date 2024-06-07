import "./qlgiaotrinh.scss"
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import Datatable_qlgt from "../../components/datatable/Datatable_qlgt";

const Qlgiaotrinh = () => {
    return (
        <div className="list-qlgiaotrinh">
            <Sidebar />
            <div className="listContainer-qlgiaotrinh">
                <Navbar />
                <Datatable_qlgt />

            </div>
        </div>
    );
};

export default Qlgiaotrinh;