import { Link, Outlet, useNavigate } from "react-router-dom";

const AdminLayout = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <div style={{ display: "flex", minHeight: "100vh" }}>
      {/* Sidebar bên trái */}
      <nav
        style={{
          width: "250px",
          background: "#2c3e50",
          color: "white",
          padding: "20px",
        }}
      >
        <h3>HUTECH Admin</h3>
        <ul style={{ listStyle: "none", padding: 0 }}>
          <li style={{ margin: "15px 0" }}>
            <Link to="/admin/dashboard" style={{ color: "white" }}>
              Tổng quan
            </Link>
          </li>
          <li style={{ margin: "15px 0" }}>
            <Link to="/admin/categories" style={{ color: "white" }}>
              Danh mục
            </Link>
          </li>
          <li style={{ margin: "15px 0" }}>
            <Link to="/admin/products" style={{ color: "white" }}>
              Sản phẩm
            </Link>
          </li>
        </ul>
        <button onClick={handleLogout} style={{ marginTop: "50px" }}>
          Đăng xuất
        </button>
      </nav>

      {/* Nội dung chính bên phải */}
      <main style={{ flex: 1, padding: "20px", background: "#ecf0f1" }}>
        <header
          style={{
            borderBottom: "1px solid #bdc3c7",
            paddingBottom: "10px",
            marginBottom: "20px",
          }}
        >
          <h2>Bảng điều khiển</h2>
        </header>
        {/* Nơi hiển thị nội dung các trang con */}
        <Outlet />
      </main>
    </div>
  );
};

export default AdminLayout;
