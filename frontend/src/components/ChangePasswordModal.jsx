import { Button, Form, Input, Modal, message } from "antd";
import { useEffect, useState } from "react";
import api from "../api/api";
import { getApiErrorMessage } from "../utils/getApiErrorMessage";

const ChangePasswordModal = ({ open, onClose }) => {
  const [form] = Form.useForm();
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (!open) {
      form.resetFields();
    }
  }, [form, open]);

  const handleSubmit = async (values) => {
    setSubmitting(true);

    try {
      await api.put("/users/profile/password", values);
      message.success("Password updated successfully.");
      onClose();
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Could not update password. Please try again."),
      );
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Modal
      title="Change Password"
      open={open}
      onCancel={onClose}
      footer={null}
      destroyOnHidden
    >
      <Form form={form} layout="vertical" onFinish={handleSubmit}>
        <Form.Item
          label="Current Password"
          name="currentPassword"
          rules={[{ required: true, message: "Please enter your current password." }]}
        >
          <Input.Password placeholder="Enter your current password" />
        </Form.Item>

        <Form.Item
          label="New Password"
          name="newPassword"
          rules={[
            { required: true, message: "Please enter your new password." },
            { min: 6, message: "Your new password must be at least 6 characters." },
          ]}
        >
          <Input.Password placeholder="Enter your new password" />
        </Form.Item>

        <Form.Item
          label="Confirm New Password"
          name="confirmPassword"
          dependencies={["newPassword"]}
          rules={[
            { required: true, message: "Please confirm your new password." },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("newPassword") === value) {
                  return Promise.resolve();
                }

                return Promise.reject(new Error("The password confirmation does not match."));
              },
            }),
          ]}
        >
          <Input.Password placeholder="Re-enter your new password" />
        </Form.Item>

        <div className="modal-actions">
          <Button onClick={onClose}>Cancel</Button>
          <Button type="primary" htmlType="submit" loading={submitting}>
            Update Password
          </Button>
        </div>
      </Form>
    </Modal>
  );
};

export default ChangePasswordModal;
