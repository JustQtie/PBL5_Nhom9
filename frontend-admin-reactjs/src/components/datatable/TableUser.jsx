import { useEffect, useState } from "react";
import { getAllUsers } from "../../services/apiServices";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import ModalBanUser from "../modal/ModalBanUser";
import ModalUnbanUser from "../modal/ModalUnbanUser";


const TableUser = () => {
    const [listUsers, setListUsers] = useState([]);
    const [showModalBanuser, setShowModalBan] = useState(false);
    const [dataBan, setDataBan] = useState({});
    const [showModalUnbanuser, setShowModalUnban] = useState(false);
    const [dataUnban, setDataUnban] = useState({});
    const navigate = useNavigate();

    useEffect(() => {
        fetchListUsers();
    }, []);

    const fetchListUsers = async () => {

        try {
            // Lấy token từ localStorage
            const token = localStorage.getItem("token");

            // Nếu không có token, không gọi API và kết thúc hàm
            if (!token) {
                toast.error("Token not found");
                return;
            }

            // Gọi API với token được đính kèm trong header Authorization

            const res = await getAllUsers(token);

            console.log("check>>> res", res);

            if (res.EC === 0) {
                setListUsers(res.userResponseList);
            }
        } catch (error) {
            toast.error("Error fetching list of users", error);
        }
    }

    const handleBtnSuccess = (dt) => {
        navigate(`/qlnguoidung/${dt.id}`, { state: { user: dt } })
    }

    const handleBtnBan = (dt) => {
        setShowModalBan(true);
        setDataBan(dt);

    }

    const handleBtnUnban = (dt) => {
        setShowModalUnban(true);
        setDataUnban(dt);
    }


    return (
        <>
            <table className="table table-light table-striped table-hover table-bordered">
                <thead>
                    <tr>
                        <th scope="col">STT</th>
                        <th scope="col">Username</th>
                        <th scope="col">Address</th>
                        <th scope="col">Gender</th>
                        <th scope="col">Phone Number</th>
                        <th scope="col">Active</th>
                        <th scope="col">Action</th>
                    </tr>
                </thead>
                <tbody>
                    {listUsers && listUsers.length > 0 &&
                        listUsers.map((item, index) => {
                            return (
                                <tr key={`table-users-${index}`}>
                                    <td>{index + 1}</td>
                                    <td>{item.phone_number}</td>
                                    <td>{item.address}</td>
                                    <td>{item.gender ? "Nam" : "Nữ"}</td>
                                    <td>{item.phone_number}</td>
                                    <td>{item.active ? "Hoạt động" : "Cấm hoạt động"}</td>
                                    <td>
                                        <button className="btn btn-success" onClick={() => handleBtnSuccess(item)} >View</button>
                                        {item.active ? (
                                            <button className="btn btn-danger mx-3" onClick={() => handleBtnBan(item)}>Ban</button>
                                        ) : (
                                            <button className="btn btn-warning mx-3" onClick={() => handleBtnUnban(item)}>Unban</button>
                                        )}
                                    </td>
                                </tr>
                            )
                        })
                    }
                    {listUsers && listUsers.length === 0 && <tr><td colSpan={'7'}>Not found data</td></tr>}
                </tbody>
            </table>

            <ModalBanUser
                show={showModalBanuser}
                setShow={setShowModalBan}
                fetchListUsers={fetchListUsers}
                dataBan={dataBan}
            />

            <ModalUnbanUser
                show={showModalUnbanuser}
                setShow={setShowModalUnban}
                fetchListUsers={fetchListUsers}
                dataUnban={dataUnban}
            />

        </>
    )
}

export default TableUser;