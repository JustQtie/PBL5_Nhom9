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

    return axios.post('api/v1/products', {}, {
        headers: {
            Authorization: `Bearer ${token}` // Đính kèm token vào header Authorization

        }
    });

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
    const data = new FormData();
    data.append('fullname', fullname);
    data.append('phone_number', phone_number);
    data.append('address', address);
    data.append('gender', gender);



    return axios.put(`api/v1/users/${id}`, data, {
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
}