import React, { useState } from "react";
import { Form, Input, Button, Card, Typography, message } from "antd";
import { UserOutlined, LockOutlined, MailOutlined } from "@ant-design/icons";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/api";

const { Title, Text } = Typography;

const Register = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const onFinish = async (values) => {
    setLoading(true);
    try {
      // Chỉ gửi username, email, password về backend
      const { confirmPassword, ...registerData } = values;
      await api.post("/auth/register", registerData);

      message.success("Đăng ký thành công! Hãy đăng nhập nhé.");
      navigate("/login");
    } catch (error) {
      // Hiển thị lỗi từ Backend (ví dụ: Trùng username/email)
      const errorMsg = error.response?.data || "Đăng ký thất bại!";
      message.error(errorMsg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        minHeight: "100vh",
        background: "#f0f2f5",
      }}
    >
      <Card style={{ width: 400, boxShadow: "0 4px 12px rgba(0,0,0,0.1)" }}>
        <div style={{ textAlign: "center", marginBottom: 24 }}>
          <Title level={2}>Đăng ký Tài khoản</Title>
          <Text type="secondary">Gia nhập cộng đồng HUTECH Admin</Text>
        </div>

        <Form name="register" onFinish={onFinish} layout="vertical">
          <Form.Item
            name="username"
            rules={[{ required: true, message: "Vui lòng nhập Username!" }]}
          >
            <Input
              prefix={<UserOutlined />}
              placeholder="Username"
              size="large"
            />
          </Form.Item>

          <Form.Item
            name="email"
            rules={[
              { required: true, message: "Vui lòng nhập Email!" },
              { type: "email", message: "Email không đúng định dạng!" },
            ]}
          >
            <Input prefix={<MailOutlined />} placeholder="Email" size="large" />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[
              { required: true, message: "Vui lòng nhập Mật khẩu!" },
              { min: 6, message: "Mật khẩu phải từ 6 ký tự trở lên!" },
            ]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="Mật khẩu"
              size="large"
            />
          </Form.Item>

          {/* Ô xác nhận mật khẩu - Logic đặc biệt của Ant Design */}
          <Form.Item
            name="confirmPassword"
            dependencies={["password"]}
            rules={[
              { required: true, message: "Vui lòng xác nhận lại mật khẩu!" },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue("password") === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(
                    new Error("Mật khẩu nhập lại không khớp!"),
                  );
                },
              }),
            ]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="Xác nhận mật khẩu"
              size="large"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              block
              size="large"
              loading={loading}
            >
              Đăng ký ngay
            </Button>
          </Form.Item>

          <div style={{ textAlign: "center" }}>
            Đã có tài khoản? <Link to="/login">Đăng nhập</Link>
          </div>
        </Form>
      </Card>
    </div>
  );
};

export default Register;
