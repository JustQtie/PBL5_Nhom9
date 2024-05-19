import axios from "axios";



const instance = axios.create({
    // baseURL: 'https://fc73-27-69-244-129.ngrok-free.app/'
    baseURL: process.env.REACT_APP_API_URL
});

instance.interceptors.request.use(function (config) {

    return config;
}, function (error) {

    return Promise.reject(error);
});


instance.interceptors.response.use(function (response) {

    return response && response.data ? response.data : response;
}, function (error) {

    return error && error.response && error.response.data ? error.response.data : Promise.reject(error);
});
export default instance;