import axios from "axios";
import { clearToken, getRefreshToken, getToken, setAuthSession } from "../utils/auth";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

let refreshPromise = null;

api.interceptors.request.use((config) => {
  const token = getToken();

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    const refreshToken = getRefreshToken();
    const isAuthRefreshRequest = originalRequest?.url?.includes("/auth/refresh");

    if (
      error.response?.status === 401 &&
      refreshToken &&
      originalRequest &&
      !originalRequest._retry &&
      !isAuthRefreshRequest
    ) {
      originalRequest._retry = true;

      try {
        refreshPromise =
          refreshPromise ??
          axios.post(
            `${api.defaults.baseURL}/auth/refresh`,
            { refreshToken },
            {
              headers: {
                "Content-Type": "application/json",
              },
            },
          );

        const response = await refreshPromise;
        setAuthSession(response.data);
        originalRequest.headers.Authorization = `Bearer ${response.data.accessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        clearToken();
        return Promise.reject(refreshError);
      } finally {
        refreshPromise = null;
      }
    }

    if (error.response?.status === 401 && getToken()) {
      clearToken();
    }

    return Promise.reject(error);
  },
);

export default api;
