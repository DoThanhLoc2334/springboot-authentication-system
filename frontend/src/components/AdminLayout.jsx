import {
  DashboardOutlined,
  LogoutOutlined,
  UserOutlined,
} from "@ant-design/icons";
import { Layout, Menu, Typography, message, theme } from "antd";
import { useState } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { ROUTES } from "../constants/routes";
import { clearToken, isAdmin } from "../utils/auth";

const { Header, Sider, Content } = Layout;
const { Text } = Typography;

const AdminLayout = () => {
  const [collapsed, setCollapsed] = useState(false);
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
      label: "Tong quan",
    },
    ...(admin
      ? [
          {
            key: ROUTES.users,
            icon: <UserOutlined />,
            label: "Nguoi dung",
          },
        ]
      : []),
    {
      type: "divider",
    },
    {
      key: "logout",
      icon: <LogoutOutlined />,
      label: "Dang xuat",
      danger: true,
    },
  ];

  const handleMenuClick = ({ key }) => {
    if (key === "logout") {
      clearToken();
      message.success("Dang xuat thanh cong.");
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
          onClick={handleMenuClick}
        />
      </Sider>

      <Layout>
        <Header
          className="admin-header"
          style={{ background: colorBgContainer }}
        />
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
    </Layout>
  );
};

export default AdminLayout;
