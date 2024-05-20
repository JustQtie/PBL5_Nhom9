import { useEffect, useState } from "react";
import { getAllProducts } from "../../services/apiServices";
import { toast } from "react-toastify";
import { Link, useNavigate } from "react-router-dom";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import "./tableProduct.scss";


const TableProduct = () => {
    const [listProducts, setListProducts] = useState([]);

    const navigate = useNavigate();

    useEffect(() => {
        fetchListProducts();
    }, []);

    const fetchListProducts = async () => {

        try {
            // Lấy token từ localStorage
            const token = localStorage.getItem("token");

            // Nếu không có token, không gọi API và kết thúc hàm
            if (!token) {
                toast.error("Token not found");
                return;
            }

            // Gọi API với token được đính kèm trong header Authorization

            const res = await getAllProducts(token);

            // console.log("API Response:", res.EC);

            if (res && res.EC === "0") {
                setListProducts(res.productResponseList);

            }
            else {
                toast.error("Error fetching list of products");
            }
        } catch (error) {
            toast.error("Error fetching list of products", error);
            console.error(error);
        }
    }

    const handleBtnView = (dt) => {
        navigate(`/qlgiaotrinh/${dt.id}`, { state: { product: dt } })
    }


    return (
        <>

            <TableContainer component={Paper} className="table-products">
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell className="tableCell">STT</TableCell>
                            <TableCell className="tableCell">Mã giáo trình</TableCell>
                            <TableCell className="tableCell">Tiêu đề</TableCell>
                            <TableCell className="tableCell">Giá</TableCell>
                            <TableCell className="tableCell">Trạng thái</TableCell>
                            <TableCell className="tableCell">Số lượng</TableCell>
                            <TableCell className="tableCell">Action</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {listProducts && listProducts.length > 0 &&
                            listProducts.map((item, index) => {
                                return (
                                    <TableRow key={`table-products-${index}`}>
                                        <TableCell className="tableCell">{index + 1}</TableCell>
                                        <TableCell className="tableCell">{item.id}</TableCell>
                                        <TableCell className="tableCell">
                                            <div className="cellWrapper-products">
                                                <img src={item.thumbnail ? `${process.env.REACT_APP_API_URL}api/v1/users/images/${item.thumbnail}` : "https://i.imgur.com/2zLfMh6.jpeg"} alt="User" className="image" />
                                                {/* <img src={item.thumbnail ? item.thumbnail : "https://i.imgur.com/2zLfMh6.jpeg"} alt="User" className="image" /> */}
                                                {item.name}
                                            </div>
                                        </TableCell>
                                        <TableCell className="tableCell">{item.price}</TableCell>
                                        <TableCell className="tableCell">{item.status}</TableCell>
                                        <TableCell className="tableCell">{item.quantity}</TableCell>
                                        <TableCell className="tableCell">
                                            <button className="btn btn-success btn-sm" onClick={() => handleBtnView(item)} >View</button>
                                        </TableCell>
                                    </TableRow>
                                )
                            })
                        }
                        {listProducts && listProducts.length === 0 && <TableRow><TableCell colSpan={'7'}>Not found data</TableCell></TableRow>}
                    </TableBody>
                </Table>
            </TableContainer>



        </>
    )
}

export default TableProduct;