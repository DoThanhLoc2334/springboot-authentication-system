import { DeleteOutlined, EditOutlined } from "@ant-design/icons";
import {
  Button,
  Input,
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
    </div>
  );
};

export default Users;
