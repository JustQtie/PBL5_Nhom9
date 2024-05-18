import "./single_giaotrinh.scss"
import Sidebar from "../../components/sidebar/Sidebar"
import Navbar from "../../components/navbar/Navbar"
import { useLocation } from "react-router-dom";

const Single = () => {


    const location = useLocation();
    const productDetail = location.state?.product;
    if (!productDetail) {
        return <div>No user data available</div>;
    }

    return (
        <div className="single-giaotrinh">
            <Sidebar />
            <div className="single-giaotrinhContainer">
                <Navbar />
                <div className="single-giaotrinhtop">
                    <h1>Thông tin chi tiết giáo trình</h1>
                </div>
                <div className="single-giaotrinhbottom">
                    <div className="single-giaotrinhleft">
                        <img
                            src="https://i.imgur.com/2zLfMh6.jpeg"
                            alt=""
                            className="single-giaotrinhimg"
                        />
                    </div>
                    <div className="single-giaotrinhright">
                        <div className="single-giaotrinh-fill">
                            <div className="col-md-5">
                                <label className="form-label">Mã giáo trình</label>
                                <div className="form-control">22222</div>
                            </div>
                            <div className="col-md-5">
                                <label className="form-label">Tiêu đề</label>
                                <div className="form-control">Giáo trình giải tích 1</div>
                            </div>
                        </div>

                        <div className="single-giaotrinh-fill">
                            <div className="col-md-5">
                                <label className="form-label">Loại</label>
                                <div className="form-control">Giáo trình</div>
                            </div>
                            <div className="col-md-5">
                                <label className="form-label">Trạng thái</label>
                                <div className="form-control">Còn mới</div>
                            </div>
                        </div>

                        <div className="single-giaotrinh-fill">
                            <div className="col-md-5">
                                <label className="form-label">Giá bán</label>
                                <div className="form-control">5$</div>
                            </div>
                            <div className="col-md-5">
                                <label className="form-label">Người bán</label>
                                <div className="form-control">Hồ Văn Thảo</div>
                            </div>
                        </div>

                        <div className="card-body">
                            <label className="form-label">Mô tả</label>
                            <p className="card-text">
                                Đây là mô tả của bạn. Nội dung này sẽ tự động điều chỉnh kích thước và tự xuống dòng nếu cần thiết để hiển thị đúng.d
                                Đây là mô tả của bạn. Nội dung này sẽ tự động điều chỉnh kích thước và tự xuống dòng nếu cần thiết để hiển thị đúng.d
                                Đây là mô tả của bạn. Nội dung này sẽ tự động điều chỉnh kích thước và tự xuống dòng nếu cần thiết để hiển thị đúng.d
                                Đây là mô tả của bạn. Nội dung này sẽ tự động điều chỉnh kích thước và tự xuống dòng nếu cần thiết để hiển thị đúng.d

                            </p>
                        </div>



                        <div className="d-flex justify-content-center">
                            <button>Xóa bài</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
};

export default Single;