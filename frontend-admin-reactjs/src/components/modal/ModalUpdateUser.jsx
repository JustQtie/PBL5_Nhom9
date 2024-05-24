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

        console.log("check", id, fullname, phoneNumber, address, gender, token);

        let response = await putUpdateUser(id, fullname, phoneNumber, address, gender, token);

        if (response && response.EC === 0) { // Điều chỉnh cho cấu trúc dữ liệu đúng
            toast.success(response.EM); // Đảm bảo bạn đang truy cập phản hồi đúng cách
            handleClose();
            await fetchUser();
        } else {
            toast.error(response.EM); // Đảm bảo bạn đang truy cập thông báo lỗi đúng cách
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
                    <Button variant="primary" onClick={handSubmitUpdateUser}>
                        Lưu thông tin
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default ModalUpdateUser;
