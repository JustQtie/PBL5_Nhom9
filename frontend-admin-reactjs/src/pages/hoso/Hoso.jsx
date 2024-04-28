import "./hoso.scss"
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";


const Hoso = () => {
    return (
        <div className="list">
            <Sidebar />
            <div className="listContainer">
                <Navbar />
                <p>Hồ sơ</p>
            </div>
        </div>
    );
};

export default Hoso;