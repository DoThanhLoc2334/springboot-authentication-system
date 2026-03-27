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
      message.success("Dang ky thanh cong. Hay dang nhap.");
      navigate(ROUTES.login, { replace: true });
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Dang ky that bai. Vui long thu lai."),
      );
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <AuthPageShell
      title="Dang ky"
      subtitle="Tao tai khoan de truy cap khu vuc quan tri san pham."
    >
      <Form name="register_form" layout="vertical" onFinish={onFinish}>
        <Form.Item
          name="username"
          rules={[{ required: true, message: "Vui long nhap username." }]}
        >
          <Input prefix={<UserOutlined />} placeholder="Username" size="large" />
        </Form.Item>

        <Form.Item
          name="email"
          rules={[
            { required: true, message: "Vui long nhap email." },
            { type: "email", message: "Email khong dung dinh dang." },
          ]}
        >
          <Input prefix={<MailOutlined />} placeholder="Email" size="large" />
        </Form.Item>

        <Form.Item
          name="password"
          rules={[
            { required: true, message: "Vui long nhap mat khau." },
            { min: 6, message: "Mat khau phai co it nhat 6 ky tu." },
          ]}
        >
          <Input.Password
            prefix={<LockOutlined />}
            placeholder="Mat khau"
            size="large"
          />
        </Form.Item>

        <Form.Item
          name="confirmPassword"
          dependencies={["password"]}
          rules={[
            { required: true, message: "Vui long nhap lai mat khau." },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("password") === value) {
                  return Promise.resolve();
                }

                return Promise.reject(new Error("Mat khau nhap lai khong khop."));
              },
            }),
          ]}
        >
          <Input.Password
            prefix={<LockOutlined />}
            placeholder="Nhap lai mat khau"
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
            Tao tai khoan
          </Button>
        </Form.Item>

        <div className="auth-footer">
          <Text type="secondary">Da co tai khoan?</Text>
          <Link to={ROUTES.login}>Dang nhap</Link>
        </div>
      </Form>
    </AuthPageShell>
  );
};

export default Register;
