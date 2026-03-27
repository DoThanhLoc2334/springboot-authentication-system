import { LockOutlined, UserOutlined } from "@ant-design/icons";
import { Button, Form, Input, Typography, message } from "antd";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";
import AuthPageShell from "../components/AuthPageShell";
import { ROUTES } from "../constants/routes";
import { setToken } from "../utils/auth";
import { getApiErrorMessage } from "../utils/getApiErrorMessage";

const { Text } = Typography;

const Login = () => {
  const navigate = useNavigate();
  const [submitting, setSubmitting] = useState(false);

  const onFinish = async (values) => {
    setSubmitting(true);

    try {
      const response = await api.post("/auth/login", values);
      setToken(response.data.accessToken);
      message.success("Dang nhap thanh cong.");
      navigate(ROUTES.dashboard, { replace: true });
    } catch (error) {
      message.error(
        getApiErrorMessage(
          error,
          "Tai khoan hoac mat khau khong chinh xac.",
        ),
      );
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <AuthPageShell
      title="Dang nhap"
      subtitle="Quan ly cua hang voi giao dien admin don gian va nhanh gon."
    >
      <Form name="login_form" layout="vertical" onFinish={onFinish}>
        <Form.Item
          name="username"
          rules={[{ required: true, message: "Vui long nhap username." }]}
        >
          <Input
            prefix={<UserOutlined />}
            placeholder="Username hoac email"
            size="large"
          />
        </Form.Item>

        <Form.Item
          name="password"
          rules={[{ required: true, message: "Vui long nhap mat khau." }]}
        >
          <Input.Password
            prefix={<LockOutlined />}
            placeholder="Mat khau"
            size="large"
          />
        </Form.Item>

        <Form.Item style={{ marginBottom: 12 }}>
          <Button
            type="primary"
            htmlType="submit"
            size="large"
            block
            loading={submitting}
          >
            Dang nhap
          </Button>
        </Form.Item>

        <div className="auth-footer">
          <Text type="secondary">Chua co tai khoan?</Text>
          <Button
            type="link"
            onClick={() => navigate(ROUTES.register)}
            style={{ padding: 0 }}
          >
            Dang ky ngay
          </Button>
        </div>
      </Form>
    </AuthPageShell>
  );
};

export default Login;
