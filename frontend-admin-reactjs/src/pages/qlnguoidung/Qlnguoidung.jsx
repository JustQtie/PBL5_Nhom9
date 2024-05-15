import "./qlnguoidung.scss"
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import Datatable_qlnd from "../../components/datatable/Datatable_qlnd";


const Qlnguoidung = () => {
    return (
        <div className="list-qlnguoidung">
            <Sidebar />
            <div className="listContainer-qlnguoidung">
                <Navbar />
                <Datatable_qlnd />
            </div>
        </div>
    );
};

export default Qlnguoidung;