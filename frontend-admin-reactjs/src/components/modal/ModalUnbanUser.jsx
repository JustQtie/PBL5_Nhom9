import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { putUnbanUser } from '../../services/apiServices';
import { toast } from 'react-toastify';

const ModalUnbanUser = (props) => {
    const { show, setShow, dataUnban } = props;

    const handleClose = () => setShow(false);

    const handleSubmitUnban = async () => {

        const token = localStorage.getItem("token");

        // Nếu không có token, không gọi API và kết thúc hàm
        if (!token) {
            toast.error("Token not found");
            return;
        }
        // call API

        let data = await putUnbanUser(dataUnban.id, token);
        console.log(">>> check res ", data);
        if (data && data.EC === 0) {
            toast.success(data.EM);
            handleClose();
            await props.fetchListUsers();
        }
        if (data && data.EC !== 0) {
            toast.error(data.EM);
            handleClose();
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
                    <Modal.Title>Confirm Ban the User</Modal.Title>
                </Modal.Header>
                <Modal.Body>Bạn có chắc muốn bỏ cấm hoạt động tài khoản: {dataUnban && dataUnban.phone_number ? dataUnban.phone_number : ""} </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        cancel
                    </Button>
                    <Button variant="primary" onClick={handleSubmitUnban}>
                        Unban
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalUnbanUser;