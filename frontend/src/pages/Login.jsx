import { Form, Input, Button, Card, message } from "antd";
import { UserOutlined, LockOutlined } from "@ant-design/icons";
import api from "../api/api";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const navigate = useNavigate();

  const onFinish = async (values) => {
    try {
      const response = await api.post("/auth/login", values);
      localStorage.setItem("token", response.data.accessToken);
      message.success("Đăng nhập thành công!");
      navigate("/admin/dashboard");
    } catch (error) {
      message.error(
        error.response?.data?.message || "Tài khoản hoặc mật khẩu sai!",
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
        title="HUTECH E-COMMERCE"
        style={{
          width: 400,
          textAlign: "center",
          boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
        }}
      >
        <Form name="login_form" onFinish={onFinish}>
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
            name="password"
            rules={[{ required: true, message: "Vui lòng nhập Password!" }]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="Password"
              size="large"
            />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" size="large" block>
              {" "}
              Đăng nhập{" "}
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default Login;
