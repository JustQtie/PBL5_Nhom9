import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { deleteProductById, deleteProductThumbnailsById } from '../../services/apiServices';
import { toast } from 'react-toastify';
import { useNavigate } from "react-router-dom";


const ModalDeleteProduct = (props) => {
    const { show, setShow, dataDelete } = props;

    const navigate = useNavigate();

    const handleClose = () => setShow(false);

    const handleSubmitDelete = async () => {
        // call API

        const token = localStorage.getItem("token");
        if (!token) {
            toast.error("Token không được tìm thấy");
            return;
        }


        if (dataDelete.thumbnail) {
            let data = await deleteProductThumbnailsById(dataDelete.id, token);

            if (data) {
                try {
                    let data_2 = await deleteProductById(dataDelete.id, token);
                    toast.success(`${data_2} : ${dataDelete.name}`);
                    handleClose();
                    navigate(`/qlgiaotrinh`);
                } catch (error) {
                    toast.error(error);
                    handleClose();
                    navigate(`/qlgiaotrinh/${dataDelete.id}`);
                }


            }
        } else {
            try {
                let data_2 = await deleteProductById(dataDelete.id, token);
                toast.success(`${data_2} : ${dataDelete.name}`);
                handleClose();
                navigate(`/qlgiaotrinh`);
            } catch (error) {
                toast.error(error);
                handleClose();
                navigate(`/qlgiaotrinh/${dataDelete.id}`);
            }

        }

    }


    return (
        <>

            <Modal
                show={show}
                onHide={handleClose}
                backdrop="static"
            >
                <Modal.Header closeButton>
                    <Modal.Title>Confirm delete the Product</Modal.Title>
                </Modal.Header>
                <Modal.Body>Bạn có chắc muốn xóa sản phẩm {dataDelete && dataDelete.name ? dataDelete.name : ""}?</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        cancel
                    </Button>
                    <Button variant="primary" onClick={handleSubmitDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalDeleteProduct;