import "./single.scss"
import Sidebar from "../../components/sidebar/Sidebar"
import Navbar from "../../components/navbar/Navbar"
// import Chart from "../../components/chart/Chart"
// import Chart from "../../components/chart2/Chart2"
import Chart from "../../components/chart3/Chart3"
import List from "../../components/table/Table"


const Single = () => {
    return (
        <div className="single">
            <Sidebar />
            <div className="singleContainer">
                <Navbar />
                <div className="top">
                    <div className="left">
                        <div className="editButton">Chỉnh sửa</div>
                        <h1 className="title">Thông tin</h1>
                        <div className="item">
                            <img src="https://i.imgur.com/1nORATT.png" alt="" className="itemImg" />
                            <div className="details">
                                <h1 className="itemTitle">Hồ Văn Thảo</h1>
                                <div className="detailItem">
                                    <span className="itemKey">Email:</span>
                                    <span className="itemValue">hovanthao0611@gmail.com</span>
                                </div>
                                <div className="detailItem">
                                    <span className="itemKey">Số điện thoại:</span>
                                    <span className="itemValue">0369276372</span>
                                </div>
                                <div className="detailItem">
                                    <span className="itemKey">Địa chỉ:</span>
                                    <span className="itemValue">Gia lai</span>
                                </div>

                            </div>
                        </div>

                    </div>
                    <div className="right">
                        {/* <Chart aspect={3 / 1} title="Mức chi tiêu của người dùng trong 6 tháng qua" /> */}
                        {/* <Chart aspect={3 / 1} title="Biểu đồ hoạt động của người dùng trong 6 tháng qua" /> */}
                        <Chart aspect={3 / 1} title="Biểu đồ thống kê số lượng giao dịch trong 6 tháng qua" />
                    </div>
                </div>
                <div className="bottom">
                    <h1 className="title">Các giao dịch gần đây</h1>
                    <List />
                </div>
            </div>
        </div>
    );
};

export default Single;