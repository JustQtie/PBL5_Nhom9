import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { FcPlus } from 'react-icons/fc';
import { toast } from 'react-toastify';
import Spinner from 'react-bootstrap/Spinner';
import { putUpdateUser, postUpdateImageUser } from '../../services/apiServices';
import _ from 'lodash';
import "./modalUpdateUser.scss"

const ModalUpdateUser = (props) => {
    const { show, setShow, userData, fetchUser } = props;

    const handleClose = () => {
        if (!isLoading) {
            setShow(false);
        }
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
            return;
        }

        // Validate required fields
        if (!fullname && !phoneNumber && !address) {
            toast.error("Xin vui lòng nhập thông tin!!");
            return;
        }
        if (!fullname) {
            toast.error("Họ và tên không được để trống!");
            return;
        }
        if (!phoneNumber) {
            toast.error("Số điện thoại không được để trống!");
            return;
        }
        if (!address) {
            toast.error("Địa chỉ không được để trống!");
            return;
        }
        if (!gender) {
            toast.error("Giới tính không được để trống!");
            return;
        }

        setIsLoading(true); // Bắt đầu hiển thị trạng thái loading

        try {
            // Gọi API cập nhật thông tin người dùng
            let response = await putUpdateUser(id, fullname, phoneNumber, address, gender, token);

            if (response && response.EC === 0) {
                let response_image = await postUpdateImageUser(id, image, token);

                if (response_image && response_image.EC === 0) {
                    toast.success("Cập nhật thông tin thành công!");
                } else {
                    toast.error("Cập nhật ảnh thất bại!");
                    return; // Nếu gặp lỗi, dừng luôn
                }
            } else {
                toast.error("Lỗi khi cập nhật thông tin!");
                return; // Nếu gặp lỗi, dừng luôn
            }



            // Nếu cả hai API đều thành công, đóng modal và gọi fetchUser
            await fetchUser();
            handleClose();
        } catch (error) {
            toast.error("Đã xảy ra lỗi khi cập nhật người dùng!");
        } finally {
            setIsLoading(false); // Dừng hiển thị trạng thái loading
        }
    };

    return (
        <>
            <Modal className='modal-add-user' backdrop="static" show={show} onHide={handleClose} size='xl'>
                <Modal.Header closeButton={!isLoading}>
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
                    <Button variant="secondary" onClick={handleClose} disabled={isLoading}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={handSubmitUpdateUser} disabled={isLoading}>
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
                            'Lưu thông tin'
                        )}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default ModalUpdateUser;