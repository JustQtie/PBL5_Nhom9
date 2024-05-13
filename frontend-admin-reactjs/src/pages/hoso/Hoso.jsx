import "./hoso.scss"
import Sidebar from "../../components/sidebar/Sidebar";
import Navbar from "../../components/navbar/Navbar";
import { useEffect, useState } from "react";
import _ from 'lodash';
import ModalUpdateUser from "../../components/modal/ModalUpdateUser";
import { putUpdateUser } from "../../services/apiServices";

const Hoso = () => {

    const [showModalUpdateuser, setShowModalUpdate] = useState(false);
    // const [dataUpdate, setDataUpdate] = useState({});
    const [userData, setUserData] = useState({});
    const [listUsers, setListUsers] = useState([]);

    useEffect(() => {
        // fetchUsers();
        const userDataString = localStorage.getItem("userData");
        if (userDataString) {
            // const userData = JSON.parse(userDataString);
            setUserData(JSON.parse(userDataString));

        }
    }, []);

    // const fetchUsers = async () => {
    //     let res = await getAllUsers();
    //     if (res.EC === 0) {
    //         setListUsers(res.DT)
    //     }
    // }


    const handClickBtnUpdate = () => {
        setShowModalUpdate(true);
        // setDataUpdate(user);
    }

    const [id, setId] = useState("");
    const [fullname, setFullName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [address, setAddress] = useState("");
    const [gender, setGender] = useState("");
    const [previewImage, setPreviewImage] = useState("");


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
                        {previewImage ? <img className="hoso-img" src={previewImage} /> : <img className="hoso-img" src={"https://i.imgur.com/2zLfMh6.jpeg"} />}
                    </div>
                    <div className="hoso-right">

                        <div className="col-md-6">
                            <label className="form-label">ID</label>
                            <input type="text" disabled className="form-control" value={userData.id} onChange={(event) => setId(event.target.value)} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Full Name</label>
                            <input type="text" disabled className="form-control" value={userData.fullname} onChange={(event) => setFullName(event.target.value)} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Phone Number</label>
                            <input type="text" disabled className="form-control" value={userData.phoneNumber} onChange={(event) => setPhoneNumber(event.target.value)} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Address</label>
                            <input type="text" disabled className="form-control" value={userData.address} onChange={(event) => setAddress(event.target.value)} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Gender</label>
                            <input type="text" disabled className="form-control" value={userData.gender ? "Nam" : "Nữ"} onChange={(event) => setGender(event.target.value)} />
                        </div>
                        <div className="col-md-6 d-flex justify-content-center">
                            <button

                                onClick={() => {
                                    handClickBtnUpdate();
                                }}
                            >
                                Chỉnh sửa
                            </button>
                        </div>
                        <ModalUpdateUser
                            show={showModalUpdateuser}
                            setShow={setShowModalUpdate}
                            userData={userData}
                        // fetchUsers={fetchUsers}


                        />

                    </div>
                </div>
            </div>
        </div>
    );
};

export default Hoso;