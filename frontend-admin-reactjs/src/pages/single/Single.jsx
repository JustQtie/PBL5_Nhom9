import "./single.scss"
import Sidebar from "../../components/sidebar/Sidebar"
import Navbar from "../../components/navbar/Navbar"
import Chart from "../../components/chart_single/Chart"
import Chart2 from "../../components/chart2__single/Chart2"
import WidgetSingle from "../../components/widgets/WidgetSingle"
import { useLocation } from "react-router-dom";
import { getGiaoDichThanhCongById } from "../../services/apiServices"
import { toast } from "react-toastify";
import { useState, useEffect } from "react"


const Single = () => {
    const location = useLocation();
    const [dataChuyenDi, setDatachuyenDi] = useState();

    useEffect(() => {
        fetchData();
    }, []);




    const userDetail = location.state?.user;
    if (!userDetail) {
        return <div>No user data available</div>;
    }

    const fetchData = async () => {
        try {
            // Lấy token từ localStorage
            const token = localStorage.getItem("token");


            // Nếu không có token, không gọi API và kết thúc hàm
            if (!token) {
                toast.error("Token not found");
                return;
            }

            // Gọi API với token được đính kèm trong header Authorization

            const res = await getGiaoDichThanhCongById(userDetail.id, token);

            if (res && res.EC === "0") {
                setDatachuyenDi(res);
            }
        } catch (error) {
            toast.error("Error fetching list of users", error);
        }
    };




    return (

        <div className="single">
            <Sidebar />
            <div className="singleContainer">
                <Navbar />
                <div className="top">
                    <div className="left">
                        {/* <div className="editButton">Chỉnh sửa</div> */}
                        <h1 className="title">Thông tin</h1>
                        <div className="item">
                            <img src={userDetail.thumbnail ? `${process.env.REACT_APP_API_URL}api/v1/users/images/${userDetail.thumbnail}` : "https://i.imgur.com/2zLfMh6.jpeg"} alt="users" className="itemImg" />

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
                        <Chart aspect={3 / 1} title="Mức chi tiêu của người dùng trong năm" dataChuyenDi={dataChuyenDi} />
                    </div>
                </div>
                <div className="widgets-single">
                    <WidgetSingle type="chitieu" dataChuyenDi={dataChuyenDi} />
                    <WidgetSingle type="thanhcong" dataChuyenDi={dataChuyenDi} />
                    <WidgetSingle type="thatbai" dataChuyenDi={dataChuyenDi} />
                </div>

                <div className="bottom">
                    <Chart2 title="Biểu đồ hoạt động của người dùng trong năm" aspect={3 / 1} dataChuyenDi={dataChuyenDi} />
                </div>
            </div>
        </div>
    );
};

export default Single;