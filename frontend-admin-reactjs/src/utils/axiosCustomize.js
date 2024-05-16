import axios from "axios";

const instance = axios.create({
    baseURL: 'https://5e4f-116-105-167-76.ngrok-free.app/'
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