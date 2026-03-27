import { LockOutlined, MailOutlined, UserOutlined } from "@ant-design/icons";
import { Button, Form, Input, Typography, message } from "antd";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api/api";
import AuthPageShell from "../components/AuthPageShell";
import { ROUTES } from "../constants/routes";
import { getApiErrorMessage } from "../utils/getApiErrorMessage";

const { Text } = Typography;

const Register = () => {
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();

  const onFinish = async (values) => {
    setSubmitting(true);

    try {
      const { confirmPassword, ...payload } = values;
      await api.post("/auth/register", payload);
      message.success("Registration successful. Please log in.");
      navigate(ROUTES.login, { replace: true });
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Registration failed. Please try again."),
      );
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <AuthPageShell
      title="Sign up"
      subtitle="Create an account to access the system."
    >
      <Form name="register_form" layout="vertical" onFinish={onFinish}>
        <Form.Item
          name="username"
          rules={[{ required: true, message: "Please enter your username." }]}
        >
          <Input prefix={<UserOutlined />} placeholder="Username" size="large" />
        </Form.Item>

        <Form.Item
          name="email"
          rules={[
            { required: true, message: "Please enter your email." },
            { type: "email", message: "The email is not in the correct format." },
          ]}
        >
          <Input prefix={<MailOutlined />} placeholder="Email" size="large" />
        </Form.Item>

        <Form.Item
          name="password"
          rules={[
            { required: true, message: "Please enter your password." },
            { min: 6, message: "Password must be at least 6 characters." },
          ]}
        >
          <Input.Password
            prefix={<LockOutlined />}
            placeholder="Password"
            size="large"
          />
        </Form.Item>

        <Form.Item
          name="confirmPassword"
          dependencies={["password"]}
          rules={[
            { required: true, message: "Please confirm your password." },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("password") === value) {
                  return Promise.resolve();
                }

                return Promise.reject(new Error("The two passwords do not match."));
              },
            }),
          ]}
        >
          <Input.Password
            prefix={<LockOutlined />}
            placeholder="Confirm password"
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
            Create Account
          </Button>
        </Form.Item>

        <div className="auth-footer">
          <Text type="secondary">Already have an account?</Text>
          <Link to={ROUTES.login}>Log in</Link>
        </div>
      </Form>
    </AuthPageShell>
  );
};

export default Register;