import React, { useState } from "react";
import { Layout, Menu, theme, message } from "antd";
import {
  DashboardOutlined,
  AppstoreOutlined,
  ShoppingCartOutlined,
  LogoutOutlined,
} from "@ant-design/icons";
import { Outlet, useNavigate, useLocation } from "react-router-dom";

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
    message.success("Đăng xuất thành công!");
    navigate("/login");
  };

  // Định nghĩa Menu items - Đăng xuất nằm ở cuối cùng
  const menuItems = [
    { key: "dashboard", icon: <DashboardOutlined />, label: "Tổng quan" },
    { key: "categories", icon: <AppstoreOutlined />, label: "Danh mục" },
    { key: "products", icon: <ShoppingCartOutlined />, label: "Sản phẩm" },
    { type: "divider" }, // Vạch kẻ ngăn cách
    {
      key: "logout",
      icon: <LogoutOutlined />,
      label: "Đăng xuất",
      danger: true,
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
            overflow: "hidden",
          }}
        >
          {collapsed ? "H" : "HUTECH ADMIN"}
        </div>

        <Menu
          theme="dark"
          mode="inline"
          // Tự động sáng đúng ô dựa trên đường dẫn URL
          selectedKeys={[location.pathname.split("/").pop()]}
          items={menuItems}
          onClick={({ key }) => {
            if (key === "logout") {
              handleLogout();
            } else {
              navigate(`/admin/${key}`);
            }
          }}
        />
        {/* ĐÃ XÓA PHẦN DIV ABSOLUTE Ở ĐÂY */}
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
