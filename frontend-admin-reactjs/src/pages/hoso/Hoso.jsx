import "./hoso.scss";
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import { useEffect, useState } from "react";
import ModalUpdateUser from "../../components/modal/ModalUpdateUser";
import { getUserById } from "../../services/apiServices";
import { toast } from 'react-toastify';

const Hoso = () => {
    const [showModalUpdateuser, setShowModalUpdate] = useState(false);
    const [userData, setUserData] = useState({});
    const [userId, setUserId] = useState("");

    const [id, setId] = useState("");
    const [fullname, setFullName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [address, setAddress] = useState("");
    const [gender, setGender] = useState("");
    const [previewImage, setPreviewImage] = useState("");

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            toast.error("Token không được tìm thấy");
            return;
        }

        const userDataString = localStorage.getItem("userData");
        if (userDataString) {
            const userDataObject = JSON.parse(userDataString);
            setUserId(userDataObject.id);
        }
    }, []);

    useEffect(() => {
        if (userId) {
            fetchUser();
        }
    }, [userId]);

    const fetchUser = async () => {
        const token = localStorage.getItem("token");
        try {
            const res = await getUserById(userId, token);
            if (res.EC === 0) {
                const user = res;
                setUserData(user);
                setId(user.id);
                setFullName(user.fullname);
                setPhoneNumber(user.phone_number);
                setAddress(user.address);
                setGender(user.gender);
                setPreviewImage(user.image ? `data:image/jpeg;base64,${user.image}` : "");
            } else {
                toast.error("Không thể lấy dữ liệu người dùng");
            }
        } catch (error) {
            console.error("Fetch user error:", error);
            toast.error("Có lỗi xảy ra khi lấy dữ liệu người dùng");
        }
    }

    const handClickBtnUpdate = () => {
        setShowModalUpdate(true);
    }

    return (
        <div className="hoso-list">
            <Sidebar />
            <div className="hoso-listContainer">
                <Navbar />
                <div className="hoso-top">
                    <h1>Hồ sơ cá nhân</h1>
                </div>
                <div className="hoso-bottom">
                    <div className="hoso-left">
                        <img className="hoso-img" src={previewImage || "https://i.imgur.com/2zLfMh6.jpeg"} alt="profile" />
                    </div>
                    <div className="hoso-right">
                        <div className="col-md-6">
                            <label className="form-label">ID</label>
                            <input type="text" disabled className="form-control" value={id} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Họ và tên</label>
                            <input type="text" disabled className="form-control" value={fullname} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Số điện thoại</label>
                            <input type="text" disabled className="form-control" value={phoneNumber} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Địa chỉ</label>
                            <input type="text" disabled className="form-control" value={address} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Giới tính</label>
                            <input type="text" disabled className="form-control" value={gender ? "Nam" : "Nữ"} />
                        </div>
                        <div className="col-md-6 d-flex justify-content-center">
                            <button onClick={handClickBtnUpdate}>
                                Chỉnh sửa
                            </button>
                        </div>
                        <ModalUpdateUser
                            show={showModalUpdateuser}
                            setShow={setShowModalUpdate}
                            userData={userData}
                            fetchUser={fetchUser}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Hoso;
