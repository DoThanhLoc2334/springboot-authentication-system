import {
  KeyOutlined,
  DashboardOutlined,
  LogoutOutlined,
  UserOutlined,
} from "@ant-design/icons";
import { Button, Layout, Menu, Space, Typography, message, theme } from "antd";
import { useState } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import api from "../api/api";
import ChangePasswordModal from "./ChangePasswordModal";
import { ROUTES } from "../constants/routes";
import { clearToken, isAdmin } from "../utils/auth";
import { getApiErrorMessage } from "../utils/getApiErrorMessage";

const { Header, Sider, Content } = Layout;
const { Text } = Typography;

const AdminLayout = () => {
  const [collapsed, setCollapsed] = useState(false);
  const [passwordModalOpen, setPasswordModalOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const admin = isAdmin();
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const navigationItems = [
    {
      key: ROUTES.dashboard,
      icon: <DashboardOutlined />,
      label: "Dashboard",
    },
    ...(admin
      ? [
          {
            key: ROUTES.users,
            icon: <UserOutlined />,
            label: "Users",
          },
        ]
      : []),
    {
      type: "divider",
    },
    {
      key: "logout",
      icon: <LogoutOutlined />,
      label: "Logout",
      danger: true,
    },
  ];

  const handleMenuClick = async ({ key }) => {
    if (key === "logout") {
      try {
        await api.post("/auth/logout");
      } catch (error) {
        message.warning(
          getApiErrorMessage(error, "Could not sync logout with the server."),
        );
      }
      clearToken();
      message.success("Logged out successfully.");
      navigate(ROUTES.login, { replace: true });
      return;
    }

    navigate(key);
  };

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sider
        collapsible
        collapsed={collapsed}
        onCollapse={(value) => setCollapsed(value)}
      >
        <div className="admin-brand">
          <Text strong style={{ color: "inherit" }}>
            {collapsed ? "UM" : "USER MANAGER"}
          </Text>
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[location.pathname]}
          items={navigationItems}
          onClick={(info) => {
            void handleMenuClick(info);
          }}
        />
      </Sider>

      <Layout>
        <Header
          className="admin-header"
          style={{ background: colorBgContainer }}
        >
          <div className="admin-header__actions">
            <Space>
              <Button
                icon={<KeyOutlined />}
                onClick={() => setPasswordModalOpen(true)}
              >
                Change Password
              </Button>
            </Space>
          </div>
        </Header>
        <Content style={{ margin: 16 }}>
          <div
            className="page-panel"
            style={{
              background: colorBgContainer,
              borderRadius: borderRadiusLG,
            }}
          >
            <Outlet />
          </div>
        </Content>
      </Layout>

      <ChangePasswordModal
        open={passwordModalOpen}
        onClose={() => setPasswordModalOpen(false)}
      />
    </Layout>
  );
};

export default AdminLayout;
