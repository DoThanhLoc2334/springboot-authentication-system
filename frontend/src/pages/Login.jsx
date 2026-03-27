import React from "react";
import { Form, Input, Button, Card, message, Typography } from "antd";
import { UserOutlined, LockOutlined } from "@ant-design/icons";
import api from "../api/api";
import { useNavigate } from "react-router-dom";

const { Text } = Typography;

const Login = () => {
  const navigate = useNavigate();

  // Hàm xử lý khi nhấn Đăng nhập
  const onFinish = async (values) => {
    try {
      const response = await api.post("/auth/login", values);

      // Lưu JWT Token vào localStorage để các request sau sử dụng
      localStorage.setItem("token", response.data.accessToken);

      message.success("Đăng nhập thành công!");

      // Chuyển hướng người dùng vào trang Dashboard
      navigate("/admin/dashboard");
    } catch (error) {
      // Hiển thị lỗi từ server hoặc lỗi mặc định
      message.error(
        error.response?.data?.message ||
          "Tài khoản hoặc mật khẩu không chính xác!",
      );
    }
  };

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
        background: "#f0f2f5",
      }}
    >
      <Card
        title={
          <span
            style={{ color: "#1890ff", fontSize: "24px", fontWeight: "bold" }}
          >
            HUTECH E-COMMERCE
          </span>
        }
        style={{
          width: 400,
          textAlign: "center",
          boxShadow: "0 4px 12px rgba(0,0,0,0.15)",
          borderRadius: "8px",
        }}
      >
        <Form name="login_form" onFinish={onFinish} layout="vertical">
          {/* Trường Username */}
          <Form.Item
            name="username"
            rules={[{ required: true, message: "Vui lòng nhập Username!" }]}
          >
            <Input
              prefix={<UserOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              placeholder="Username"
              size="large"
            />
          </Form.Item>

          {/* Trường Password */}
          <Form.Item
            name="password"
            rules={[{ required: true, message: "Vui lòng nhập Password!" }]}
          >
            <Input.Password
              prefix={<LockOutlined style={{ color: "rgba(0,0,0,.25)" }} />}
              placeholder="Password"
              size="large"
            />
          </Form.Item>

          {/* Nút Login */}
          <Form.Item style={{ marginBottom: "10px" }}>
            <Button type="primary" htmlType="submit" size="large" block>
              Đăng nhập
            </Button>
          </Form.Item>

          {/* Nút chuyển hướng sang Register */}
          <div style={{ marginTop: "15px" }}>
            <Text type="secondary">Chưa có tài khoản? </Text>
            <Button
              type="link"
              onClick={() => navigate("/register")}
              style={{ padding: 0 }}
            >
              Đăng ký ngay
            </Button>
          </div>
        </Form>
      </Card>
    </div>
  );
};

export default Login;
