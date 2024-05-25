import axios from '../utils/axiosCustomize';

const postLogin = (username, password) => {
    return axios.post('api/v1/users/login', { phone_number: username, password: password });
}

const getAllUsers = (token) => {

    return axios.post('api/v1/users', {}, {
        headers: {
            Authorization: `Bearer ${token}` // Đính kèm token vào header Authorization

        }
    });
}

const getAllProducts = (token) => {

    return axios.post('api/v1/products/get_list', {}, {
        headers: {
            Authorization: `Bearer ${token}` // Đính kèm token vào header Authorization

        }
    });

    // return axios.get('api/v1/products/get_list');

}



const putBanUser = (id, token) => {

    return axios.put(`api/v1/users/ban/${id}`, {}, {
        headers: {
            Authorization: `Bearer ${token}` // Đính kèm token vào header Authorization
        }
    });
}

const putUnbanUser = (id, token) => {

    return axios.put(`api/v1/users/unban/${id}`, {}, {
        headers: {
            Authorization: `Bearer ${token}` // Đính kèm token vào header Authorization
        }
    });
}

const getUserById = (id, token) => {

    return axios.post(`api/v1/users/${id}`, {}, {
        headers: {
            Authorization: `Bearer ${token}` // Đính kèm token vào header Authorization
        }
    });
}

const putUpdateUser = (id, fullname, phone_number, address, gender, token) => {
    const data = {
        fullname: fullname,
        phone_number: phone_number,
        address: address,
        gender: gender
    };

    return axios.put(`api/v1/users/${id}`, data, {
        headers: {
            Authorization: `Bearer ${token}`, // Đính kèm token vào header Authorization
            'Content-Type': 'application/json' // Đảm bảo kiểu nội dung là JSON
        }
    });
}


const postUpdateImageUser = (id, image, token) => {
    const data = new FormData();
    data.append('file', image);

    return axios.post(`api/v1/users/uploads/${id}`, data, {
        headers: {
            Authorization: `Bearer ${token}`, // Đính kèm token vào header Authorization
            'Content-Type': 'application/json' // Đảm bảo kiểu nội dung là JSON
        }
    });
}


const deleteProductById = (id, token) => {

    return axios.delete(`api/v1/products/${id}`, {
        headers: {
            Authorization: `Bearer ${token}` // Đính kèm token vào header Authorization
        }
    });
}

const deleteProductThumbnailsById = (id, token) => {

    return axios.delete(`api/v1/products/thumbnails/${id}`, {
        headers: {
            Authorization: `Bearer ${token}` // Đính kèm token vào header Authorization
        }
    });
}

export {
    postLogin,
    getAllUsers,
    putUpdateUser,
    putBanUser,
    putUnbanUser,
    getAllProducts,
    getUserById,
    deleteProductById,
    deleteProductThumbnailsById,
    postUpdateImageUser,

}