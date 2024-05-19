import "./qlgiaotrinh.scss"
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import Datatable_qlgt from "../../components/datatable/Datatable_qlgt";
import Table from "../../components/table/Table"

const Qlgiaotrinh = () => {
    return (
        <div className="list-qlgiaotrinh">
            <Sidebar />
            <div className="listContainer-qlgiaotrinh">
                <Navbar />
                <Datatable_qlgt />
                {/* <Table /> */}
            </div>
        </div>
    );
};

export default Qlgiaotrinh;