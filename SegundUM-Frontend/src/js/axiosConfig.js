import axios from "axios";
const api = axios.create({
  baseURL: "http://localhost:8090",
  
  withCredentials: true, 
  
  headers: {
    "Content-Type": "application/json"
  }
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token'); 
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("Interceptado error de API:", error.response?.status, error.response?.data);
    return Promise.reject(error);
  }
);

export default api;