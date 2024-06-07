import "./single_giaotrinh.scss"
import Sidebar from "../../components/sidebar/Sidebar"
import Navbar from "../../components/navbar/Navbar"
import { useLocation } from "react-router-dom";
import ModalDeleteProduct from "../../components/modal/ModalDeleteProduct";
import { useState, useEffect } from "react";

const Single = () => {

    const [showModalDeleteProduct, setShowModalDelete] = useState(false);
    const [dataDelete, setDataDelete] = useState({});

    const location = useLocation();
    const productDetail = location.state?.product;

    useEffect(() => {
        if (productDetail) {
            setDataDelete(productDetail);
        }
    }, [productDetail]);

    if (!productDetail) {
        return <div>No user data available</div>;
    }



    const handClickBtnDelete = () => {
        setShowModalDelete(true);
    }

    return (
        <div className="single-giaotrinh">
            <Sidebar />
            <div className="single-giaotrinhContainer">
                <Navbar />
                <div className="single-giaotrinhtop">
                    Thông tin chi tiết giáo trình
                </div>
                <div className="single-giaotrinhbottom">
                    <div className="single-giaotrinhleft">
                        <img src={productDetail.thumbnail ? `${process.env.REACT_APP_API_URL}api/v1/products/images/${productDetail.thumbnail}` : "https://i.imgur.com/2zLfMh6.jpeg"} alt="products" className="single-giaotrinhimg" />
                    </div>
                    <div className="single-giaotrinhright">
                        <div className="single-giaotrinh-fill">
                            <div className="col-md-5">
                                <label className="form-label">Mã giáo trình</label>
                                <div className="form-control">{productDetail.id}</div>
                            </div>
                            <div className="col-md-5">
                                <label className="form-label">Tiêu đề</label>
                                <div className="form-control">{productDetail.name}</div>
                            </div>
                        </div>

                        <div className="single-giaotrinh-fill">
                            <div className="col-md-5">
                                <label className="form-label">Loại</label>
                                <div className="form-control">Giáo trình</div>
                            </div>
                            <div className="col-md-5">
                                <label className="form-label">Trạng thái</label>
                                <div className="form-control">{productDetail.status}</div>
                            </div>
                        </div>

                        <div className="single-giaotrinh-fill">
                            <div className="col-md-5">
                                <label className="form-label">Giá bán</label>
                                <div className="form-control">{productDetail.price}</div>
                            </div>
                            <div className="col-md-5">
                                <label className="form-label">Người bán</label>
                                <div className="form-control">Hồ Văn Thảo</div>
                            </div>
                        </div>

                        <div className="card-body">
                            <label className="form-label">Mô tả</label>
                            <p className="card-text">
                                {productDetail.description}
                            </p>
                        </div>

                        <div className="d-flex justify-content-center">
                            <button onClick={handClickBtnDelete}>
                                Xóa bài
                            </button>
                        </div>

                    </div>
                </div>
            </div>

            <ModalDeleteProduct
                show={showModalDeleteProduct}
                setShow={setShowModalDelete}
                dataDelete={dataDelete}
            />
        </div>
    )
};

export default Single;