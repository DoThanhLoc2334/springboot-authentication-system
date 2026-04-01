const summaryCards = [
  {
    title: "Tai khoan",
    value: "User admin",
    description: "He thong dang tap trung vao quan ly nguoi dung.",
  },
  {
    title: "Phan quyen",
    value: "2 vai tro",
    description: "ROLE_ADMIN va ROLE_USER da san sang de su dung.",
  },
  {
    title: "Bao mat",
    value: "JWT",
    description: "Dang nhap va truy cap duoc bao ve bang token.",
  },
];

const Dashboard = () => {
  return (
    <div>
      <div className="page-section-header">
        <div>
          <h2>Admin dashboard</h2>
          <p>Theo doi nhanh he thong quan ly nguoi dung va phan quyen.</p>
        </div>
      </div>

      <div className="dashboard-grid">
        {summaryCards.map((card) => (
          <section className="dashboard-card" key={card.title}>
            <span className="dashboard-card__label">{card.title}</span>
            <strong className="dashboard-card__value">{card.value}</strong>
            <p>{card.description}</p>
          </section>
        ))}
      </div>
    </div>
  );
};

export default Dashboard;
