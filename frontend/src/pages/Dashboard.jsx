const Dashboard = () => {
  return (
    <div>
      <h3>Chào mừng Huy đến với trang Quản trị!</h3>
      <p>Đây là nơi bạn sẽ quản lý toàn bộ cửa hàng của mình.</p>
      <div style={{ display: "flex", gap: "20px", marginTop: "20px" }}>
        <div
          style={{
            background: "white",
            padding: "20px",
            borderRadius: "8px",
            flex: 1,
            boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
          }}
        >
          <h4>Sản phẩm</h4>
          <p>120 items</p>
        </div>
        <div
          style={{
            background: "white",
            padding: "20px",
            borderRadius: "8px",
            flex: 1,
            boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
          }}
        >
          <h4>Đơn hàng</h4>
          <p>45 mới</p>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
