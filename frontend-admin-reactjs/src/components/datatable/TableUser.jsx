import { useEffect, useState } from "react";
import { getAllUsers } from "../../services/apiServices";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import ModalBanUser from "../modal/ModalBanUser";
import ModalUnbanUser from "../modal/ModalUnbanUser";
import "./tableUser.scss";

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

            console.log(res);

            if (res && res.EC === 0) {
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

            <TableContainer component={Paper} className="table-user">
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell className="tableCell">STT</TableCell>
                            <TableCell className="tableCell">Họ và tên</TableCell>
                            <TableCell className="tableCell">Địa chỉ</TableCell>
                            <TableCell className="tableCell">Giới tính</TableCell>
                            <TableCell className="tableCell">Số điện thoại</TableCell>
                            <TableCell className="tableCell">Trạng thái</TableCell>
                            <TableCell className="tableCell">Thao tác</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {listUsers && listUsers.length > 0 &&
                            listUsers.map((item, index) => {
                                return (
                                    <TableRow key={`table-users-${index}`}>
                                        <TableCell className="tableCell">{index + 1}</TableCell>
                                        <TableCell className="tableCell">
                                            <div className="cellWrapper-user">
                                                <img src={item.thumbnail ? `${process.env.REACT_APP_API_URL}api/v1/users/images/${item.thumbnail}` : "https://i.imgur.com/2zLfMh6.jpeg"} alt="User" className="image" />
                                                {item.fullname}
                                            </div>
                                        </TableCell>
                                        <TableCell className="tableCell">{item.address}</TableCell>
                                        <TableCell className="tableCell">{item.gender ? "Nam" : "Nữ"}</TableCell>
                                        <TableCell className="tableCell">{item.phone_number}</TableCell>
                                        <TableCell className="tableCell">
                                            <span className={`status ${item.active}`}>{item.active ? "Hoạt động" : "Cấm hoạt động"}</span>
                                        </TableCell>
                                        <TableCell className="tableCell">
                                            <button className="btn btn-success btn-sm" onClick={() => handleBtnSuccess(item)} >View</button>
                                            {item.active ? (
                                                <button className="btn btn-danger btn-sm mx-3" onClick={() => handleBtnBan(item)}>Ban</button>
                                            ) : (
                                                <button className="btn btn-warning btn-sm mx-3" onClick={() => handleBtnUnban(item)}>Unban</button>
                                            )}

                                        </TableCell>
                                    </TableRow>
                                )
                            })
                        }
                        {listUsers && listUsers.length === 0 && <TableRow><TableCell colSpan={'7'}>Not found data</TableCell></TableRow>}
                    </TableBody>
                </Table>
            </TableContainer>



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