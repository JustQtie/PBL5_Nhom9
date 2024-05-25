import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { FcPlus } from 'react-icons/fc';
import { toast } from 'react-toastify';
import { putUpdateUser, postUpdateImageUser } from '../../services/apiServices';
import _ from 'lodash';
import "./modalUpdateUser.scss"

const ModalUpdateUser = (props) => {
    const { show, setShow, userData, fetchUser } = props;

    const handleClose = () => {
        setShow(false);
    };
    const [isLoading, setIsLoading] = useState(false);
    const [id, setId] = useState("");
    const [fullname, setFullName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [address, setAddress] = useState("");
    const [gender, setGender] = useState("");
    const [previewImage, setPreviewImage] = useState("");
    const [image, setImage] = useState("");

    useEffect(() => {
        if (!_.isEmpty(userData)) {
            setId(userData.id);
            setFullName(userData.fullname);
            setPhoneNumber(userData.phone_number);
            setAddress(userData.address);
            setGender(userData.gender);
            if (userData.image) {
                setPreviewImage(`data:image/jpeg;base64,${userData.image}`);
            }
        }
    }, [userData]);

    const handleUploadImage = (event) => {
        if (event.target && event.target.files && event.target.files[0]) {
            setPreviewImage(URL.createObjectURL(event.target.files[0]));
            setImage(event.target.files[0]);
        }
    };

    const handSubmitUpdateUser = async () => {
        const token = localStorage.getItem("token");
        if (!token) {
            toast.error("Token không được tìm thấy");
            return;
        }

        setIsLoading(true); // Bắt đầu hiển thị trạng thái loading

        try {
            // Gọi API cập nhật thông tin người dùng
            let response = await putUpdateUser(id, fullname, phoneNumber, address, gender, token);

            if (response && response.EC === 0) {
                toast.success(response.EM);
            } else {
                toast.error(response.EM);
                return; // Nếu gặp lỗi, dừng luôn
            }

            // Gọi API cập nhật ảnh người dùng
            let response_image = await postUpdateImageUser(id, image, token);

            if (response_image && response_image.EC === 0) {
                toast.success("Cập nhật ảnh thành công");
            } else {
                toast.error("Cập nhật ảnh thất bại");
                return; // Nếu gặp lỗi, dừng luôn
            }

            // Nếu cả hai API đều thành công, đóng modal và gọi fetchUser
            handleClose();
            await fetchUser();
        } catch (error) {
            toast.error("Đã xảy ra lỗi khi cập nhật người dùng", error);
        } finally {
            setIsLoading(false); // Dừng hiển thị trạng thái loading
        }
    };

    return (
        <>
            <Modal className='modal-add-user' backdrop="static" show={show} onHide={handleClose} size='xl'>
                <Modal.Header closeButton>
                    <Modal.Title>Chỉnh sửa thông tin</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form className="row g-3">
                        <div className="col-md-6">
                            <label className="form-label">ID</label>
                            <input type="text" disabled className="form-control" value={id} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Họ và tên</label>
                            <input type="text" className="form-control" value={fullname} onChange={(event) => setFullName(event.target.value)} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Số điện thoại</label>
                            <input type="text" className="form-control" value={phoneNumber} onChange={(event) => setPhoneNumber(event.target.value)} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Địa chỉ</label>
                            <input type="text" className="form-control" value={address} onChange={(event) => setAddress(event.target.value)} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Giới tính</label>
                            <select className="form-select" value={gender} onChange={(event) => setGender(event.target.value)}>
                                <option value="true">Nam</option>
                                <option value="false">Nữ</option>
                            </select>
                        </div>
                        <div className='col-md-12'>
                            <label className="form-label label-upload" htmlFor='labelUpload'><FcPlus /> Upload File Image</label>
                            <input type='file' id='labelUpload' hidden onChange={handleUploadImage} />
                        </div>
                        <div className='col-md-12 img-preview'>
                            {previewImage ? <img src={previewImage} alt="preview" /> : <span>Preview Image</span>}
                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={handSubmitUpdateUser} disabled={isLoading}>
                        {isLoading ? 'Đang cập nhật...' : 'Lưu thông tin'}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default ModalUpdateUser;
