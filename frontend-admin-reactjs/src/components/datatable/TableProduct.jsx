import { useEffect, useState } from "react";
import { getAllProducts } from "../../services/apiServices";
import { toast } from "react-toastify";
import { Link, useNavigate } from "react-router-dom";




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

            console.log("check>>> res", res);

            if (res.EC === 0) {
                setListProducts(res.userResponseList); ///cho nay can sua

            }
        } catch (error) {
            toast.error("Error fetching list of products", error);
        }
    }

    const handleBtnView = (dt) => {
        navigate(`/qlgiaotrinh/${dt.id}`, { state: { product: dt } })
    }


    return (
        <>
            <table className="table table-light table-striped table-hover table-bordered">
                <thead>
                    <tr>
                        <th scope="col">STT</th>
                        <th scope="col">Mã giáo trình</th>
                        <th scope="col">Tiêu đề</th>
                        <th scope="col">Loại</th>
                        <th scope="col">Giá</th>
                        <th scope="col">Người bán</th>
                        <th scope="col">Trạng thái</th>
                        <th scope="col">Action</th>
                    </tr>
                </thead>
                <tbody>
                    {listProducts && listProducts.length > 0 &&
                        listProducts.map((item, index) => {
                            return (
                                <tr key={`table-users-${index}`}>
                                    <td>{index + 1}</td>
                                    <td>{item.phone_number}</td>
                                    <td>{item.address}</td>
                                    <td>{item.gender ? "Nam" : "Nữ"}</td>
                                    <td>{item.phone_number}</td>
                                    <td>{item.active ? "Hoạt động" : "Cấm hoạt động"}</td>
                                    <td>
                                        <button className="btn btn-success" onClick={() => handleBtnView(item)} >View</button>
                                    </td>

                                </tr>
                            )
                        })
                    }
                    {listProducts && listProducts.length === 0 && <tr><td colSpan={'8'}>Not found data</td></tr>}
                </tbody>
            </table>



        </>
    )
}

export default TableProduct;