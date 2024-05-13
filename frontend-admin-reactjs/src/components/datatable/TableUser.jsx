import { useEffect, useState } from "react";
import { getAllUsers } from "../../services/apiServices";
import { toast } from "react-toastify";


const TableUser = () => {
    const [listUsers, setListUsers] = useState([]);

    useEffect(() => {
        fetchListUsers();
    }, []);

    const fetchListUsers = async () => {
        console.log("thao", "1");
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

    return (
        <>
            <table className="table table-success table-striped table-hover table-bordered">
                <thead>
                    <tr>
                        <th scope="col">STT</th>
                        <th scope="col">Username</th>
                        <th scope="col">Address</th>
                        <th scope="col">Gender</th>
                        <th scope="col">Phone Number</th>
                        <th scope="col">Action</th>
                    </tr>
                </thead>
                <tbody>
                    {listUsers && listUsers.length > 0 &&
                        listUsers.map((item, index) => {
                            return (
                                <tr key={`table-users-${index}`}>
                                    <td>{index + 1}</td>
                                    <td>{item.username}</td>
                                    <td>{item.address}</td>
                                    <td>{item.gender ? "Nam" : "Nữ"}</td>
                                    <td>{item.phone_number}</td>
                                    <td>
                                        <button className="btn btn-success">View</button>
                                        <button className="btn btn-danger mx-3">Ban</button>
                                    </td>
                                </tr>
                            )
                        })
                    }
                    {listUsers && listUsers.length === 0 && <tr><td colSpan={'6'}>Not found data</td></tr>}
                </tbody>
            </table>
        </>
    )
}

export default TableUser;