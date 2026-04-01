import { jwtDecode } from "jwt-decode";
import { STORAGE_KEYS } from "../constants/storage";

export const getToken = () => localStorage.getItem(STORAGE_KEYS.token);

export const getRefreshToken = () =>
  localStorage.getItem(STORAGE_KEYS.refreshToken);

const setRoleFromToken = (token) => {
  try {
    const decoded = jwtDecode(token);
    const authorities = Array.isArray(decoded.authorities)
      ? decoded.authorities
      : [];
    const role = authorities.includes("ROLE_ADMIN")
      ? "ROLE_ADMIN"
      : authorities[0] || "ROLE_USER";
    localStorage.setItem(STORAGE_KEYS.role, role);
  } catch (error) {
    console.error("Failed to decode token:", error);
    localStorage.setItem(STORAGE_KEYS.role, "ROLE_USER");
  }
};

export const setAuthSession = ({ accessToken, refreshToken }) => {
  if (accessToken) {
    localStorage.setItem(STORAGE_KEYS.token, accessToken);
    setRoleFromToken(accessToken);
  }

  if (refreshToken) {
    localStorage.setItem(STORAGE_KEYS.refreshToken, refreshToken);
  }
};

export const setToken = (token) => {
  setAuthSession({ accessToken: token });
};

export const clearToken = () => {
  localStorage.removeItem(STORAGE_KEYS.token);
  localStorage.removeItem(STORAGE_KEYS.refreshToken);
  localStorage.removeItem(STORAGE_KEYS.role);
  localStorage.removeItem(STORAGE_KEYS.user);
};

export const isAuthenticated = () => Boolean(getToken());

export const getUserRole = () => {
  return localStorage.getItem(STORAGE_KEYS.role) || "ROLE_USER";
};

export const isAdmin = () => getUserRole() === "ROLE_ADMIN";

export const hasRole = (role) => getUserRole() === role;
