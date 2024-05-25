import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import { toast } from 'react-toastify';
import Spinner from 'react-bootstrap/Spinner';
import { putChangePassword } from '../../services/apiServices';


const ModalDoiMatKhau = (props) => {
    const { show, setShow, userData } = props;

    const handleClose = () => {
        if (!isLoading) {
            setShow(false);
            setOldPassword("");
            setNewPassword("");
            setConfirmPassword("");
        }
    };

    const [isLoading, setIsLoading] = useState(false);
    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const handleChangePassword = async () => {
        if (newPassword !== confirmPassword) {
            toast.error("Mật khẩu mới và xác nhận mật khẩu không khớp");
            return;
        }

        const token = localStorage.getItem("token");
        if (!token) {
            toast.error("Token không được tìm thấy");
            return;
        }

        setIsLoading(true);

        try {
            console.log("check users doi mat khau: ", userData.id, oldPassword, newPassword, token);
            let response = await putChangePassword(userData.id, oldPassword, newPassword, token);

            if (response && response.EC === 0) {
                toast.success("Đổi mật khẩu thành công");
                handleClose();
            } else {
                toast.error(response.EM);
            }
        } catch (error) {
            toast.error("Đã xảy ra lỗi khi đổi mật khẩu", error);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <>
            <Modal className='modal-change-password' backdrop="static" show={show} onHide={handleClose} size='md'>
                <Modal.Header closeButton={!isLoading}>
                    <Modal.Title>Đổi mật khẩu</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Mật khẩu cũ</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Nhập mật khẩu cũ"
                                value={oldPassword}
                                onChange={(event) => setOldPassword(event.target.value)}
                                disabled={isLoading}
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Mật khẩu mới</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Nhập mật khẩu mới"
                                value={newPassword}
                                onChange={(event) => setNewPassword(event.target.value)}
                                disabled={isLoading}
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Xác nhận mật khẩu mới</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Xác nhận mật khẩu mới"
                                value={confirmPassword}
                                onChange={(event) => setConfirmPassword(event.target.value)}
                                disabled={isLoading}
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose} disabled={isLoading}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={handleChangePassword} disabled={isLoading}>
                        {isLoading ? (
                            <>
                                <Spinner
                                    as="span"
                                    animation="border"
                                    size="sm"
                                    role="status"
                                    aria-hidden="true"
                                />
                                <span className="visually-hidden">Đang cập nhật...</span>
                            </>
                        ) : (
                            'Đổi mật khẩu'
                        )}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default ModalDoiMatKhau;
