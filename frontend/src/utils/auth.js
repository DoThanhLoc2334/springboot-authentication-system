import { jwtDecode } from "jwt-decode";
import { STORAGE_KEYS } from "../constants/storage";

export const getToken = () => localStorage.getItem(STORAGE_KEYS.token);

export const setToken = (token) => {
  localStorage.setItem(STORAGE_KEYS.token, token);
  try {
    const decoded = jwtDecode(token);
    const role = decoded.authorities?.[0] || "ROLE_USER";
    localStorage.setItem(STORAGE_KEYS.role, role);
  } catch (error) {
    console.error("Failed to decode token:", error);
  }
};

export const clearToken = () => {
  localStorage.removeItem(STORAGE_KEYS.token);
  localStorage.removeItem(STORAGE_KEYS.role);
  localStorage.removeItem(STORAGE_KEYS.user);
};

export const isAuthenticated = () => Boolean(getToken());

export const getUserRole = () => {
  return localStorage.getItem(STORAGE_KEYS.role) || "ROLE_USER";
};

export const isAdmin = () => getUserRole() === "ROLE_ADMIN";

export const hasRole = (role) => getUserRole() === role;
