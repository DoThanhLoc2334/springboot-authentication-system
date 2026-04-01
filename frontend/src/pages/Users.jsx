import { EditOutlined, EyeOutlined } from "@ant-design/icons";
import {
  Button,
  Descriptions,
  Input,
  Modal,
  Space,
  Spin,
  Table,
  Tag,
  Typography,
  message,
} from "antd";
import { useEffect, useState } from "react";
import api from "../api/api";
import { getApiErrorMessage } from "../utils/getApiErrorMessage";

const { Title, Text } = Typography;

const Users = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchText, setSearchText] = useState("");
  const [statusUpdateLoading, setStatusUpdateLoading] = useState({});
  const [detailsOpen, setDetailsOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [detailsLoading, setDetailsLoading] = useState(false);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const response = await api.get("/users");
      setUsers(response.data || []);
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Could not load the user list."),
      );
    } finally {
      setLoading(false);
    }
  };

  const handleStatusChange = async (userId, currentStatus) => {
    setStatusUpdateLoading((prev) => ({ ...prev, [userId]: true }));
    try {
      await api.put(`/users/${userId}/status`);
      message.success("User status updated successfully.");
      fetchUsers();
    } catch (error) {
      message.error(
        getApiErrorMessage(error, "Could not update the user status."),
      );
    } finally {
      setStatusUpdateLoading((prev) => ({ ...prev, [userId]: false }));
    }
  };

  const handleViewDetails = async (userId) => {
    setDetailsOpen(true);
    setDetailsLoading(true);

    try {
      const response = await api.get(`/users/${userId}`);
      setSelectedUser(response.data);
    } catch (error) {
      setDetailsOpen(false);
      message.error(
        getApiErrorMessage(error, "Could not load the user details."),
      );
    } finally {
      setDetailsLoading(false);
    }
  };

  const closeDetailsModal = () => {
    setDetailsOpen(false);
    setSelectedUser(null);
  };

  const filteredUsers = users.filter(
    (user) =>
      user.username?.toLowerCase().includes(searchText.toLowerCase()) ||
      user.email?.toLowerCase().includes(searchText.toLowerCase()),
  );

  const columns = [
    {
      title: "ID",
      dataIndex: "id",
      key: "id",
      width: 60,
    },
    {
      title: "Username",
      dataIndex: "username",
      key: "username",
      render: (text) => <Text strong>{text}</Text>,
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
    },
    {
      title: "Role",
      dataIndex: "roles",
      key: "roles",
      render: (_, record) => {
        const isAdmin = record.roles?.includes("ROLE_ADMIN");
        return (
          <Tag color={isAdmin ? "red" : "blue"}>
            {isAdmin ? "ADMIN" : "USER"}
          </Tag>
        );
      },
    },
    {
      title: "Status",
      dataIndex: "enabled",
      key: "enabled",
      render: (enabled, record) => (
        <Button
          size="small"
          loading={statusUpdateLoading[record.id]}
          onClick={() => handleStatusChange(record.id, enabled)}
          type={enabled ? "primary" : "dashed"}
          danger={!enabled}
        >
          {enabled ? "Active" : "Inactive"}
        </Button>
      ),
    },
    {
      title: "Actions",
      key: "action",
      render: (_, record) => (
        <Space>
          <Button
            icon={<EyeOutlined />}
            size="small"
            onClick={() => handleViewDetails(record.id)}
          >
            View Details
          </Button>
          <Button
            type="primary"
            icon={<EditOutlined />}
            size="small"
            disabled
            title="Coming soon"
          />
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Title level={2}>User Management</Title>

      <div style={{ marginBottom: 16, display: "flex", gap: 8 }}>
        <Input
          placeholder="Search by username or email..."
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          allowClear
          style={{ flex: 1 }}
        />
        <Button type="primary" onClick={fetchUsers}>
          Reload
        </Button>
      </div>

      <Spin spinning={loading}>
        <Table
          columns={columns}
          dataSource={filteredUsers}
          rowKey="id"
          pagination={{ pageSize: 10 }}
          bordered
        />
      </Spin>

      <Modal
        title="User Details"
        open={detailsOpen}
        onCancel={closeDetailsModal}
        footer={[
          <Button key="close" onClick={closeDetailsModal}>
            Close
          </Button>,
        ]}
        destroyOnHidden
      >
        <Spin spinning={detailsLoading}>
          {selectedUser ? (
            <Descriptions column={1} bordered size="small">
              <Descriptions.Item label="ID">
                {selectedUser.id}
              </Descriptions.Item>
              <Descriptions.Item label="Username">
                {selectedUser.username}
              </Descriptions.Item>
              <Descriptions.Item label="Email">
                {selectedUser.email}
              </Descriptions.Item>
              <Descriptions.Item label="Status">
                <Tag color={selectedUser.enabled ? "green" : "default"}>
                  {selectedUser.enabled ? "Active" : "Inactive"}
                </Tag>
              </Descriptions.Item>
              <Descriptions.Item label="Roles">
                <Space wrap>
                  {(selectedUser.roles || []).map((role) => (
                    <Tag
                      key={role}
                      color={role === "ROLE_ADMIN" ? "red" : "blue"}
                    >
                      {role}
                    </Tag>
                  ))}
                </Space>
              </Descriptions.Item>
            </Descriptions>
          ) : null}
        </Spin>
      </Modal>
    </div>
  );
};

export default Users;
