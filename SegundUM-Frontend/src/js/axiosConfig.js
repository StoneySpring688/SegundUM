import axios from "axios";
const api = axios.create({
  baseURL: "http://79.72.53.253:8090",
  
  withCredentials: true, 
  
  headers: {
    "Content-Type": "application/json"
  }
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("Interceptado error de API:", error.response?.status, error.response?.data);
    return Promise.reject(error);
  }
);

export default api;