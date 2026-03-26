import { useState } from "react";
import api from "../api/api";

function Login() {
  const [credentials, setCredentials] = useState({
    username: "",
    password: "",
  });
  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post("/auth/login", credentials);
      // Lưu token vào LocalStorage để dùng cho các API sau này
      localStorage.setItem("token", response.data.accessToken);
      setMessage("Đăng nhập thành công! Token đã được lưu.");
      console.log("JWT Token:", response.data.accessToken);
    } catch (error) {
      setMessage(error.response?.data?.message || "Đăng nhập thất bại!");
    }
  };

  return (
    <div style={{ padding: "20px", maxWidth: "400px" }}>
      <h2>Đăng nhập hệ thống</h2>
      <form onSubmit={handleLogin}>
        <div>
          <label>Username:</label>
          <br />
          <input type="text" name="username" onChange={handleChange} required />
        </div>
        <br />
        <div>
          <label>Password:</label>
          <br />
          <input
            type="password"
            name="password"
            onChange={handleChange}
            required
          />
        </div>
        <br />
        <button type="submit">Đăng nhập</button>
      </form>
      {message && (
        <p>
          <b>{message}</b>
        </p>
      )}
    </div>
  );
}

export default Login;
