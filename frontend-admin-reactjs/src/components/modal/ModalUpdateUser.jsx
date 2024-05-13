import { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { FcPlus } from 'react-icons/fc';
import { toast } from 'react-toastify';
import { putUpdateUser } from '../../services/apiServices';
import _ from 'lodash';
import "./modalUpdateUser.scss"

const ModalUpdateUser = (props) => {
    const { show, setShow, userData } = props;

    const handleClose = () => {
        setShow(false);
        // setId("");
        // setFullName("");
        // setPhoneNumber("");
        // setAddress("");
        // setGender("");
        // setPreviewImage("");
        // resetUpdateData();

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
            setPhoneNumber(userData.phoneNumber);
            setAddress(userData.address);
            setGender(userData.gender);
            if (userData.image) {
                setPreviewImage(`data:image/jpeg;base64,${userData.image}`);
            }

        }
    }, [userData])

    const handleUploadImage = (event) => {
        if (event.target && event.target.files && event.target.files[0]) {
            setPreviewImage(URL.createObjectURL(event.target.files[0]));
            setImage(event.target.files[0]);
        } else {
            // setPreviewImage("");
        }

    }


    const handSubmitCreateUser = async () => {
        // alert(gender);

        // Lấy token từ localStorage
        const token = localStorage.getItem("token");

        // Nếu không có token, không gọi API và kết thúc hàm
        if (!token) {
            toast.error("Token not found");
            return;
        }
        // // call API
        console.log("check>>>", 1);
        let data = await putUpdateUser(userData.id, fullname, phoneNumber, address, gender, token);

        if (data && data.EC === 0) {
            toast.success(data.EM);
            handleClose();
            // await props.fetchUsers();
        }
        if (data && data.EC !== 0) {
            toast.error(data.EM);
            handleClose();
        }
    }


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
                            <input type="text" disabled className="form-control" value={id} onChange={(event) => setId(event.target.value)} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Full Name</label>
                            <input type="text" className="form-control" value={fullname} onChange={(event) => setFullName(event.target.value)} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Phone Number</label>
                            <input type="text" className="form-control" value={phoneNumber} onChange={(event) => setPhoneNumber(event.target.value)} />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Address</label>
                            <input type="text" className="form-control" value={address} onChange={(event) => setAddress(event.target.value)} />
                        </div>

                        <div className="col-md-6">
                            <label className="form-label">Gender</label>
                            <select className="form-select" value={gender} onChange={(event) => setGender(event.target.value)}>
                                <option value="true" >Nam</option>
                                <option value="false">Nữ</option>
                            </select>
                        </div>
                        <div className='col-md-12'>
                            <label className="form-label label-upload" htmlFor='labelUpload' > <FcPlus /> Upload File Image</label>
                            <input type='file' id='labelUpload' hidden onChange={(event) => handleUploadImage(event)} />
                        </div>

                        <div className='col-md-12 img-preview'>
                            {previewImage ? <img src={previewImage} /> : <span >preview img</span>}

                        </div>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={handSubmitCreateUser}>
                        Lưu thông tin
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalUpdateUser;