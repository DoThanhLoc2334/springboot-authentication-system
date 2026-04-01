import { MailOutlined } from "@ant-design/icons";
import { Button, Form, Input, Typography, message, Steps } from "antd";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";
import AuthPageShell from "../components/AuthPageShell";
import { ROUTES } from "../constants/routes";
import { getApiErrorMessage } from "../utils/getApiErrorMessage";

const { Text } = Typography;

const ForgotPassword = () => {
  const navigate = useNavigate();
  const [current, setCurrent] = useState(0);
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [form] = Form.useForm();

  const handleForgotPassword = async (values) => {
    setSubmitting(true);
    try {
      await api.post("/auth/forgot-password", { email: values.email });
      setEmail(values.email);
      setCurrent(1);
      message.success("OTP has been sent to your email.");
      form.resetFields();
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Failed to send OTP. Please try again.")
      );
    } finally {
      setSubmitting(false);
    }
  };

  const handleVerifyOtp = async (values) => {
    setSubmitting(true);
    try {
      await api.post("/auth/verify-otp", { email, otp: values.otp });
      setOtp(values.otp);
      setCurrent(2);
      message.success("OTP verified successfully.");
      form.resetFields();
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Invalid OTP. Please try again.")
      );
    } finally {
      setSubmitting(false);
    }
  };

  const handleResetPassword = async (values) => {
    setSubmitting(true);
    try {
      if (values.newPassword !== values.confirmPassword) {
        message.error("Passwords do not match.");
        setSubmitting(false);
        return;
      }

      await api.post("/auth/reset-password", {
        email,
        otp: values.otp,
        newPassword: values.newPassword,
        confirmPassword: values.confirmPassword,
      });

      message.success("Password reset successfully. Please log in.");
      navigate(ROUTES.login, { replace: true });
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Failed to reset password. Please try again.")
      );
    } finally {
      setSubmitting(false);
    }
  };

  const steps = [
    { title: "Email" },
    { title: "Verify OTP" },
    { title: "New Password" },
  ];

  return (
    <AuthPageShell title="Reset Password" subtitle="Enter your email to reset your password.">
      <Steps current={current} items={steps} style={{ marginBottom: 24 }} />

      {current === 0 && (
        <Form name="forgot_password_form" layout="vertical" onFinish={handleForgotPassword}>
          <Form.Item
            name="email"
            rules={[
              { required: true, message: "Please enter your email" },
              { type: "email", message: "Please enter a valid email" },
            ]}
          >
            <Input
              prefix={<MailOutlined />}
              placeholder="Enter your email"
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
              Send OTP
            </Button>
          </Form.Item>

          <div className="auth-footer">
            <Text type="secondary">Remember your password?</Text>
            <Button
              type="link"
              onClick={() => navigate(ROUTES.login)}
              style={{ padding: 0 }}
            >
              Log in
            </Button>
          </div>
        </Form>
      )}

      {current === 1 && (
        <Form name="verify_otp_form" layout="vertical" onFinish={handleVerifyOtp} form={form}>
          <Form.Item>
            <Text type="secondary">
              Enter the 6-digit OTP sent to {email}
            </Text>
          </Form.Item>

          <Form.Item
            name="otp"
            rules={[
              { required: true, message: "Please enter the OTP" },
              { len: 6, message: "OTP must be 6 digits" },
              { pattern: /^\d{6}$/, message: "OTP must contain only digits" },
            ]}
          >
            <Input
              placeholder="Enter 6-digit OTP"
              size="large"
              maxLength="6"
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
              Verify OTP
            </Button>
          </Form.Item>

          <div style={{ textAlign: "center", marginTop: 12 }}>
            <Button
              type="link"
              onClick={() => {
                setCurrent(0);
                form.resetFields();
              }}
            >
              Back
            </Button>
          </div>
        </Form>
      )}

      {current === 2 && (
        <Form
          name="reset_password_form"
          layout="vertical"
          onFinish={handleResetPassword}
          initialValues={{ otp }}
          form={form}
        >
          <Form.Item
            name="newPassword"
            rules={[
              { required: true, message: "Please enter your new password" },
              { min: 6, message: "Password must be at least 6 characters" },
            ]}
          >
            <Input.Password placeholder="New Password" size="large" />
          </Form.Item>

          <Form.Item
            name="confirmPassword"
            rules={[
              { required: true, message: "Please confirm your password" },
              { min: 6, message: "Password must be at least 6 characters" },
            ]}
          >
            <Input.Password placeholder="Confirm Password" size="large" />
          </Form.Item>

          <Form.Item
            name="otp"
            hidden
          >
            <Input />
          </Form.Item>

          <Form.Item style={{ marginBottom: 12 }}>
            <Button
              type="primary"
              htmlType="submit"
              size="large"
              block
              loading={submitting}
            >
              Reset Password
            </Button>
          </Form.Item>

          <div style={{ textAlign: "center", marginTop: 12 }}>
            <Button
              type="link"
              onClick={() => {
                setCurrent(1);
                form.resetFields();
              }}
            >
              Back
            </Button>
          </div>
        </Form>
      )}
    </AuthPageShell>
  );
};

export default ForgotPassword;
