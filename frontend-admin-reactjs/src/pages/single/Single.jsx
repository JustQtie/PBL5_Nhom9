import "./single.scss"
import Sidebar from "../../components/sidebar/Sidebar"
import Navbar from "../../components/navbar/Navbar"
import Chart from "../../components/chart/Chart"
import Chart2 from "../../components/chart2/Chart2"
// import Chart3 from "../../components/chart3/Chart3"
import Widget from "../../components/widgets/Widget"
import { useLocation } from "react-router-dom";



const Single = () => {
    const location = useLocation();
    const userDetail = location.state?.user;
    if (!userDetail) {
        return <div>No user data available</div>;
    }




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
                                <h1 className="itemTitle">{userDetail.phone_number}</h1>

                                <div className="detailItem">
                                    <span className="itemKey">Trạng thái:</span>
                                    <span className="itemValue">{userDetail.active ? "Hoạt động" : "Cấm hoạt động"}</span>
                                </div>
                                <div className="detailItem">
                                    <span className="itemKey">Số điện thoại:</span>
                                    <span className="itemValue">{userDetail.phone_number}</span>
                                </div>
                                <div className="detailItem">
                                    <span className="itemKey">Giới tính:</span>
                                    <span className="itemValue">{userDetail.gender ? "Nam" : "Nữ"}</span>
                                </div>
                                <div className="detailItem">
                                    <span className="itemKey">Địa chỉ:</span>
                                    <span className="itemValue">{userDetail.address}</span>
                                </div>

                            </div>
                        </div>

                    </div>
                    <div className="right">
                        <Chart aspect={3 / 1} title="Mức chi tiêu của người dùng trong 6 tháng qua" />
                        {/* <Chart aspect={3 / 1} title="Biểu đồ hoạt động của người dùng trong 6 tháng qua" /> */}
                        {/* <Chart aspect={3 / 1} title="Biểu đồ thống kê số lượng giao dịch trong 6 tháng qua" /> */}
                    </div>
                </div>
                <div className="widgets-single">
                    <Widget type="order" />
                    <Widget type="balance" />
                </div>


                <div className="bottom">
                    <Chart2 title="Biểu đồ hoạt động của người dùng trong 6 tháng qua" aspect={3 / 1} />
                </div>
            </div>
        </div>
    );
};

export default Single;