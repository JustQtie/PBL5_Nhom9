import axios from '../utils/axiosCustomize';

// const postCreateNewUser = (email, password, username, role, image) => {
//     const data = new FormData();
//     data.append('email', email);
//     data.append('password', password);
//     data.append('username', username);
//     data.append('role', role);
//     data.append('userImage', image);

//     return axios.post('api/v1/participant', data);
// }

const postLogin = (username, password) => {
    return axios.post('api/v1/users/login', { phone_number: username, password: password });
}

const getAllUsers = (token) => {
    console.log("check token", token);

    return axios.get('api/v1/users', {}, {
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
}