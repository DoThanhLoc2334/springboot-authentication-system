import React, { useState } from "react";
import { Layout, Menu, Button, theme } from "antd";
import {
  DashboardOutlined,
  AppstoreOutlined,
  ShoppingOutlined,
  LogoutOutlined,
} from "@ant-design/icons";
import { Link, Outlet, useNavigate, useLocation } from "react-router-dom";

const { Header, Sider, Content } = Layout;

const AdminLayout = () => {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  const menuItems = [
    {
      key: "/admin/dashboard",
      icon: <DashboardOutlined />,
      label: <Link to="/admin/dashboard">Tổng quan</Link>,
    },
    {
      key: "/admin/categories",
      icon: <AppstoreOutlined />,
      label: <Link to="/admin/categories">Danh mục</Link>,
    },
    {
      key: "/admin/products",
      icon: <ShoppingOutlined />,
      label: <Link to="/admin/products">Sản phẩm</Link>,
    },
  ];

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sider
        collapsible
        collapsed={collapsed}
        onCollapse={(value) => setCollapsed(value)}
      >
        <div
          style={{
            height: 32,
            margin: 16,
            background: "rgba(255, 255, 255, 0.2)",
            borderRadius: 6,
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            color: "white",
            fontWeight: "bold",
          }}
        >
          {!collapsed && "HUTECH ADMIN"}
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[location.pathname]}
          items={menuItems}
        />
        <div
          style={{
            position: "absolute",
            bottom: 20,
            width: "100%",
            padding: "0 16px",
          }}
        >
          <Button
            type="primary"
            danger
            icon={<LogoutOutlined />}
            block
            onClick={handleLogout}
          >
            {!collapsed && "Đăng xuất"}
          </Button>
        </div>
      </Sider>
      <Layout>
        <Header style={{ padding: 0, background: colorBgContainer }} />
        <Content style={{ margin: "16px" }}>
          <div
            style={{
              padding: 24,
              minHeight: 360,
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
