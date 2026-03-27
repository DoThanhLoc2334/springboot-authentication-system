import { STORAGE_KEYS } from "../constants/storage";

export const getToken = () => localStorage.getItem(STORAGE_KEYS.token);

export const setToken = (token) => {
  localStorage.setItem(STORAGE_KEYS.token, token);
};

export const clearToken = () => {
  localStorage.removeItem(STORAGE_KEYS.token);
};

export const isAuthenticated = () => Boolean(getToken());
