import { LockOutlined, UserOutlined } from "@ant-design/icons";
import { Button, Form, Input, Typography, message } from "antd";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";
import AuthPageShell from "../components/AuthPageShell";
import { ROUTES } from "../constants/routes";
import { setAuthSession } from "../utils/auth";
import { getApiErrorMessage } from "../utils/getApiErrorMessage";

const { Text } = Typography;

const Login = () => {
  const navigate = useNavigate();
  const [submitting, setSubmitting] = useState(false);

  const onFinish = async (values) => {
    setSubmitting(true);

    try {
      const response = await api.post("/auth/login", values);
      setAuthSession(response.data);
      message.success("Login successful.");
      navigate(ROUTES.dashboard, { replace: true });
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Incorrect username or password."),
      );
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <AuthPageShell title="Login" subtitle="Welcome.">
      <Form name="login_form" layout="vertical" onFinish={onFinish}>
        <Form.Item
          name="usernameOrEmail"
          rules={[
            { required: true, message: "Please enter your username or email." },
          ]}
        >
          <Input
            prefix={<UserOutlined />}
            placeholder="Username or email"
            size="large"
          />
        </Form.Item>

        <Form.Item
          name="password"
          rules={[{ required: true, message: "Please enter your password." }]}
        >
          <Input.Password
            prefix={<LockOutlined />}
            placeholder="Password"
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
            Log in
          </Button>
        </Form.Item>

        <div className="auth-footer">
          <Text type="secondary">Don't have an account?</Text>
          <Button
            type="link"
            onClick={() => navigate(ROUTES.register)}
            style={{ padding: 0 }}
          >
            Sign up now
          </Button>
        </div>
      </Form>
    </AuthPageShell>
  );
};

export default Login;
