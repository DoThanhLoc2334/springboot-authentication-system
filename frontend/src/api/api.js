import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api", // Đường dẫn Backend của Huy
  headers: {
    "Content-Type": "application/json",
  },
});

// Tự động đính kèm JWT vào mọi request nếu đã đăng nhập
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
